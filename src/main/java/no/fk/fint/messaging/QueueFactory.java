package no.fk.fint.messaging;

/**
 * Created by FSjovatsen on 15.04.2016.
 */

public class QueueFactory {

    private final String inQueueTemplate = "fint:%s:ansatt:in";
    private final String outQueueTemplate = "fint:%s:ansatt:out";
    private final String inQueue;
    private final String outQueue;

    public QueueFactory(String orgID) {
        inQueue = String.format(inQueueTemplate, orgID);
        outQueue = String.format(outQueueTemplate, orgID);
    }

    public String getOutQueue() {
        return outQueue;
    }

    public String getInQueue() {
        return inQueue;
    }
}
