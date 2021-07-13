package com.aiden.pk.util;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumers<K, V>  implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumers.class);
    private KafkaConsumer<K, V> consumer;

    /**
     *   props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.getBootstrap());
     *   props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroup());
     *   props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
     *   props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
     *   props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
     */
    public KafkaConsumers (Properties consumerProps) {
        this.consumer = new KafkaConsumer(consumerProps);
    }


    public KafkaConsumer<K, V> subscribe(String topicName) {
        this.consumer.subscribe(Collections.singleton(topicName));
        return this.consumer;
    }
    public KafkaConsumer<K, V> assign(TopicPartition topicPartition) {
        this.consumer.assign(Collections.singleton(topicPartition));
        return this.consumer;
    }
    public KafkaConsumer<K, V> seek(TopicPartition topicPartition,Long offset) {
        this.consumer.seek(topicPartition,offset);
        return this.consumer;
    }
    public ConsumerRecords<K, V> poll(Duration duration) {
        return this.consumer.poll(duration);
    }

    public void commit() {
        this.consumer.commitSync();
    }
    public void commitByPartition(Map<TopicPartition, OffsetAndMetadata> offsets){
        this.consumer.commitSync(offsets);
    }


    @Override
    public void close() throws IOException {

        this.consumer.close();
    }


    public static void main(String[] args) {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-ox.newegg.org:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "ecbd_test-topic-consumer");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  StringDeserializer.class);
        KafkaConsumers<String, String>  consumer  = new KafkaConsumers<>(consumerProps);

        consumer.subscribe("ecbd_test-topic");
        try {
            while(true) {
                ConsumerRecords<String, String>  records  = consumer.poll(Duration.ofMillis(100));
                for(ConsumerRecord<String, String> record : records) {
                    LOGGER.info("consuming from topic = {}, partition = {}, ts = {}, ts-type = {},  offset = {}, key = {}, value = {}",
                            record.topic(), record.partition(), record.timestamp(), record.timestampType(), record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }finally {
            try {
                consumer.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
