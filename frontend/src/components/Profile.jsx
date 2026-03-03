import styles from "../styles/Profile.module.css";
import {Link} from "react-router";
import {useState} from "react";

function Profile() {
    const [signUp, setSignUp] = useState(false);

    async function sendInfo({data, signUp}) {
        const endpoint = signUp ? 'http://localhost:8080/api/register/' : 'http://localhost:8080/api/login/';
        try {
            const response = await fetch(endpoint, {
                method: "POST",
                body: JSON.stringify(data)
            });
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            const result = await response.json();
            console.log(result);
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

    function LoginForm() {
        return (
            <form onSubmit={handleSubmitLogin}>
                <div>
                    <label htmlFor="username">Username</label>
                    <input name="username" id="username" minLength="3" type="text" placeholder="Username" required/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input name="password" id="password" minLength="3" type="Password" placeholder="Password" required/>
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
                    <input name="username" id="username" minLength="3" type="text" placeholder="Username" required/>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input name="password" id="password" minLength="3" type="Password" placeholder="Password" required/>
                </div>
                <div>
                    <label htmlFor="password-confirm">Confirm Password</label>
                    <input name="confirmPassword" id="password-confirm" minLength="3" type="Password" placeholder="Password" required/>
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

    return (
        <div className={styles.profile}>
            {signUp === false ? <LoginForm /> : <RegisterForm />}
        </div>
    );
};

export default Profile;