import {useNavigate, Link, useParams} from 'react-router';
import {useState, useEffect} from 'react';

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

    function handleSubmit(e) {
        e.preventDefault();
        const data = { ...formData };

        Object.keys(data).forEach((key) => {
            if (typeof data[key] === 'string' && data[key].trim() === "") {
                delete data[key];
            }
        });
        
        if (data.otherColors) {
            data.otherColors = data.otherColors.split(',').map(color => color.trim()).filter(color => color !== "");
        }

        console.log('Form Data:', data);
        sendInfo({data});
    }

    async function sendInfo({data}) {
        let endpoint = "";
        if (id === "NEW") {
            endpoint = "/api/catalog";
        } else {
            endpoint = `/api/catalog?id=${id}`;
        }
        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data)
            });

            if (response.status === 401) {
                alert("You do not have the correct permissions to edit this item!");
                return;
            } else if (!response.ok) {
                const errorData = await response.json();
                alert(errorData.errorMessage);
                return;
            }
            const result = await response.json();
            console.log(result);
            navigate(`/item/${result.id}`);
            
            if (onUpdate) {
                onUpdate(result);
            }

        } catch (error) {
            console.log(error);
        }
    }

    return (
        <div>
            <h1>Editor</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="name">Name</label>
                <input id="name" type="text" name="name" placeholder="Name" value={formData.name} onChange={handleChange} />
                <label htmlFor="price">Price</label>
                <input id="price" type="number" name="price" placeholder="Price" value={formData.price} onChange={handleChange} />
                <label htmlFor="description">Description</label>
                <textarea id="description" name="description" placeholder="Description" value={formData.description} onChange={handleChange} />
                <label htmlFor="color">Color</label>
                <input id="color" type="text" name="color" placeholder="Color" value={formData.color} onChange={handleChange} />
                <label htmlFor="otherColors">Other Colors</label>
                <input id="otherColors" type="text" name="otherColors" placeholder="Other Colors" value={formData.otherColors} onChange={handleChange} />
                <label htmlFor="file-input">Upload Image</label>
                <input type="file" id="file-input" name="ImageStyle"/>
                <button type="submit">Submit</button>
            </form>
        </div>
    )
}