import { StrictMode, useState} from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import { createBrowserRouter, RouterProvider, Outlet } from "react-router";

// Imports from routes.jsx
import App from "./components/App.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import Profile from "./components/Profile.jsx";
import Catalog from "./components/Catalog.jsx";
import Item from "./components/Item.jsx";
import {FrontPage} from "./components/FrontPage.jsx";
import Wishlist from "./components/Wishlist.jsx";
// Imports from Layout.jsx
import Header from "./components/Header.jsx";


const Root = () => {
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("Login")) || "");
    const [loggedIn, setLoggedIn] = useState(JSON.parse(localStorage.getItem("loggedIn")) || false);
    const [wishlist, setWishlist] = useState([]);

    async function fetchWishlist() {
        try {
            const res = await fetch('/api/wishlist/', { credentials: 'include' });
            if (res.ok) {
                const data = await res.json();
                setWishlist(data);
            }
        } catch (error) {
            console.log(error);
        }


    }
    return (
        <>
            <Header username={user} wishlist={wishlist} />
            <main>
                <Outlet context={{ user, setUser, loggedIn, setLoggedIn, wishlist, setWishlist, fetchWishlist }} />
            </main>
        </>
    );
};

const router = createBrowserRouter([
    {
        path: "/",
        element: <Root />,
        errorElement: <ErrorPage />,
        children: [
            {
                path: "hello-world",
                element: <App />,
            },
            {
              index: true,
              element: <FrontPage />
            },
            {
                path: "catalog/",
                element: <Catalog />,
            },
            {
                path: "catalog/item/:id",
                element: <Item />,
            },
            {
                path: "profile",
                element: <Profile />,
            },
            {
                path: "wishlist",
                element: <Wishlist />,
            },
        ],
    }
]);

createRoot(document.getElementById("root")).render(
    <StrictMode>
        <RouterProvider router={router} />
    </StrictMode>
);
