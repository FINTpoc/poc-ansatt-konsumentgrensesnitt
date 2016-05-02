package no.fk.fint.messaging;

import no.fk.event.Event;

public interface MessageBroker {

    Event<?> sendAndReceive(Event<?> event);

}
