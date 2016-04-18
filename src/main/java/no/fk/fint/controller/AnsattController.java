package no.fk.fint.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.fk.fint.QueueFactory;
import no.fk.fint.Rabbit;
import no.skate.*;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RestController
@RequestMapping("/ansatte")
@Api(tags = "Ansatte")
public class AnsattController {
    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    @Autowired
    private Rabbit rabbit;

    List<Ansatt> ansatte;

    @PostConstruct
    public void init() throws ParseException {
        ansatte = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yyyy");
        Date fodselsdato = dateFormat.parse("27/12-1971");

        Ansatt ole = new Ansatt(new Personnavn("Ole", "Olsen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.ENKE_ELLER_ENKEMANN);
        ole.addIdentifikator(new Identifikator("fodselsnummer", "12345678901"));

        Ansatt mari = new Ansatt(new Personnavn("Mari", "Hansen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        mari.addIdentifikator(new Identifikator("ansattnummer", "123"));

        Ansatt trine = new Ansatt(new Personnavn("Trine", "Johansen"), Kjonn.KVINNE, Landkode.SE, fodselsdato, Sivilstand.GJENLEVENDE_PARTNER);
        Ansatt line = new Ansatt(new Personnavn("Line", "Svendsen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.SKILT);
        Ansatt pal = new Ansatt(new Personnavn("Pål", "Persen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        ansatte.add(ole);
        ansatte.add(mari);
        ansatte.add(trine);
        ansatte.add(line);
        ansatte.add(pal);
    }

    /*
    @RabbitListener(queues = "vaf-ut")
    public void receiveResponse(byte[] content) {
        log.info("Response: {}", new String(content));
    }
    */

    @ApiOperation("Henter alle ansatte")
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Ansatt> getAnsatte(@RequestParam(required = false) final String navn) {
        log.info("getAnsatte - navn: {}", navn);

        if (navn == null) {
            return ansatte;
        } else {
            return ansatte.stream().filter(ansatt -> {
                String fornavn = ansatt.getNavn().getFornavn().toLowerCase();
                String etternavn = ansatt.getNavn().getEtternavn().toLowerCase();
                return (navn.equals(fornavn) || navn.equals(etternavn));
            }).collect(Collectors.toList());
        }
    }

    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt hentAnsatt(@PathVariable String identifikatortype, @PathVariable String id, @RequestHeader("x-org-id") String orgID) {
        log.info("hentAnsatt - identifikatorType: {}, id: {}", identifikatortype, id);

        //String orgID = getOrgID(headers);
        QueueFactory queueFactory = new QueueFactory(orgID);


        log.info("OrgID: {}", orgID);
        log.info(queueFactory.getInQueue());
        log.info(queueFactory.getOutQueue());

        rabbit.setQueue(queueFactory.getInQueue());
        Message response = rabbit.SendAndReceive("Hello world!");

        /*
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(UUID.randomUUID().toString().getBytes());
        rabbitTemplate.setReplyTimeout(3);
        Message response = rabbitTemplate.sendAndReceive("fint:vaf.no:ansatt", new Message(("{\"identifikatortype\":\"" + identifikatortype + "\", \"id\":\"" + id + "\"}").getBytes(), messageProperties));
        //System.out.println(new String(response.getBody()));
        */

        Optional<Ansatt> ansatt = findAnsatt(identifikatortype, id);
        if (ansatt.isPresent())
            return ansatt.get();

        return new Ansatt();
    }

    private Optional<Ansatt> findAnsatt(String identifikatortype, String id) {
        return ansatte.stream().filter(ansatt -> {
            if (ansatt.getIdentifikatorer().size() > 0) {
                Identifikator identifikator = ansatt.getIdentifikatorer().get(0);
                String type = identifikator.getIdentifikatortype();
                String verdi = identifikator.getIdentifikatorverdi();

                return (identifikatortype.equals(type) && id.equals(verdi));
            }
            return false;
        }).findFirst();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void oppdaterAnsatt(@RequestBody Ansatt ansatt) {
        log.info("oppdaterAnsatt - {}", ansatt);

        Identifikator identifikator = ansatt.getIdentifikatorer().get(0);
        String type = identifikator.getIdentifikatortype();
        String verdi = identifikator.getIdentifikatorverdi();
        Optional<Ansatt> existingAnsatt = findAnsatt(type, verdi);
        if (existingAnsatt.isPresent()) {
            Kontaktinformasjon kontaktinformasjon = existingAnsatt.get().getKontaktinformasjon();
            if (kontaktinformasjon == null) {
                kontaktinformasjon = new Kontaktinformasjon();
                existingAnsatt.get().setKontaktinformasjon(kontaktinformasjon);
            }

            kontaktinformasjon.setEpostadresse(ansatt.getKontaktinformasjon().getEpostadresse());
        }
    }


}
