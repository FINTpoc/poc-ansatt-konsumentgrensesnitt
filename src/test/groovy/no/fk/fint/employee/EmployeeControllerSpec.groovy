package no.fk.fint.employee

import com.fasterxml.jackson.databind.ObjectMapper
import no.fk.fint.Application
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("mock")
class EmployeeControllerSpec extends Specification {
    @Rule
    RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets")

    @Autowired
    private WebApplicationContext context

    private MockMvc mockMvc
    private HttpHeaders headers
    private ObjectMapper objectMapper

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build()

        headers = new HttpHeaders()
        headers.add("x-org-id", "test-orgId")
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE)

        objectMapper = new ObjectMapper()
    }

    def "Get all employees"() {
        when:
        def path = "/ansatte"

        then:
        mockMvc.perform(get(path).headers(headers))
                .andExpect(status().isOk())
                .andDo(document("ansatte/getAllEmployees", preprocessResponse(prettyPrint()))
        )
    }

    def "Get employee by name"() {
        when:
        def path = "/ansatte"
        def name = "ole"

        then:
        mockMvc.perform(get(path).param("name", name).headers(headers))
                .andExpect(status().isOk())
                .andDo(document("ansatte/getEmployees", preprocessResponse(prettyPrint())))
    }

    def "Get employee by identifikator"() {
        when:
        def path = "/ansatte/{identifikatortype}/{id}"
        def identifikatortype = "fodselsnummer"
        def id = "23456789012"

        then:
        mockMvc.perform(get(path, identifikatortype, id).headers(headers))
                .andExpect(status().isOk())
                .andDo(document("ansatte/getEmployee", preprocessResponse(prettyPrint())))

    }

    def "Update employee"() {
        when:
        def employee = new MockEmployeeService().getEmployees().get(0)
        def path = "/ansatte"

        then:
        mockMvc.perform(put(path).headers(headers)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andDo(document("ansatte/updateEmployee", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
    }

    def "Clear cache"() {
        when:
        def path = "/ansatte/cache"

        then:
        mockMvc.perform(delete(path))
                .andExpect(status().isOk())
                .andDo(document("ansatte/cache"))
    }
}
