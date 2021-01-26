import Bowser from "bowser";
import { ItemType } from "../data/models/item-type";
const browser = Bowser.getParser(window.navigator.userAgent);
const imageExtensions = ["jpg", "jpeg", "png", "gif"];

//eslint-disable-next-line
const urlRegex = /^[(http(s)?):\/\/(www\.)?a-zA-Z0-9@:%._\-\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/;

export const getBrowserName = () => {
  const browserInfo = browser.getBrowser();
  return `${browserInfo.name}-${browserInfo.version}`;
};

export const getFileType = (name: string): ItemType => {
  let extension = name.split(".").pop();
  return imageExtensions.includes(extension ?? "")
    ? ItemType.IMAGE
    : ItemType.FILE;
};

export const isUrl = (message: string): boolean => {
  return urlRegex.test(message);
};

export const getFileNameFromURL = (url: string): string => {
  const pureUrl = url.substring(0, url.lastIndexOf("?"));
  let name = pureUrl.split("/").pop() as string;
  name = name.substring(name.lastIndexOf("%2F") + 3);
  name = name.replace("sync_files/","");
  return name;
}