package com.offershopper.feedbackdatabaseservice.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.offershopper.feedbackdatabaseservice.CouponFeedbackApplication;

@Service
public class MessageSender {
	
	//autowired a template of rabbitmq to send a message
	@Autowired
	private AmqpTemplate amqpTemplate;
		
	public void produceMsg(String msg){
		//using the template defining the needed parameters- exchange name,key and message
		amqpTemplate.convertAndSend(CouponFeedbackApplication.EXCHANGE_NAME, CouponFeedbackApplication.ROUTING_KEY, msg);
	}
}