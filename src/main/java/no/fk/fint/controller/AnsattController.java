package no.fk.fint.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.fk.Ansatt;
import no.skate.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ansatte")
@Api(tags = "Ansatte")
public class AnsattController {
    List<Ansatt> ansatte;

    @PostConstruct
    public void init() throws ParseException {
        ansatte = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yyyy");
        Date fodselsdato = dateFormat.parse("27/12-1971");

        Ansatt ole = new Ansatt(new Personnavn("Ole", "Olsen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.ENKE_ELLER_ENKEMANN);
        ole.setIdentifikator(new Identifikator("fodselsnummer", "12345678901"));

        Ansatt mari = new Ansatt(new Personnavn("Mari", "Hansen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        mari.setIdentifikator(new Identifikator("ansattnummer", "123"));

        Ansatt trine = new Ansatt(new Personnavn("Trine", "Johansen"), Kjonn.KVINNE, Landkode.SE, fodselsdato, Sivilstand.GJENLEVENDE_PARTNER);
        Ansatt line = new Ansatt(new Personnavn("Line", "Svendsen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.SKILT);
        Ansatt pal = new Ansatt(new Personnavn("Pål", "Persen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        ansatte.add(ole);
        ansatte.add(mari);
        ansatte.add(trine);
        ansatte.add(line);
        ansatte.add(pal);
    }

    @ApiOperation("Henter alle ansatte")
    @RequestMapping(method = RequestMethod.GET)
    public List<Ansatt> hentAnsatte(@RequestParam(required = false) String navn) {
        log.info("hentAnsatte - navn: {}", navn);
        if (navn == null) {
            return ansatte;
        } else {
            List<Ansatt> ansattListe = new ArrayList<>();
            navn = navn.toLowerCase();
            for (Ansatt ansatt : ansatte) {
                String fornavn = ansatt.getNavn().getFornavn().toLowerCase();
                String etternavn = ansatt.getNavn().getEtternavn().toLowerCase();
                if (navn.equals(fornavn) || navn.equals(etternavn))
                    ansattListe.add(ansatt);
            }
            return ansattListe;
        }
    }

    @RequestMapping(value = "/{identifikatortype}/{id}", method = RequestMethod.GET)
    public Ansatt hentAnsatt(@PathVariable String identifikatortype, @PathVariable String id) {
        log.info("hentAnsatt - identifikatorType: {}, id: {}", identifikatortype, id);
        Ansatt ansatt = findAnsatt(identifikatortype, id);
        if (ansatt != null)
            return ansatt;

        return new Ansatt();
    }

    private Ansatt findAnsatt(String identifikatortype, String id) {
        for (Ansatt ansatt : ansatte) {
            if (ansatt.getIdentifikator() != null) {
                String type = ansatt.getIdentifikator().getIdentifikatorType();
                String verdi = ansatt.getIdentifikator().getIdentifikatorVerdi();

                if (identifikatortype.equals(type) && id.equals(verdi)) {
                    return ansatt;
                }
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void oppdaterAnsatt(@RequestBody Ansatt ansatt) {
        log.info("oppdaterAnsatt - {}", ansatt);

        String type = ansatt.getIdentifikator().getIdentifikatorType();
        String verdi = ansatt.getIdentifikator().getIdentifikatorVerdi();
        Ansatt existingAnsatt = findAnsatt(type, verdi);
        if (existingAnsatt != null) {
            if (existingAnsatt.getKontaktInformasjon() == null)
                existingAnsatt.setKontaktInformasjon(new KontaktInformasjon());

            existingAnsatt.getKontaktInformasjon().setEpostadresse(ansatt.getKontaktInformasjon().getEpostadresse());
        }
    }
}
