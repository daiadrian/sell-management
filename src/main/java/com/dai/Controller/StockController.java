package com.dai.Controller;


import com.dai.common.pojo.EasyUIDataGridResult;
import com.dai.pojo.TbStock;
import com.dai.service.StockService;
import com.dai.util.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * 进货记录的控制器
 *
 * @author adrain
 */
@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 查看所有进货记录
     *
     * @return
     */
    @RequestMapping("/stock/list")
    @ResponseBody
    public EasyUIDataGridResult getTbStockList(int page, int rows) {
        EasyUIDataGridResult result = stockService.getTbStockList(page, rows);
        return result;
    }

    /**
     * 按货品名称查询进货记录
     *
     * @return
     */
    @RequestMapping("/stock/findTitleWithStockList")
    @ResponseBody
    public EasyUIDataGridResult findTitleWithStockList(String title, int page, int rows) throws Exception {
        title = new String(title.getBytes("ISO-8859-1"),"UTF-8");
        EasyUIDataGridResult result = null;
        if (StringUtils.isNotBlank(title)) {
            result = stockService.findTitleWithStockList(page, rows, title);
        } else {
            result = stockService.getTbStockList(page, rows);
        }
        return result;
    }


    /**
     * 添加进货记录
     *
     * @return
     */
    @RequestMapping("/stock/save")
    @ResponseBody
    public E3Result addTbStock(TbStock tbStock) {
        E3Result result = stockService.addTbStock(tbStock);
        return result;
    }


    /**
     * 修改进货记录
     *
     * @return
     */
    @RequestMapping("/stock/rest/update")
    @ResponseBody
    public E3Result updateTbStock(TbStock tbStock) {
        E3Result result = stockService.updateStock(tbStock);
        return result;
    }


    /**
     * 删除进货记录
     *
     * @return
     */
    @RequestMapping("/stock/rest/delete")
    @ResponseBody
    public E3Result deleteTbStock(@RequestParam("ids") long ids) {
        E3Result result = stockService.deleteStockById(ids);
        return result;
    }


}
