package no.fk.fint.employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.skate.Identifikator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping(value = "/ansatte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Api(tags = "Ansatte")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("Henter alle ansatte")
    @RequestMapping(method = RequestMethod.GET)
    public Collection<Ansatt> getEmployees(@RequestHeader("x-org-id") String orgId, @RequestParam(required = false) String navn) {
        log.info("OrgId: {}", orgId);
        log.info("getEmployees - navn: {}", navn);
        if (navn == null) {
            return employeeService.getEmployees(orgId);
        } else {
            return employeeService.getEmployees(orgId, navn);
        }
    }

    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt getEmployee(@RequestHeader("x-org-id") String orgId, @PathVariable String identifikatortype, @PathVariable String id) {
        log.info("OrgId: {}", orgId);
        log.info("getEmployee - identifikatorType: {}, id: {}", identifikatortype, id);
        return employeeService.getEmployee(new Identifikator(identifikatortype, id));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateEmployee(@RequestHeader("x-org-id") String orgId, @RequestBody Ansatt ansatt) {
        log.info("OrgId: {}", orgId);
        log.info("updateEmployee - {}", ansatt);
        employeeService.updateEmployee(orgId, ansatt);
    }

}
