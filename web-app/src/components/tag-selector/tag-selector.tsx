import { Box, Tag, Select } from "grommet";
import { useContext, useState } from "react";
import styled from "styled-components";
import { AppStoreContext } from "../../data/store";

const StyledSelect = styled(Select)`
  height: 30px;
  width: 70px;
`;

export default function TagSelector({ currentTags, onTagsChanged }) {
  const { state } = useContext(AppStoreContext);
  const [selectedTags, setSelectedTags] = useState<string[]>(currentTags);

  return (
    <Box wrap direction="row" gap="5px" alignContent="start">
      {selectedTags.map((tag) => (
        <Tag
          key={tag}
          value={tag}
          onRemove={() => {
            let tags = selectedTags.filter((tagItem) => tagItem !== tag);
            setSelectedTags(tags);
            onTagsChanged(tags);
          }}
        />
      ))}

      <StyledSelect
        placeholder="tags"
        options={state.tags
          .filter((item) => !selectedTags.some((tag) => tag === item.title))
          .map((tag) => tag.title)}
        value=""
        onChange={(option) => {
          let tag = option.target.value;
          let tags = [...selectedTags, tag];
          setSelectedTags(tags);
          onTagsChanged(tags);
        }}
      />
    </Box>
  );
}
