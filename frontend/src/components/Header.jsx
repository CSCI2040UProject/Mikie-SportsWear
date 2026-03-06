import { Link } from "react-router";
import styles from "../styles/Header.module.css";
import Icon from '@mdi/react';
import { mdiAccount } from '@mdi/js';

const Header = ({username}) => {
    return (
        <header className={styles.header}>
            <h1><Link to="/">Mikie</Link></h1>
                <Link to="/profile/">
                    <div>
                    <h3>{username ? username : "Profile"}</h3>
                    <Icon path={mdiAccount} size={1} />
                    </div>
                </Link>
        </header>
    );
};

export default Header;