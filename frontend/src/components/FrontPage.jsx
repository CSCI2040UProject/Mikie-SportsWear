import {Link} from "react-router";

export function FrontPage() {

    return (
        <>
            <h1 className={'nike'}>WELCOME TO MIKIE SPORTSWEAR</h1>
            <Link to={'/catalog/'}>Shop All items</Link>
        </>
    )
}
