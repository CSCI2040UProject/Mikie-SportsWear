import { Outlet } from "react-router";
import Header from "./components/Header.jsx";
import {useState} from "react";

const Layout = () => {
    const [username, setUsername] = useState(localStorage.getItem("username") || "");

    return (
        <>
            <Header username={username} />
            <main>
                <Outlet context={{ setUsername }}/>
            </main>
        </>
    );
};

export default Layout;