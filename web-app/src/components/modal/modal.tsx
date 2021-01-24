import React from "react";
import "./modal.css";

interface IModalProps {
  title: string;
  children: React.ReactNode;
}

const Modal = ({ title, children }: IModalProps) => {
  return (
    <div className="modal">
      <div className="modal-container">
        <h4>{title}</h4>
        {children}
      </div>
    </div>
  );
};

export default Modal;