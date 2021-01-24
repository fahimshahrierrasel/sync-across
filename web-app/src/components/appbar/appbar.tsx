import React from "react";
import firebaseApp from "../../data/firebase";
import "./appbar.css";

const Appbar = () => {
  return (
    <div className="appbar">
      <span className="title">Sync Across</span>

      <button
        onClick={(e) => {
          firebaseApp.auth.signOut();
        }}
      >
        Signout
      </button>
    </div>
  );
};

export default Appbar;
