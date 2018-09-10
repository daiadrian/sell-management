package com.dai.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页显示控制器
 * 分页控制器
 * @author adrain
 *
 */
@Controller
public class PageController {

	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/")
	public String index(){
		return "index";
	}
	/**
	 * 跳转到指定的jsp
	 * @param page 获取到get请求的后缀来进行jsp跳转
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
	
	@RequestMapping("/rest/page/{edit}")
	public String showEditPage(@PathVariable String edit){
		return edit;
	}


}
