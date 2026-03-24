import styles from "../styles/AddItem.module.css";
import {Link} from "react-router";
import {useContext, useState} from "react";
import { useOutletContext } from "react-router"
import Icon from '@mdi/react';
import { mdiAccountCircle } from '@mdi/js';

function AddItem() {
    const {user} = useOutletContext();

    function NonAdmin() {
        // Redirect non-admin users to the catalog
        window.location.replace("/");
    }

    async function sendInfo({data}, endpoint) {
        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data)
            });

            if (response.status === 200) {
                alert("Item saved successfully!");
                return;
            } else if (!response.ok) {
                const errorData = await response.json();
                alert(errorData.errorMessage);
                return;
            }
            const result = await response.json();
        } catch (error) {
            console.log(error);
        }
    }

    async function handleAddItem(e) {
        e.preventDefault();
        const endpoint = "/api/newitem";
        const formData = new FormData(e.target);
        const data = {}

        // For every field in the form,
        // take the entry and add it to the data to be sent over
        for (const [key, value] of formData.entries()) {
                const converted = value instanceof File ? await imageToBase64(value) : value;

                if (key in data) {
                    if (Array.isArray(data[key])) {
                        data[key].push(converted);
                    } else {
                        data[key] = [data[key], converted];
                    }
                } else {
                    // Check if this input is a multi-file input
                    const input = e.target.elements[key];
                    if (input && input.multiple) {
                        data[key] = [converted];
                    } else {
                        data[key] = converted;
                    }
                }
            }

        console.log('Form Data:', data);
        sendInfo({data}, endpoint);
    }

    function imageToBase64(img) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => {
                const base64 = reader.result.split(',')[1];
                resolve(base64);
            };
            reader.onerror = reject;
            reader.readAsDataURL(img);
        });
    }

    function ItemForm() {
        const priceField = document.getElementById("itemPrice");

        const priceChange = (event) => {
            limitPrice(event.target);
        };

        function limitPrice(priceField) {
            const price = priceField.value;
            const regex = new RegExp("\\.");
            const match = price.match(regex);
            if (match) {
            // If a match is found, check the length after the symbol
                const charsAfterSymbol = price.substring(match.index + 1);
                if (charsAfterSymbol.length > 2) {
                    // Truncate the value to only allow 2 characters after the symbol
                    const newValue = price.substring(0, match.index + 3);
                    priceField.value = newValue;
                }
            }
        }

        // Main HTML where the new item form will be filled
        return (
            <form onSubmit={handleAddItem}>
                <div>
                    <label htmlFor="itemName">Item Name</label>
                    <input name="itemName" id="itemName" minLength="3" maxLength="20" type="text" placeholder="Item Name" required/>
                </div>
                <div>
                    <label htmlFor="itemDescription">Description</label>
                    <input name="itemDescription" id="itemDescription" minLength="3" maxLength="50" type="text" placeholder="Description" required/>
                </div>
                <div>
                    <label htmlFor="itemPrice">Price in $</label>
                    <input name="itemPrice" id="itemPrice" minLength="3" maxLength="10" type="number" onChange={priceChange} placeholder="0.00" required/>
                </div>
                <div>
                    <label htmlFor="itemColour">Item Colour</label>
                    <input name="itemColour" id="itemColour" minLength="3" maxLength="20" type="text" placeholder="red" required/>
                </div>
                <div>
                    <label htmlFor="itemOtherColours">(Optional) Other Colours</label>
                    <input name="itemOtherColours" id="itemOtherColour" minLength="3" maxLength="20" type="text" placeholder="red,blue,green"/>
                </div>
                <div>
                    <label htmlFor="itemTags">Comma-Separated Tags</label>
                    <input name="itemTags" id="itemTags" minLength="3" maxLength="50" type="text" placeholder="tag1,tag2,tag3" required/>
                </div>
                <div>
                    <label htmlFor="itemImages">(Optional) Images</label>
                    <input name="itemImages" id="itemImages" type="file" accept="image/*" multiple/>
                </div>
                <div className={styles.buttons}>
                    <button type="submit">Add Item</button>
                </div>
            </form>
        )
    }

    // Check if current user is logged in as an admin or not
    return(
        <div>
            {user.isAdmin ? <ItemForm/> : <NonAdmin/>}
        </div>
    )
}

export default AddItem