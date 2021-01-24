import React, { useState } from "react";
import add from "../../assets/plus.svg";
import text from "../../assets/text.svg";
import file from "../../assets/file.svg";
import close from "../../assets/close.svg";
import "./fab.css";

interface IFabProps {
  createMessageAction: Function;
  uploadFileAction: Function;
}

const Fab = ({ createMessageAction, uploadFileAction }: IFabProps) => {
  const [fabOpen, setFabOpen] = useState<boolean>(false);

  return (
    <div className="fab-container">
      <div className={`mini-fab__container ${!fabOpen ? "hide" : ""}`}>
        <div
          className="speed-dial"
          onClick={() => {
            setFabOpen(!fabOpen);
            uploadFileAction();
          }}
        >
          <span>New File</span>
          <div className="fab fab-mini">
            <img src={file} />
          </div>
        </div>
        <div
          className="speed-dial"
          onClick={() => {
            setFabOpen(!fabOpen);
            createMessageAction();
          }}
        >
          <span>New Message</span>
          <div className="fab fab-mini">
            <img src={text} />
          </div>
        </div>
      </div>
      <div
        className="fab"
        onClick={() => {
          setFabOpen(!fabOpen);
        }}
      >
        <img src={fabOpen ? close : add} />
      </div>
    </div>
  );
};

export default Fab;
