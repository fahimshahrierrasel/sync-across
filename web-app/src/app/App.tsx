import  { useState, useEffect, useReducer } from "react";
import firebase from "firebase";
import Login from "../pages/login";
import SyncAcross from "../pages/sync-across";
import firebaseApp from "../data/firebase";
import { appReducer, AppStoreContext, DEFAULT_STATE } from "../data/store";

function App() {
  const [user, setUser] = useState<firebase.User | null>(null);
  const [state, dispatch] = useReducer(appReducer, DEFAULT_STATE);
  const store = { state, dispatch };

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

  return (
    <>
      {user ? (
        <AppStoreContext.Provider value={store}>
          <SyncAcross />
        </AppStoreContext.Provider>
      ) : (
        <Login />
      )}
    </>
  );
}

export default App;
