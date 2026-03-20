// Each new page should be added as an import statement here
import App from "./components/App.jsx";
import ErrorPage from "./components/ErrorPage.jsx";
import Layout from "./Layout.jsx";
import Profile from "./components/Profile.jsx";
import Catalog from "./components/Catalog.jsx";
import Item from "./components/Item.jsx";
import AddItem from "./components/AddItem.jsx";

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
                index: true, // setting index makes this child the front page
                element: <Catalog />,
            },
            {
                path: "item/:id",
                element: <Item/>,
            },
            {
                path: "profile",
                element: <Profile />,
            },
            {
                path: "add-item",
                element: <AddItem/>
            }
        ],
    }
];

export default routes;
