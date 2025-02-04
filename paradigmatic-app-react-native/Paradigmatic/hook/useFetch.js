import { useState, useEffect } from 'react';
import axios from 'axios';
import Constants from 'expo-constants';

const useFetch = (endpoint, query) => {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const options = {
        method: 'GET',
        url: `https://jsearch.p.rapidapi.com/${endpoint}`,
        headers: {
            'X-RapidAPI-Key': Constants.expoConfig.extra.rapidApiKey,
            'X-RapidAPI-Host': 'jsearch.p.rapidapi.com'
        },
        /* TODO-FIXME
        params: {
            query: 'iOS developer jobs Remote',
            page: '1',
            num_pages: '1',
            country: 'us',
            date_posted: 'all',
            employment_types: 'CONTRACTOR'
        },
        */
        params: { ...query },
    };

    const fetchData = async () => {
        setIsLoading(true);

        try {
            const response = await axios.request(options);
            setData(response.data.data);
            setIsLoading(false);
            
        } catch (error) {
            alert('There was an error')
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        fetchData();
    }, []);
    
    const refetch = () => {
        setIsLoading(true)
        fetchData();
    }

    return { data, isLoading, error, refetch };

};

export default useFetch;
