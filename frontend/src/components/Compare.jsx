import { useOutletContext } from "react-router";
import { useEffect, useState } from "react";
import { Link } from "react-router";
import styles from "../styles/Compare.module.css";

function Compare() {
    const { compareItem, setCompareItem } = useOutletContext();
    const [searchQuery, setSearchQuery] = useState("");
    const [results, setResults] = useState([]);
    const [secondItem, setSecondItem] = useState(null);

    useEffect(() => {
        if (!searchQuery) { setResults([]); return; }
        const timeout = setTimeout(async () => {
            try {
                const res = await fetch(`/api/catalog?search=${searchQuery}`);
                const data = await res.json();
                setResults(data.slice(0, 6));
            } catch (err) {
                console.log(err);
            }
        }, 400);
        return () => clearTimeout(timeout);
    }, [searchQuery]);

    if (!compareItem) {
        return (
            <div className={styles.empty}>
                <p>No item selected to compare.</p>
                <Link to="/catalog">Go to Catalog</Link>
            </div>
        );
    }

    return (
        <div className={styles.container}>
            <h2>Compare Items</h2>

            {/* Search for second item */}
            {!secondItem && (
                <div className={styles.search}>
                    <input
                        type="text"
                        placeholder="Search for an item to compare..."
                        value={searchQuery}
                        onChange={e => setSearchQuery(e.target.value)}
                    />
                    <div className={styles.results}>
                        {results.map(item => (
                            <div
                                key={item.id}
                                className={styles.resultItem}
                                onClick={() => { setSecondItem(item); setResults([]); }}
                            >
                                <img src={item.thumbnailUrl} alt={item.name} />
                                <span>{item.name}</span>
                                <span>${item.price}</span>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {/* Comparing the products in a table */}
            {secondItem && (
                <>
                    <button onClick={() => setSecondItem(null)} className={styles.resetBtn}>
                        ✕ Change second item
                    </button>
                    <div className={styles.compareGrid}>

                        <div className={styles.labelCol}></div>
                        <div className={styles.itemCol}>
                            <Link to={`/catalog/item/${compareItem.id}`}>{compareItem.name}</Link>
                        </div>
                        <div className={styles.itemCol}>
                            <Link to={`/catalog/item/${secondItem.id}`}>{secondItem.name}</Link>
                        </div>

                        <div className={styles.labelCol}>Image</div>
                        <div className={styles.itemCol}>
                            <img src={compareItem.images?.[0] ?? compareItem.thumbnailUrl} alt={compareItem.name} />
                        </div>
                        <div className={styles.itemCol}>
                            <img src={secondItem.images?.[0] ?? secondItem.thumbnailUrl} alt={secondItem.name} />
                        </div>

                        <div className={styles.labelCol}>Price</div>
                        <div className={styles.itemCol}>${compareItem.price}</div>
                        <div className={styles.itemCol}>${secondItem.price}</div>

                        <div className={styles.labelCol}>Color</div>
                        <div className={styles.itemCol}>{compareItem.color ?? "—"}</div>
                        <div className={styles.itemCol}>{secondItem.color ?? "—"}</div>
                        <div className={styles.labelCol}>Categories</div>
                        <div className={styles.itemCol}>{compareItem.categories?.join(", ") ?? "—"}</div>
                        <div className={styles.itemCol}>{secondItem.categories?.join(", ") ?? "—"}</div>

                    </div>
                </>
            )}

            <button onClick={() => { setCompareItem(null); setSecondItem(null); }} className={styles.clearBtn}>
                Clear Compare
            </button>
        </div>
    );
}

export default Compare;