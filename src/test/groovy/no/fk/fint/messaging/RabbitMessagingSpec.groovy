package no.fk.fint.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import no.fk.event.Event
import no.fk.event.Type
import no.fk.fint.employee.Events
import no.fk.fint.employee.MockEmployeeService
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification

class RabbitMessagingSpec extends Specification {

    private RabbitTemplate rabbitTemplate
    private MockEmployeeService mockEmployeeService

    void setup() {
        rabbitTemplate = Mock(RabbitTemplate)
        mockEmployeeService = new MockEmployeeService()
    }

    def "Send and receive an event"() {
        given:
        def rabbitMessaging = new RabbitMessaging(rabbitTemplate: rabbitTemplate)
        def event = new Event(type: Type.REQUEST, verb: Events.GET_EMPLOYEES.verb(), data: mockEmployeeService.getEmployees())
        def eventJson = new ObjectMapper().writeValueAsBytes(event)

        when:
        def response = rabbitMessaging.sendAndReceive(event)

        then:
        1 * rabbitTemplate.setReplyTimeout(_ as Long)
        1 * rabbitTemplate.sendAndReceive(_ as String, _ as Message) >> new Message(eventJson, new MessageProperties())
        response.getData().size() == 5
    }

}
