import { ItemType } from "./item-type";
import firebaseApp from 'firebase/app';

export interface ISyncItem {
    id: string;
    type: ItemType;
    value: string;
    origin: string;
    createdAt: firebaseApp.firestore.Timestamp;
}