# Paradigmatic Mobile App
- Paradigmatic is the mobile app for the open source Paradigmatic platform.
- Paradigmatic is an agentic AI platform which serves as a marketplace for AI agents

# Develop

```
cd paradigmatic-app-react-native/Paradigmatic

npm run ios

npm run android

```

## Steps 

* Add .gitignore from https://github.com/expo/expo/blob/main/.gitignore


* Initialize a new project
```
cd paradigmatic-app-react-native
npx create-expo-app@latest -e with-router
```

```
╰─❯ npx create-expo-app@latest -e with-router
Need to install the following packages:
create-expo-app@3.2.0
Ok to proceed? (y) y

Creating an Expo project using the with-router example.

✔ What is your app named? … Paradigmatic
✔ Downloaded and extracted example files.
> npm install
added 977 packages, and audited 978 packages in 1m

93 packages are looking for funding
  run `npm fund` for details

5 low severity vulnerabilities

To address all issues (including breaking changes), run:
  npm audit fix --force

Run `npm audit` for details.

✅ Your project is ready!

To run your project, navigate to the directory and run one of the following npm commands.

- cd Paradigmatic
- npm run android
- npm run ios
- npm run web
```

* Install expo-font and expo-constants
* Install axios for networking
* Install react-native-dotenv for environment variables

```
cd paradigmatic-app-react-native

cd Paradigmatic

npm install expo-font axios react-native-dotenv
```


## Paranoid clean

```
rm -rf node_modules && rm -rf package-lock.json && rm -rf ~/.expo && npm install && npx expo start -c
```
