import {useEffect, useState} from 'react'
import '../styles/App.css'

function FetchUserData({ url }) {
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function loadData() {
            try {
                const response = await fetch(url);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const result = await response.text();
                setData(result);
            } catch (err) {
                console.error('Fetch error:', err);
                setError(err.message);
            }
        }

        loadData();
    }, [url]);



    if (error) return <div>Error loading data!</div>;
    if (!data) return <div>Loading...</div>;

    return <data>{data}</data>;
}

function App() {

  return (
    <>
        <div>
            <FetchUserData url="/api/helloworld/"/>
        </div>
    </>
  )
}

export default App
