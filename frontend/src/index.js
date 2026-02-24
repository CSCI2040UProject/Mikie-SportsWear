// src/index.js
import "./styles.css";



export async function fetchUserData(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.text();
        return data;
    } catch (error) {
        console.error('Fetch error:', error);
    }
}


const recieved = document.querySelector("#recieved");

fetchUserData('http://localhost:8080/helloworld/')
    .then(data => {
        if (data) {
            recieved.textContent = data;
            console.log(data);
        }
    })

