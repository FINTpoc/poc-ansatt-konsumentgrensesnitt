package no.fk.fint.employee.event;

import no.fk.event.Event;
import no.fk.event.Type;
import no.fk.fint.employee.Events;

public class RequestEvent<T> extends Event<T> {
    public RequestEvent(String orgId, Events eventType) {
        super(orgId, eventType.verb(), Type.REQUEST);
    }
}
