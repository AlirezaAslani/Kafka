package kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerCooperative {

    private static final Logger log = LoggerFactory.getLogger(ConsumerCooperative.class.getSimpleName());

    public static void main(String[] args) {
       log.info("starting producer");

       String bootStrap_server = "127.0.0.1:9092";
       String topic_name = "kafka_demo";
       String groupId = "consumer_group_cooperative";

       //create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrap_server);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG,"1");

        //create the Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run(){
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

    
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        try {
            //subscribe consumer to our topic(s)
            //to collection of single topic
            //consumer.subscribe(Collections.singleton(topic_name));

            //to collection of multiple topic
            consumer.subscribe(Arrays.asList(topic_name));


            while (true) {
                log.info("Polling");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Key: " + record.key() + " Value: " + record.value());
                    log.info("Partition: " + record.partition() + " Offset: " + record.offset());
                }
            }

        }catch (WakeupException e){
            log.info("Wake up exception!");
        }catch (Exception e){
            log.error("Unexpected error");
        }finally {
            consumer.close(); //this will also commit offset
            log.info("Consumer is now gracefully close");
        }

    }
}
