package no.fk.fint.employee

import no.fk.Ansatt
import no.fk.event.Event
import no.fk.event.EventResponse
import no.fk.fint.messaging.RabbitMessaging
import no.skate.Identifikator
import spock.lang.Specification

class RabbitEmployeeServiceSpec extends Specification {

    private MockEmployeeService mockEmployeeService
    private RabbitMessaging rabbitMessaging

    private RabbitEmployeeService rabbitEmployeeService

    void setup() {
        mockEmployeeService = new MockEmployeeService()
        rabbitMessaging = Mock(RabbitMessaging)
        rabbitEmployeeService = new RabbitEmployeeService(rabbitMessaging: rabbitMessaging)
    }

    def "Get employees by orgId"() {
        when:
        def employees = rabbitEmployeeService.getEmployees("test-orgId")

        then:
        1 * rabbitMessaging.sendAndReceive(_ as Event, _ as Class) >> (mockEmployeeService.getEmployees() as Ansatt[])
        employees.size() == 5
    }

    def "Get employees by orgId and name"() {
        when:
        def employees = rabbitEmployeeService.getEmployees("test-orgId", "trine")

        then:
        1 * rabbitMessaging.sendAndReceive(_ as Event, _ as Class) >> (mockEmployeeService.getEmployees() as Ansatt[])
        employees.size() == 1
        employees.get(0).getNavn().getFornavn() == "Trine"
    }

    def "Get employee by identifikator"() {
        given:
        def id = new Identifikator("fodselsnummer", "45678901234")

        when:
        def employee = rabbitEmployeeService.getEmployee("test-orgId", id)

        then:
        1 * rabbitMessaging.sendAndReceive(_ as Event, _ as Class) >> (mockEmployeeService.getEmployees().get(0))
        employee.getNavn().getFornavn() == "Pål"
    }

    def "Update employee"() {
        when:
        def response = rabbitEmployeeService.updateEmployee("test-orgId", new Ansatt())

        then:
        1 * rabbitMessaging.sendAndReceive(_ as Event, _ as Class) >> new EventResponse("ok", "ansatt oppdatert")
        response.getStatus() == "ok"
    }
}
