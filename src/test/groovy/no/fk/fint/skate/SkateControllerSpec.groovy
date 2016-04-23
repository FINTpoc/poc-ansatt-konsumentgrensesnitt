package no.fk.fint.skate

import com.fasterxml.jackson.databind.ObjectMapper
import no.fk.fint.Application
import no.fk.fint.employee.EmployeeController
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
class SkateControllerSpec extends Specification {

    @Rule
    RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets")

    @Autowired
    private WebApplicationContext context

    @Autowired
    private EmployeeController employeeController

    private MockMvc mockMvc
    private ObjectMapper objectMapper

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build()

        objectMapper = new ObjectMapper()
    }

    def "Get all marital statuses"() {
        when:
        def path = "/skate/vokabular/sivilstand"

        then:
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(document("skate/vokabular/getMaritalStatus", preprocessResponse(prettyPrint())))
    }

    def "Get all genders"() {
        when:
        def path = "/skate/vokabular/kjonn"

        then:
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andDo(document("skate/vokabular/getGender", preprocessResponse(prettyPrint())))
    }
}
