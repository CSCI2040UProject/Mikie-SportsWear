import { Link } from "react-router";
import styles from "../styles/Header.module.css";
import Icon from '@mdi/react';
import { mdiAccount } from '@mdi/js';

const Header = ({username}) => {
    const newUsername = username.username;
    return (
        <header className={styles.header}>
            <Link to="/">
                <img src="/Mikie.svg" alt="Mikie" />
            </Link>
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