import { useEffect } from "react";
import { createPortal } from "react-dom";

interface IPortalProps {
  children: React.ReactNode;
}

const Portal = ({ children }: IPortalProps) => {
  const mount = document.getElementById("portal");
  const el = document.createElement("div");

  useEffect(() => {
    mount?.appendChild(el);
    return () => {
      mount?.removeChild(el);
    };
  }, [el, mount]);

  return createPortal(children, el);
};

export default Portal;