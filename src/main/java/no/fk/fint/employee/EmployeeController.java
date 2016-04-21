package no.fk.fint.employee;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.fk.event.Event;
import no.fk.event.Type;
import no.fk.fint.messaging.MessageBroker;
import no.skate.Identifikator;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping(value = "/ansatte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Api(tags = "Ansatte")
public class EmployeeController {

    @Autowired
    private ObjectFactory<MessageBroker> messageBroker;

    @ApiOperation("Henter alle ansatte")
    @RequestMapping(method = RequestMethod.GET)
    public List<Ansatt> getEmployees(@RequestParam(required = false) final String navn, @RequestHeader("x-org-id") String orgId) {
        log.info("OrgID: {}", orgId);
        log.info("hentAnsatte - navn: {}", navn);

        Event<Identifikator> event = new Event<>(orgId, "hentAnsatte", Type.REQUEST);
        messageBroker.getObject().sendAndReceive(event);

        /*Rabbit instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        Message response = instance.sendAndReceive("hentAnsatte");*/

        return Lists.newArrayList();
    }

    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt getEmployee(@PathVariable String identifikatortype, @PathVariable String id, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("hentAnsatt - identifikatorType: {}, id: {}", identifikatortype, id);

        MessageBroker instance = messageBroker.getObject();
        String response = instance.sendAndReceive(new Event<>());

        return new Ansatt();
    }

    private Optional<Ansatt> findEmployee(String identifikatortype, String id) {
        log.info("findAnsatt");
        return Optional.empty();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateEmployee(@RequestBody Ansatt ansatt, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("oppdaterAnsatt - {}", ansatt);

        /*Rabbit instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        Message response = instance.sendAndReceive("oppdaterAnsatt");*/
    }


}
