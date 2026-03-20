import { useState, useEffect } from 'react';
import { useParams, useOutletContext } from 'react-router';
import Item from './Item.jsx';
import Editor from './Editor.jsx';

export default function ItemEditor() {
    const { id } = useParams();
    const [item, setItem] = useState(null);
    const [loading, setLoading] = useState(true);
    const context = useOutletContext(); // Pass through context if needed by children

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
            <Item itemProp={item} hideModifyButton={true} />
            <Editor itemProp={item} onUpdate={handleUpdate} />
        </>
    );
}
