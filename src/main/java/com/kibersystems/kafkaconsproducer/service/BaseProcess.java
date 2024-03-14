package com.kibersystems.kafkaconsproducer.service;

import com.kibersystems.kafkaconsproducer.configure.Configure;
import com.kibersystems.kafkaconsproducer.utils.Supports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

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
        if (files != null && !files.isEmpty()){
            files.forEach(file-> {
                    logger.info("Message to kafka:{}", file.getName());
                    logger.info("Content:{}", supports.getMessageFromFile(file));

            });
        }
    }
}
