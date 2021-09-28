package se.liu.ida.tdp024.account.util.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaUtilImpl implements KafkaUtil {

    private KafkaProducer<String, String> producer;

    public KafkaUtilImpl() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<String, String>(props);
    }

    @Override
    public void publishMessage(KafkaTopic topic, String message) {
        this.producer.send(new ProducerRecord<String,String>(topic.getString(), message));
    }
    
}
