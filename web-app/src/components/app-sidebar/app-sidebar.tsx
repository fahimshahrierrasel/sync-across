import {
  Anchor,
  Box,
  Button,
  Collapsible,
  Heading,
  Image,
  Layer,
  Nav,
  Sidebar,
  Tag,
} from "grommet";
import {
  BlockQuote,
  Bookmark,
  Flows,
  Multimedia,
  Tag as TagIcon,
  AddCircle,
  CaretUpFill,
  CaretDownFill,
} from "grommet-icons";
import { useContext, useEffect, useState } from "react";
import Logo from "../../assets/logo.svg";
import { ActionTypes, AppStoreContext } from "../../data/store";
import TagForm from "../tag-form";
import db from "../../data/db";
import firebaseApp from "../../data/firebase";

export default function AppSidebar() {
  const { state, dispatch } = useContext(AppStoreContext);
  const [isTagOpen, setIsTagOpen] = useState(false);
  const [showTagForm, setShowTagForm] = useState(false);

  useEffect(() => {
    const loadTypes = async () => {
      try {
        const tags = await db.getTags();
        dispatch({ type: ActionTypes.AddTags, data: tags });
      } catch (err) {
        console.error(err);
      }
    };

    loadTypes();
  }, [dispatch]);

  return (
    <Sidebar
      direction="column"
      gap="0"
      pad={{ vertical: "1rem", horizontal: ".5rem" }}
      header={
        <Box direction="column" align="center">
          <Box height="80px" width="80px">
            <Image src={Logo} />
          </Box>
          <Heading level="3" color="brand">
            Sync Across
          </Heading>
        </Box>
      }
      footer={
        <Button
          label="Logout"
          onClick={() => {
            firebaseApp.auth.signOut();
          }}
        />
      }
      border={{ side: "right", size: "small", color: "light-3" }}
    >
      <Nav
        pad={{ left: "medium", top: "medium" }}
        direction="column"
        alignContent="center"
      >
        <Anchor icon={<Flows />} label="All" />
        <Anchor icon={<Bookmark />} label="Bookmarks" />
        <Anchor icon={<Multimedia />} label="Multimedias" />
        <Anchor icon={<BlockQuote />} label="Notes" />
        <Box direction="row" justify="between" align="center">
          <Box direction="row" align="center">
            <Anchor
              icon={<TagIcon />}
              label="Tags"
              onClick={() => setIsTagOpen(!isTagOpen)}
            />
            {isTagOpen ? <CaretUpFill /> : <CaretDownFill />}
          </Box>
          <AddCircle
            color="brand"
            onClick={() => {
              setShowTagForm(true);
            }}
          />
        </Box>
      </Nav>
      <Collapsible open={isTagOpen}>
        <Box
          wrap
          direction="row"
          overflow={{ horizontal: "hidden", vertical: "auto" }}
          height="100%"
          margin={{ bottom: "medium" }}
          alignContent="start"
        >
          {state.tags.map((tag) => (
            <Box key={tag.id} pad="3px">
              <Tag value={tag.title} />
            </Box>
          ))}
        </Box>
      </Collapsible>
      {showTagForm && (
        <Layer onEsc={() => setShowTagForm(false)}>
          <TagForm
            onFormCancel={() => setShowTagForm(false)}
            onFormSubmit={async (tag) => {
              if (tag.title.length <= 3) {
                alert("Tag title must be greater than 3");
                return;
              }
              dispatch({ type: ActionTypes.ToggleLoading, data: true });
              const newTag = await db.createTag(tag);
              dispatch({ type: ActionTypes.AddTag, data: newTag });
              setShowTagForm(false);
              dispatch({ type: ActionTypes.ToggleLoading, data: false });
            }}
          />
        </Layer>
      )}
    </Sidebar>
  );
}
