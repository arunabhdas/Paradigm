import { useEffect, useState } from 'react';

import axios from 'axios';

import Constants from 'expo-constants';

const useFetch = () => {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);


    const options = {
        method: 'GET',
        hostname: 'jsearch.p.rapidapi.com',
        port: null,
        path: '/search?query=iOS%20developer%20jobs%20Remote&page=1&num_pages=1&country=us&date_posted=all&employment_types=CONTRACTOR',
        headers: {
            'x-rapidapi-key': Constants.expoConfig?.env?.RAPID_API_KEY,
            'x-rapidapi-host': 'jsearch.p.rapidapi.com'
        }
    };

}

