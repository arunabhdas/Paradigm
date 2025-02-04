import 'dotenv/config';

export default {
  expo: {
    name: "Paradigmatic",
    extra: {
      rapidApiKey: process.env.RAPID_API_KEY,
    },
  },
};