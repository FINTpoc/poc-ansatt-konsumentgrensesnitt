package no.fk.fint.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fk.event.Event;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Scope("prototype")
@Profile("!mock")
public class RabbitMessaging implements MessageBroker {

    @Value("${rabbitmq.timeout:30000}")
    private long timeout;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public <T> T sendAndReceive(Event<?> event, Class<T> responseType) {
        MessageProperties messageProperties = new MessageProperties();
        rabbitTemplate.setReplyTimeout(timeout);
        messageProperties.setCorrelationId(event.getId().getBytes());

        try {
            String jsonValue = new ObjectMapper().writeValueAsString(event);
            Message message = new Message(jsonValue.getBytes(), messageProperties);
            Message response = rabbitTemplate.sendAndReceive(QueueFactory.getInQueue(event.getOrgId()), message);
            return new ObjectMapper().readValue(response.getBody(), responseType);
        } catch (IOException e) {
            log.error("Exception when trying to send and receive a RabbitMQ message", e);
            return null;
        }
    }
}
