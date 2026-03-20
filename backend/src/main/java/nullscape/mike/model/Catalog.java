package nullscape.mike.model;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
    public static final List <Item> catalog = new ArrayList<Item>();

    public static void addItem(Item item){
        catalog.add(item);
    }

    public static List<Item> getItems() {
        return catalog;
    }

    public static void printCatalog() {
        System.out.println("Catalog{catalog=" + catalog + "}");
    } //For testing
}
