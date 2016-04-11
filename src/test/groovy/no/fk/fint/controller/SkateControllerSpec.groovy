package no.fk.fint.controller

import no.fk.fint.Application
import no.skate.Kjonn
import no.skate.Sivilstand
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import spock.lang.Specification

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
class SkateControllerSpec extends Specification {

    @Value('${local.server.port}')
    private int port

    def "Hent alle sivilstander"() {
        when:
        def sivilstand = new TestRestTemplate().getForObject("http://localhost:${port}/skate/vokabular/sivilstand", Sivilstand[])

        then:
        sivilstand != null
        sivilstand.length == 10
    }

    def "Hent alle kjønn"() {
        when:
        def kjonn = new TestRestTemplate().getForObject("http://localhost:${port}/skate/vokabular/kjonn", Kjonn[])

        then:
        kjonn != null
        kjonn.length == 4
    }
}
