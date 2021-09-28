package se.liu.ida.tdp024.account.util.kafka;

public enum KafkaTopic {
    REST("bank.rest"),
    TRANSACTION("bank.transaction");

    private final String topic;

    private KafkaTopic(String topic) {
        this.topic = topic;
    }

    public String getString() {
        return this.topic;
    }
}
