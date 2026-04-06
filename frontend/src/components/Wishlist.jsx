import { useOutletContext } from "react-router";
import { useNavigate } from "react-router";
import styles from "../styles/Wishlist.module.css";

function Wishlist() {
    const { loggedIn, wishlist, setWishlist } = useOutletContext();
    const navigate = useNavigate();

    async function removeFromWishlist(productId) {
        try {
            const res = await fetch('/api/wishlist/', {
                method: 'DELETE',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ productId })
            });
            if (res.ok) {
                setWishlist(prev => prev.filter(item => item.id !== productId));
            }
        } catch (error) {
            console.log(error);
        }
    }

    if (!loggedIn) {
        return (
            <div className={styles.empty}>
                <p>Please <a href="/profile/">log in</a> to view your wishlist.</p>
            </div>
        );
    }

    if (!wishlist || wishlist.length === 0) {
        return (
            <div className={styles.empty}>
                <p>Your wishlist is empty!</p>
            </div>
        );
    }

    return (
        <div className={styles.container}>
            <h2>Your Wishlist</h2>
            <div className={styles.grid}>
                {wishlist.map(item => (
                    <div key={item.id} className={styles.card}>
                        <img
                            src={item.image ?? item.images?.[0]}
                            alt={item.name}
                            onClick={() => navigate(`/catalog/item/${item.id}`)}
                            className={styles.cardImage}
                        />
                        <h3>{item.name}</h3>
                        <p>${item.price}</p>
                        <div className={styles.cardButtons}>
                            <button onClick={() => navigate(`/catalog/item/${item.id}`)}>
                                View Item
                            </button>
                            <button onClick={() => removeFromWishlist(item.id)}>
                                ♥ Remove
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Wishlist;