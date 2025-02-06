const COLORS = {
  primary: "#292C41",
  secondary: "#889EAF",
  tertiary: "#373F55",
  quad: "#233C5A",
  cello: "#6399cd",

  gray: "#83829A",
  gray2: "#C1C0C8",

  white: "#F3F4F8",
  lightWhite: "#FAFAFC",

  darkCharcoal: "#0F0F0F",
  lightGreen: "#036D59",
  skyGreen: "#95FFEB",
  coralGreen: "#50D1BA",
  
  // New dark mode colors
  background: "#292C41",
};

/*
const COLORS = {
  primary: "#312651",
  secondary: "#444262",
  tertiary: "#FF7754",

  gray: "#83829A",
  gray2: "#C1C0C8",

  white: "#F3F4F8",
  lightWhite: "#FAFAFC",

  darkGreen: "001A14",

  lightGreen: "036D59",

  skyGreen: "95FFEB",

  coralGreen: "50D1BA",
};
*/

const FONT = {
  regular: "DMRegular",
  medium: "DMMedium",
  bold: "DMBold",
};

const SIZES = {
  xSmall: 10,
  small: 12,
  medium: 16,
  large: 20,
  xLarge: 24,
  xxLarge: 32,
};

const SHADOWS = {
  small: {
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 2,
  },
  medium: {
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 5.84,
    elevation: 5,
  },
};

export { COLORS, FONT, SIZES, SHADOWS };
