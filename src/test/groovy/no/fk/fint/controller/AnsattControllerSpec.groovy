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
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
class AnsattControllerSpec extends Specification {

    @Autowired
    private AnsattController ansattController

    @Value('${local.server.port}')
    private int port

    def "Hent alle ansatte"() {
        when:
        def ansatte = new TestRestTemplate().getForObject("http://localhost:${port}/ansatte", Ansatt[])

        then:
        ansatte != null
        ansatte.length == 5
    }

    def "Søk etter ansatte med navn"() {
        when:
        def ansatte = new TestRestTemplate().getForObject("http://localhost:${port}/ansatte?navn=ole", Ansatt[])

        then:
        ansatte != null
        ansatte.length == 1
        ansatte[0].getNavn().getFornavn() == "Ole"
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
        def ansatt = ansattController.ansatte.get(0)

        def kontaktinformasjon = new Kontaktinformasjon()
        kontaktinformasjon.setEpostadresse("ole.olsen@fint.no")
        ansatt.setKontaktinformasjon(kontaktinformasjon)

        when:
        def response = new TestRestTemplate().exchange("http://localhost:${port}/ansatte", HttpMethod.PUT, new HttpEntity<>(ansatt), Ansatt)

        then:
        response.statusCode == HttpStatus.OK
    }
}
