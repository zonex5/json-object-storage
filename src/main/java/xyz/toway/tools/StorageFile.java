package xyz.toway.tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

public class StorageFile {

    private final String fileName;

    public StorageFile(String fileName) throws Exception {
        this.fileName = fileName;
        if (Objects.isNull(fileName) || fileName.isBlank()) {
            throw new Exception("Bad storage file name " + fileName);
        }
        File storageFile = new File(fileName);
        if (!storageFile.exists() && !storageFile.createNewFile()) {
            throw new Exception("Can't create file " + fileName);
        }
        if (!storageFile.canWrite() || !storageFile.canRead()) {
            throw new Exception("No permission to write or read file " + fileName);
        }
    }

    public void write(String text) {
        try {
            Files.write(Paths.get(fileName), Collections.singleton(text), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        try {
            return Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
