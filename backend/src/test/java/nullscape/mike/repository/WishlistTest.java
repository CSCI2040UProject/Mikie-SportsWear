package nullscape.mike.repository;

import nullscape.mike.model.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistTest {

    @Test
    void testAddItemToWishlist() {
        String username = "test_user_add";

        WishlistRepository.removeItem(username, "1");

        Item item = new Item();
        item.setId("1");
        item.setName("Test Item 1");
        item.setPrice("10.00");
        item.setImage("image.jpg");

        boolean result = WishlistRepository.addItem(username, item);
        List<Item> wishlist = WishlistRepository.getWishlist(username);

        assertTrue(result);
        assertEquals(1, wishlist.size());
        assertEquals("1", wishlist.get(0).getId());
    }

    @Test
    void testAddDuplicateItemIgnored() {
        String username = "test_user_duplicate";

        WishlistRepository.removeItem(username, "1");

        Item item = new Item();
        item.setId("1");
        item.setName("Test Item 1");
        item.setPrice("10.00");
        item.setImage("image.jpg");

        WishlistRepository.addItem(username, item);
        WishlistRepository.addItem(username, item);

        List<Item> wishlist = WishlistRepository.getWishlist(username);

        assertEquals(1, wishlist.size());
    }

    @Test
    void testRemoveItemFromWishlist() {
        String username = "test_user_remove";

        WishlistRepository.removeItem(username, "1");

        Item item = new Item();
        item.setId("1");
        item.setName("Test Item 1");
        item.setPrice("10.00");
        item.setImage("image.jpg");

        WishlistRepository.addItem(username, item);
        boolean removed = WishlistRepository.removeItem(username, "1");

        List<Item> wishlist = WishlistRepository.getWishlist(username);

        assertTrue(removed);
        assertTrue(wishlist.isEmpty());
    }

    @Test
    void testRemoveNonExistingItem() {
        String username = "test_user_non_existing";

        boolean result = WishlistRepository.removeItem(username, "999");

        assertTrue(result);
    }

    @Test
    void testGetWishlistEmpty() {
        String username = "no_items_user_unique";

        WishlistRepository.removeItem(username, "1");

        List<Item> wishlist = WishlistRepository.getWishlist(username);

        assertNotNull(wishlist);
        assertTrue(wishlist.isEmpty());
    }

    @Test
    void testMultipleItemsInWishlist() {
        String username = "test_user_multiple";

        WishlistRepository.removeItem(username, "1");
        WishlistRepository.removeItem(username, "2");

        Item item1 = new Item();
        item1.setId("1");
        item1.setName("Test Item 1");
        item1.setPrice("10.00");
        item1.setImage("image.jpg");

        Item item2 = new Item();
        item2.setId("2");
        item2.setName("Test Item 2");
        item2.setPrice("10.00");
        item2.setImage("image.jpg");

        WishlistRepository.addItem(username, item1);
        WishlistRepository.addItem(username, item2);

        List<Item> wishlist = WishlistRepository.getWishlist(username);

        assertEquals(2, wishlist.size());
    }
}