import firebaseApp from "firebase/app";

export interface ITagItem {
  id: string;
  title: string;
  createdAt: firebaseApp.firestore.Timestamp;
}
