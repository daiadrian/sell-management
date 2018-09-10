package com.dai.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.dai.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 消息队列的监听器
 * @author adrain
 *
 */
public class SearchMessageListener implements MessageListener {

	@Autowired
	private SearchItemService searchItemService;
	
	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage){
				TextMessage textMessage = (TextMessage) message;
				String itemId = textMessage.getText();
				Thread.sleep(2000);
				searchItemService.addDocument(Long.parseLong(itemId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
