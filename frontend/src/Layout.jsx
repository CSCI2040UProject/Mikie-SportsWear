import {Outlet, useOutletContext} from "react-router";
import Header from "./components/Header.jsx";
import {createContext, useState} from "react";

const Layout = () => {
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("Login")) || "");
    const [loggedIn, setLoggedIn] = useState(JSON.parse(localStorage.getItem("loggedIn")) || false);
    return (
        <>
            <Header username={user} />
            <main>
                <Outlet context={{user, setUser, loggedIn, setLoggedIn }} />
            </main>
        </>
    );
};

export default Layout;