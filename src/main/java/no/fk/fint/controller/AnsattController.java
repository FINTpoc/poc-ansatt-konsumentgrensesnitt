package no.fk.fint.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.fk.event.Event;
import no.fk.event.Type;
import no.fk.fint.messaging.MessageBroker;
import no.fk.fint.messaging.QueueFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.messageBroker.annotation.RabbitListener;
//import org.springframework.amqp.messageBroker.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping(value = "/ansatte", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Api(tags = "Ansatte")
public class AnsattController {

    @Autowired
    private ObjectFactory<MessageBroker> messageBroker;

    @ApiOperation("Henter alle ansatte")
    @RequestMapping(method = RequestMethod.GET)
    public List<Ansatt> getEmployees(@RequestParam(required = false) final String navn, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("hentAnsatte - navn: {}", navn);

        Event<Ansatt> event = new Event<>("hentAnsatte", Type.REQUEST);
        messageBroker.getObject().sendAndReceive(event);

        QueueFactory queueFactory = new QueueFactory(orgID);

        /*Rabbit instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        Message response = instance.sendAndReceive("hentAnsatte");*/

        return Lists.newArrayList();
    }

    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt getEmployee(@PathVariable String identifikatortype, @PathVariable String id, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("hentAnsatt - identifikatorType: {}, id: {}", identifikatortype, id);

        QueueFactory queueFactory = new QueueFactory(orgID);

        MessageBroker instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
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

        QueueFactory queueFactory = new QueueFactory(orgID);

        /*Rabbit instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        Message response = instance.sendAndReceive("oppdaterAnsatt");*/
    }


}
