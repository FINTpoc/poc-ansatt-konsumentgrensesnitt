package no.fk.fint.employee;

import no.fk.Ansatt;
import no.skate.Identifikator;

import java.util.List;

public interface EmployeeService {
    List<Ansatt> getEmployees(String orgId);

    List<Ansatt> getEmployees(String orgId, String navn);

    Ansatt getEmployee(Identifikator identifikator);

    void updateEmployee(String orgId, Ansatt ansatt);
}
