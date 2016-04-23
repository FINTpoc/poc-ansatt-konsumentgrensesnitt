package no.fk.fint.skate;

import io.swagger.annotations.Api;
import no.skate.Kjonn;
import no.skate.Sivilstand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skate/vokabular")
@Api(tags = "Skate")
public class SkateController {

    @RequestMapping(value = "/sivilstand", method = RequestMethod.GET)
    public Sivilstand[] sivilstand() {
        return Sivilstand.values();
    }

    @RequestMapping(value = "/kjonn", method = RequestMethod.GET)
    public Kjonn[] kjonn() {
        return Kjonn.values();
    }
}
