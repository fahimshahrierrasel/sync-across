import React from "react";
import "./sync-item.css";
import deleteIcon from "../../assets/deleteIcon.svg";
import { ISyncItem } from "../../data/models/isync-item";
import { ItemType } from "../../data/models/item-type";
import { isUrl } from "../../utils/utils";

interface ISyncItemProps {
  syncItem: ISyncItem;
  deleteItem: Function;
}

const SyncItem = ({ syncItem, deleteItem }: ISyncItemProps) => {
  const getMessageDetails = (syncItem: ISyncItem) => {
    switch (syncItem.type) {
      case ItemType.IMAGE:
        return <img src={syncItem.value} alt="SyncItem" />;
      case ItemType.FILE:
        return (
          <a href={syncItem.value} target="_blank" rel={"noreferrer"}>
            {syncItem.value}
          </a>
        );
      default:
        if (isUrl(syncItem.value)) {
          return (
            <a href={syncItem.value} target="_blank" rel={"noreferrer"}>
              {syncItem.value}
            </a>
          );
        } else {
          return <p>{syncItem.value}</p>;
        }
    }
  };

  const getFormattedDate = (date: Date): string => {
    return new Intl.DateTimeFormat("en-US", {
      dateStyle: "medium",
      timeStyle: "medium",
    } as Intl.DateTimeFormatOptions).format(date);
  };

  return (
    <div className="sync-item">
      {getMessageDetails(syncItem)}
      <hr />
      <div className="sync-item__footer">
        <div className="sync-item__meta">
          <span>{syncItem.origin}</span>
          <span>{getFormattedDate(syncItem.createdAt.toDate())}</span>
        </div>
        <div className="action-button">
          <img
            src={deleteIcon}
            alt="delete-icon"
            onClick={() => {
              deleteItem();
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default SyncItem;
