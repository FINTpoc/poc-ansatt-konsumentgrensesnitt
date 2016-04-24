package no.fk.fint.skate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.skate.Kjonn;
import no.skate.Sivilstand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Skate")
@RestController
@RequestMapping("/skate/vokabular")
public class SkateController {

    @ApiOperation("Sivilstand")
    @RequestMapping(value = "/sivilstand", method = RequestMethod.GET)
    public Sivilstand[] sivilstand() {
        return Sivilstand.values();
    }

    @ApiOperation("Kjønn")
    @RequestMapping(value = "/kjonn", method = RequestMethod.GET)
    public Kjonn[] kjonn() {
        return Kjonn.values();
    }
}
