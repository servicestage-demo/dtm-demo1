package com.huawei.kafka.service.config;

import com.huawei.kafka.service.model.KafkaTemplate;
import com.huawei.middleware.dtm.client.tcc.kafka.DtmKafkaProducer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class WebConfig {

    @Bean
    public DtmKafkaProducer<String, String> dtmKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("acks", "all");
        props.put("batch.size", 16384);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        // 必须设置的
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "dtm-kafka-tx");
        props.put("retries", 3);
        DtmKafkaProducer<String, String> producer = new DtmKafkaProducer<>(props);
        producer.initTransactions();
        return producer;
    }

    @Bean
    public KafkaTemplate kafkaTemplate(@Qualifier("dtmKafkaProducer") DtmKafkaProducer<String, String> producer) {
        return new KafkaTemplate(producer);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
