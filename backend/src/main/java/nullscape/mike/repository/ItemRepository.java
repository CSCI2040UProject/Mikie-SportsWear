package nullscape.mike.repository;

import nullscape.mike.model.Item;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ItemRepository {
    // This ideally will be where the methods for adding/editing/removing items will be
    // Not sure if we want to allow duplicate items yet, for now we keep track of

    public Item addItem(String itemName, String itemDescription, String itemPrice, String itemTags, File[] itemImages) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("backend/src/resources/Nike_Dataset.csv", true))) {
            return new Item();
            // TODO
            // It is 1:21 AM and I am so tired I'll probably fix this in the morning
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editItem() {
        // TODO
    }

    public void deleteItem() {
        // TODO
    }
}
