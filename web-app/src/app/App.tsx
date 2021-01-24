import React, { useState, useEffect } from "react";
import firebase from "firebase";
import Login from "../pages/login";
import SyncAcross from "../pages/sync-across";
import firebaseApp from "../data/firebase";
import Bowser from "bowser";

import "./App.css";

function App() {
  const [user, setUser] = useState<firebase.User | null>(null);

  useEffect(() => {
    firebaseApp.auth.onAuthStateChanged(
      (user) => {
        setUser(user);
      },
      (error: firebase.auth.Error) => {
        console.error(error);
      }
    );
  }, []);

  return <div className="App">{user ? <SyncAcross /> : <Login />}</div>;
}

export default App;
