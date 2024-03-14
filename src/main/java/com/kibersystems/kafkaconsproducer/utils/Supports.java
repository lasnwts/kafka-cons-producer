package com.kibersystems.kafkaconsproducer.utils;

import com.kibersystems.kafkaconsproducer.configure.Configure;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Supports {
    private final SetCharset setCharset;
    private final Configure configure;

    @Autowired
    public Supports(SetCharset setCharset, Configure configure) {
        this.setCharset = setCharset;
        this.configure = configure;
    }

    Logger logger = LoggerFactory.getLogger(Supports.class);

    /**
     * Обертка над значением null в строке
     *
     * @param line - переданная строка
     * @return - строка после проверки на NULL.
     */
    public String getWrapNull(String line) {
        if (line == null) {
            return "";
        } else {
            return line.trim();
        }
    }

    /**
     * Обертка над количеством сообщений
     *
     * @param i - установленное число повторов
     * @return - если число 0 -> вернем 1.
     */
    public int getWrapInt(int i) {
        if (i <= 0) {
            return 1;
        } else {
            return i;
        }
    }

    /**
     * **************************************************
     * Проверка существования шары
     * **************************************************
     *
     * @param targetPath - полный путь к каталогу шары
     * @return - true - если шара есть, false - шары нет
     */
    public boolean checkPathExists(String targetPath) {
        Path path = Paths.get(targetPath);
        if (Files.exists(path)) {
            return true;
        }
        logger.error("Supports:checkPathExists::Error! Directory not exists! Не удалось получить список файлов в директории ::{}", targetPath);
        return false;
    }

    /**
     * Передача всех файлов директории
     *
     * @param directory - имя директории как строка
     * @return - список файлов File
     */
    public List<File> getCurrentDirListFiles(String directory) {
        if (!checkPathExists(directory)) {
            return Collections.emptyList();
        }
        List<Path> pathList = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Paths.get(directory), 1)) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            return pathList.stream().map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("PrintStackTrace:Не удалось получить список файлов в директории:Не удалось получить список файлов в директории:", e);
            return Collections.emptyList();
        }
    }

    /**
     * Подготовка сообщения к отправке. Вычитываем файл, определяем его тип. Если файл бинарный, то преобразуем его в Base64
     *
     * @param file
     * @return
     */
    public String getMessageFromFile(File file) {
        if (getExtensionFromFile(file).equals("xml") || getExtensionFromFile(file).equals("json") || getExtensionFromFile(file).equals("txt")) {
            try {
                return FileUtils.readFileToString(file, setCharset.getCharset(configure.getFileEncode()));
            } catch (IOException e) {
                logger.error("Error read file:{}", file.getAbsolutePath());
                logger.error("Error stack", e);
                return null;
            }
        } else {
            try {
                return encodeFileToBase64Binary(file.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Error read binary file:{}", file.getAbsolutePath());
                logger.error("Error stack", e);
                return null;
            }
        }
    }

    /**
     * Возвращает EXT расширение
     *
     * @param fileName - файл с типом File
     * @return - расширение
     */
    public String getExtensionFromFile(File fileName) {
        String ext = StringUtils.getFilenameExtension(fileName.getName());
        if (ext == null) {
            ext = "";
        }
        return ext;
    }


    /**
     * Чтение файла в Base64
     *
     * @param fileName - имя файла
     * @return - строка Base64
     * @throws IOException
     */
    private String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
