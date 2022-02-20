import firebase from "firebase";
import { getBrowserName } from "../utils/utils";
import firebaseApp from "./firebase";
import { ISyncItem } from "./models/isync-item";
import { ITagItem } from "./models/itag-item";

const collections = {
  MESSAGE: "messages",
  TAG: "tags",
};

const db = {
  createSyncItem: async (syncItem: ISyncItem): Promise<ISyncItem> => {
    let { id, ...newItem } = syncItem;
    newItem = {
      ...newItem,
      origin: getBrowserName(),
      createdAt: firebase.firestore.Timestamp.fromDate(new Date()),
    };
    const messageRef = await firebaseApp.db
      .collection(collections.MESSAGE)
      .add(newItem);

    return {
      ...newItem,
      id: messageRef.id,
    };
  },
  createTag: async (tag: ITagItem): Promise<ITagItem> => {
    let { id, ...newTag } = tag;
    newTag.createdAt = firebase.firestore.Timestamp.fromDate(new Date());
    const tagRef = await firebaseApp.db.collection(collections.TAG).add(newTag);

    return {
      ...newTag,
      id: tagRef.id,
    };
  },
  updateSyncItem: async (syncItem: ISyncItem): Promise<ISyncItem> => {
    const itemRef = firebaseApp.db.collection(collections.MESSAGE).doc(syncItem.id);
    await itemRef.update({
      title: syncItem.title,
      type: syncItem.type,
      value: syncItem.value,
      tags: syncItem.tags,
    });
    return syncItem;
  },
  getTags: async (): Promise<ITagItem[]> => {
    const tagRef = await firebaseApp.db
      .collection(collections.TAG)
      .orderBy("createdAt", "desc")
      .get();
    const tags: ITagItem[] = [];
    tagRef.forEach((doc) => {
      const data = doc.data();
      tags.push({
        ...data,
        id: doc.id,
      } as ITagItem);
    });
    return tags;
  },
  getSyncItems: async (): Promise<ISyncItem[]> => {
    const messageRef = await firebaseApp.db
      .collection(collections.MESSAGE)
      .orderBy("createdAt", "desc")
      .get();
    const messages: ISyncItem[] = [];
    messageRef.forEach((doc) => {
      const data = doc.data();
      messages.push({
        ...data,
        id: doc.id,
      } as ISyncItem);
    });
    return messages;
  },
  deleteSyncItem: async (id: string): Promise<void> => {
    await firebaseApp.db.collection(collections.MESSAGE).doc(id).delete();
  },
};

export default db;
