package no.fk.fint.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fk.event.Event;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype")
@Profile("!mock")
public class RabbitMessaging implements MessageBroker {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private long replyTimeout;
    private MessageProperties messageProperties;

    public RabbitMessaging() {
        messageProperties = new MessageProperties();
        replyTimeout = 0;
    }

    public String sendAndReceive(Event<?> event) {
        rabbitTemplate.setReplyTimeout(replyTimeout);
        messageProperties.setCorrelationId(event.getId().getBytes());

        try {
            String jsonValue = new ObjectMapper().writeValueAsString(event);
            Message message = new Message(jsonValue.getBytes(), messageProperties);
            Message response = rabbitTemplate.sendAndReceive(QueueFactory.getInQueue(event.getOrgId()), message);
            return new String(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public <T> T sendAndReceive(Event<?> event, Class<T> responseType) {
        return null;
    }

    @Override
    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }
}
