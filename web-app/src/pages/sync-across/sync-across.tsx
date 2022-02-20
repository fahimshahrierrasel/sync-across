import { Box, Button, Grid, Grommet, Layer, Spinner, TextInput } from "grommet";
import { AddCircle, FormSearch } from "grommet-icons";
import { useContext, useEffect, useState } from "react";
import AppSidebar from "../../components/app-sidebar";
import ItemForm from "../../components/item-form";
import SyncItem from "../../components/sync-item";
import db from "../../data/db";
import { ISyncItem } from "../../data/models/isync-item";
import { ItemType } from "../../data/models/item-type";
import storage from "../../data/storage";
import { ActionTypes, AppStoreContext } from "../../data/store";
import appTheme from "../../utils/theme";
import { getFileNameFromURL } from "../../utils/utils";

const SyncAcross = () => {
  const { state, dispatch } = useContext(AppStoreContext);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    const loadMessages = async () => {
      try {
        dispatch({ type: ActionTypes.ToggleLoading, data: true });
        const messages = await db.getSyncItems();
        dispatch({ type: ActionTypes.AddSyncItems, data: messages });
        dispatch({ type: ActionTypes.ToggleLoading, data: false });
      } catch (err) {
        console.error(err);
      }
    };
    loadMessages();
  }, [dispatch]);

  const createSyncItem = async (syncItem): Promise<boolean> => {
    if (syncItem.title.length < 5) {
      alert("Title is too short");
      return false;
    }
    dispatch({ type: ActionTypes.ToggleLoading, data: true });
    if (syncItem.type === ItemType.MULTIMEDIA) {
      console.log(typeof syncItem.value, syncItem.value, syncItem);
      const downloadUrl = await uploadFile(syncItem.value);
      if (!downloadUrl) {
        dispatch({ type: ActionTypes.ToggleLoading, data: false });
        return false;
      }
      syncItem.value = downloadUrl;
    }
    const newItem = await db.createSyncItem(syncItem);
    dispatch({ type: ActionTypes.ToggleLoading, data: false });
    if (newItem) {
      dispatch({ type: ActionTypes.AddSyncItem, data: newItem });
      return true;
    }
    return false;
  };

  const uploadFile = async (files: FileList) => {
    const file = files[0];
    const downloadUrl = await storage.uploadFile(file);
    if (downloadUrl == null) {
      alert("File not uploaded");
      return;
    }
    return downloadUrl;
  };

  const deleteSyncItem = async (syncItem: ISyncItem) => {
    dispatch({ type: ActionTypes.ToggleLoading, data: true });
    if (syncItem.type === ItemType.MULTIMEDIA) {
      const fileName = getFileNameFromURL(syncItem.value);
      if (fileName.length < 0) {
        dispatch({ type: ActionTypes.ToggleLoading, data: false });
        alert("File name can not determine can not delete!!");
        return;
      }
      const isDeleted = await storage.deleteFile(fileName);
      if (!isDeleted) {
        dispatch({ type: ActionTypes.ToggleLoading, data: false });
        alert("File can not be deleted!!");
        return;
      }
    }
    await db.deleteSyncItem(syncItem.id);
    dispatch({ type: ActionTypes.DeleteSyncItem, data: syncItem.id });
    dispatch({ type: ActionTypes.ToggleLoading, data: false });
  };

  return (
    <Grommet theme={appTheme}>
      <Grid height="100vh" columns={["300px", "auto"]}>
        <AppSidebar />
        <Grid rows={["60px", "auto"]}>
          <Box direction="row" pad="small" align="center">
            <Button
              icon={<AddCircle size="32px" color="brand" />}
              onClick={() => setShowForm(true)}
            />
            <FormSearch size="large" color="brand" />
            <TextInput placeholder="Search..." />
          </Box>
          <Box
            direction="column"
            height={"calc(100vh - 65px)"}
            overflow={{ horizontal: "hidden", vertical: "auto" }}
            pad="medium"
            gap="small"
          >
            {state.syncItems.map((item) => (
              <SyncItem
                key={item.id}
                syncItem={item}
                onDeleteItem={async () => {
                  await deleteSyncItem(item);
                }}
              />
            ))}
          </Box>
        </Grid>
      </Grid>
      {showForm && (
        <Layer onEsc={() => setShowForm(false)}>
          <ItemForm
            syncItem={null}
            onFormCancel={() => setShowForm(false)}
            onFormSubmit={async (syncItem) => {
              const canClose = await createSyncItem(syncItem);
              if (canClose) {
                setShowForm(false);
              }
            }}
          />
        </Layer>
      )}
      {state.isLoading && (
        <Layer
          full
          modal
          background={{ color: "brand", opacity: "weak" }}
          animation="fadeIn"
        >
          <Box align="center" justify="center" height="100%" width="100%">
            <Spinner size="large" color="accent-1" />
          </Box>
        </Layer>
      )}
    </Grommet>
  );
};

export default SyncAcross;
