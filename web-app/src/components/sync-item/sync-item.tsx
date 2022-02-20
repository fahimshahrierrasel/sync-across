import {
  Box,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Heading,
  Layer,
  Tag,
  Text,
} from "grommet";
import {
  Trash,
  Multimedia,
  Edit,
  Notes,
  Bookmark,
  StatusUnknown,
} from "grommet-icons";
import { useContext, useState } from "react";
import db from "../../data/db";
import { ISyncItem } from "../../data/models/isync-item";
import { ItemType } from "../../data/models/item-type";
import { ActionTypes, AppStoreContext } from "../../data/store";
import { getFormattedDate, isUrl } from "../../utils/utils";
import ItemForm from "../item-form";

interface ISyncItemProps {
  syncItem: ISyncItem;
  onDeleteItem: Function;
}

const SyncItem = ({ syncItem, onDeleteItem }: ISyncItemProps) => {
  const { dispatch } = useContext(AppStoreContext);
  const [showForm, setShowForm] = useState(false);

  const getMessageDetails = (syncItem: ISyncItem) => {
    switch (syncItem.type) {
      case ItemType.MULTIMEDIA:
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

  const getTypeIcons = (type: string) => {
    switch (type) {
      case "bookmark": {
        return <Bookmark />;
      }
      case "multimedia": {
        return <Multimedia />;
      }
      case "notes": {
        return <Notes />;
      }
      default: {
        return <StatusUnknown />;
      }
    }
  };

  return (
    <Card flex="grow">
      <CardHeader pad="small">
        <Heading level="4">{syncItem.title ?? "Untitled"}</Heading>
        <Box direction="row" gap="0.5rem">
          <Edit
            color="brand"
            onClick={() => {
              if (syncItem.type === ItemType.MULTIMEDIA) {
                alert("Cannot update multimedia type");
                return;
              }
              setShowForm(true);
            }}
          />
          <Trash
            color="status-critical"
            onClick={() => {
              onDeleteItem();
            }}
          />
        </Box>
      </CardHeader>
      <CardBody pad="small">
        <Text textAlign="start">{getMessageDetails(syncItem)}</Text>
      </CardBody>
      <CardFooter pad="small">
        <Box direction="row" gap="0.5rem">
          {getTypeIcons(syncItem.type)}
          <Text>|</Text>
          <Text>{getFormattedDate(syncItem.createdAt.toDate())}</Text>
          <Text>|</Text>
          <Text>{syncItem.origin}</Text>
        </Box>
        <Box direction="row" justify="end" gap="3px">
          {syncItem.tags &&
            syncItem.tags.map((tag) => (
              <Tag key={`${syncItem.id}-${tag}`} value={tag} />
            ))}
        </Box>
      </CardFooter>
      {showForm && (
        <Layer onEsc={() => setShowForm(false)}>
          <ItemForm
            syncItem={syncItem}
            onFormCancel={() => setShowForm(false)}
            onFormSubmit={async (updatedItem) => {
              const updatedSyncItem = {
                ...syncItem,
                ...updatedItem,
              };
              dispatch({type: ActionTypes.ToggleLoading, data: true});
              const item = await db.updateSyncItem(updatedSyncItem);
              if(item){
                console.log(item);
                dispatch({ type: ActionTypes.UpdateSyncItem, data: item });
                setShowForm(false);
              }
              dispatch({type: ActionTypes.ToggleLoading, data: false});
            }}
          />
        </Layer>
      )}
    </Card>
  );
};

export default SyncItem;
