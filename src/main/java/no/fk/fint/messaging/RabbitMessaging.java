package no.fk.fint.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
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
    private String route;

    public RabbitMessaging() {
        messageProperties = new MessageProperties();
        replyTimeout = 0;
    }

    @Override
    public String sendAndReceive(Event<Ansatt> event) {
        rabbitTemplate.setReplyTimeout(replyTimeout);
        messageProperties.setCorrelationId(event.getId().getBytes());

        try {
            String jsonValue = new ObjectMapper().writeValueAsString(event);
            Message message = new Message(jsonValue.getBytes(), messageProperties);
            Message response = rabbitTemplate.sendAndReceive(route, message);
            return new String(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }
}
