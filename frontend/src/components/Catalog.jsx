import {useEffect, useState} from 'react'
import '../styles/Catalog.module.css'

function FetchUserData({ url }) {
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const controller = new AbortController();
        const signal = controller.signal;

        async function loadData() {
            try {
                const response = await fetch(url, {signal});
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const result = await response.text();
                setData(result);
            } catch (err) {
                if (err.name === 'AbortError') {
                    console.log('Fetch aborted due to navigation');
                } else {
                    console.error('Fetch error:', err);
                    setError(err.message);
                }
            }
        }

        loadData();
        return () => {
            controller.abort();
        };
    }, [url]);

    if (error) return <div>Error loading data!</div>;
    if (!data) return <div>Loading...</div>;
    return (
        <div>
            <p>Loaded {data.length} items from the catalog.</p>
        </div>
    )
}

function App() {

    return (
        <>
            <div>
                <FetchUserData url="/api/catalog"/>
            </div>
        </>
    )
}

export default App
