package nullscape.mike.model;

import nullscape.mike.repository.ItemRepository;

import java.util.List;

public class Catalog {

    public static List<Item> getAllItems() {
        return ItemRepository.getAllItems();
    }

    public static Item getItemById(String productId) {
        return ItemRepository.getItemById(productId);
    }

    public static void addItem(Item item) {
        ItemRepository.addItem(item);
    }

    public static void printCatalog() {
        List<Item> items = getAllItems();
        System.out.println("Catalog{catalog=" + items + "}");
    }
}
