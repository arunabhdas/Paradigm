import { View, Text, SafeAreaView, ScrollView } from 'react-native';
import { Stack, useRouter } from 'expo-router';
import { COLORS, SIZES } from '../../constants';
import { ScreenHeaderBtn } from '../../components';

const Landing = () => {
    const router = useRouter();

    return (
        <SafeAreaView style={{ flex: 1, backgroundColor: COLORS.darkCharcoal }}>
            <Stack.Screen
                options={{
                    headerStyle: { backgroundColor: COLORS.darkCharcoal },
                    headerShadowVisible: false,
                    headerTitle: "Search Results",
                    headerTitleStyle: { color: COLORS.lightWhite },
                    headerRight: () => (
                        <ScreenHeaderBtn
                            iconUrl={icons.menu}
                            dimension="100%"
                        />
                    ),
                }}
            />

            <ScrollView showsVerticalScrollIndicator={false}>
                <View style={{ flex: 1, padding: SIZES.medium }}>
                    <Text style={{ color: COLORS.lightWhite, fontSize: SIZES.large }}>
                        Search Results Page
                    </Text>
                    {/* Add your search results components here */}
                </View>
            </ScrollView>
        </SafeAreaView>
    );
};

export default Landing;