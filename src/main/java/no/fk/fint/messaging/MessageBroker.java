package no.fk.fint.messaging;

import no.fk.Ansatt;
import no.fk.event.Event;

public interface MessageBroker {
    String sendAndReceive(Event<Ansatt> event);
    void setRoute(String route);
    void setReplyTimeout(long replyTimeout);
}
