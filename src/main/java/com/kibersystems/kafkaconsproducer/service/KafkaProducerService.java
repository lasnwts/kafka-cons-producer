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
    private String mesLog = null;

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
            logger.info("Send prepare.Topic={};", topic);
            mesLog = getMessageToLog(msg);
            logger.info("Send message={}", mesLog);
            kafkaTemplate.send(topic, msg);
            return true;
        } catch (Exception exception) {
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!<ERROR send message>!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("UsbLog:Error:send failure:topic:{}", topic);
            logger.error("UsbLog:Error:send failure:message:{}", getMessageToLog(msg));
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
            mesLog = getMessageToLog(msg);
            logger.info("Send prepare.Topic={}; Send message={}", topic, mesLog);
            kafkaTemplate.send(topic, key, msg);
            return true;
        } catch (Exception exception) {
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!<ERROR send message>!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("UsbLog:Error:send failure:topic:{}; key:{}", topic, key);
            logger.error("UsbLog:Error:send failure:message:{}", getMessageToLog(msg));
            logger.error("UsbLog:Execution:", exception);
            logger.error("UsbLog:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
    }

    /**
     * Получаем укороченное сообщение для лога
     * @param msg - тело сообщения в кафка
     * @return - возврат сообщегния для лога
     */
    private String getMessageToLog(String msg){
        if (msg == null){
            return "";
        }
        if (msg.length() > 20){
            return msg.trim().substring(0,19);
        }
        return msg;
    }

}
