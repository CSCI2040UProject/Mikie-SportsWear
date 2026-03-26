package nullscape.mike.repository;

public class ImageRepository {

    public static String generateImagePath(String fileName) {
        return "/uploads/" + fileName;
    }
}