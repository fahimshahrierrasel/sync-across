import React, { useState, useRef, useEffect } from "react";
import firebase from "firebase";
import Appbar from "../../components/appbar";
import Fab from "../../components/fab";
import SyncItem from "../../components/sync-item";
import { ISyncItem } from "../../data/models/isync-item";
import "./sync-across.css";
import db from "../../data/db";
import Portal from "../../portal";
import Modal from "../../components/modal";
import ItemMessage from "../../components/item-message";
import { ItemType } from "../../data/models/item-type";
import { getBrowserName, getFileNameFromURL, getFileType } from "../../utils/utils";
import storage from "../../data/storage";

const SyncAcross = () => {
  let [syncItems, setSyncItems] = useState<ISyncItem[]>([]);
  const inputFile = useRef<HTMLInputElement>(null);
  const [showCreateMessageModal, setShowCreateMessageModal] = useState<boolean>(
    false
  );

  const loadMessages = async () => {
    try {
      const messages = await db.getMessages();
      setSyncItems(messages);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadMessages();
  }, []);

  const createMessage = async (message: string) => {
    if (message.length < 5) {
      return;
    }
    let syncItem = {
      type: ItemType.MESSAGE,
      value: message,
      origin: getBrowserName(),
      createdAt: firebase.firestore.Timestamp.fromDate(new Date()),
    } as ISyncItem;
    await db.createMessage(syncItem);
    setShowCreateMessageModal(false);
    await loadMessages();
  };

  const uploadFile = async (files: FileList) => {
    const file = files[0];
    const fileType = getFileType(file.name);
    const downloadUrl = await storage.uploadFile(file);
    if (downloadUrl == null) {
      alert("File not uploaded");
      return;
    }

    let syncItem = {
      type: fileType,
      value: downloadUrl,
      origin: getBrowserName(),
      createdAt: firebase.firestore.Timestamp.fromDate(new Date()),
    } as ISyncItem;

    await db.createMessage(syncItem);
    await loadMessages();
  };

  const deleteMessage = async (syncItem: ISyncItem) => {
    if(syncItem.type === ItemType.FILE || syncItem.type === ItemType.IMAGE){
      const fileName = getFileNameFromURL(syncItem.value);
      if(fileName.length < 0){
        alert("File name can not determine can not delete!!");
        return;
      }
      const isDeleted = await storage.deleteFile(fileName);
      if(!isDeleted){
        alert("File can not be deleted!!");
        return;
      }
    }
    await db.deleteMessage(syncItem.id);
    await loadMessages();
  };

  return (
    <div className="sync-across">
      <Appbar />
      <div className="sync-items">
        {syncItems.map((item) => (
          <SyncItem
            key={item.id}
            syncItem={item}
            deleteItem={async () => {
              await deleteMessage(item);
            }}
          />
        ))}
      </div>
      <Fab
        createMessageAction={() => {
          setShowCreateMessageModal(true);
        }}
        uploadFileAction={() => {
          inputFile?.current?.click();
        }}
      />
      <input
        type="file"
        ref={inputFile}
        className="fileInput"
        onChange={async (e) => {
          await uploadFile(e.target?.files!);
        }}
      />
      {showCreateMessageModal && (
        <Portal>
          <Modal title="Create Message">
            <ItemMessage
              placeholder="New Message"
              onCancel={() => setShowCreateMessageModal(false)}
              onSave={async (message: string) => {
                await createMessage(message);
              }}
            />
          </Modal>
        </Portal>
      )}
    </div>
  );
};

export default SyncAcross;
