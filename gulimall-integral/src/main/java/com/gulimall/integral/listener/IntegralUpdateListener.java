package com.gulimall.integral.listener;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gulimall.integral.service.IntegralFeeGradeService;
import com.gulimall.integral.service.IntegralUserPointService;
import com.gulimall.integral.to.OrderTo;
import com.rabbitmq.client.Channel;

@Service
@RabbitListener(queues = { "order.finish.integral.queue" })
public class IntegralUpdateListener {
	@Autowired
	IntegralFeeGradeService		integralFeeGradeService;
	@Autowired
	IntegralUserPointService	integralUserPointService;

	//todo:监听订单完成之后的会员队列并做相关的处理
	@RabbitHandler
	public void handleIntegralUpdate(OrderTo orderTo, Message message, Channel channel) throws IOException {
		//收到订单购买的消息
		try {
			integralFeeGradeService.updateUserFreeGrade(orderTo);
			integralUserPointService.updateUserPoint(orderTo);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
		}

	}

}
