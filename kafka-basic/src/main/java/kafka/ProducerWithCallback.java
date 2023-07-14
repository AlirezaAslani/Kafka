package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerWithCallback.class);

    public static void main(String[] args) throws InterruptedException {
       log.info("starting producer");

       //create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create the Producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);


    for(int i=0;i<400;i++){

        //create a producer record
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("kafka_demo","Hello World "+i);

        //send the data - asynchronous
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null) {
                    log.info("Recieved new metadata. \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "timestamp: " + recordMetadata.timestamp() + "\n"+
                            "---------------");
                } else {
                    log.error("Error while producing", e);
                }
            }

        });
        Thread.sleep(1000);
        }

        //flush data - synchronous
        producer.flush();

        //flush and close producer
        producer.close();






    }
}
