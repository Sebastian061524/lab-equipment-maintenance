package com.sebastian.workorderservice.service;

import com.sebastian.workorderservice.model.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class OutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private static final String QUEUE_NAME = "work-order.completed";

    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public boolean publish(OutboxEvent event){
        CorrelationData correlation = new CorrelationData();

        try{
            rabbitTemplate.convertAndSend("", QUEUE_NAME, event, correlation);
            CorrelationData.Confirm confirm = correlation.getFuture().get(3, TimeUnit.SECONDS);
            return confirm.ack() && correlation.getReturned() == null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("等待 RabbitMQ 确认时被中断，事件 ID={}", event.id());
            return false;
        } catch (AmqpException | ExecutionException | TimeoutException e){
            log.warn("发送工单事件失败，事件 ID={}", event.id(), e);
            return false;
        }
    }
}
