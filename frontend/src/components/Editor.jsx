import {useNavigate, Link, useParams} from 'react-router';
import {useState, useEffect} from 'react';
import styles from "../styles/Editor.module.css";

export default function Editor({ itemProp, onUpdate }) {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        price: '',
        description: '',
        color: '',
        otherColors: ''
    });
    const [selectedFiles, setSelectedFiles] = useState(null);

    useEffect(() => {
        if (itemProp) {
            setFormData({
                name: itemProp.name || '',
                price: itemProp.price || '',
                description: itemProp.description || '',
                color: itemProp.color || '',
                otherColors: Array.isArray(itemProp.otherColors) ? itemProp.otherColors.join(', ') : (itemProp.otherColors || '')
            });
        }
    }, [itemProp]);

    function handleChange(e) {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    }
    function handleFileChange(e) {
        setSelectedFiles(Array.from(e.target.files));
    }

    async function uploadImage(file, productId) {
        if (!file) return;

        const res = await fetch(`/api/images?id=${productId}`, {
            method: "POST",
            headers: { "Content-Type": "application/octet-stream" },
            body: file
        });

        return await res.text();
    }

    async function handleSubmit(e) {
        e.preventDefault();

        const data = { ...formData };

        Object.keys(data).forEach((key) => {
            if (typeof data[key] === 'string' && data[key].trim() === "") {
                delete data[key];
            }
        });

        if (data.otherColors) {
            data.otherColors = data.otherColors
                .split(',')
                .map(color => color.trim())
                .filter(color => color !== "");
        }

        const productId = await sendInfo({ data });
        if (!productId) return;

        if (selectedFiles && selectedFiles.length > 0) {
            for (const file of selectedFiles) {
                await uploadImage(file, productId);
            }
        }

        const updatedItem = await fetch(`/api/catalog?id=${productId}`).then(r => r.json());

        navigate(`/catalog/item/${productId}`);
        if (onUpdate) onUpdate(updatedItem);
    }

    async function sendInfo({ data }) {
        let endpoint = id === "NEW" ? "/api/catalog" : `/api/catalog?id=${id}`;

        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            if (response.status === 401) {
                alert("You do not have the correct permissions to edit this item!");
                return null;
            } else if (!response.ok) {
                const errorData = await response.json();
                alert(errorData.errorMessage);
                return null;
            }

            const result = await response.json();
            return result.id;

        } catch (error) {
            console.log(error);
            return null;
        }
    }

    return (
        <div className={`${styles.editor} editor`}>
            <h1>Editor</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="name">Name</label>
                <input id="name" type="text" name="name" placeholder="Name" value={formData.name} required onChange={handleChange} />
                <label htmlFor="price">Price</label>
                <input id="price" type="number" name="price" placeholder="Price" value={formData.price} onChange={handleChange} />
                <label htmlFor="description">Description</label>
                <textarea id="description" name="description" placeholder="Description" value={formData.description} onChange={handleChange} />
                <label htmlFor="color">Color</label>
                <input id="color" type="text" name="color" placeholder="Color" value={formData.color} onChange={handleChange} />
                <label htmlFor="otherColors">Other Colors</label>
                <input id="otherColors" type="text" name="otherColors" placeholder="Other Colors" value={formData.otherColors} onChange={handleChange} />
                <label htmlFor="file-input">Upload Image</label>
                <input type="file" id="file-input" multiple name="ImageStyle" onChange={handleFileChange}/>
                <button type="submit">Submit</button>
            </form>
        </div>
    )
}