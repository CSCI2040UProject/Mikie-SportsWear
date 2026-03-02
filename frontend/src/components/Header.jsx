import { Link } from "react-router";
import styles from "../styles/Header.module.css";

const Header = () => {
    return (
        <header className={styles.header}>
            <h1><Link to="/">Mike</Link></h1>
            <h3><Link to="/profile/">Profile</Link></h3>
        </header>
    );
};

export default Header;