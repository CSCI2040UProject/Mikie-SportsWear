import {Link, useParams} from 'react-router';

export default function Editor() {
    const { id } = useParams(); // Get the item ID from the URL
    return (
        <div>
            <h1>Editor</h1>
        </div>
    )
}