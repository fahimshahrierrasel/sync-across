import { Box, Button, Form, Heading, TextInput } from "grommet";
import styled from "styled-components";

const StyledForm = styled(Form)`
  width: 300px;
  padding: 1rem;
`;

const TagForm = ({ onFormSubmit, onFormCancel }) => {
  return (
    <StyledForm
      onSubmit={({ value }) => {
        onFormSubmit({ ...value });
      }}>
      <Box direction="column" gap="1rem">
        <Heading level="3">New Tag</Heading>
        <TextInput required name="title" placeholder="Tag title" />
        <Box direction="row" gap="0.5rem" justify="end">
          <Button label="Cancel" color="status-critical" onClick={() => onFormCancel()} />
          <Button type="submit" label="Save" />
        </Box>
      </Box>
    </StyledForm>
  );
};

export default TagForm;
