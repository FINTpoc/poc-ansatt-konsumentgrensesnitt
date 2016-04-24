package no.fk.fint.employee;

import no.fk.Ansatt;
import no.fk.event.EventResponse;
import no.skate.Identifikator;

import java.util.List;

public interface EmployeeService {
    List<Ansatt> getEmployees(String orgId);

    List<Ansatt> getEmployees(String orgId, String navn);

    Ansatt getEmployee(String orgId, Identifikator identifikator);

    EventResponse updateEmployee(String orgId, Ansatt ansatt);
}
