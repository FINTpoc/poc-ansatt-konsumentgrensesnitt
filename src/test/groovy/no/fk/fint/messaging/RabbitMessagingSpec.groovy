package no.fk.fint.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import no.fk.Ansatt
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
    private String employeesJson

    void setup() {
        rabbitTemplate = Mock(RabbitTemplate)
        mockEmployeeService = new MockEmployeeService()
        employeesJson = new ObjectMapper().writeValueAsString(mockEmployeeService.getEmployees())
    }

    def "Send and receive an event"() {
        given:
        def rabbitMessaging = new RabbitMessaging(rabbitTemplate: rabbitTemplate)
        def event = new Event(type: Type.REQUEST, verb: Events.GET_EMPLOYEES.verb(), data: mockEmployeeService.getEmployees())

        when:
        def employees = rabbitMessaging.sendAndReceive(event, Ansatt[])

        then:
        1 * rabbitTemplate.setReplyTimeout(_ as Long)
        1 * rabbitTemplate.sendAndReceive(_ as String, _ as Message) >> new Message(employeesJson.getBytes(), new MessageProperties())
        employees.size() == 5
    }

}
