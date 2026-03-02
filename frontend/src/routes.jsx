import App from "./components/App.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import Layout from "./Layout.jsx";
import Profile from "./components/Profile.jsx";

const routes = [
    {
        path: "/",
        element: <Layout />,
        errorElement: <ErrorPage />,
        children: [
            {
                index: true,
                element: <App />,
            },
        ],
    },
    {
        path: "/profile/",
        element: <Layout />,
        errorElement: <ErrorPage />,
        children: [
            {
                index: true,
                element: <Profile/>,
            },
        ],
    },
];

export default routes;
