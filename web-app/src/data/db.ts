import firebaseApp from "./firebase";
import { ISyncItem } from "./models/isync-item";

const collections = {
  MESSAGE: "messages",
};

const db = {
  createMessage: async (syncItem: ISyncItem): Promise<ISyncItem> => {
    const { id, ...newItem } = syncItem;
    const messageRef = await firebaseApp.db
      .collection(collections.MESSAGE)
      .add(newItem);

    return {
      ...syncItem,
      id: messageRef.id,
    };
  },
  getMessages: async (): Promise<ISyncItem[]> => {
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
  deleteMessage: async (id: string): Promise<void> => {
    await firebaseApp.db.collection(collections.MESSAGE).doc(id).delete();
  },
};

export default db;
