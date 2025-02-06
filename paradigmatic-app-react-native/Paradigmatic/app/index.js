import { View, Text } from "react-native";
import { useState } from "react";
import { ScrollView, SafeAreaView } from "react-native";
import { Stack, useRouter } from "expo-router";

import { COLORS, icons, images, SIZES } from "../constants";
import styles from "../components/home/welcome/welcome.style";

import { 
    Nearbytasks, Popularproducts, ScreenHeaderBtn, Welcome
} from "../components";


const Home = () => {
    const router = useRouter();
    return (
        <SafeAreaView style={{ flex: 1, backgroundColor: COLORS.darkCharcoal }}>
            <Stack.Screen
            options={{
                headerStyle: { backgroundColor: COLORS.darkCharcoal },
                headerShadowVisible: false,
                headerLeft: () => (
                    <ScreenHeaderBtn iconUrl={icons.menu} dimension="60%" />
                ),
                headerRight: () => (
                    <ScreenHeaderBtn iconUrl={images.profile} dimension="100%" />
                ),
                headerTitle: "",
            }}
            
            />
            <ScrollView showsVerticalScrollIndicator={false}>
                <View style={{ flex: 1, padding: SIZES.medium }}>
                    <Welcome />
                    <Popularproducts />
                    <Nearbytasks />
                </View>
            </ScrollView>
        </SafeAreaView>
    );
};

export default Home;