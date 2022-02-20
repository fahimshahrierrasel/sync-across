import { Box, Button, Form, Grommet, Heading, TextInput, Image } from "grommet";
import firebaseApp from "../../data/firebase";
import appTheme from "../../utils/theme";
import Logo from "../../assets/logo.svg";

const Login = () => {
  const loginUser = async ({ email, password }: any) => {
    if (password.length < 3) {
      alert("Password too short");
      return;
    }
    try {
      await firebaseApp.auth.signInWithEmailAndPassword(email, password);
    } catch (err) {
      console.error("Error on sign in", err);
    }
  };

  return (
    <Grommet theme={appTheme}>
      <Box
        background="light-6"
        align="center"
        justify="center"
        height="100vh"
        flex="grow"
        width="100vw"
      >
        <Box
          width="450px"
          background="light-1"
          pad="medium"
          round="small"
          elevation="medium"
        >
          <Form
            onSubmit={({ value }) => {
              loginUser({ ...value });
            }}
          >
            <Box direction="column" gap="1rem" align="center">
              <Box height="80px" width="80px">
                <Image src={Logo} />
              </Box>
              <Heading level="2" color="brand">
                Sync Across
              </Heading>
              <TextInput
                required
                name="email"
                placeholder="Email"
                type="email"
              />

              <TextInput
                required
                name="password"
                placeholder="Password"
                type="password"
              />
              <Button primary label="Signin" type="submit" size="large" />
            </Box>
          </Form>
        </Box>
      </Box>
    </Grommet>
  );
};

export default Login;
