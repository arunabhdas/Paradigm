import { View, Text } from "react-native";
import { useState } from "react";
import { ScrollView, SafeAreaView } from "react-native";
import { Stack, useRouter } from "expo-router";

import { COLORS, icons, images, SIZES } from "../constants";

import { 
    Nearbyjobs, Popularjobs, ScreenHeaderBtn, Welcome
} from "../components";


const Home = () => {
    return (
        <View>
            <Text>Welcome to Paradigmatic</Text>
        </View>
    );
};

export default Home;