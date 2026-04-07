import {Link, useNavigate, useParams} from 'react-router';
import {useEffect, useState} from "react";
import styles from "../styles/Item.module.css";
import { useOutletContext } from "react-router"
import Editor from "./Editor.jsx";

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
        <Link to={`/catalog/item/${id}`}>
            <img
                src={thumbnailUrl}
                alt={`Other colour`}
                className={`${styles.thumbnail}`}
            />
        </Link>
    );
}

function CreateItem({item}) {
    return (
        <Link to={'/catalog/item/' + item.id}>
            <div className={styles.item}>
                <img src={item.thumbnailUrl} alt="Item" />
                <h4>{item.name}</h4>
                <p className={styles.price}>${item.price}</p>
            </div>
        </Link>
    )
}

function SimilarItems({similarItems}){
    return (
        <div className={styles.similarItems}>
            {similarItems.map ((item, index) => (
                <CreateItem key={item.id || index} item={item}/>
                ))}
        </div>
    )
}



export default function Item({itemProp = null }) {
    const { id } = useParams(); // Get the item ID from the URL
    const [fetchedItem, setFetchedItem] = useState(null);
    const [similarItems, setSimilarItems] = useState([]);
    const [error, setError] = useState(null);
    const [imageIndex, setImageIndex] = useState(0);
    const [isEditing, setIsEditing] = useState(false);
    const { user, loggedIn, wishlist, setWishlist, compareItem, setCompareItem } = useOutletContext();
    const navigate = useNavigate();
    const item = itemProp || fetchedItem;

    const isWishlisted = wishlist && item
        ? wishlist.some(w => w.id === item.id)
        : false;
        //adding item to the wishlist
    async function addToWishlist() {
        if (!loggedIn) {
            alert("Please log in to save items to your wishlist!");
            navigate('/profile/');
            return;
        }
        try {
            const res = await fetch('/api/wishlist/', {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    productId: item.id,
                    productName: item.name,
                    price: item.price,
                    image: item.images?.[0] ?? null
                })
            });
            if (res.ok) setWishlist(prev => [...prev, item]);
        } catch (error) {
            console.log(error);
        }
    }
        //removing products from the wishlist
    async function removeFromWishlist() {
        try {
            const res = await fetch('/api/wishlist/', {
                method: 'DELETE',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ productId: item.id })
            });
            if (res.ok) setWishlist(prev => prev.filter(w => w.id !== item.id));
        } catch (error) {
            console.log(error);
        }
    }
    function handleCompare() {
        setCompareItem(item);
        navigate('/compare');
    }
    // Use the prop if available, otherwise use local state

    const handleUpdate = (updatedItem) => {
        setFetchedItem(updatedItem);
        setIsEditing(false);
    };

    useEffect(() => {
        // If parent provided data, we don't need to fetch
        if (itemProp || id === "NEW") return;

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

    useEffect(() => {
        const controller = new AbortController();
        async function loadData() {

            try {
                const response = await fetch(`/api/catalog?id=${id}&similar=true`, { signal: controller.signal });
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                const result = await response.json();
                setSimilarItems(result);
            } catch (err) {
                if (err.name !== 'AbortError') {
                    console.error('Fetch error:', err);
                    setError(err.message);
                }
            }
        }
        loadData();
        return () => controller.abort();
    }, [fetchedItem]);

    if (id === "NEW") return (
        <div className={styles.newEditor}>
            <Editor itemProp={null} onUpdate={handleUpdate} />
        </div>
    )

    if (error) return <div>Error loading data!</div>;
    if (!item) return <div></div>;

    function ModifyItem({user, isEditing, setIsEditing}){
        if (user && user.isAdmin){
            return (
                <div className={styles.modifyButtons}>
                    <button onClick={() => setIsEditing(!isEditing)}>
                        {isEditing ? "Close Editor" : "Modify this item"}
                    </button>
                    <button onClick={deleteItem}>
                        Delete this item
                    </button>
                </div>
            )
        }
        return null;
    }

    function deleteItem() {
        const confirmDelete = window.confirm("Are you sure you want to delete this item?");
        if (confirmDelete) {
            fetch(`/api/catalog?id=${id}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (response.ok) {
                        sessionStorage.removeItem('catalogData');
                        navigate('/');
                    } else {
                        console.error('Failed to delete item');
                    }
                })
        }
    }



    const images = item.images || [];
    const otherColors = item.otherColors || [];

    return (
        <div className={styles.container}>
            <div className={styles.images}>
                <img className={styles.mainImage} src={(images[imageIndex] || images[0]) ?? null} alt={item.name ?? 'Photo'} />
                <div className={styles.thumbnailContainer}>
                    {images.map((imgUrl, index) => ( //Iterate over all the images and add them to this container
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
            <div className={styles.details}>
                <h1>{item.name ?? 'Name'}</h1>
                <h3>${item.price ?? 0.00}</h3>
                <p>{item.description ?? 'Description'}</p>
                <p>Color: {item.color ?? 'Colour'}</p>
                <button onClick={isWishlisted ? removeFromWishlist : addToWishlist}>
                    {isWishlisted ? '♥ Remove from Wishlist' : '♡ Add to Wishlist'}
                </button>
                <button onClick={handleCompare}> Compare</button>
                {otherColors.length > 0 &&

                <div>Other colours:
                    <div className={`${styles.thumbnailContainer} ${styles.otherColorThumbnail}`}>
                        {otherColors.map((colorId, index) => (
                            <OtherColorThumbnail key={index} id={colorId} />
                        ))}
                    </div>
                </div>
                }
                <ModifyItem user={user} isEditing={isEditing} setIsEditing={setIsEditing} />
            </div>
            {isEditing && <Editor itemProp={item} onUpdate={handleUpdate} />}
            <h1 className={styles.similarHeader}>Similar Items</h1>
            <SimilarItems similarItems={similarItems}/>
            <div style={{ gridColumn: '1 / -1', height: '30px' }} /> {/*I HATE THIS STUPID DUMMY SPACER THAT I HAVE TO USE BECAUSE SAFARI ISN'T ACTING RIGHT WITH THE HEIGHT OF THE CONTAINER*/}
        </div>
        )
}
            