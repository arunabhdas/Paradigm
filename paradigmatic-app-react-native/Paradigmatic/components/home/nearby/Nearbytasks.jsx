import { React } from 'react'
import { View, Text, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useRouter } from 'expo-router';


import styles from './nearbytasks.style'

import { COLORS, SIZES } from '../../../constants';
import NearbyTaskCard from '../../common/cards/nearby/NearbyTaskCard';
import useFetch from '../../../hook/useFetch';

const supplierTypes = ["Ecommerce", "Gaming", "Travel", "Kids", "Education", "Fashion", "Sports", "Media", "Cars", "Pets", "Finance", "Fitness", "CPG"]

const Nearbytasks = () => {
  const router = useRouter();

  const { data, isLoading, error, refetch } = useFetch(
    'search', { 
      query: 'iOS developer jobs Remote',
      num_pages: '1',
    }
  )


  console.log(data);
  
  return (
    <View styles={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Shop Nearby</Text>
        <TouchableOpacity>
          <Text style={styles.headerBtn}>Show All</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.cardsContainer}>
        {isLoading ? (
          <ActivityIndicator size="large" colors={COLORS.primary} />
        ) : error ? (
          <Text>Something went wrong</Text>
        ) : (
          data?.map((item) => (
            <NearbyTaskCard 
              item={item}
              key={`nearby-job-${item?.job_id}`}
              handleNavigate={() => router.push(`/job-details/${item?.job_id}`)}
            />
          ))
        )} 
      </View>

    </View>
  )
}

export default Nearbytasks