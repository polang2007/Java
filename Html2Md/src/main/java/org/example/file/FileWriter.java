package org.example.file;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileWriter {
    public static void writeToFile(String fileName, String content) {
        File file = new File(fileName);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
