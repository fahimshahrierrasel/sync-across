import {
  Box,
  Button,
  FileInput,
  Form,
  Heading,
  RadioButtonGroup,
  TextArea,
  TextInput,
} from "grommet";
import { useState } from "react";
import { ItemType, itemTypes } from "../../data/models/item-type";
import TagSelector from "../tag-selector";
import styled from "styled-components";
import "../../extensions";

const StyledForm = styled(Form)`
  width: 600px;
  padding: 1rem;
`;

export default function ItemForm({ syncItem, onFormCancel, onFormSubmit }) {
  const [tags, setTags] = useState(syncItem?.tags ?? []);
  const [type, setType] = useState(ItemType.BOOKMARK.toString());
  return (
    <StyledForm
      onSubmit={({ value }) => {
        onFormSubmit({ ...value, tags: tags });
      }}
    >
      <Box direction="column" gap="1rem">
        <Heading level="3">{syncItem !== null ? "Update" : "New"} Item</Heading>
        <TextInput
          required
          name="title"
          defaultValue={syncItem?.title}
          placeholder="Title"
        />
        <RadioButtonGroup
          direction="row"
          name="type"
          defaultValue={syncItem?.type}
          value={type}
          options={itemTypes()
            .filter((item) => syncItem == null || item !== ItemType.MULTIMEDIA)
            .map((item) => {
              return { label: item.capitalize(), value: item.toLowerCase() };
            })}
          onChange={(event) => {
            setType(event.target.value);
          }}
        />
        {type === ItemType.Note && (
          <TextArea
            required
            name="value"
            resize={false}
            defaultValue={syncItem?.value}
            placeholder="Write note here"
          />
        )}
        {type === ItemType.BOOKMARK && (
          <TextInput
            required
            name="value"
            placeholder="Bookmark"
            defaultValue={syncItem?.value}
          />
        )}
        {type === ItemType.MULTIMEDIA && (
          <FileInput name="value" multiple={false} required />
        )}
        <TagSelector
          currentTags={tags}
          onTagsChanged={(tags) => setTags(tags)}
        />
        <Box direction="row" gap="0.5rem" justify="end">
          <Button
            label="Cancel"
            color="status-critical"
            onClick={() => onFormCancel()}
          />
          <Button type="submit" label="Save" />
        </Box>
      </Box>
    </StyledForm>
  );
}
