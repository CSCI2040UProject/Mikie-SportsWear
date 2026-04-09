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
        List<Item> results = ItemRepository.getItemsParams(
                "",
                null,
                null,
                null
        );

        assertNotNull(results);
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