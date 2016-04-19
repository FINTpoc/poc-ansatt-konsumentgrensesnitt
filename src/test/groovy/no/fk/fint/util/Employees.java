package no.fk.fint.util;

import no.fk.Ansatt;
import no.skate.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum Employees {

    ;

    private static List<Ansatt> ansatte = new ArrayList<>();

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM-yyyy");
        Date fodselsdato;
        try {
            fodselsdato = dateFormat.parse("27/12-1971");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Ansatt ole = new Ansatt(new Personnavn("Ole", "Olsen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.ENKE_ELLER_ENKEMANN);
        ole.addIdentifikator(new Identifikator("fodselsnummer", "12345678901"));
        Kontaktinformasjon oleKI = new Kontaktinformasjon();
        oleKI.setEpostadresse("ole.olsen@gmail.com");
        oleKI.setMobiltelefonnummer("90909090");
        ole.setKontaktinformasjon(oleKI);
        ole.setAvdeling("IKT avdelingen");

        Ansatt mari = new Ansatt(new Personnavn("Mari", "Hansen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        mari.addIdentifikator(new Identifikator("ansattnummer", "123"));
        mari.setAvdeling("Personalavdelingen");

        Ansatt trine = new Ansatt(new Personnavn("Trine", "Johansen"), Kjonn.KVINNE, Landkode.SE, fodselsdato, Sivilstand.GJENLEVENDE_PARTNER);
        trine.setAvdeling("Skeisvang videregående skole");
        Ansatt line = new Ansatt(new Personnavn("Line", "Svendsen"), Kjonn.KVINNE, Landkode.NO, fodselsdato, Sivilstand.SKILT);
        line.setAvdeling("Seksjon for kvalitet, analyse og dimensjonering");
        Ansatt pal = new Ansatt(new Personnavn("Pål", "Persen"), Kjonn.MANN, Landkode.NO, fodselsdato, Sivilstand.GIFT);
        pal.setAvdeling("Rådmannens stab");

        ansatte.add(ole);
        ansatte.add(mari);
        ansatte.add(trine);
        ansatte.add(line);
        ansatte.add(pal);
    }

    public static Ansatt getEmployee() {
        return ansatte.get(0);
    }

    public static List<Ansatt> getEmployees() {
        return ansatte;
    }
}
