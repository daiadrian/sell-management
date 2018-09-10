package com.dai.service;

import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.pojo.TbItem;
import com.dai.pojo.TbItemDesc;
import com.dai.pojo.TbStock;
import com.dai.util.E3Result;

import java.util.List;


/**
 * 进货信息的service接口
 *
 * @author adrain
 */
public interface StockService {


    /**
     * 查询所有记录并且进行分页显示
     *
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGridResult getTbStockList(int page, int rows);

    /**
     * 按货品名称查询进货记录
     *
     * @param page
     * @param rows
     * @param title
     * @return
     */
    EasyUIDataGridResult findTitleWithStockList(int page, int rows, String title);

    /**
     * 新增记录
     * @param tbStock
     * @return
     */
    public E3Result addTbStock(TbStock tbStock);

    /**
     * 修改记录
     * @param tbStock
     * @return
     */
    public E3Result updateStock(TbStock tbStock);

    /**
     * 根据id删除记录
     * @param ids
     * @return
     */
    public E3Result deleteStockById(long ids);
}

