import { ItemType } from "./item-type";
import firebaseApp from 'firebase/app';

export interface ISyncItem {
    id: string;
    title: string;
    type: ItemType;
    value: string;
    origin: string;
    tags: string[];
    createdAt: firebaseApp.firestore.Timestamp;
}
/**
 * id
 * type
 * title
 * value
 * origin
 * createdAt
 * tags
 */