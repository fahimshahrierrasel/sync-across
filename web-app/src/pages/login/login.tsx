import React, { useState } from "react";
import firebaseApp from "../../data/firebase";
import "./login.css";

const Login = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  
  const loginUser = async () => {
    if (email.length < 3 || password.length < 3) {
      alert("Email and Password too short");
      return;
    }
    try {
      await firebaseApp.auth.signInWithEmailAndPassword(email, password);
    } catch (err) {
      console.error("Error on sign in", err);
    }
  };
  
  return (
    <div className="login">
      <h1 className="title">Sync Across</h1>
      <input
        className="form-item"
        type="email"
        placeholder="Email"
        onChange={(e) => setEmail(e.target.value)}
      />
      <input
        className="form-item"
        type="password"
        placeholder="Password"
        onChange={(e) => setPassword(e.target.value)}
      />
      <button className="form-button" onClick={(e) => loginUser()}>Log In</button>
    </div>
  );
};

export default Login;
