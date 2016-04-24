package no.fk.fint.messaging;

public enum QueueFactory {
    ;

    private static final String IN_QUEUE_TEMPLATE = "fint:%s:employee:in";
    private static final String OUT_QUEUE_TEMPLATE = "fint:%s:employee:out";

    public static String getInQueue(String orgId) {
        return String.format(IN_QUEUE_TEMPLATE, orgId);
    }

    public static String getOutQueue(String orgId) {
        return String.format(OUT_QUEUE_TEMPLATE, orgId);
    }
}
