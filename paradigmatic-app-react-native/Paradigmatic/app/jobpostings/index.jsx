import React from 'react';
import { Stack } from 'expo-router';
import JobPostings from "../../components/jobpostings/JobPostings";

const JobPostingsScreen = () => {
  return (
    <>
      <Stack.Screen
        options={{
          headerTitle: "Job Postings",
        }}
      />
        <JobPostings/>
    </>
  );
};

export default JobPostingsScreen;