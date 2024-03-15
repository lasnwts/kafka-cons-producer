package com.kibersystems.kafkaconsproducer;

import com.kibersystems.kafkaconsproducer.configure.Configure;
import com.kibersystems.kafkaconsproducer.service.BaseProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaConsProducerApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(KafkaConsProducerApplication.class);
	@Value("${info.app.version:none}")
	private String appVersion;
	@Value("${info.app.name:none}")
	private String appName;
	private final Configure configure;
	private final BaseProcess baseProcess;

	@Autowired
	public KafkaConsProducerApplication(Configure configure, BaseProcess baseProcess) {
		this.configure = configure;
		this.baseProcess = baseProcess;
	}
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsProducerApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		logger.info("+-----------------------------------------------------------------------------------------------------------+");
		logger.info(" Created by 08.03.2024   : Author: Lyapustin A.S./ Ляпустин Александр");
		logger.info("-------------------------------------------------------------------------------------------------------------");
		logger.info("| Application Name       :{}", appName);
		logger.info("| Current version        :{}", appVersion);
		logger.info("=------------------------------------------------------------------------------------------------------------=");

		//Установка тестовых параметров
		configure.setRepeatCount(configure.getServiceMessageCount());
		configure.setKey(configure.getServiceMessageKey());
		configure.setTopicName(configure.getServiceTopicName());
		configure.setRandomizeLoad(configure.isServiceSendRandomly());

		//Запускаем процесс
		baseProcess.processSendMessage();
	}
}
