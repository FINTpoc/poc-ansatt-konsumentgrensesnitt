package no.fk.fint.employee;

public enum Events {
    GET_EMPLOYEES("getEmployees"),
    GET_EMPLOYEE("getEmployee"),
    UPDATE_EMPLOYEE("updateEmployee");

    private final String verb;

    Events(String verb) {
        this.verb = verb;
    }

    public String verb() {
        return verb;
    }
}
