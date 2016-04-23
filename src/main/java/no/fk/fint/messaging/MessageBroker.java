package no.fk.fint.messaging;

import no.fk.event.Event;

public interface MessageBroker {

    <T> T sendAndReceive(Event<?> event, Class<T> responseType);

}
