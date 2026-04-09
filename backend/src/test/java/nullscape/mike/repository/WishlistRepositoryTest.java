package nullscape.mike.repository;

import nullscape.mike.model.Item;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WishlistRepositoryTest {

    private static final String TEST_USER = "user";

    private static Item makeItem( String name, String price) {
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        return item;
    }

    @BeforeEach
    void cleanup() {
        WishlistRepository.removeItem( TEST_USER,"1") ;
        WishlistRepository.removeItem(TEST_USER, "2");
    }

    @Test
    @Order(1)
    void testAddItem() {
        Item item = makeItem( "Air Max 95", "170.00");
        boolean result = WishlistRepository.addItem(TEST_USER, item);
        assertTrue(result);
    }


    @Test
    @Order(2)
    void testGetWishlistEmpty() {
        List<Item> wishlist = WishlistRepository.getWishlist("EMPTY");
        assertNotNull(wishlist);
        assertTrue(wishlist.isEmpty());
    }

    @Test
    @Order(3)
    void testRemoveItem() {
        Item item = makeItem( "Air Max 95", "170.00");
        WishlistRepository.addItem(TEST_USER, item);

        boolean result = WishlistRepository.removeItem(TEST_USER, "1");
        assertTrue(result);

        List<Item> wishlist = WishlistRepository.getWishlist(TEST_USER);
        boolean stillExists = wishlist.stream().anyMatch(i -> "1".equals(i.getId()));
        assertFalse(stillExists);
    }

    @Test
    @Order(4)
    void testRemoveItemtDoesNotThrow() {
        boolean result = WishlistRepository.removeItem(TEST_USER, "NULL");
        assertTrue(result);
    }

    @Test
    @Order(5)
    void testGetMultipleItems() {
        Item item1 = makeItem("Air Max 95", "170.00");
        Item item2 = makeItem( "Air Force 1", "150.00");
        WishlistRepository.addItem(TEST_USER, item1);
        WishlistRepository.addItem(TEST_USER, item2);

        List<Item> wishlist = WishlistRepository.getWishlist(TEST_USER);
        long count = wishlist.stream()
                .filter(i -> i.getId().equals("1") || i.getId().equals("2"))
                .count();
        assertEquals(2, count);
    }
}