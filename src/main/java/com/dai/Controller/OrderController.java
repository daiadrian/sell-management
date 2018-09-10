package com.dai.Controller;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.common.pojo.OrderDetailResult;
import com.dai.service.OrderService;
import com.dai.util.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 查询订单控制器
 *
 * @author adrain
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询订单列表
     *
     * @param page 当前页
     * @param rows 每页大小
     * @return
     */
    @RequestMapping("/order/list")
    @ResponseBody
    public EasyUIDataGridResult getTbOrderList(int page, int rows) {
        EasyUIDataGridResult result = orderService.getTbOrderList(page, rows);
        return result;
    }

    /**
     * 根据订单ID删除订单
     *
     * @param ids
     * @return
     */
    @RequestMapping("/order/rest/delete")
    @ResponseBody
    public E3Result deleteTbOrderById(@RequestParam("ids") String ids) {
        E3Result result = orderService.deleteTbOrderById(ids);
        return result;
    }

    /**
     * 根据订单ID查看完整订单
     *
     * @param ids
     * @return
     */
    @RequestMapping("/order/rest/findDetail/{ids}")
    @ResponseBody
    public E3Result findDetail(@PathVariable("ids") String ids, HttpServletRequest request) {
        E3Result result = orderService.findTbOrderDetailById(ids);
        OrderDetailResult odr = (OrderDetailResult) result.getData();
        request.getSession().setAttribute("paymentType", odr.getPaymentType());
        request.getSession().setAttribute("orderItems", odr.getOrderItems());
        return result;
    }

    /**
     * 根据查询条件获取list
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/order/findOrderIdOrUsernameWithOrderList")
    @ResponseBody
    public EasyUIDataGridResult getIdOrTitleWithItemList(int page, int rows, String orderId, String username) throws Exception {
        if (StringUtils.isNotBlank(orderId))
            return orderService.getIdOrTitleWithItemList(page, rows, orderId, null);
        if (StringUtils.isNotBlank(username)) {
            username = new String(username.getBytes("ISO-8859-1"),"UTF-8");
            return orderService.getIdOrTitleWithItemList(page, rows, null, username);
        }
        return orderService.findAllCompleteOrderList(page, rows);
    }

    /**
     * 查看所有交易完成的订单
     *
     * @return
     */
    @RequestMapping("/order/findAllCompleteOrderList")
    @ResponseBody
    public EasyUIDataGridResult findAllCompleteOrderList(int page, int rows) {
        EasyUIDataGridResult result = orderService.findAllCompleteOrderList(page, rows);
        return result;
    }

    /**
     * 后台查询前台发来的实时的订单
     *
     * @return
     */
    @RequestMapping("/order/current/untreated")
    @ResponseBody
    public E3Result getcurUntreated() {
        return orderService.getcurUntreated();
    }

    /**
     * 查询实时的订单
     *
     * @return
     */
    @RequestMapping("/order/current/list")
    @ResponseBody
    public EasyUIDataGridResult getCurrentList() throws Exception{
        return orderService.getCurrentList();
    }

    /**
     * 根据订单ID查看完整的实时订单
     *
     * @param ids
     * @return
     */
    @RequestMapping("/order/rest/findCurOrderDetail/{ids}")
    @ResponseBody
    public E3Result findCurOrderDetail(@PathVariable("ids") String ids, HttpServletRequest request) {
        E3Result result = orderService.findCurOrderDetail(ids);
        OrderDetailResult odr = (OrderDetailResult) result.getData();
        request.getSession().setAttribute("paymentType", odr.getPaymentType());
        request.getSession().setAttribute("orderItems", odr.getOrderItems());
        return result;
    }

    /**
     * 完成实时订单
     *
     * @param orderId
     * @return
     */
    @RequestMapping("/order/current/manage/{orderId}")
    @ResponseBody
    public E3Result manageCurOrderDetail(@PathVariable("orderId") String orderId) {
        E3Result result = orderService.manageCurOrderDetail(orderId);
        return result;
    }

}
