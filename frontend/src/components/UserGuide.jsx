import { useState } from "react";
import { Link } from "react-router";
import styles from "../styles/UserGuide.module.css";

export default function UserGuide() {
    const [openIndex, setOpenIndex] = useState(null);

    // Write questions and answers here
    const faqs = [
        {
            question: "Q: Am I able to make purchases from the catalog?",
            answer: "A: No, there is no payment or \"add to cart\" functionality. The catalog only displays items, and doesn't allow for direct purchases."
        },
        { question: "Q: Are all items on the catalog available for purchase?",
            answer: "A: No, not all items can be purchased. As purchases are handled through official channels outside the catalog, we cannot guarantee item availability." },
        { question: "Q: Is there a way for me to delete my account?",
            answer: "A: No, account deletion is handled by the developers removing the entry from the database directly." },
    ];

    const toggleQuestion = (index) => {
        setOpenIndex(openIndex === index ? null : index);
    };

    // Top of page will be user guide with images, gifs, videos
    // Details the entire program functionality
    // Create FAQ at the bottom
    return (
        <>
            <div className = {styles.main}>
                <h1>MIKIE SPORTSWEAR USER GUIDE</h1>
                <p>This guide will detail how users will be able to interact with the catalog.</p>
                <img className = {styles.guideImg} src = "images/guide0.png" alt = "Front page"/>

                <h2>Front Page</h2>
                <div className = {styles.text}>
                    <p>
                        When users first enter the site, they will land on the front page shown in the
                        image above. From here, users have direct access to select items, or can view
                        the entire catalog by pressing "Shop all items" between the welcome text and
                        the logo.<br/><br/>

                        At the top of every page will be a header with links to frequently visited areas
                        of the website.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide1.png" alt = "Header"/>
                    <p>
                        From left to right, these links will redirect users to:
                    </p>
                    <ul>
                        <li>Mikie Logo - Front page</li>
                        <li>Information Logo - User guide</li>
                        <li>Men/Women/Kids - Catalog filtered by mens/womens/childrens items</li>
                        <li>Heart icon - User wishlist (more details later)</li>
                        <li>Log in/User icon - Log in/Sign up page, or Profile page if logged in</li>
                    </ul>
                </div>

                <h2>Catalog</h2>
                <div className = {styles.text}>
                    <p>
                        The catalog can be accessed from the front page by clicking on "Shop all items",
                        or by clicking on any of the three categories labelled on the header.
                        Accessing the catalog through the latter method will automatically apply the
                        appropriate filter that the label is written as.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide2.png" alt = "Catalog page"/>
                    <p>
                        Here, users will be able to look through all the items in the catalog by
                        scrolling downwards. Please note that only a certain number of items are
                        displayed at a time, and that more items will be loaded as the user scrolls
                        further down the page. Clicking on any one of the listed items will redirect
                        the user to that item's respective page, which will give more details about
                        the item.<br/><br/>

                        On the left-hand side of the page is a list of filters that the user may
                        apply to the catalog. This is highlighted in the image below.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide3.png" alt = "Highlight filter function"/>
                    <p>
                        Here, users are able to narrow down the items in the catalog based on what
                        they want to find. For example, if a user were to check the box "Men" under
                        the "Category" list, the catalog will display only men's items. The catalog
                        also supports filtering by multiple categories. Selecting "Black" under the
                        "Color" list will then present the user with men's clothing that come in the
                        colour black.<br/><br/>

                        Users are also able to sort the catalog user the "Sort by" function located
                        around the top right-hand side of the catalog. Clicking on this button will
                        allow the user to sort the catalog by four presets:
                    </p>
                    <ol>
                        <li>Price, High-Low</li>
                        <li>Price, Low-High</li>
                        <li>Name, Ascending</li>
                        <li>Name, Descending</li>
                    </ol>
                    <img className = {styles.guideImg} src = "images/guide4.png" alt = "Highlight sort function"/>
                    <p>
                        When sorting by price, users may choose to display the items where the prices
                        are sorted in ascending or descending order. This means sorting the price from
                        cheapest to most expensive, and vice versa. Sorting by item name will display
                        the items in alphabetical or reverse alphabetical order.<br/><br/>

                        Finally, users are able to search the catalog directly by using the searchbar
                        at the top left-hand corner of the catalog.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide5.png" alt = "Highlight search function"/>
                    <p>
                        If the user is unable to find a specific item through the provided filter and
                        sort functions, users may instead choose to directly enter the name of the item
                        that they are looking for. For example, entering the word "Jordan" into the
                        searchbar will have the catalog display all items that include "Jordan" in
                        the item name.
                    </p>
                </div>

                <h2>Item</h2>
                <div className = {styles.text}>
                    <p>
                        After the user finds an item they may be interested in, they can click on the
                        image or name of the item from the catalog to be redirected to a separate
                        page that includes more of the item details.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide6.png" alt = "Item page"/>
                    <p>
                        It is on this page that users are able to view more images of the item, as well
                        as read the description of the item. The item description provides a general
                        idea of what the item may include, as well as if it comes in any alternate
                        colours.<br/><br/>

                        Below the description is a button that allows logged in users to add the item
                        to their personal wishlist. If the user clicks the button while not currently
                        logged in, they will be prompted to do so.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide7.png" alt = "Highlight wishlist function"/>
                    <p>
                        Scrolling towards the bottom of the page will present the user with a list of
                        items that are similar to the one the user is currently looking at. This is
                        done by comparing the categories related to the current item and looking for
                        any other items that may share a most of the same categories.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide8.png" alt = "Highlight similar items"/>
                </div>

                <h2>Log In/Sign Up</h2>
                <div className = {styles.text}>
                    <p>
                        If users decide they want to create an account or log into their existing one,
                        they can do so by clicking the "Log in" text at the rightmost side of the site
                        header. By default, they will be redirected to the log in page, however the
                        option to sign up for an account is provided beneath the log in form.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide9.png" alt = "Log in page"/>
                    <p>
                        Users simply need to enter their personal username and password to gain access
                        to more site features, such as being able to add items to a wishlist.<br/><br/>

                        Users will be able to create an account by clicking "Sign up" beneath the log
                        in form, and will be redirected to the sign up page.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide10.png" alt = "Sign up page"/>
                    <p>
                        Users are asked to provide a username and password for their new account. Users
                        must enter their password twice to prevent issues from an unfortunate
                        spelling error or typo.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide11.png" alt = "Highlight security function"/>
                    <p>
                        To be able to create an account, the chosen username and password must follow
                        these guidelines:
                    </p>
                    <ol>
                        <li>The username and password must be longer than 3 characters</li>
                        <li>The username and password must be shorter than 20 characters</li>
                        <li>The username can only have alphanumeric characters</li>
                        <li>The password must include at least one number, special character, and uppercase letter</li>
                    </ol>
                    <p>
                        Users will be notified if their username and/or password does not follow
                        these guidelines, as shown in the screenshot. Upon meeting these requirements,
                        a new account is added to the database and the user is automatically logged in
                        to the account.
                    </p>
                </div>

                <h2>Profile</h2>
                <div className = {styles.text}>
                    <p>
                        Upon logging into an account, creating a new account, or clicking the user icon
                        at the far right-hand side of the header will redirect the user to the profile
                        page. Here, users will be able to log out or change their account details.
                        The access level of the account is also displayed underneath the currently
                        logged in user's username.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide12.png" alt = "Profile page"/>
                    <p>
                        Users are able to freely change the username and password of their account.
                        To do so, users simply need to enter their new username and/or password into
                        the form highlighted in the following screenshot. If the user is satisfied with
                        their changes, they may click the "Change" button to finalize.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide13.png" alt = "Highlight change information"/>
                    <p>
                        Please note that users do not need to enter anything into the username or
                        password fields if they do not wish to change them. For example, someone wanting
                        to change just their username would not need to enter a new password into
                        either of the fields.
                    </p>
                </div>

                <h2>Wishlist</h2>
                <div className = {styles.text}>
                    <p>
                        The wishlist feature is a way for users to save items they may be interested in,
                        and find them again easily without searching the catalog again. However, this
                        feature is only available to users who have created an account and logged into
                        it. Users who are not logged in are prompted to do so before being granted
                        access to the wishlist.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide14.png" alt = "Wishlist without account"/>
                    <p>
                        If the current user is already logged in but has an empty wishlist, the text
                        will instead be changed to, "Your wishlist is empty!"
                    </p>
                    <img className = {styles.guideImg} src = "images/guide15.png" alt = "Wishlist with account"/>
                    <p>
                        Users are then free to go to any item in the catalog and click the "Add to
                        Wishlist" to add the item to their wishlist. If the item is already on the
                        user's wishlist, then the button changes to "Remove from wishlist". All
                        wishlist items are counted, and displayed as a number next to the heart icon
                        on the right-hand side of the header.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide16.png" alt = "Added to wishlist"/>
                    <img className = {styles.guideImg} src = "images/guide17.png" alt = "Updated wishlist"/>
                    <p>
                        Returning to the wishlist page afterwards will show all items that the user
                        has added to their wishlist. Users will also have the options to once again
                        view the individual items they have saved, or to remove it from the wishlist
                        entirely.
                    </p>
                </div>

                <h2>Admin Actions</h2>
                <div className = {styles.text}>
                    <p>
                        The following features are only available to users who own an account
                        with an access level of "Admin":
                    </p>
                    <ul>
                        <li>Adding, modifying, and deleting items from the catalog</li>
                        <li>Granting/Revoking admin access from other users</li>
                    </ul>
                    <p>
                        For an admin to grant/revoke access from other users, they must use the
                        panel at the bottom of their profile page.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide18.png" alt = "Highlight change permission function"/>
                    <p>
                        Upon entering the valid username of another user in the account system,
                        the checkbox show as empty for non-admin users, and filled for admin users.
                        The admin then has the option to uncheck the box to revoke access, or fill it
                        in to grant access. Changes are finalized by clicking the "Submit" button
                        next to the field.<br/><br/>

                        For an admin to add items to the catalog, they must first go to the catalog
                        page. Here, located immediately to the left of the search bar, is a new button
                        labelled "Add a new item". Clicking this button will redirect the admin to an
                        item editor page.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide19.png" alt = "Highlight add item function"/>
                    <img className = {styles.guideImg} src = "images/guide20.png" alt = "Item editor page"/>
                    <p>
                        On this page, admins are presented with a form consisting of all possible
                        fields that an item must have. These fields are item name, item price, item
                        description, item colour, alternative colours, and a button allowing admins to
                        upload images for the item. All fields must be filled out for the item to be
                        added to the catalog. Upon filling all the fields, admins simply need to click
                        "Submit" for the new item to appear on the catalog.<br/><br/>

                        Admins are also given the ability to modify and delete existing items from the
                        catalog. To do so, admins must first go to the item page of the item they
                        wish to modify or delete. Doing so will present the admin with two new buttons
                        beneath the "Add to Wishlist" button.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide21.png" alt = "Highlight modify/delete item function"/>
                    <p>
                        Clicking on the "Delete this item" button will present the admin with a prompt,
                        asking if they wish to continue. Choosing to do so will delete the item from
                        the catalog, and it will no longer exist. Clicking "Modify this Item" will
                        instead display an editor similar to the add item functionality, however the
                        fields are already filled with the current item's details. Admins may edit
                        these details in any way they see fit.
                    </p>
                    <img className = {styles.guideImg} src = "images/guide22.png" alt = "Item page with editor"/>
                    <p>
                        After making changes to the fields, admins must click "Submit" to finalize
                        changes, which are immediately applied to the item.
                    </p>
                </div>
            </div>

            <div className = {styles.faq}>
                <h1>Frequently Asked Questions</h1>
                { faqs.map((faq, index) => (
                    <div key={index} className = {styles.question} onClick={() => toggleQuestion(index)}>
                        <p style={{ fontWeight: "bold" }}>{faq.question}</p>
                        { openIndex === index && (
                            <div className = {styles.answer}>
                                <p>{faq.answer}</p>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </>
    )
}