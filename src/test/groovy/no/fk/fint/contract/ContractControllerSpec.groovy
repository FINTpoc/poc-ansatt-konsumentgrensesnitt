package no.fk.fint.contract

import no.fk.fint.Application
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.restdocs.RestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("mock")
class ContractControllerSpec extends Specification {

    @Rule
    RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets")

    @Autowired
    private WebApplicationContext context

    private MockMvc mockMvc

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build()
    }

    def "Get ansatt.xsd"() {
        when:
        def path = "/contracts/ansatt.xsd"

        then:
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(document("contracts/ansatt.xsd", preprocessResponse(prettyPrint())))
    }

    def "Get person.xsd"() {
        when:
        def path = "/contracts/person.xsd"

        then:
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(document("contracts/person.xsd", preprocessResponse(prettyPrint())))
    }

    def "Get event.xsd"() {
        when:
        def path = "/contracts/event.xsd"

        then:
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(document("contracts/event.xsd", preprocessResponse(prettyPrint())))
    }

}
