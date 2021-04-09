package com.aiden.pk.util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringSerializer;


import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducers<K, V>  implements Closeable {


    private KafkaProducer<K, V> producer;

    /**
     * props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrap());
     * props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
     * props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
     * props.put("acks", "all");
     * props.put("retries", "0");
     * props.put("batch.size", "16384");
     *
     */
    public KafkaProducers (Properties producerProps) {
        this.producer = new KafkaProducer<K, V>(producerProps);
    }

    // 同步发送
    public void send(String toppicName, K k, V v) throws InterruptedException, ExecutionException {
        this.producer.send(new ProducerRecord<K, V>(toppicName, k, v)).get();
    }

    // 同步发送
    public void send(String topic, Integer partition, Long timestamp, K key, V value, Iterable<Header> headers) throws InterruptedException, ExecutionException {
        this.producer.send(new ProducerRecord<K, V>(topic, partition, timestamp, key, value, headers)).get();
    }

    @Override
    public void close() throws IOException {
        this.producer.close();
    }

    public void flush() {
        this.producer.flush();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-ox.newegg.org:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaProducers<String, String> pro  = new KafkaProducers<>(props);
        for(int i = 0 ; i < 10000 ; i ++) {
            pro.send("ecbd_test-topic", i +"", i+"");
            Thread.sleep(1000);
        }
        pro.flush();
        System.out.println("-------------------");
    }

}