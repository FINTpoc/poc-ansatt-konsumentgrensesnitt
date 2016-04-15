package no.fk.fint;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope(value = "singleton")
public class Rabbit {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private long replyTimeout;
    private MessageProperties messageProperties;
    private String queue;

    public Rabbit() {

        //rabbitTemplate = new RabbitTemplate();
        messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(UUID.randomUUID().toString().getBytes());
        replyTimeout = 0;
        queue = "fint:default:ansatt";
    }

    public Message SendAndReceive(String event) {

        rabbitTemplate.setReplyTimeout(replyTimeout);
        Message message = new Message(event.getBytes(), messageProperties);
        log.info(message.toString());

        Message response = rabbitTemplate.sendAndReceive(queue, message);
        //Message response = null;

        return response;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }
}
