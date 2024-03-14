package com.kibersystems.kafkaconsproducer.utils;

import com.kibersystems.kafkaconsproducer.configure.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SetCharset {
    private final Configure configure;

    @Autowired
    public SetCharset(Configure configure) {
        this.configure = configure;
    }

    Logger logger = LoggerFactory.getLogger(SetCharset.class);

    /**
     * Определяем charset
     *
     * @param sCharSet строка с наименованием charset: utf-8, utf-16, Windows-1251, ISO-8859-2
     * @return - возвращаем Charset
     */
    public Charset getCharset(String sCharSet) {
        try {
            return Charset.forName(sCharSet.trim().toUpperCase());
        } catch (Exception e) {
            logger.error("Error determined charset:{}", sCharSet.trim().toUpperCase());
            logger.error("Error stack:", e);
            return StandardCharsets.UTF_8;
        }
    }

    /**
     * Получение строки в Base64 для передачи в ЦФТ
     *
     * @param message - строка
     * @return - base64(СТРОКА)
     */
    public String getBase64(String message) {
        if (message == null) {
            logger.error("UsbLog:ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("UsbLog:BaseProcess:getBase64.message = NULL!!");
            logger.error("UsbLog:ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            //Отправляем письмо
            return "";
        }
        return Base64.getEncoder().encodeToString(message.getBytes());
    }


}
