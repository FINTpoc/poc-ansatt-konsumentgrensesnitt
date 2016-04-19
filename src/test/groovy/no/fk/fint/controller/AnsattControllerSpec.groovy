package no.fk.fint.controller

import no.fk.Ansatt
import no.fk.fint.Application
import no.skate.Kontaktinformasjon
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("mock")
class AnsattControllerSpec extends Specification {

    @Autowired
    private AnsattController ansattController

    @Value('${local.server.port}')
    private int port

    def "Hent alle ansatte"() {
        given:
        HttpHeaders headers = new HttpHeaders()
        headers.add("x-org-id", "test-org")

        when:
        def response = new TestRestTemplate().exchange("http://localhost:${port}/ansatte", HttpMethod.GET, new HttpEntity(headers), Ansatt[])

        then:
        response.statusCode == HttpStatus.OK
        response.getBody().length == 5
    }

    def "Søk etter ansatte med navn"() {
        given:
        HttpHeaders headers = new HttpHeaders()
        headers.add("x-org-id", "test-org")

        when:
        def response = new TestRestTemplate().exchange("http://localhost:${port}/ansatte?navn=ole", HttpMethod.GET, new HttpEntity(headers), Ansatt[])

        then:
        response.statusCode == HttpStatus.OK
        response.getBody()[0].navn.fornavn == "Ole"
    }

    def "Hent ansatt"() {
        when:
        def ansatt = new TestRestTemplate().getForObject("http://localhost:${port}/ansatte/fodselsnummer/12345678901", Ansatt)

        then:
        ansatt != null
        //ansatt.getNavn().getFornavn() == "Ole"
    }

    def "Oppdater ansatt"() {
        given:
        HttpHeaders headers = new HttpHeaders()
        headers.add("x-org-id", "test-org")
        def ansatt = ansattController.ansatte.get(0)

        def kontaktinformasjon = new Kontaktinformasjon()
        kontaktinformasjon.setEpostadresse("ole.olsen@fint.no")
        ansatt.setKontaktinformasjon(kontaktinformasjon)

        when:
        def response = new TestRestTemplate().exchange("http://localhost:${port}/ansatte", HttpMethod.PUT, new HttpEntity(ansatt, headers), Ansatt)

        then:
        response.statusCode == HttpStatus.OK
    }
}
