package com.jpmc.midascore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;


@Component
public class FileLoader {
    private static final Logger logger = LoggerFactory.getLogger(FileLoader.class);

    public String[] loadStrings(String path) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                logger.error("File not found: {}", path);
                throw new IllegalArgumentException("File not found: " + path);
            }
            String fileText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return fileText.split(System.lineSeparator());
        } catch (Exception e) {
            logger.error("Error loading file: {}", path, e);
            return new String[0];
        }
    }
}