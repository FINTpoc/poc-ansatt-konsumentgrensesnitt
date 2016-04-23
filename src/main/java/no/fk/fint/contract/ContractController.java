package no.fk.fint.contract;

import io.swagger.annotations.Api;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "Contract")
@RestController
@RequestMapping(value = "/contracts", method = RequestMethod.GET)
public class ContractController {

    @RequestMapping("/ansatt.xsd")
    public void getEmployeeContract(HttpServletResponse response) throws IOException {
        getFile("ansatt.xsd", response);
    }

    @RequestMapping("/person.xsd")
    public void getPersonContract(HttpServletResponse response) throws IOException {
        getFile("person.xsd", response);
    }

    @RequestMapping("/event.xsd")
    public void getEventContract(HttpServletResponse response) throws IOException {
        getFile("event.xsd", response);
    }

    private void getFile(String fileName, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", MediaType.APPLICATION_XML_VALUE);
        InputStream file = this.getClass().getClassLoader().getResourceAsStream(fileName);
        IOUtils.copy(file, response.getOutputStream());
        response.flushBuffer();
    }
}
