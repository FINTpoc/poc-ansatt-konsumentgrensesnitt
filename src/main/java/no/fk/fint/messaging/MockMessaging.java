package no.fk.fint.messaging;

import no.fk.Ansatt;
import no.fk.event.Event;
import no.skate.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Profile("mock")
@Component
public class MockMessaging implements MessageBroker {

    Map<Identifikator, Ansatt> ansatte = new HashMap<>();

    @PostConstruct
    public void init() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yyyy");
        Date fodselsdato = dateFormat.parse("27/12-1971");

        Ansatt ole = new Ansatt(new Personnavn("Ole", "Olsen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.ENKE_ELLER_ENKEMANN);
        ole.addIdentifikator(new Identifikator("fodselsnummer", "12345678901"));
        ole.setKontaktinformasjon(new Kontaktinformasjon("ole.olsen@gmail.com", "90909090", "90909090"));
        ole.setAvdeling("IKT avdelingen");
        ansatte.put(ole.getIdentifikatorer().get(0), ole);

        Ansatt mari = new Ansatt(new Personnavn("Mari", "Hansen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        mari.addIdentifikator(new Identifikator("ansattnummer", "123"));
        mari.setAvdeling("Personalavdelingen");
        ansatte.put(mari.getIdentifikatorer().get(0), mari);

        Ansatt trine = new Ansatt(new Personnavn("Trine", "Johansen"), Kjonn.KVINNE, Landkode.SE, fodselsdato, Sivilstand.GJENLEVENDE_PARTNER);
        trine.setAvdeling("Skeisvang videregående skole");
        ansatte.put(trine.getIdentifikatorer().get(0), trine);

        Ansatt line = new Ansatt(new Personnavn("Line", "Svendsen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.SKILT);
        line.setAvdeling("Seksjon for kvalitet, analyse og dimensjonering");
        ansatte.put(line.getIdentifikatorer().get(0), line);

        Ansatt pal = new Ansatt(new Personnavn("Pål", "Persen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        pal.setAvdeling("Rådmannens stab");
        ansatte.put(pal.getIdentifikatorer().get(0), pal);
    }


    @Override
    public String sendAndReceive(Event<?> event) {
//        for (Identifikator identifikator : event.getData()) {
//            Ansatt employee = ansatte.get(identifikator);
//            if (employee != null)
//                return employee.toString();
//        }
        return "";
    }

    @Override
    public <T> T sendAndReceive(Event<?> event, Class<T> responseType) {
        if(event.getVerb().equals("hentAnsatte")) {
            return (T) ansatte.values();
        }

        return null;
    }

    @Override
    public void setReplyTimeout(long replyTimeout) {
    }
}
