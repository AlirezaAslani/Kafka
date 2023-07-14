package io.demos.kafka.stream;

import io.demos.kafka.stream.processor.BotCountStreamBuilder;
import io.demos.kafka.stream.processor.EventCountTimeseriesBuilder;
import io.demos.kafka.stream.processor.WebsiteCountStreamBuilder;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class WikimediaStreamsApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaStreamsApp.class);
    private static final Properties properties;
    private static final String INPUT_TOPIC = "wikimedia.recentchange";
    private static final String OUTPUT_RECENTCHANGE_TOPIC = "wikimedia.recentchange";


    static{
        properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG,"wikimedia-stream-app");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    }

    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,String> changeJsonStream = builder.stream(INPUT_TOPIC);

        StreamsBuilder outBuilder = new StreamsBuilder();
        KStream<String,String> outChangeJsonStream = builder.stream(OUTPUT_RECENTCHANGE_TOPIC);

        BotCountStreamBuilder botCountStreamBuilder = new BotCountStreamBuilder(changeJsonStream);
        botCountStreamBuilder.setup();

        WebsiteCountStreamBuilder websiteCountStreamBuilder = new WebsiteCountStreamBuilder(changeJsonStream);
        websiteCountStreamBuilder.setup();

        EventCountTimeseriesBuilder eventCountTimeseriesBuilder = new EventCountTimeseriesBuilder(changeJsonStream);
        eventCountTimeseriesBuilder.setup();

        final Topology appTopology = builder.build();
        LOGGER.info("Topology: {}", appTopology.describe());
        KafkaStreams streams = new KafkaStreams(appTopology, properties);
        streams.start();
    }
}
