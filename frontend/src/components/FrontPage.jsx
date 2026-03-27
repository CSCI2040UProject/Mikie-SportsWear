import {Link} from "react-router";

export function FrontPage() {

    return (
        <>
            <div style={{ textAlign: 'center', paddingTop: '2vh' }}>
            <h1 className={'nike'}>WELCOME TO MIKIE SPORTSWEAR</h1>
            <Link to={'/catalog/'}>Shop All items</Link>
                <div style= {{imgAlign: 'center', paddingTop: '5vh'}}>

                </div>
            <Link to="/catalog/">
                <img src="/Mikie.svg" alt="Mikie"  width={750} height={750} />
            </Link>
            </div>
        </>
    )
}
