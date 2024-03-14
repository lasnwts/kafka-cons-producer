package com.kibersystems.kafkaconsproducer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Класс отправки сообщений Кафка
 */
@Service
public class KafkaProducerService {
    Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Простой вариант отправки сообщений, без проверки
     *
     * @param topic - топик
     * @param msg   - сообщение
     */
    public boolean sendMessage(String topic, String msg) {
        try {
            logger.info("Send prepare.Topic={}; Send message={}", topic, msg);
            kafkaTemplate.send(topic, msg);
            return true;
        } catch (Exception exception) {
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!<ERROR send message>!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("UsbLog:Error:send failure:topic:{}", topic);
            logger.error("UsbLog:Error:send failure:message:{}", msg);
            logger.error("UsbLog:Execution:", exception);
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
    }

    /**
     * Простой вариант отправки сообщений, без проверки
     *
     * @param topic - топик
     * @param msg   - сообщение
     */
    public boolean sendMessage(String topic, String key, String msg) {
        try {
            logger.info("Send prepare.Topic={}; Send message={}", topic, msg);
            kafkaTemplate.send(topic, key, msg);
            return true;
        } catch (Exception exception) {
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!<ERROR send message>!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("UsbLog:Error:send failure:topic:{}; key:{}", topic, key);
            logger.error("UsbLog:Error:send failure:message:{}", msg);
            logger.error("UsbLog:Execution:", exception);
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
    }

}
