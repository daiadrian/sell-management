package com.dai.service.Impl;


import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.mapper.TbStockMapper;
import com.dai.pojo.TbItem;
import com.dai.pojo.TbStock;
import com.dai.pojo.TbStockExample;
import com.dai.service.StockService;
import com.dai.util.E3Result;
import com.dai.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 进货记录的service
 *
 * @author adrain
 */
@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private TbStockMapper tbStockMapper;

    @Override
    public EasyUIDataGridResult getTbStockList(int page, int rows) {
        TbStockExample example = new TbStockExample();
        TbStockExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);
        //设置分页参数
        PageHelper.startPage(page, rows);
        List<TbStock> tbStockList = tbStockMapper.selectByExample(example);
        //通过list得到一个PageInfo
        PageInfo<TbStock> info = new PageInfo<TbStock>(tbStockList);
        //作为一个结果集传给表现层
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        return result;
    }

    @Override
    public EasyUIDataGridResult findTitleWithStockList(int page, int rows, String title) {
        TbStockExample example = new TbStockExample();
        //设置分页参数
        PageHelper.startPage(page, rows);
        TbStockExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        List<TbStock> tbStockList = tbStockMapper.selectByExample(example);
        //通过list得到一个PageInfo
        PageInfo<TbStock> info = new PageInfo<TbStock>(tbStockList);
        //作为一个结果集传给表现层
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        return result;
    }

    @Override
    public E3Result addTbStock(TbStock tbStock) {
        final long stockId = IDUtils.getStockId();
        tbStock.setId(stockId);
        if (tbStock.getNum() != null && tbStock.getNum() > 0) {
            tbStock.setTotalprice(tbStock.getPrice() * tbStock.getNum());
        } else {
            tbStock.setNum(0);
            tbStock.setTotalprice(tbStock.getPrice());
        }
        tbStock.setStatus(1);
        tbStock.setCreated(new Date());
        tbStock.setUpdated(new Date());
        tbStockMapper.insert(tbStock);
        return E3Result.ok();
    }

    @Override
    public E3Result updateStock(TbStock tbStock) {
        if (tbStock.getNum() != null && tbStock.getNum() > 0) {
            tbStock.setTotalprice(tbStock.getPrice() * tbStock.getNum());
        } else {
            tbStock.setNum(0);
            tbStock.setTotalprice(tbStock.getPrice());
        }
        tbStock.setUpdated(new Date());
        tbStockMapper.updateByPrimaryKeySelective(tbStock);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteStockById(long ids) {
        TbStock tbStock = tbStockMapper.selectByPrimaryKey(ids);
        tbStock.setStatus(2);
        tbStock.setUpdated(new Date());
        tbStockMapper.updateByPrimaryKeySelective(tbStock);
        return E3Result.ok();
    }

}
