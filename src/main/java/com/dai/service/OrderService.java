package com.dai.service;


import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.util.E3Result;

public interface OrderService {

    /**
     * 查询所有订单
     *
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGridResult getTbOrderList(int page, int rows);

    /**
     * 根据订单id删除订单
     *
     * @param ids
     * @return
     */
    public E3Result deleteTbOrderById(String ids);

    /**
     * 将前台传来的实时订单号更新到redis中等待前台获取
     *
     * @param orderId
     * @return
     */
    public void currentOrderInRedis(String orderId);

    /**
     * 根据订单ID查看完整订单
     *
     * @param ids
     * @return
     */
    public E3Result findTbOrderDetailById(String ids);

    /**
     * 查看所有交易完成的订单
     *
     * @return
     */
    public EasyUIDataGridResult findAllCompleteOrderList(int page, int rows);

    /**
     * 根据查询条件获取list
     *
     * @return
     */
    public EasyUIDataGridResult getIdOrTitleWithItemList(int page, int rows, String orderId, String username);

    /**
     * 查询实时订单的数量
     * @return
     */
    public E3Result getcurUntreated();

    /**
     * 查询所有实时订单,从redis中获取
     * @return
     */
    public EasyUIDataGridResult getCurrentList() throws Exception;

    /**
     * 根据订单ID查看完整的实时订单
     *
     * @param ids
     * @return
     */
    public E3Result findCurOrderDetail(String ids);

    /**
     * 完成实时订单
     *
     * @param orderId
     * @return
     */
    public E3Result manageCurOrderDetail(String orderId);
}
