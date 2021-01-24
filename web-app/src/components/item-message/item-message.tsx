import React, { useState } from "react";
import "./item-message.css";

interface IItemMessage {
  placeholder: string;
  onSave: Function;
  onCancel: Function;
}

const ItemMessage = ({ placeholder, onSave, onCancel }: IItemMessage) => {
  const [field, setField] = useState<string>("");
  return (
    <div className="item-message">
      <div className="form-fields">
        <textarea
          rows={5}
          onChange={(e) => setField(e.target.value)}
        ></textarea>
      </div>
      <div className="form-actions">
        <button title="CANCEL" onClick={() => onCancel()}>
          Cancel
        </button>
        <button title="SAVE" onClick={() => onSave(field)}>
          Save
        </button>
      </div>
    </div>
  );
};

export default ItemMessage;
