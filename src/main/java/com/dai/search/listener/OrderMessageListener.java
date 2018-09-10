package com.dai.search.listener;

import com.dai.service.OrderService;
import com.dai.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * 消息队列的监听器
 * @author adrain
 *
 */
public class OrderMessageListener implements MessageListener {

	@Autowired
	private OrderService orderService;

	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage){
				TextMessage textMessage = (TextMessage) message;
				String orderId = textMessage.getText();
				Thread.sleep(1000);
				orderService.currentOrderInRedis(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
