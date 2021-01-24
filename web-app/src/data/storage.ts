import firebaseApp from "./firebase";

const storageLocations = {
  SYNC_FILES: "sync_files",
};

const storage = {
  uploadFile: async (file: File): Promise<string | null> => {
    const folderRef = firebaseApp.storage
      .ref()
      .child(storageLocations.SYNC_FILES);
    try {
      const extension = file.name.split(".").pop() as string;
      const fileRef = folderRef.child(`${new Date().getTime()}.${extension}`);
      const snapshot = await fileRef.put(file);
      const downloadUrl = await snapshot.ref.getDownloadURL();
      return downloadUrl;
    } catch (error) {
      console.error(error);
      return null;
    }
  },
  deleteFile: async (fileName: string): Promise<boolean> => {
    const fileRef = firebaseApp.storage
      .ref()
      .child(storageLocations.SYNC_FILES)
      .child(fileName);

    try {
      await fileRef.delete();
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  },
};

export default storage;
