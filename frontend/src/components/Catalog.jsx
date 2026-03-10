import { useEffect, useState, useRef } from 'react'
import styles from '../styles/Catalog.module.css'
import {Link} from "react-router";

function Catalog({}){
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [visibleCount, setVisibleCount] = useState(40); // Initial items to display
    const observer = useRef();
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

    // Callback ref to attach the Intersection Observer to the last element
    const lastElementRef = (node) => {
        if (!data) return;
        
        // Disconnect the previous observer if it exists
        if (observer.current) observer.current.disconnect();
        
        observer.current = new IntersectionObserver(entries => {
            // If the element is visible and we haven't loaded all items yet
            if (entries[0].isIntersecting && visibleCount < data.length) {
                // Load 20 more items
                setVisibleCount(prev => prev + 20);
            }
        });
        
        if (node) observer.current.observe(node);
    };

    if (error) return <div>Error loading data!</div>;
    if (!data) return <div>Loading...</div>;

    return (
        <>
            <div>
                <p>Showing {Math.min(visibleCount, data.length)} of {data.length} items from the catalog.</p>
                {/* Only render items up to the visibleCount */}
                <div className={styles.catalog}>
                {data.slice(0, visibleCount).map((item, index) => (
                    <CreateItem key={item.id || index} item={item} />
                ))}
                </div>
                
                {/* Invisible element at the bottom to trigger the observer */}
                {visibleCount < data.length && (
                    <div ref={lastElementRef} style={{ height: '20px', textAlign: 'center', padding: '10px' }}>
                        Loading more items...
                    </div>
                )}
            </div>
        </>
    )
}

function CreateItem({item}) {
    return (
        <Link to={'/item/' + item.id}>
        <div className={styles.item}>
            <img src={item.thumbnailUrl} alt="Item" />
            <h4>{item.name}</h4>
        </div>
        </Link>
    )
}

export default Catalog