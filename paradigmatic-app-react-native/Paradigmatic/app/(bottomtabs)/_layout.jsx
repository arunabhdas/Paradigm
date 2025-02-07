import { Stack } from "expo-router";
import { useCallback, useEffect } from "react";
import { useFonts } from "expo-font";
import { View } from 'react-native';
import { Tabs, Redirect } from 'expo-router';
import * as SplashScreen from "expo-splash-screen";

// Keep the splash screen visible while we fetch resources
SplashScreen.preventAutoHideAsync();

/*  TODO-FIXME-CLEANUP
const TabsLayout = () => {
    return (
        <>
        <Tabs>
            <Tabs.Screen name="home" />
        </Tabs>
        </>
    )
}

export default TabsLayout;
*/


const BottomTabsLayout = () => {
    const [fontsLoaded] = useFonts({
        DMBold: require("../../assets/fonts/DMSans-Bold.ttf"),
        DMMedium: require("../../assets/fonts/DMSans-Medium.ttf"),
        DMRegular: require("../../assets/fonts/DMSans-Regular.ttf"),
    });

    useEffect(() => {
        const hideSplash = async () => {
            try {
                if (fontsLoaded) {
                    // Hide the splash screen after the fonts have loaded and the
                    // UI is ready.
                    await SplashScreen.hideAsync();
                }
            } catch (e) {
                console.warn('Error hiding splash screen:', e);
            }
        };

        hideSplash();
    }, [fontsLoaded]);

    if (!fontsLoaded) {
        return null;
    }

    return (
        <Stack 
            screenOptions={{
                headerStyle: {
                    backgroundColor: "#FFFFFF",
                },
                headerShadowVisible: false,
                headerTitle: "",
            }}
        />
    );
};

export default BottomTabsLayout;
