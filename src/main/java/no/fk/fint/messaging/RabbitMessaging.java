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
import java.text.SimpleDateFormat;

@Slf4j
@Component
@Scope("prototype")
@Profile("!mock")
public class RabbitMessaging implements MessageBroker {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Value("${rabbitmq.timeout:300000}")
    private long timeout;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Event<?> sendAndReceive(Event<?> event) {
        MessageProperties messageProperties = new MessageProperties();
        rabbitTemplate.setReplyTimeout(timeout);
        messageProperties.setCorrelationId(event.getId().getBytes());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(dateFormat);
            String jsonValue = objectMapper.writeValueAsString(event);
            Message message = new Message(jsonValue.getBytes(), messageProperties);
            Message response = rabbitTemplate.sendAndReceive(QueueFactory.getInQueue(event.getOrgId()), message);
            if (response == null) {
                return new Event<>();
            } else {
                return new ObjectMapper().readValue(response.getBody(), Event.class);
            }
        } catch (IOException e) {
            log.error("Exception when trying to send and receive a RabbitMQ message", e);
            return null;
        }
    }
}
