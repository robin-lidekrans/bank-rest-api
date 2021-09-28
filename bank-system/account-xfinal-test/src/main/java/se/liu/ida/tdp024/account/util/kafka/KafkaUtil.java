package se.liu.ida.tdp024.account.util.kafka;

public interface KafkaUtil {

    public void publishMessage(KafkaTopic topic, String message);

}
