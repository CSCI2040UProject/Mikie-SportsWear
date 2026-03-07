import styles from "../styles/Profile.module.css";
import {Link} from "react-router";
import {useContext, useState} from "react";
import { useOutletContext } from "react-router"

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
            } else if (response.status === 409) {
                alert("Username already exists!");
                return;
            } else if (!response.ok) {
                throw new Error(response.statusText);
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



    function LoggedInPage(){
        return(
        <div className={styles.loggedInStyle}>
            <h1>Username: {user.username}</h1>
            <h1>Access Level: {user.isAdmin ? 'Admin' : 'User'}</h1>
            <button onClick={handleSubmitLogout}>Log out</button>
            <br/>
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
                    Don't have an account?
                    <button type="button" onClick={() => {
                        setSignUp(true);
                    }}>Sign-up</button>
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
                    Already have an account?
                    <button type="button" onClick={() => {
                        setSignUp(false);
                    }}>Login</button>
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
};

export default Profile;