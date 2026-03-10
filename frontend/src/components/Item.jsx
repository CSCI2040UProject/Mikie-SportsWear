import {Link, useParams} from 'react-router';
import {useEffect, useState} from "react";
import styles from "../styles/Item.module.css";

export default function Item() {
    const { id } = useParams(); // Get the item ID from the URL
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [imageIndex, setImageIndex] = useState(0);
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

    const item = data.find(i => String(i.id) === String(id)); //Downloading the full 13MB catalog just to get one object...
    return (
        <div className={styles.container}>
            <div className={styles.left}>
                <img className={styles.mainImage} src={item.images[imageIndex] || item.images[0]} alt={item.name} />
                <div className={styles.thumbnailContainer}>
                    {item.images.map((imgUrl, index) => ( //Iterate over all the images and add them to this container
                        <img 
                            key={index} 
                            src={imgUrl} 
                            alt={`${item.name} thumbnail ${index + 1}`}
                            className={`${styles.thumbnail} ${index === imageIndex ? styles.activeThumbnail : ''}`}
                            onClick={() => setImageIndex(index)}
                        />
                    ))}
                </div>
            </div>
            <div className={styles.right}>
                <h1>{item.name}</h1>
                <h3>${item.price}</h3>
                <p>{item.description}</p>
                <p>Color: {item.color}</p>

                <div>Other colours:
                    <div className={styles.thumbnailContainer}>
                        {item.otherColors.map((id, index) => (
                            <Link to={`/item/${id}`} key={index}>{id}</Link>
                        ))}
                    </div>
                </div>
            </div>
        </div>
        )
}
            