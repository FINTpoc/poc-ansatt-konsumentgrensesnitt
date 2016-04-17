package no.fk.fint;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
public class Rabbit {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private long replyTimeout;
    private MessageProperties messageProperties;
    private String queue;

    public Rabbit() {

        messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(UUID.randomUUID().toString().getBytes());
        replyTimeout = 0;
    }

    public Message SendAndReceive(String event) {

        rabbitTemplate.setReplyTimeout(replyTimeout);
        Message message = new Message(event.getBytes(), messageProperties);
        Message response = rabbitTemplate.sendAndReceive(queue, message);

        return response;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }
}
