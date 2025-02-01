import { View, Text } from "react-native";
import { useState } from "react";
import { ScrollView, SafeAreaView } from "react-native";
import { Stack, useRouter } from "expo-router";

import { COLORS, icons, images, SIZES } from "../constants";

import { 
    Nearbyjobs, Popularjobs, ScreenHeaderBtn, Welcome
} from "../components";


const Home = () => {
    const router = useRouter();
    return (
        <SafeAreaView>
            <Text>Welcome to Paradigmatic</Text>
        </SafeAreaView>
    );
};

export default Home;