import { useState, useEffect } from 'react';
import { useParams, Outlet, useLocation } from 'react-router';
import Item from './Item.jsx';

export default function ItemLayout() {
    const { id } = useParams();
    const [item, setItem] = useState(null);
    const [loading, setLoading] = useState(true);
    const location = useLocation();

    // Check if we are currently on the 'edit' sub-route
    const isEditing = location.pathname.endsWith('/edit');

    useEffect(() => {
        const controller = new AbortController();
        const signal = controller.signal;

        async function loadData() {
            try {
                const response = await fetch(`/api/catalog?id=${id}`, { signal });
                if (response.ok) {
                    const result = await response.json();
                    setItem(result);
                }
            } catch (err) {
                if (err.name !== 'AbortError') {
                    console.error('Fetch error:', err);
                }
            } finally {
                setLoading(false);
            }
        }

        loadData();
        return () => controller.abort();
    }, [id]);

    const handleUpdate = (updatedItem) => {
        setItem(updatedItem);
    };

    if (loading) return <div>Loading...</div>;

    return (
        <>
            {/* Item is always rendered here, so it never unmounts/reloads */}
            <Item itemProp={item} hideModifyButton={isEditing} />
            
            {/* The Editor (if the URL matches) will render here and receive the context */}
            <Outlet context={{ item, onUpdate: handleUpdate }} />
        </>
    );
}
