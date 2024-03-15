package com.kibersystems.kafkaconsproducer.service;

import com.kibersystems.kafkaconsproducer.configure.Configure;
import com.kibersystems.kafkaconsproducer.model.KafkaPrepareMessage;
import com.kibersystems.kafkaconsproducer.utils.Supports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseProcess {

    private final Configure configure;
    private final ExecuteService executeService;
    private final Supports supports;

    @Autowired
    public BaseProcess(Configure configure, ExecuteService executeService, Supports supports) {
        this.configure = configure;
        this.executeService = executeService;
        this.supports = supports;
    }

    Logger logger = LoggerFactory.getLogger(BaseProcess.class);

    /**
     *
     */
    public void processSendMessage() {
        logger.info("Start process...");
        List<File> files = supports.getCurrentDirListFiles(configure.getServiceCatalogMessages());
        List<KafkaPrepareMessage> prepareMessages = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                logger.info("Prepare Message to kafka:{}", file.getName());
                prepareMessages.add(new KafkaPrepareMessage(configure.getRepeatCount(),
                        configure.getTopicName(), configure.getKey(), supports.getMessageFromFile(file)));
            });
            if (configure.getRepeatCount() <= files.size()) {
                logger.info("Files count={} in directory:{} more than repeat message count:{}.", files.size(),
                        configure.getServiceCatalogMessages(), configure.getRepeatCount());
                sendMessageCycle(prepareMessages,files.size());
            } else {
                logger.info("Files count={} in directory:{} fewer than repeat message count:{}.", files.size(),
                        configure.getServiceCatalogMessages(), configure.getRepeatCount());
                int j = configure.getRepeatCount() % files.size();
                for (int i = 0; i <= configure.getRepeatCount() % files.size(); i++){
                    sendMessageCycle(prepareMessages,j);
                }
                int k = files.size() * j - configure.getRepeatCount();
                sendMessageCycle(prepareMessages,files.size() * j - configure.getRepeatCount());
            }
        }
    }

    /**
     * Отправка сообщений в Кафка когда число файлов больше чем число заданных сообщений
     *
     * @param prepareMessages - сообщение
     * @param cycleCount - количество итераций
     */
    private void sendMessageCycle(List<KafkaPrepareMessage> prepareMessages, int cycleCount) {
        for (int i = 0; i <= cycleCount; i++) {
            executeService.getTask(prepareMessages.get(i));
        }
    }

}
