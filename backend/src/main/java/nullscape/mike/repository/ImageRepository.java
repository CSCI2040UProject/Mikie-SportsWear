package nullscape.mike.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;



public class ImageRepository {

    public static String generateImagePath(String fileName) {
        return "http://localhost:8080/api/images/" + fileName;
    }


    public static String addImage(InputStream input) {
        final String UPLOAD_DIR = "uploads/";
        String fileName;

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            fileName = UUID.randomUUID().toString() + ".jpg";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return generateImagePath(fileName);
    }
}

