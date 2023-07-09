package xyz.toway.tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public abstract class BaseStorageService {

    private final String fileName;

    public BaseStorageService(String fileName) throws Exception {
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

    public void writeToFile(String data, boolean binary) {
        try {
            Path path = Paths.get(fileName);
            if (binary) {
                Files.write(path, compressString(data));
            } else {
                Files.writeString(path, data, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't save to the storage file.");
        }
    }

    public String readFromFile(boolean binary) {
        try {
            Path path = Paths.get(fileName);
            return binary ? decompressBytes(Files.readAllBytes(path)) : Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("The storage file is corrupted.");
        }
    }

    /**
     * Generate uid
     */
    protected String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static byte[] compressString(String input) throws IOException {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, new Deflater(1));

        deflaterOutputStream.write(inputBytes);
        deflaterOutputStream.close();

        return outputStream.toByteArray();
    }

    private static String decompressBytes(byte[] compressedBytes) throws IOException {
        if (compressedBytes.length == 0) return "";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBytes);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inflaterInputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inflaterInputStream.close();

        return outputStream.toString();
    }

    protected Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream inputStream = BaseStorageService.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (inputStream != null) {
                    properties.load(inputStream);
                }
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Can't readt application propetries file.");
        }
    }
}
