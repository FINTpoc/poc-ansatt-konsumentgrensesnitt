package no.fk.fint.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.fk.event.Event;
import no.fk.fint.messaging.MessageBroker;
import no.fk.fint.messaging.QueueFactory;
import no.skate.*;
import org.springframework.beans.factory.ObjectFactory;
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

    List<Ansatt> ansatte;


    @PostConstruct
    public void init() throws ParseException {
//        ansatte = new ArrayList<>();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yyyy");
//        Date fodselsdato = dateFormat.parse("27/12-1971");
//
//        Ansatt ole = new Ansatt(new Personnavn("Ole", "Olsen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.ENKE_ELLER_ENKEMANN);
//        ole.addIdentifikator(new Identifikator("fodselsnummer", "12345678901"));
//        Kontaktinformasjon oleKI = new Kontaktinformasjon();
//        oleKI.setEpostadresse("ole.olsen@gmail.com");
//        oleKI.setMobiltelefonnummer("90909090");
//        ole.setKontaktinformasjon(oleKI);
//        ole.setAvdeling("IKT avdelingen");
//
//        Ansatt mari = new Ansatt(new Personnavn("Mari", "Hansen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.GIFT);
//        mari.addIdentifikator(new Identifikator("ansattnummer", "123"));
//        mari.setAvdeling("Personalavdelingen");
//
//        Ansatt trine = new Ansatt(new Personnavn("Trine", "Johansen"), Kjonn.KVINNE, Landkode.SE, fodselsdato, Sivilstand.GJENLEVENDE_PARTNER);
//        trine.setAvdeling("Skeisvang videregående skole");
//        Ansatt line = new Ansatt(new Personnavn("Line", "Svendsen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.SKILT);
//        line.setAvdeling("Seksjon for kvalitet, analyse og dimensjonering");
//        Ansatt pal = new Ansatt(new Personnavn("Pål", "Persen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.GIFT);
//        pal.setAvdeling("Rådmannens stab");
//
//        ansatte.add(ole);
//        ansatte.add(mari);
//        ansatte.add(trine);
//        ansatte.add(line);
//        ansatte.add(pal);
    }

    @ApiOperation("Henter alle ansatte")
    @RequestMapping(method = RequestMethod.GET)
    public List<Ansatt> hentAnsatte(@RequestParam(required = false) final String navn, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("hentAnsatte - navn: {}", navn);

        QueueFactory queueFactory = new QueueFactory(orgID);

        /*Rabbit instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        Message response = instance.sendAndReceive("hentAnsatte");*/

        if (navn == null) {
            return ansatte;
        } else {
            return ansatte.stream().filter(ansatt -> {
                String fornavn = ansatt.getNavn().getFornavn().toLowerCase();
                String etternavn = ansatt.getNavn().getEtternavn().toLowerCase();
                String fulltNavn = String.format("%s %s", fornavn, etternavn);
                //return (navn.equals(fornavn) || navn.equals(etternavn));
                return (fulltNavn.contains(navn));
            }).collect(Collectors.toList());
        }
    }

    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt hentAnsatt(@PathVariable String identifikatortype, @PathVariable String id, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("hentAnsatt - identifikatorType: {}, id: {}", identifikatortype, id);

        QueueFactory queueFactory = new QueueFactory(orgID);

        MessageBroker instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        String response = instance.sendAndReceive(new Event<>());

        Optional<Ansatt> ansatt = findAnsatt(identifikatortype, id);
        if (ansatt.isPresent())
            return ansatt.get();

        return new Ansatt();
    }

    private Optional<Ansatt> findAnsatt(String identifikatortype, String id) {
        log.info("findAnsatt");

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
    public void oppdaterAnsatt(@RequestBody Ansatt ansatt, @RequestHeader("x-org-id") String orgID) {
        log.info("OrgID: {}", orgID);
        log.info("oppdaterAnsatt - {}", ansatt);

        QueueFactory queueFactory = new QueueFactory(orgID);

        /*Rabbit instance = messageBroker.getObject();
        instance.setRoute(queueFactory.getInQueue());
        Message response = instance.sendAndReceive("oppdaterAnsatt");*/

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
