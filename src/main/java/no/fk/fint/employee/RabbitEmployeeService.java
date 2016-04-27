package no.fk.fint.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fk.Ansatt;
import no.fk.event.Event;
import no.fk.event.EventResponse;
import no.fk.fint.employee.event.RequestEvent;
import no.fk.fint.messaging.RabbitMessaging;
import no.skate.Identifikator;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableRabbit
@Profile("!mock")
@Service
public class RabbitEmployeeService implements EmployeeService {

    @Autowired
    private RabbitMessaging rabbitMessaging;

    @Override
    public List<Ansatt> getEmployees(String orgId) {
        Event event = new RequestEvent(orgId, Events.GET_EMPLOYEES);
        Event eventResponse = rabbitMessaging.sendAndReceive(event, Event.class);
        Ansatt[] employees = new ObjectMapper().convertValue(eventResponse.getData(), Ansatt[].class);
        return Arrays.asList(employees);
    }

    @Override
    public List<Ansatt> getEmployees(String orgId, String name) {
        String searchName = name.toLowerCase();
        List<Ansatt> employees = getEmployees(orgId);
        return employees.stream().filter(employee -> {
            String firstName = employee.getNavn().getFornavn().toLowerCase();
            String lastName = employee.getNavn().getEtternavn().toLowerCase();
            return ((firstName.contains(searchName) || lastName.contains(searchName)) || (searchName.contains(firstName) || searchName.contains(lastName)));
        }).collect(Collectors.toList());
    }

    @Override
    public Ansatt getEmployee(String orgId, Identifikator identifikator) {
        Event event = new RequestEvent(orgId, Events.GET_EMPLOYEE);
        ArrayList arrayList = new ArrayList();
        arrayList.add(identifikator);
        event.setData(arrayList);
        Event eventResponse = rabbitMessaging.sendAndReceive(event, Event.class);
        Ansatt[] employee = new ObjectMapper().convertValue(eventResponse.getData(), Ansatt[].class);
        return employee[0];
    }

    @Override
    public EventResponse updateEmployee(String orgId, Ansatt ansatt) {
        Event event = new RequestEvent(orgId, Events.UPDATE_EMPLOYEE);
        ArrayList arrayList = new ArrayList();
        arrayList.add(ansatt);
        event.setData(arrayList);

        Event eventResponse = rabbitMessaging.sendAndReceive(event, Event.class);
        EventResponse[] responseReturn = new ObjectMapper().convertValue(eventResponse.getData(), EventResponse[].class);
        return responseReturn[0];
    }
}
