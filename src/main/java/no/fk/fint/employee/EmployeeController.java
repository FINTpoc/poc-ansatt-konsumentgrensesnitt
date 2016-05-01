package no.fk.fint.employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.skate.Identifikator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Api(tags = "Ansatte")
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/ansatte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class EmployeeController {
    private static final String CACHE_EMPLOYEE = "employee";
    private static final String CACHE_EMPLOYEES = "employees";

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("Hent alle ansatte")
    @Cacheable(CACHE_EMPLOYEES)
    @RequestMapping(method = RequestMethod.GET)
    public Collection<Ansatt> getEmployees(@RequestHeader("x-org-id") String orgId, @RequestParam(required = false) String name) {
        log.info("OrgId: {}", orgId);
        log.info("getEmployees - name: {}", name);
        if (name == null) {
            return employeeService.getEmployees(orgId);
        } else {
            return employeeService.getEmployees(orgId, name);
        }
    }

    @ApiOperation("Hent ansatt med identifikator")
    @Cacheable(CACHE_EMPLOYEE)
    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt getEmployee(@RequestHeader("x-org-id") String orgId, @PathVariable String identifikatortype, @PathVariable String id) {
        log.info("OrgId: {}", orgId);
        log.info("getEmployee - identifikatorType: {}, id: {}", identifikatortype, id);
        return employeeService.getEmployee(orgId, new Identifikator(identifikatortype, id));
    }

    @ApiOperation("Oppdater ansatt")
    @CacheEvict(cacheNames = {CACHE_EMPLOYEE, CACHE_EMPLOYEES}, allEntries = true)
    @RequestMapping(method = RequestMethod.PUT)
    public void updateEmployee(@RequestHeader("x-org-id") String orgId, @RequestBody Ansatt ansatt) {
        log.info("OrgId: {}", orgId);
        log.info("updateEmployee - {}", ansatt);
        employeeService.updateEmployee(orgId, ansatt);
    }

    @ApiOperation("Slett ansatt cache")
    @CacheEvict(cacheNames = {CACHE_EMPLOYEE, CACHE_EMPLOYEES}, allEntries = true)
    @RequestMapping(value = "/cache", method = RequestMethod.DELETE)
    public void clearCaches() {
        log.info("Cleared getEmployee and getEmployees cache");
    }

}
