import { Link } from "react-router";
import styles from "../styles/Header.module.css";
import Icon from '@mdi/react';
import { mdiAccount, mdiHeart } from '@mdi/js';
const NAV_LINKS = ['Men', 'Women', 'Kids',];

const Header = ({username, wishlist}) => {
    const newUsername = username.username;
    return (
        <header className={styles.header}>
            <Link to="/">
                <img src="/Mikie.svg" alt="Mikie" />
            </Link>
            <nav className={styles.nav}>                          {/* ← new */}
                {NAV_LINKS.map(link => (
                    <Link
                        key={link}
                        to={`/catalog?category=${link}`}
                        className={styles.navLink}
                    >
                        {link}
                    </Link>
                ))}
            </nav>  <div className={styles.headerRight}>
            <Link to="/wishlist/" className={styles.wishlistIcon}>
                <Icon path={mdiHeart} size={1} />
                {wishlist && wishlist.length > 0 && (
                    <span className={styles.wishlistBadge}>{wishlist.length}</span>
                )}
            </Link>
            <Link to="/profile/">
                    <div>
                    <h3>{newUsername ? ("Hi, " + newUsername) : "Log in"}</h3>
                    <Icon path={mdiAccount} size={1} />
                    </div>
                </Link>
        </div>
        </header>
    );
};

export default Header;