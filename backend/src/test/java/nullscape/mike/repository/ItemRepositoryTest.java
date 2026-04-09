package nullscape.mike.repository;

import nullscape.mike.model.Item;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
    public void testDeleteItem() {
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
    public void testDeleteInvalidItem() {
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
        assertEquals("Cheap Item", ascResults.getFirst().getName());

        List<Item> descResults = ItemRepository.getItemsParams(
                null,
                "price-desc",
                new String[]{"test-price-sort"},
                null
        );

        assertEquals(2, descResults.size());
        assertEquals("Expensive Item", descResults.getFirst().getName());

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
        assertEquals("A Item", ascResults.getFirst().getName());

        List<Item> descResults = ItemRepository.getItemsParams(
                null,
                "name-desc",
                new String[]{"test-name-sort"},
                null
        );

        assertEquals(2, descResults.size());
        assertEquals("Z Item", descResults.getFirst().getName());

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
    void testCombinedFilters() {
        Item item = new Item();
        item.setId("combo1");
        item.setName("Combo Test");
        item.setCategories(new String[]{"Men"});
        item.setColor("Blue");

        ItemRepository.addItem(item);

        List<Item> results = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"Men"},
                new String[]{"Blue"}
        );

        assertFalse(results.isEmpty());
    }

    @Test
    void testFilterAndSortTogether() {
        List<Item> results = ItemRepository.getItemsParams(
                null,
                "name-asc",
                new String[]{"Women"},
                null
        );

        assertNotNull(results);

        for (Item item : results) {
            String[] categories = item.getCategories();
            if (categories == null) continue;
            assertTrue(Arrays.asList(categories).contains("Women"));
        }
    }

    @Test
    void testAddAndRetrieveItem() {
        Item item = new Item();
        item.setId("test123");
        item.setName("Test Shoe");
        item.setPrice("$99");
        item.setCategories(new String[]{"Men"});
        item.setColor("Black");

        ItemRepository.addItem(item);

        Item retrieved = ItemRepository.getItemById("test123");

        assertNotNull(retrieved);
        assertEquals("Test Shoe", retrieved.getName());
    }

    @Test
    void testModifyItem() {
        Item item = new Item();
        item.setId("mod123");
        item.setName("Old Name");

        ItemRepository.addItem(item);

        Item updated = new Item();
        updated.setId("mod123");
        updated.setName("New Name");

        ItemRepository.modifyItem(updated);

        Item result = ItemRepository.getItemById("mod123");

        assertEquals("New Name", result.getName());
    }

    @Test
    void testModifyNonExistentItem() {
        Item item = new Item();
        item.setId("does_not_exist");
        item.setName("Ghost Item");

        ItemRepository.modifyItem(item);

        Item result = ItemRepository.getItemById("does_not_exist");

        assertNull(result);
    }

    @Test
    void testRemoveNullId() {
        assertDoesNotThrow(() -> {
            ItemRepository.removeItem(null);
        });
    }

    @Test
    void testAddDeleteThenSearch() {
        Item item = new Item();
        item.setId("cycle1");
        item.setName("Cycle Item");

        ItemRepository.addItem(item);
        ItemRepository.removeItem("cycle1");

        List<Item> results = ItemRepository.getItemsParams(
                "Cycle Item",
                null,
                null,
                null
        );

        assertTrue(results.isEmpty());
    }

    @Test
    void testRepeatedSearchConsistency() {
        List<Item> results1 = ItemRepository.getItemsParams(
                "Jordan", null, null, null
        );

        List<Item> results2 = ItemRepository.getItemsParams(
                "Jordan", null, null, null
        );

        assertEquals(results1.size(), results2.size());
    }

    @Test
    void testCategoryCaseInsensitive() {
        List<Item> lower = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"men"},
                null
        );

        List<Item> upper = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"Men"},
                null
        );

        assertEquals(upper.size(), lower.size());
    }

    @Test
    void testColorCaseInsensitive() {
        List<Item> lower = ItemRepository.getItemsParams(
                null,
                null,
                null,
                new String[]{"black"}
        );

        List<Item> upper = ItemRepository.getItemsParams(
                null,
                null,
                null,
                new String[]{"Black"}
        );

        assertEquals(upper.size(), lower.size());
    }

    @Test
    void testInvalidFilterCombination() {
        List<Item> results = ItemRepository.getItemsParams(
                null,
                null,
                new String[]{"Men"},
                new String[]{"InvisibleColor"}
        );

        assertTrue(results.isEmpty());
    }
}