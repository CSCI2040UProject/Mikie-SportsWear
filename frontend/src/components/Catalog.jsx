import { useEffect, useState, useRef } from 'react'
import styles from '../styles/Catalog.module.css'
import {Link, useNavigate} from "react-router";
import { useOutletContext } from "react-router"

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

function NewItemButton({user}) {
    const navigate = useNavigate();

    async function handleCreate() {
        try {
            const response = await fetch('/api/catalog', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (!response.ok) {
                console.log("Error creating item");
                return;
            }
            const result = await response.json();
            navigate(`/item/editor/${result.id}`);
        } catch (error) {
            console.log(error);
        }
    }

    if (user && user.isAdmin) {
        return (
            <button onClick={handleCreate}>Add a new item</button>
        )
    }
    return null;
}

    function Catalog({}) {
        const [data, setData] = useState(() => {
            const cached = sessionStorage.getItem('catalogData');
            return cached ? JSON.parse(cached) : null;
        });
        const [error, setError] = useState(null);
        const [visibleCount, setVisibleCount] = useState(() => {
            const cached = sessionStorage.getItem('catalogVisibleCount');
            return cached ? parseInt(cached, 10) : 40;
        });
        const observer = useRef();
        const url = "/api/catalog";
        const {user} = useOutletContext();

        // Clear cache on manual reload
        useEffect(() => {
            const handleBeforeUnload = () => {
                if (performance.navigation.type === 1) {
                    sessionStorage.removeItem('catalogData');
                    sessionStorage.removeItem('catalogVisibleCount');
                    sessionStorage.removeItem('catalogScrollPos');
                }
            };

            window.addEventListener('beforeunload', handleBeforeUnload);
            return () => window.removeEventListener('beforeunload', handleBeforeUnload);
        }, []);

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
                    sessionStorage.setItem('catalogData', JSON.stringify(result));
                } catch (err) {
                    if (err.name === 'AbortError') {
                        console.log('Fetch aborted due to navigation');
                    } else {
                        console.error('Fetch error:', err);
                        setError(err.message);
                    }
                }
            }

            if (!data) {
                loadData();
            }
            return () => {
                controller.abort();
            };
        }, [url, data]);

        // Save visible count to sessionStorage whenever it changes
        useEffect(() => {
            sessionStorage.setItem('catalogVisibleCount', visibleCount.toString());
        }, [visibleCount]);

        // Restore scroll position after data loads
        useEffect(() => {
            if (data) {
                const savedScrollPos = sessionStorage.getItem('catalogScrollPos');
                if (savedScrollPos) {
                    window.scrollTo(0, parseInt(savedScrollPos, 10));
                }
            }
        }, [data]);

        // Save scroll position when leaving the page
        useEffect(() => {
            const handleScroll = () => {
                sessionStorage.setItem('catalogScrollPos', window.scrollY.toString());
            };

            window.addEventListener('scroll', handleScroll);
            return () => window.removeEventListener('scroll', handleScroll);
        }, []);

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
        if (!data) return <div></div>;

        return (
            <>
                <NewItemButton user={user}/>
                <div>
                    <div className={styles.catalog}>
                        {data.slice(0, visibleCount).map((item, index) => (
                            <CreateItem key={item.id || index} item={item}/>
                        ))}
                    </div>

                    {/* Invisible element at the bottom to trigger the observer */}
                    {visibleCount < data.length && (
                        <div ref={lastElementRef} style={{height: '20px', textAlign: 'center', padding: '10px'}}>
                            Loading more items...
                        </div>
                    )}
                </div>
            </>
        )
    }


    export default Catalog;