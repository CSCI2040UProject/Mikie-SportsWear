package nullscape.mike.repository;

import nullscape.mike.model.Item;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRepositoryTest {

    @Test
    public void testAddItem() {

        Item item = new Item();
        item.setId("test123");
        item.setName("Test Item");

        ItemRepository.addItem(item);

        Item fetched = ItemRepository.getItemById(item.getId());

        assertNotNull(fetched);

        ItemRepository.removeItem("test123");
    }

    @Test
    public void testAddNullItem() {

        Item item = new Item();
        item.setId("test123");
        item.setName(null);

        ItemRepository.addItem(item);

        Item fetched = ItemRepository.getItemById(item.getId());

        assertNull(fetched);

        ItemRepository.removeItem("test123");
    }

    @Test
    public void testAddDupeItem() {

        Item item = new Item();
        item.setId("test123");
        item.setName("dupe");

        Item item2 = new Item();
        item2.setId("test123");
        item2.setName("dupe2");

        ItemRepository.addItem(item);
        ItemRepository.addItem(item2);

        Item fetched = ItemRepository.getItemById("test123");

        assertNotNull(fetched);
        assertEquals("dupe", fetched.getName()); // second item should NOT overwrite

        ItemRepository.removeItem("test123");
    }

    @Test
    public void testDeleteItem() { //Valid Delete Test

        Item item = new Item();
        item.setId("delete123");
        item.setName("Delete Item");

        ItemRepository.addItem(item);

        ItemRepository.removeItem("delete123");

        Item deleted = ItemRepository.getItemById("delete123");

        assertNull(deleted);

        ItemRepository.removeItem("delete123");
    }

    @Test
    public void testDeleteInvalidItem() {//Invalid Delete test

        Item item = new Item();
        item.setId("delete123");
        item.setName("Valid Item");

        ItemRepository.addItem(item);

        ItemRepository.removeItem("invalid999");

        Item stillExists = ItemRepository.getItemById("delete123");

        assertNotNull(stillExists);
        assertEquals("Valid Item", stillExists.getName());

        ItemRepository.removeItem("delete123");
    }

    @Test
    public void testDeleteItemNullId() { //delete null item
        assertDoesNotThrow(() -> {
            ItemRepository.removeItem(null);
        });
    }

    @Test
    public void testFilterByCategory() {

        Item item1 = new Item();
        item1.setId("cat1-test");
        item1.setName("Shirt");
        item1.setPrice("$10");
        item1.setCategories(new String[]{"test-men"});
        item1.setColor("black");

        Item item2 = new Item();
        item2.setId("cat2-test");
        item2.setName("Dress");
        item2.setPrice("$20");
        item2.setCategories(new String[]{"test-women"});
        item2.setColor("red");

        ItemRepository.removeItem("cat1-test");
        ItemRepository.removeItem("cat2-test");

        ItemRepository.addItem(item1);
        ItemRepository.addItem(item2);

        List<Item> results = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"test-men"},
                new String[]{"black"}
        );

        assertEquals(1, results.size());
        assertEquals("Shirt", results.get(0).getName());

        ItemRepository.removeItem("cat1-test");
        ItemRepository.removeItem("cat2-test");
    }

    @Test
    public void testSortByPrice() {

        Item item1 = new Item();
        item1.setId("price1-test");
        item1.setName("Cheap Item");
        item1.setPrice("$10");
        item1.setCategories(new String[]{"test-price-sort"});
        item1.setColor("black");

        Item item2 = new Item();
        item2.setId("price2-test");
        item2.setName("Expensive Item");
        item2.setPrice("$20");
        item2.setCategories(new String[]{"test-price-sort"});
        item2.setColor("red");

        ItemRepository.removeItem("price1-test");
        ItemRepository.removeItem("price2-test");

        ItemRepository.addItem(item1);
        ItemRepository.addItem(item2);

        List<Item> ascResults = ItemRepository.getItemsParams(
                null,
                "price-asc",
                new String[]{"test-price-sort"},
                null
        );

        assertEquals(2, ascResults.size());
        assertEquals("Cheap Item", ascResults.get(0).getName());

        List<Item> descResults = ItemRepository.getItemsParams(
                null,
                "price-desc",
                new String[]{"test-price-sort"},
                null
        );

        assertEquals(2, descResults.size());
        assertEquals("Expensive Item", descResults.get(0).getName());

        ItemRepository.removeItem("price1-test");
        ItemRepository.removeItem("price2-test");
    }

    @Test
    public void testSortByName() {

        Item item1 = new Item();
        item1.setId("name1-test");
        item1.setName("A Item");
        item1.setPrice("$10");
        item1.setCategories(new String[]{"test-name-sort"});
        item1.setColor("black");

        Item item2 = new Item();
        item2.setId("name2-test");
        item2.setName("Z Item");
        item2.setPrice("$20");
        item2.setCategories(new String[]{"test-name-sort"});
        item2.setColor("red");

        ItemRepository.removeItem("name1-test");
        ItemRepository.removeItem("name2-test");

        ItemRepository.addItem(item2);
        ItemRepository.addItem(item1);

        List<Item> ascResults = ItemRepository.getItemsParams(
                null,
                "name-asc",
                new String[]{"test-name-sort"},
                null
        );

        assertEquals(2, ascResults.size());
        assertEquals("A Item", ascResults.get(0).getName());

        List<Item> descResults = ItemRepository.getItemsParams(
                null,
                "name-desc",
                new String[]{"test-name-sort"},
                null
        );

        assertEquals(2, descResults.size());
        assertEquals("Z Item", descResults.get(0).getName());

        ItemRepository.removeItem("name1-test");
        ItemRepository.removeItem("name2-test");
    }

    @Test
    public void testGetItemsParamsCategoryMen() {

        // Act
        List<Item> result = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"Men"},
                null
        );

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetItemsParamsCategoryNull() {

        List<Item> result = ItemRepository.getItemsParams(
                null,
                null,
                null,
                null
        );

        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetItemsParamsInvalidCategory() {

        List<Item> result = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"obama"},
                null
        );

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetItemsParamsPriceAscending() {

        List<Item> result = ItemRepository.getItemsParams(
                null,
                "price-asc",
                null,
                null
        );

        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size() - 1; i++) {

            String p1 = result.get(i).getPrice();
            String p2 = result.get(i + 1).getPrice();

            if (p1 == null || p2 == null) continue;

            double price1 = Double.parseDouble(p1.replace("$","").replace(",",""));
            double price2 = Double.parseDouble(p2.replace("$","").replace(",",""));

            assertTrue(price1 <= price2);
        }
    }

    @Test
    public void testGetItemsParamsPriceDescending() {

        List<Item> result = ItemRepository.getItemsParams(
                null,
                "price-desc",
                null,
                null
        );

        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size() - 1; i++) {

            String p1 = result.get(i).getPrice();
            String p2 = result.get(i + 1).getPrice();

            if (p1 == null || p2 == null) continue;

            double price1 = Double.parseDouble(result.get(i).getPrice().replace("$",""));
            double price2 = Double.parseDouble(result.get(i + 1).getPrice().replace("$",""));

            assertTrue(price1 >= price2);
        }
    }

    @Test
    public void testGetItemsParamsNameAscending() {

        List<Item> result = ItemRepository.getItemsParams(
                null,
                "name-asc",
                null,
                null
        );

        List<Item> filtered = result.stream()
                .filter(i -> i.getName() != null)
                .toList();

        for (int i = 0; i < filtered.size() - 1; i++) {
            String name1 = filtered.get(i).getName();
            String name2 = filtered.get(i + 1).getName();

            assertTrue(name1.compareToIgnoreCase(name2) <= 0);
        }
    }

    @Test
    public void testGetItemsParamsNameDescending() {

        List<Item> result = ItemRepository.getItemsParams(
                null,
                "name-desc",
                null,
                null
        );

        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size() - 1; i++) {
            String name1 = result.get(i).getName();
            String name2 = result.get(i + 1).getName();

            assertTrue(name1.compareToIgnoreCase(name2) >= 0);
        }
    }


}
