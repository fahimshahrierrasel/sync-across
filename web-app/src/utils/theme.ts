import { ThemeType } from "grommet";

const appTheme : ThemeType = {
  global: {
    input: {
      padding: {
        left: "100px",
      },
    },
    control: {
      border: {
        color: "light-4",
      },
    },
    focus: {
      border: {
        color: "brand",
      },
      outline: {
        color: "brand",
        size: "4px",
      },
      shadow: {
        color: "brand",
        size: "4px",
      },
    },
  },
  select: {
    icons: {
      margin: "none"
    }
  },
  tag: {
    value: {
      weight: "normal",
      size: "small",
      textAlign: "center",
    },
    pad: {
      top: "0",
      bottom: ".25rem",
      horizontal: "1rem",
    },
    border: {
      color: "brand",
      size: "small",
    },
  },
  formField: {
    border: {
      side: "all",
      color: "brand",
    },
  },
};

export default appTheme;
