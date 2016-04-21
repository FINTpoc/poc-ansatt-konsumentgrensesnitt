package no.fk.fint.messaging

import no.fk.event.Event
import no.fk.event.Type
import no.fk.fint.util.Employees
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification

class RabbitMessagingSpec extends Specification {

    def "Send and receive an event"() {
        given:
        def rabbitTemplate = Mock(RabbitTemplate)
        def rabbitMessaging = new RabbitMessaging(rabbitTemplate: rabbitTemplate)
        def event = new Event<>(type: Type.REQUEST, verb: "getEmployees", data: Employees.getEmployees())

        when:
        def response = rabbitMessaging.sendAndReceive(event)

        then:
        1 * rabbitTemplate.setReplyTimeout(_ as Long)
        1 * rabbitTemplate.sendAndReceive(_ as String, _ as Message) >> new Message("test-message".getBytes(), new MessageProperties())
        response == "test-message"
    }
    
}
