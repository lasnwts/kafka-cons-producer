package com.kibersystems.kafkaconsproducer.model;

/**
 * Класс Подготовки сообщений
 */

//@Schema(description = "Сообщение в Кафка")
public class KafkaPrepareMessage {
    //  @Schema(example = "10", description = "Количество отправляемых сообщений")
    private int countMessage; //Количество сообщений
    //@Schema(example = "topic-name", description = "Имя топика")
    private String topicName; //Имя топика
    // @Schema(example = "key-100", description = "Ключ сообщения")
    private String key; //Значение ключа в сообщении
    // @Schema(example = "-body-", description = "Тело сообщения")
    private String message; //Строка с сообщением

    public KafkaPrepareMessage(int countMessage, String topicName, String key, String message) {
        this.countMessage = countMessage;
        this.topicName = topicName;
        this.key = key;
        this.message = message;
    }

    public int getCountMessage() {
        return countMessage;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "countMessage=" + countMessage +
                ", topicName='" + topicName + '\'' +
                ", key='" + key + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
