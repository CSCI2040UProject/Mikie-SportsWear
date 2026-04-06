import {Link} from "react-router";

export function FrontPage() {
    const featuredItems = [

        {
            id: "IQ0289-010",
            name: "Nike Air Max 90",
            price: "170",
            thumbnailUrl: "https://static.nike.com/a/images/t_default/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/22896731-9ec6-4d95-8b58-97620b3be14f/NIKE+AIR+MAX+90.png"
        },
        {
            id: "IQ0302-010",
            name: "Nike Air Max 95",
            price: "235",
            thumbnailUrl: "https://static.nike.com/a/images/t_default/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/b8ff2462-46de-4db6-a295-245d956196ca/NIKE+AIR+MAX+95+BIG+BUBBLE.png"
        }


];

    return (
        <>
            <div style={{ textAlign: 'center', paddingTop: '2vh' }}>
            <h1 className={'nike'}>WELCOME TO MIKIE SPORTSWEAR</h1>
            <Link to={'/catalog/'}>Shop All items</Link>
                <div style= {{imgAlign: 'center', paddingTop: '5vh'}}>

                </div>
            <Link to="/catalog/">
                <img src="/Mikie.svg" alt="Mikie"  width={500} height={500} />
            </Link>
                <div style={{ textAlign: 'center', paddingTop: '2vh' }}>
                <p> Best Sellers </p>
                </div>

                <div style={{ display: 'flex', gap: '20px', justifyContent: 'center', marginTop: '40px' }}>
                    {featuredItems.map(item => (
                        <Link to={`/catalog/item/${item.id}`} key={item.id} style={{ textDecoration: 'none', color: 'inherit' }}>
                            <div>
                                <img src={item.thumbnailUrl} alt={item.name} width={150} height={150} />
                                <h4>{item.name}</h4>
                                <p>${item.price}</p>
                            </div>
                        </Link>
                    ))}
                </div>
            </div>

        </>
    );
}
