package com.dai.Controller;

import com.dai.util.FastDFSClient;
import com.dai.util.PictureResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * 图片上传控制器
 * @author adrain
 *
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload")
	@ResponseBody
	public PictureResult fileUpload(MultipartFile uploadFile){
		try{
			//使用图片服务器的工具类
			//初始化工具类
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/tracker.conf");
			//获取到图片的后缀，得到图片的格式
			//获取图片的真实名称
			String originalFilename = uploadFile.getOriginalFilename();
			//截取获取格式名
			originalFilename = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//返回上传图片后的图片在服务器的url
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), originalFilename);
			String totalURL = IMAGE_SERVER_URL + url;
			//封装成功的返回值
			PictureResult result = new PictureResult(0,totalURL,"上传成功");
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			PictureResult result = new PictureResult();
			result.setError(1);
			result.setMessage("上传失败");
			return result;
		}
	}
	
}
