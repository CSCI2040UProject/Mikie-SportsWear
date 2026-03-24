package nullscape.mike.service;

import nullscape.mike.model.Item;
import nullscape.mike.repository.ItemRepository;

import java.util.Scanner;
import java.util.UUID;

public class ItemService {
    public static Item makeItem(String itemName, String itemDescription, String itemPrice, String itemTags, String itemColour, String itemOtherColours, byte[][] imageBytes) {
        String[] imagePaths = new String[imageBytes.length];
        String itemID = makeItemID();

        for (int i = 0; i < imageBytes.length; i++) {
            imagePaths[i] = "src/resources/img/" + itemID + "/" + UUID.randomUUID() + ".png";
        }
        return ItemRepository.addItem(itemID, itemName, itemDescription, itemPrice, itemTags, imagePaths, itemColour, itemOtherColours, imageBytes);
    }

    public static String makeItemID() {
        int count = 1;

        // Counts every custom item added, returns a new itemID based on
        // how many there are
        try (Scanner scan = new Scanner("src/resources/Nike_Dataset.csv")) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.startsWith("CUSTOM")) {
                    count++;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "CUSTOM" + count;
    }
}
