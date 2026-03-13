import styles from "../styles/Profile.module.css";
import {Link} from "react-router";
import {useContext, useState} from "react";
import { useOutletContext } from "react-router"
import Icon from '@mdi/react';
import { mdiAccountCircle } from '@mdi/js';

function Profile() {
    const [signUp, setSignUp] = useState(false);
    const {user, setUser, loggedIn, setLoggedIn } = useOutletContext();

    async function sendInfo({data, signUp}) {
        const endpoint = signUp ? '/api/register/' : '/api/login/';
        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data)
            });

            if (response.status === 401) {
                alert("Incorrect username or password!");
                return;
            } else if (!response.ok) {
                const errorData = await response.json();
                alert(errorData.errorMessage);
                return;
            }
            const result = await response.json();

            setUser(result);
            setLoggedIn(true);
            localStorage.setItem("Login", JSON.stringify(result));
            localStorage.setItem("loggedIn", JSON.stringify(true));

        } catch (error) {
            console.log(error);
        }
    }

    function handleSubmitLogin(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData.entries());
        console.log('Form Data:', data);
        sendInfo({data, signUp});
    }

    function handleSubmitRegister(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData.entries());
        console.log('Form Data:', data);
        if (data.password !== data.confirmPassword) {
            alert("Passwords don't match");
        } else {
            sendInfo({data, signUp});
        }
    }

    async function handleSubmitLogout() {
        const endpoint = "/api/logout/";
        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "text/plain",
                },
            });

            if (response.status === 400) {
                alert("An error occured while logging out!");
                throw new Error(response.statusText);
            }
            console.log(response);
            setUser("");
            setLoggedIn(false);
            localStorage.removeItem("Login");
            localStorage.removeItem("loggedIn");
        } catch (error){
            console.log(error);
        }
    }

    async function handleModifyUser({modifyUser}) {
        const endpoint = "/api/user/";
        try {
            const response = await fetch(endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(modifyUser),
            });

            if (response.status === 403) {
                alert("You do not have the correct permissions!");
                return;
            } else if (response.status === 409) {
                alert("Username already exists!");
                return;
            } else if (!response.ok) {
                throw new Error(response.statusText);
            }

            alert("Successful!")
            const result = await response.json();

            if (modifyUser.password !== null || user.username === result.username) {
                setUser(result);
                setLoggedIn(true);
                localStorage.setItem("Login", JSON.stringify(result));
                localStorage.setItem("loggedIn", JSON.stringify(true));
            }

        } catch (error) {
            alert("an unknown error occured!");
            console.log(error);
        }
    }

    function LoggedInPage(){
        function editUser(e){
            e.preventDefault();
            const formData = new FormData(e.target);
            const modifyUser = Object.fromEntries(formData.entries());
            if (modifyUser.username !== "" || modifyUser.password !== "") {
                if (modifyUser.password !== modifyUser.confirmPassword) {
                    alert("Passwords don't match");
                } else {
                    console.log("modifyUser", modifyUser);
                    modifyUser.isAdmin = user.isAdmin;
                    handleModifyUser({modifyUser});
                }
            }
        }
        function AddAdmin(){
            function addAdmin(e){
                e.preventDefault();
                const formData = new FormData(e.target);
                const modifyUser = Object.fromEntries(formData.entries());
                if (modifyUser.isAdmin === "on"){
                    modifyUser.isAdmin = true;
                } else {
                    modifyUser.isAdmin = false;
                }
                modifyUser.password = null;
                console.log(modifyUser);
                handleModifyUser({modifyUser});
            }
            return (
                    <form onSubmit={addAdmin}>
                    <label htmlFor="addAdminText">Enter a username of an existing user to change their access level</label>
                    <div>
                        <input id="addAdminText" type="text" name="username" placeholder="username" />
                        <input type="checkbox" id="addAdminCheckbox" name="isAdmin" />
                        <label htmlFor="addAdminCheckbox">Admin</label>
                        <button type="submit">Submit</button>
                    </div>
                    </form>
            )
        }
        return(
        <div id={styles.loggedInStyle}>
            <div>
                <Icon path={mdiAccountCircle} size={3} />
                <div>
                    <h2>Username: {user.username}</h2>
                    <h4>Access Level: {user.isAdmin ? 'Admin' : 'User'}</h4>
                </div>
            <button onClick={handleSubmitLogout}>Log out</button>
            </div>
            <br/>
            <form onSubmit={editUser}>
                <h4>Update your own credentials</h4>
                <div>
                    <label htmlFor="username">Change your Username</label>
                    <input name="username" id="username" minLength="3" maxLength="20" pattern="[a-zA-z0-9]+" type="text" placeholder="Username"/>
                </div>
                <div>
                    <label htmlFor="password">Change your Password</label>
                    <input name="password" id="password" minLength="3" maxLength="20" type="Password" placeholder="Password"/>
                </div>
                <div>
                    <label htmlFor="password-confirm">Confirm Password</label>
                    <input name="confirmPassword" id="password-confirm" minLength="3" maxLength="20" type="Password" placeholder="Password"/>
                </div>
                <button type="submit">Change</button>
            </form>
            <br/>
            {user.isAdmin ? <AddAdmin /> : null}
        </div>

        )
    }

    function LoginForm() {
        return (
            <form onSubmit={handleSubmitLogin}>
                <div>
                    <label htmlFor="username">Username</label>
                    <input name="username" id="username" minLength="3" maxLength="20" pattern="[a-zA-z0-9]+" type="text" placeholder="Username" required/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input name="password" id="password" minLength="3" maxLength="20" type="Password" placeholder="Password" required/>
                </div>
                <div className={styles.buttons}>
                    <button type="submit">login</button>
                    <div>
                    Don't have an account?
                    <button type="button" onClick={() => {
                        setSignUp(true);
                    }}>Sign-up</button>
                    </div>
                </div>
            </form>
        )
    }


    function RegisterForm() {
        return (
            <form onSubmit={handleSubmitRegister}>
                <div>
                    <label htmlFor="username">Username</label>
                    <input name="username" id="username" minLength="3" maxLength="20" pattern="[a-zA-z0-9]+" type="text" placeholder="Username" required/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input name="password" id="password" minLength="3" maxLength="20" type="Password" placeholder="Password" required/>
                </div>
                <div>
                    <label htmlFor="password-confirm">Confirm Password</label>
                    <input name="confirmPassword" id="password-confirm" minLength="3" maxLength="20" type="Password" placeholder="Password" required/>
                </div>
                <div className={styles.buttons}>
                    <button type="submit">Sign-up</button>
                    <div>
                    Already have an account?
                    <button type="button" onClick={() => {
                        setSignUp(false);
                    }}>Login</button>
                    </div>
                </div>
            </form>
        )
    }

    if (loggedIn) {
        return (
            <div className={styles.profile}>
                {<LoggedInPage />}
            </div>
        )
    }
    return (
        <div className={styles.profile}>
            {signUp === false ? <LoginForm /> : <RegisterForm />}
        </div>
    );
}

export default Profile;