import { StrictMode, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import { createBrowserRouter, RouterProvider, Outlet } from "react-router";

// Imports from routes.jsx
import App from "./components/App.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import Profile from "./components/Profile.jsx";
import Catalog from "./components/Catalog.jsx";
import Item from "./components/Item.jsx";

// Imports from Layout.jsx
import Header from "./components/Header.jsx";

const Root = () => {
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("Login")) || "");
    const [loggedIn, setLoggedIn] = useState(JSON.parse(localStorage.getItem("loggedIn")) || false);

    return (
        <>
            <Header username={user} />
            <main>
                <Outlet context={{ user, setUser, loggedIn, setLoggedIn }} />
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
                index: true, // setting index makes this child the front page
                element: <Catalog />,
            },
            {
                path: "item/:id",
                element: <Item />,
            },
            {
                path: "profile",
                element: <Profile />,
            },
        ],
    }
]);

createRoot(document.getElementById("root")).render(
    <StrictMode>
        <RouterProvider router={router} />
    </StrictMode>
);
