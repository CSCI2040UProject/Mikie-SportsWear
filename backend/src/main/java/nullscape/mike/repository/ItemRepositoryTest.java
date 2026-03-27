package nullscape.mike.repository;

import nullscape.mike.model.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRepositoryTest {

    @Test
    public void testAddItem() {
        Item item = new Item();
        item.setId("test123");
        item.setName("Test Item");

        ItemRepository.addItem(item);

        Item result = ItemRepository.getItemById("test123");

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
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
    }
}