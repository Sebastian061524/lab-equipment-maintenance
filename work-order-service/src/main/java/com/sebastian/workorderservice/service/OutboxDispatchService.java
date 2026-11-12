package com.sebastian.workorderservice.service;

import com.sebastian.workorderservice.mapper.OutBoxEventMapper;
import com.sebastian.workorderservice.model.OutboxEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxDispatchService {
    private final OutboxPublisher outboxPublisher;
    private final OutBoxEventMapper outBoxEventMapper;

    public OutboxDispatchService(OutboxPublisher outboxPublisher, OutBoxEventMapper outBoxEventMapper){
        this.outboxPublisher = outboxPublisher;
        this.outBoxEventMapper = outBoxEventMapper;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPending(){
        List<OutboxEvent> pending = outBoxEventMapper.findPending(20);
        for (OutboxEvent outboxEvent : pending) {
            boolean publish = outboxPublisher.publish(outboxEvent);
            if(publish){
                outBoxEventMapper.markSent(outboxEvent.id());
            }
        }
    }
}
