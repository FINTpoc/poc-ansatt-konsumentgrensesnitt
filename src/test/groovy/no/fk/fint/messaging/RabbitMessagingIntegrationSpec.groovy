package no.fk.fint.messaging

import no.fk.event.Event
import no.fk.fint.Application
import no.fk.fint.employee.Events
import no.fk.fint.employee.event.RequestEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.IgnoreIf
import spock.lang.Specification

@IgnoreIf({ !Boolean.valueOf(properties["local.rabbitmq"]) })
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("dev")
class RabbitMessagingIntegrationSpec extends Specification {

    @Autowired
    private RabbitMessaging rabbitMessaging

    def "Send rabbit message"() {
        given:
        Event<String> event = new RequestEvent<>("test-orgId", Events.GET_EMPLOYEE)
        event.addData("test-message")

        when:
        def response = rabbitMessaging.sendAndReceive(event, String)

        then:
        response != null
    }

}
