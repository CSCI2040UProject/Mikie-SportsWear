import {Link, useParams} from 'react-router';
import {useEffect, useState} from "react";
import styles from "../styles/Item.module.css";
import { useOutletContext } from "react-router"

function OtherColorThumbnail({ id }) {
    const [thumbnailUrl, setThumbnailUrl] = useState(null);

    useEffect(() => {
        async function fetchThumbnail() {
            try {
                const response = await fetch(`/api/catalog/thumbnail?id=${id}`);
                const data = await response.text();
                setThumbnailUrl(data);
            } catch (err) {
                console.error('Error fetching thumbnail:', err);
            }
        }
        fetchThumbnail();
    }, [id]);

    if (!thumbnailUrl) return null;

    return (
        <Link to={`/item/${id}`}>
            <img
                src={thumbnailUrl}
                alt={`Other colour`}
                className={`${styles.thumbnail}`}
            />
        </Link>
    );
}

function ModifyItem({user, id, hideModifyButton}){
    if (user.isAdmin && !hideModifyButton){
        return (
            <div>
                <Link to={`/item/editor/${id}`}>
                    <button>Modify this item</button>
                </Link>
            </div>
        )
    }

}

export default function Item({ hideModifyButton = false, itemProp = null }) {
    const { id } = useParams(); // Get the item ID from the URL
    const [fetchedItem, setFetchedItem] = useState(null);
    const [error, setError] = useState(null);
    const [imageIndex, setImageIndex] = useState(0);
    const {user} = useOutletContext();

    // Use the prop if available, otherwise use local state
    const item = itemProp || fetchedItem;

    useEffect(() => {
        // If parent provided data, we don't need to fetch
        if (itemProp) return;

        const controller = new AbortController();
        const signal = controller.signal;

        async function loadData() {
            try {
                const response = await fetch(`/api/catalog?id=${id}`, {signal});
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const result = await response.json();
                setFetchedItem(result);
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
    }, [id, itemProp]);

    if (error) return <div>Error loading data!</div>;
    if (!item) return <div></div>;

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
                    <div className={'${styles.thumbnailContainer} ${styles.otherColorThumbnail}'}>
                        {item.otherColors.map((colorId, index) => (
                            <OtherColorThumbnail key={index} id={colorId} />
                        ))}
                    </div>
                </div>
                <ModifyItem user={user} id={id} hideModifyButton={hideModifyButton}/>
            </div>
        </div>
        )
}
            