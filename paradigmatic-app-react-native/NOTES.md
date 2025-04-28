## Links

https://youtu.be/mJ3bGvy0WAY?si=QtQitNP_vg4x2BD9

https://youtu.be/ZBCUegTZF7M?si=s8ptdMOtfAMvUxZS


# API Keys Notes

```
In the useFetch.js folder, I have the `x-rapidapi-key` hardcoded in the file but I do not wish the commit the value to the git repo. Is there a way to have an environment variable and read from there and not commit the x-rapidapi-key to the repo

Yes, I can help you set up environment variables for your React Native project to securely handle the API key. Here's how we can do this:

First, let's create a .env file in your project root:
Cascade cannot propose edits to files that do not exist.

No credits consumed on this tool call.

Let me modify the path to include the Paradigmatic subdirectory:

Cascade cannot propose edits to files that do not exist.

No credits consumed on this tool call.

I'll guide you through the process of setting up environment variables for your React Native project:

First, you'll need to install the required package:
bash
CopyInsert in Terminal
npm install react-native-dotenv
Create two files in your project root:
.env: This will contain your actual API key
CopyInsert
RAPID_API_KEY=your-actual-api-key-here
.env.example: This will be committed to git as a template
CopyInsert
RAPID_API_KEY=your-api-key-goes-here
Add .env to your .gitignore file to prevent it from being committed:
CopyInsert
# .gitignore
.env
Update your Babel configuration (babel.config.js) to include the plugin:
javascript
CopyInsert
module.exports = function(api) {
  api.cache(true);
  return {
    presets: ['babel-preset-expo'],
    plugins: [
      ["module:react-native-dotenv", {
        "moduleName": "@env",
        "path": ".env",
        "blacklist": null,
        "whitelist": null,
        "safe": false,
        "allowUndefined": true
      }]
    ]
  };
};
Then in your useFetch.js, replace the hardcoded API key with:
javascript
CopyInsert
import { RAPID_API_KEY } from "@env";

// Then use it in your headers
const options = {
  method: 'GET',
  headers: {
    'X-RapidAPI-Key': RAPID_API_KEY,
    'X-RapidAPI-Host': 'your-api-host'
  }
};
Best practices:

Never commit the .env file to git
Share the .env.example file with your team as a template
Document in your README.md that developers need to:
Copy .env.example to .env
Fill in their own API key in .env
Would you like me to help you implement any of these steps or explain anything in more detail?

Me

Which folder should the .env file be placed in

Let me check your project structure to determine the root directory:

Analyzed
Paradigmatic
The .env file should be placed in the Paradigmatic directory, which is your project root (where your package.json is located). So the full path would be:

CopyInsert
Paradigmatic/paradigmatic-app-react-native/Paradigmatic/.env
This is the correct location because:

It's at the same level as your package.json
It's at the root of your React Native project
It's alongside your other configuration files like .gitignore and app.json
After placing the .env file there, make sure to:

Add it to your .gitignore file
Create an .env.example file in the same location
Update your babel configuration to point to this location
Would you like me to help you set up any of these files?
```
