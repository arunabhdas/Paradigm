import React from 'react'
import { 
    View, 
    Text, 
    SafeAreaView, 
    ScrollView, 
    ActivityIndicator, 
    RefreshControl 
} from 'react-native';
import { Stack, useRouter, useLocalSearchParams } from 'expo-router';

import { useCallback, useState } from 'react';

import { Company, JobAbout, JobFooter, JobTabs, ScreenHeaderBtn, Specifics } from '../../components';

import { COLORS, icons, SIZES } from '../../constants';

import useFetch from '../../hook/useFetch';



const TaskDetails = () => {
    const params = useLocalSearchParams();

    const router = useRouter();

    const { data, isLoading, error, refetch } = useFetch(
        'job-details', {
            job_id: params.id
        }
    );

    return (
        <SafeAreaView style={{ flex: 1, backgroundColor: COLORS.lightGreen }}>
        <Stack.Screen></Stack.Screen>
        </SafeAreaView>
    )
}

export default TaskDetails