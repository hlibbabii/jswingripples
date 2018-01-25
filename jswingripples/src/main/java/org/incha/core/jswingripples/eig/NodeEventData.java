package org.incha.core.jswingripples.eig;

public class NodeEventData {
    private final Status status;
    private final String probability;

    private NodeEventData(Status status, String probability) {
        this.status = status;
        this.probability = probability;
    }

    public static NodeEventData create(Status status) {
        return new NodeEventData(status, null);
    }

    public static NodeEventData create(String probability) {
        return new NodeEventData(null, probability);
    }

    public Status getStatus() {
        return status;
    }

    public String getProbability() {
        return probability;
    }
}
