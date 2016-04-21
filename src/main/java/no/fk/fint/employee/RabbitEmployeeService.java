package no.fk.fint.employee;

import no.fk.Ansatt;
import no.fk.fint.messaging.MessageBroker;
import no.fk.fint.messaging.RabbitMessaging;
import no.skate.Identifikator;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitEmployeeService implements EmployeeService {

    @Autowired
    private ObjectFactory<RabbitMessaging> rabbitMessaging;

    @Override
    public List<Ansatt> getEmployees(String orgId) {
        return null;
    }

    @Override
    public List<Ansatt> getEmployees(String orgId, String navn) {
        return null;
    }

    @Override
    public Ansatt getEmployee(Identifikator identifikator) {
        return null;
    }

    @Override
    public void updateEmployee(String orgId, Ansatt ansatt) {

    }
}
