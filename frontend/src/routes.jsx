import App from "./components/App.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import Layout from "./Layout.jsx";
import Profile from "./components/Profile.jsx";
import Catalog from "./components/Catalog.jsx";

const routes = [
    {
        path: "/",
        element: <Layout />,
        errorElement: <ErrorPage />,
        children: [
            {
                path: "hello-world",
                element: <App />,
            },
            {
                index: true,
                element: <Catalog />,
            },
            {
                path: "profile",
                element: <Profile />,
            },
        ],
    }
];

export default routes;
