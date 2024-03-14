package com.kibersystems.kafkaconsproducer.service;

import com.kibersystems.kafkaconsproducer.configure.Configure;
import com.kibersystems.kafkaconsproducer.model.KafkaPrepareMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Класс подготовки сообщений к отправке
 */
@Service
public class ExecuteService {
    Logger logger = LoggerFactory.getLogger(ExecuteService.class);
    private final Configure configure;
    private final KafkaProducerService kafkaProducerService;
    @Autowired
    public ExecuteService(Configure configure, KafkaProducerService kafkaProducerService) {
        this.configure = configure;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Первый вариант инициализации: ExecutorService executorService = Executors.newFixedThreadPool(configure.getServicePoolSize());
     */
    private static ExecutorService executorService;

    //Кол-во потоков задаем
    @Value("${service.pool.size:5}")
    public synchronized boolean setThreadPool(Integer poolSize) {
        try {
            configure.setServicePoolSize(poolSize);
            executorService = java.util.concurrent.Executors.newFixedThreadPool(poolSize);
            logger.info("Задано количество потоков [setThreadPool]={}", poolSize);
            return true;
        } catch (Exception e){
            logger.error("Ошибка при установлении числа потоков:{}", poolSize);
            logger.error("Error:", e);
            return false;
        }
    }

    /**
     * Пул потоков
     *
     * @param messageKafka - сообщение на отправку
     */
    public void getTask(KafkaPrepareMessage messageKafka) {
            configure.setThreads(configure.getThreads() + 1); //Добавляем число в счетчик потоков
            logger.info("UsbLog:Запуск getTask({}) потока...", messageKafka);
            logger.info("UsbLog:Длина очереди задач={}", configure.getThreads());
            CountDownLatch cdl = new CountDownLatch(5);
            try {
                executorService.execute(new MyThread(cdl, messageKafka));
            } catch (Exception e) {
                logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!,ERROR, START EXECUTORS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                logger.error("Error:executorService.execute(new MyThread(cdl, messageBody, messageKafka))", e);
                logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!,ERROR, END EXECUTORS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            logger.info("Поток getTask({}) завершен...", messageKafka);
    }

    class MyThread implements Runnable {
        KafkaPrepareMessage kafkaMessage;
        CountDownLatch latch;
        MyThread(CountDownLatch c, KafkaPrepareMessage messageKafka) {
            latch = c;
            kafkaMessage = messageKafka;
            new Thread(this);
        }

        public void run() {
            logger.info("Запуск потока id={}", Thread.currentThread().getId());
            if (kafkaProducerService.sendMessage(kafkaMessage.getTopicName(), kafkaMessage.getKey(), kafkaMessage.getMessage())) {
                logger.info("Номер потока={},  сообщение отправлено:{}", Thread.currentThread().getId(), kafkaMessage);
            } else {
                logger.error("Error: Ошибка: Номер потока={}, сообщение НЕ отправлено:{}", Thread.currentThread().getId(), kafkaMessage);
            }
            logger.info("Поток завершен id={}", Thread.currentThread().getId());
            configure.setThreads(configure.getThreads() - 1);
            logger.info("Длина очереди задач={}", configure.getThreads());
        }
    }
}
