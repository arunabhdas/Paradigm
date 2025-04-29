import React from "react";
import {View, Text, StyleSheet, ScrollView, SafeAreaView, FlatList} from "react-native";
import {jobData} from "../../data";
import styles from './jobpostings.style'

const JobPostings = () => {
    const teamOrder = ['Growth', 'Engineering', 'Product', 'Partner Success']

    // Group jobs by team
    const jobsByTeam = jobData.reduce((acc, job) => {
        if (!acc[job.team]) {
            acc[job.team] = [];
        }
        acc[job.team].push(job);
        return acc;
    }, {});

    // Render a job item
    const renderJobItem = ({ item }) => (
        <View style={styles.jobCard}>
            <Text style={styles.jobTitle}>{item.title}</Text>
            <Text style={styles.jobLocation}>{item.location}</Text>
        </View>
    );

    // Render a team section
    const renderTeamSection = (team) => {
        const teamJobs = jobsByTeam[team] || [];

        // Only render the team section if there are jobs
        if (teamJobs.length === 0) return null;

        return (
            <View key={team} style={styles.teamSection}>
                <Text style={styles.teamHeader}>{team}</Text>
                <FlatList
                    data={teamJobs}
                    renderItem={renderJobItem}
                    keyExtractor={(item) => item.id.toString()}
                    scrollEnabled={false}
                />
            </View>
        );
    };

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.screenHeader}>Job Postings</Text>
            <ScrollView contentContainerStyle={styles.scrollContainer}>
                {teamOrder.map(team => renderTeamSection(team))}
            </ScrollView>
        </SafeAreaView>
    );
}

export default JobPostings;