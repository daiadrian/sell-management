package com.dai.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.dubbo.common.json.JSON;
import com.dai.common.jedis.JedisOrderClient;
import com.dai.common.pojo.*;
import com.dai.mapper.TbOrderItemMapper;
import com.dai.mapper.TbOrderMapper;
import com.dai.pojo.TbOrder;
import com.dai.pojo.TbOrderExample;
import com.dai.pojo.TbOrderItem;
import com.dai.pojo.TbOrderItemExample;
import com.dai.service.OrderService;
import com.dai.util.E3Result;
import com.dai.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 订单的服务层
 *
 * @author adrain
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private JedisOrderClient jedisOrderClient;
    @Value("${CURRENT_ORDER_INFO}")
    private String CURRENT_ORDER_INFO;

    @Override
    public EasyUIDataGridResult getTbOrderList(int page, int rows) {
        TbOrderExample orderExample = new TbOrderExample();
        //设置分页参数
        PageHelper.startPage(page, rows);
        return packagingOrderResult(orderExample);
    }

    @Override
    public E3Result deleteTbOrderById(String ids) {
        TbOrder order = tbOrderMapper.selectByPrimaryKey(ids);
        order.setStatus(4);
        tbOrderMapper.updateByPrimaryKeySelective(order);
        return E3Result.ok();
    }

    /**
     * 将实时的订单数据添加到redis中
     *
     * @param orderId
     */
    @Override
    public void currentOrderInRedis(String orderId) {
        jedisOrderClient.rpush(CURRENT_ORDER_INFO, orderId);
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        OrderInfo orderInfo = new OrderInfo();
        if (null != tbOrder) {
            if (tbOrder.getPaymentType() == 1) {
                jedisOrderClient.hset(CURRENT_ORDER_INFO + orderId, "paymentType", "堂食");
            } else {
                jedisOrderClient.hset(CURRENT_ORDER_INFO + orderId, "paymentType", "外卖");
            }
            jedisOrderClient.hset(CURRENT_ORDER_INFO + orderId, "tbOrder", JsonUtils.objectToJson(tbOrder));
            jedisOrderClient.hset(CURRENT_ORDER_INFO + orderId, "ctime",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
            TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
            criteria.andOrderIdEqualTo(orderId);
            List<TbOrderItem> tbOrderItems = tbOrderItemMapper.selectByExample(tbOrderItemExample);
            if (null != tbOrderItems && tbOrderItems.size() > 0) {
                orderInfo.setOrderItems(tbOrderItems);
                jedisOrderClient.hset(CURRENT_ORDER_INFO + orderId, "tbOrderItems", JsonUtils.objectToJson(orderInfo.getOrderItems()));
            }
        }
    }

    @Override
    public E3Result findTbOrderDetailById(String ids) {
        //查看订单的详情
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(ids);
        if (tbOrder != null) {
            return E3Result.ok(packagingOrderDetailResult(tbOrder));
        }
        return E3Result.build(400, "无此订单");
    }

    @Override
    public EasyUIDataGridResult findAllCompleteOrderList(int page, int rows) {
        TbOrderExample example = new TbOrderExample();
        PageHelper.startPage(page, rows);
        TbOrderExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(3);
        return packagingOrderResult(example);
    }

    @Override
    public EasyUIDataGridResult getIdOrTitleWithItemList(int page, int rows, String orderId, String username) {
        TbOrderExample example = new TbOrderExample();
        PageHelper.startPage(page, rows);
        TbOrderExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(orderId))
            criteria.andOrderIdEqualTo(orderId);
        if (StringUtils.isNoneBlank(username))
            criteria.andUserNameEqualTo(username);
        return packagingOrderResult(example);
    }

    @Override
    public E3Result getcurUntreated() {
        Long result = jedisOrderClient.llen(CURRENT_ORDER_INFO);
        return E3Result.ok(result);
    }

    @Override
    public EasyUIDataGridResult getCurrentList() throws Exception {
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        List<String> lrange = jedisOrderClient.lrange(CURRENT_ORDER_INFO);
        result.setTotal(lrange.size());
        List<CurrentOrderResult> curOrderResults = new ArrayList<>();
        if (null != lrange && lrange.size() > 0) {
            for (String orderId : lrange) {
                CurrentOrderResult cor = new CurrentOrderResult();
                cor.setOrderId(orderId);
                cor.setPaymentType(jedisOrderClient.hget(CURRENT_ORDER_INFO + orderId, "paymentType"));
                cor.setCreateTime(jedisOrderClient.hget(CURRENT_ORDER_INFO + orderId, "ctime"));
                curOrderResults.add(cor);
            }
        }
        result.setRows(curOrderResults);
        return result;
    }

    @Override
    public E3Result findCurOrderDetail(String ids) {
        String tbOrderJson = jedisOrderClient.hget(CURRENT_ORDER_INFO + ids, "tbOrder");
        TbOrder tbOrder = JsonUtils.jsonToPojo(tbOrderJson, TbOrder.class);
        OrderDetailResult result = new OrderDetailResult();
        result.setOrderId(tbOrder.getOrderId());
        result.setPayment(tbOrder.getPayment());
        result.setPaymentTime(tbOrder.getPaymentTime());
        if (tbOrder.getPaymentType() == 1) {
            result.setPaymentType("堂食");
            result.setShoppingCode(tbOrder.getShoppingCode());
        } else {
            result.setPaymentType("外卖");
            result.setUsername(tbOrder.getUserName());
            result.setUseraddress(tbOrder.getUserAddress());
            result.setPostFee(tbOrder.getPostFee());
            result.setUserPhone(tbOrder.getUserPhone());
        }
        switch (tbOrder.getStatus()) {
            case 1:
                result.setStatus("未付款");
                break;
            case 2:
                result.setStatus("已付款");
                break;
            case 3:
                result.setStatus("交易完成");
                break;
            case 4:
                result.setStatus("订单取消");
                break;
        }
        String tbOrderItemsJson = jedisOrderClient.hget(CURRENT_ORDER_INFO + ids, "tbOrderItems");
        List<TbOrderItem> tbOrderItems = JsonUtils.jsonToList(tbOrderItemsJson, TbOrderItem.class);
        if (tbOrderItems != null && tbOrderItems.size() > 0) {
            result.setOrderItems(tbOrderItems);
        }
        return E3Result.ok(result);
    }

    @Override
    public E3Result manageCurOrderDetail(String orderId) {
        //完成订单：1 数据库：加上完成时间和状态更改 ； 2 redis删除对应的缓存
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        if (tbOrder != null) {
            tbOrder.setStatus(3);
            if (tbOrder.getPaymentType() == 2)
                tbOrder.setConsignTime(new Date());
            tbOrder.setUpdateTime(new Date());
        }
        tbOrderMapper.updateByPrimaryKeySelective(tbOrder);
        jedisOrderClient.del(CURRENT_ORDER_INFO + orderId);
        jedisOrderClient.lrem(CURRENT_ORDER_INFO, orderId);
        return E3Result.ok();
    }

    /**
     * 封装OrderResult后返回
     *
     * @param example
     * @return
     */
    private EasyUIDataGridResult packagingOrderResult(TbOrderExample example) {
        List<TbOrder> orderList = tbOrderMapper.selectByExample(example);
        PageInfo<TbOrder> info = new PageInfo<TbOrder>(orderList);
        List<OrderResult> orderResults = new ArrayList<OrderResult>();
        for (TbOrder o : info.getList()) {
            OrderResult result = new OrderResult();
            result.setOrderId(o.getOrderId());
            result.setCreateTime(o.getCreateTime());
            result.setUpdateTime(o.getUpdateTime());
            result.setPayment(o.getPayment());
            switch (o.getStatus()) {
                case 1:
                    result.setStatus("未付款");
                    break;
                case 2:
                    result.setStatus("已付款");
                    break;
                case 3:
                    result.setStatus("交易完成");
                    break;
                case 4:
                    result.setStatus("订单取消");
                    break;
            }
            result.setPaymentTime(o.getPaymentTime());
            result.setConsignTime(o.getConsignTime());
            if (o.getPaymentType() == 1) {
                result.setPaymentType("堂食");
            } else {
                result.setPaymentType("外卖");
            }
            result.setUserName(o.getUserName());
            orderResults.add(result);
        }
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(orderResults);
        return result;
    }

    /**
     * 封装订单的详情页面数据
     *
     * @param tbOrder
     * @return
     */
    private OrderDetailResult packagingOrderDetailResult(TbOrder tbOrder) {
        OrderDetailResult result = new OrderDetailResult();
        result.setOrderId(tbOrder.getOrderId());
        result.setPayment(tbOrder.getPayment());
        result.setPaymentTime(tbOrder.getPaymentTime());
        if (tbOrder.getPaymentType() == 1) {
            result.setPaymentType("堂食");
            result.setShoppingCode(tbOrder.getShoppingCode());
        } else {
            result.setPaymentType("外卖");
            result.setUsername(tbOrder.getUserName());
            result.setUseraddress(tbOrder.getUserAddress());
            result.setPostFee(tbOrder.getPostFee());
            result.setUserPhone(tbOrder.getUserPhone());
        }
        switch (tbOrder.getStatus()) {
            case 1:
                result.setStatus("未付款");
                break;
            case 2:
                result.setStatus("已付款");
                break;
            case 3:
                result.setStatus("交易完成");
                break;
            case 4:
                result.setStatus("订单取消");
                break;
        }
        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(tbOrder.getOrderId());
        List<TbOrderItem> tbOrderItems = tbOrderItemMapper.selectByExample(example);
        if (tbOrderItems != null && tbOrderItems.size() > 0) {
            result.setOrderItems(tbOrderItems);
        }
        return result;
    }

}
