package no.fk.fint.employee;

import no.fk.Ansatt;
import no.fk.fint.messaging.MessageBroker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitEmployeeService {

    @Autowired
    private MessageBroker messageBroker;

    public List<Ansatt> getEmployees() {

    }
}
