import { useEffect, useState, useRef } from 'react'
import styles from '../styles/Catalog.module.css'
import {Link, useNavigate, useSearchParams} from "react-router";
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

function NewItemButton({}) {
    const navigate = useNavigate();
    const {user} = useOutletContext();

    if (user && user.isAdmin) {
        return (
            <button onClick={() => navigate(`/item/NEW`)}>Add a new item</button>
        )
    }
    return null;
}

function SortDropdown({searchParams, setSearchParams}) {
    const [sortOpen, setSortOpen] = useState(false);
    return (
        <div className={styles.sortBar}>
            <div className={styles.sortDropdown}>
                <button className={styles.sortButton} onClick={() => setSortOpen(o => !o)}>
                    Sort By
                    <div className={styles.currentSort}>{searchParams.get("sort")}</div>
                     {sortOpen ? '▲' : '▼'}

                </button>
                {sortOpen && (
                    <div className={styles.sortMenu}>
                        <button onClick={() => { setSearchParams({sort: 'price-desc'}); setSortOpen(false); }}>Price: High-Low</button>
                        <button onClick={() => { setSearchParams({sort: 'price-asc'}); setSortOpen(false); }}>Price: Low-High</button>
                        <button onClick={() => { setSearchParams({sort: 'name-asc'}); setSortOpen(false); }}>Name: Ascending</button>
                        <button onClick={() => { setSearchParams({sort: 'name-desc'}); setSortOpen(false); }}>Name: Descending</button>
                    </div>
                )}
            </div>
        </div>
    )
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
        const [searchParams, setSearchParams] = useSearchParams({ sort: 'price-desc' });

        //TODO: fix page reloading and resetting to the top after going back from an item

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

            async function loadData() {
                try {
                    const [sortBy, direction] = searchParams.get("sort").split('-');
                    const response = await fetch(
                        `/api/catalog?sortBy=${sortBy}&direction=${direction}`,
                        { signal: controller.signal }
                    );
                    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                    const result = await response.json();
                    setData(result);
                    sessionStorage.setItem('catalogData', JSON.stringify(result));
                    setVisibleCount(40);
                } catch (err) {
                    if (err.name !== 'AbortError') {
                        console.error('Fetch error:', err);
                        setError(err.message);
                    }
                }
            }
            loadData();
            return () => controller.abort();
        }, [searchParams, setSearchParams]);
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
                <NewItemButton/>
                <SortDropdown searchParams={searchParams} setSearchParams={setSearchParams}/>
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