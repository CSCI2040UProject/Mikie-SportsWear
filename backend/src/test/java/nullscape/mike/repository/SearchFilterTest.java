package nullscape.mike.repository;

import nullscape.mike.model.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchFilterTest {

    @Test
    void testPartialSearch() {
        List<Item> results = ItemRepository.getItemsParams(
                "ord",
                null,
                null,
                null
        );

        assertFalse(results.isEmpty());
    }

    @Test
    void testNullSearch(){
        List<Item> allItems = ItemRepository.getAllItems();
        List<Item> results = ItemRepository.getItemsParams(
                null,
                null,
                null,
                null
        );
        assertNotNull(results);
        assertEquals(allItems.size(), results.size());

        for (Item item : results) {
            assertNotNull(item.getId());
        }
    }

    @Test
    void testNothingSearch(){
        List<Item> results = ItemRepository.getItemsParams(
                "nothing",
                null,
                null,
                null
        );
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearch() {
        String testId = "jordan-search-test";
        Item testItem = new Item();
        testItem.setId(testId);
        testItem.setName("Jordan Search Test Item");
        testItem.setDescription("Jordan description");

        ItemRepository.removeItem(testId);
        ItemRepository.addItem(testItem);

        List<Item> results = ItemRepository.getItemsParams("Jordan", null, null, null);

        assertFalse(results.isEmpty());
        for (Item item : results) {
            String name = item.getName() == null ? "" : item.getName().toLowerCase();
            String description = item.getDescription() == null ? "" : item.getDescription().toLowerCase();
            assertTrue(name.contains("jordan") || description.contains("jordan"));
        }


        ItemRepository.removeItem(testId);
    }


    @Test
    void testSearchSpecialCharactersMixed() {
        List<Item> results = ItemRepository.getItemsParams(
                "Jordan@123!",
                null,
                null,
                null
        );

        assertNotNull(results);
    }

    @Test
    void testVeryLongSearchString() {
        String longInput = "a".repeat(1000);

        List<Item> results = ItemRepository.getItemsParams(
                longInput,
                null,
                null,
                null
        );

        assertNotNull(results);
    }

    @Test
    void testEmptySearchReturnsAll() {
        List<Item> allItems = ItemRepository.getAllItems();
        List<Item> results = ItemRepository.getItemsParams(
                "",
                null,
                null,
                null
        );

        assertNotNull(results);
        assertEquals(allItems.size(), results.size());

        for (Item item : results) {
            assertNotNull(item.getId());
        }



    }

    @Test
    void testSearchAndCategoryFilter() {
        List<Item> results = ItemRepository.getItemsParams(
                "Jordan",
                null,
                new String[]{"Men"},
                null
        );

        for (Item item : results) {
            assertTrue(item.getName().toLowerCase().contains("jordan"));
        }
    }

    @Test
    void testSQLInjectionInput() {
        String malicious = "Jordan'; DROP TABLE items; --";

        List<Item> results = ItemRepository.getItemsParams(
                malicious,
                null,
                null,
                null
        );

        assertNotNull(results);
    }
}
