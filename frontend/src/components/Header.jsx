import { Link } from "react-router";
import styles from "../styles/Header.module.css";
import Icon from '@mdi/react';
import { mdiAccount } from '@mdi/js';
const NAV_LINKS = ['Men', 'Women', 'Kids', 'Shoes', 'Accessories'];

const Header = ({username}) => {
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
            </nav>
            <Link to="/profile/">
                    <div>
                    <h3>{newUsername ? ("Hi, " + newUsername) : "Log in"}</h3>
                    <Icon path={mdiAccount} size={1} />
                    </div>
                </Link>
        </header>
    );
};

export default Header;