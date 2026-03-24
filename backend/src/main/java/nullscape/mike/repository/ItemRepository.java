package nullscape.mike.repository;

import nullscape.mike.model.Catalog;
import nullscape.mike.model.Item;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


public class ItemRepository {
    // This ideally will be where the methods for adding/editing/removing items will be
    // Not sure if we want to allow duplicate items yet, for now we keep track of
    public static Item addItem(String itemID, String itemName, String itemDescription, String itemPrice, String itemTags, String[] imagePaths, String itemColour, String itemOtherColours, byte[][] imageBytes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/Nike_Dataset.csv", true))) {
            // TODO
            // Save images to resources
            for (int i = 0; i < imageBytes.length; i++) {
                System.out.println(imagePaths[i]);
                Path imagePath = Paths.get(imagePaths[i]);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, imageBytes[i]);
            }
            String[] categories = itemTags.split(",");
            String[] otherColours = itemOtherColours.split(",");

            Item newItem = new Item(itemID, itemName, itemDescription, categories, itemPrice, itemColour, otherColours, "", "", imagePaths);
            bw.write(newItem + "\n");
            return newItem;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editItem() {
        // TODO
        // Users will input an item ID on the page. Get the data
        // pertaining to that ID from the dataset, and put it into fields
        // on the page. Users can then manually edit those fields, and the
        // results from those fields will be saved to the dataset.
    }

    public static boolean deleteItem(String id) {
        // Grabs catalog, then matches the given id with the correct item in the catalog
        List<Item> items = Catalog.getItems();
        return items.removeIf(item -> item.getId().equals(id));
    }
}
