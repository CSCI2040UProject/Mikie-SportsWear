import {useEffect, useState} from 'react'
import '../styles/Catalog.module.css'

function Catalog({}){
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const url = "/api/catalog";

        useEffect(() => {
            const controller = new AbortController();
            const signal = controller.signal;

            async function loadData() {
                try {
                    const response = await fetch(url, {signal});
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    const result = await response.json();
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

    console.log(data[0]);

    return (
        <>
            <div>
                <p>Loaded {data.length} items from the catalog.</p>
                <CreateItem item={data[0]} />
            </div>
        </>
    )
}

function CreateItem({item}) {
    return (
        <div className="item">
            <img src={item.itemImages[0]}/>
        </div>
    )
}

export default Catalog
