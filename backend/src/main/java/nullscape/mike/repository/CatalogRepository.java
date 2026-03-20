package nullscape.mike.repository;

import nullscape.mike.model.Catalog;
import nullscape.mike.model.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CatalogRepository {

    //Load each item in from the CSV then add it to the catalog
    public static void makeCatalog() {
        //"Product_ID","Name","Description","Categories","Price","Color","Other_Colors","Product_URL","Thumbnail_URL","Image_URLs"
        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/Nike_Dataset.csv"))) {
            br.readLine(); // Skip the labels
            while (true) {
                String line = br.readLine();

                Item item;
                if (line == null) {
                    break;
                } else {
                    String[] currInfo = parseCSVLine(line);
                    item = new Item();
                    if (currInfo.length >= 9) {
                        item.setId(currInfo[0]);
                        item.setName(currInfo[1]);
                        item.setDescription(currInfo[2]);
                        item.setCategories(cleanArrayString(currInfo[3]));
                        item.setPrice(currInfo[4]);
                        item.setColor(currInfo[5]);
                        item.setOtherColors(cleanArrayString(currInfo[6]));
                        item.setProductUrl(currInfo[7]);
                        item.setThumbnailUrl(currInfo[8]);
                        item.setImages(cleanArrayString(currInfo[9]));
                    }
                }
                Catalog.addItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Nightmare code for handling more or less quotes etc. than expected in the data

    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        // Handle lines that have fewer columns than expected
        while(result.size() < 9) {
            result.add("");
        }
        return result.toArray(new String[0]);
    }

    private static String[] cleanArrayString(String arrString) {
        if (arrString == null || arrString.trim().isEmpty()) return new String[0];
        String cleaned = arrString.trim();
        if (cleaned.startsWith("[")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("]")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        cleaned = cleaned.trim();
        if (cleaned.isEmpty()) return new String[0];
        
        if (cleaned.startsWith("\"")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("\"")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        if (cleaned.startsWith("'")) cleaned = cleaned.substring(1);
        if (cleaned.endsWith("'")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        
        return cleaned.split("[\"']\\s*,\\s*[\"']");
    }
}
