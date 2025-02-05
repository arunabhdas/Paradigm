import { React, useState } from 'react'
import { View, Text, TouchableOpacity, FlatList, ActivityIndicator } 
from 'react-native'
import { useRouter } from 'expo-router';


import styles from './popularproducts.style'

import { COLORS, SIZES } from '../../../constants';
import PopularProductCard from '../../common/cards/popular/PopularProductCard';
import useFetch from '../../../hook/useFetch';

const supplierTypes = ["Ecommerce", "Gaming", "Travel", "Kids", "Education", "Fashion", "Sports", "Media", "Cars", "Pets", "Finance", "Fitness", "CPG"]

const Popularproducts = () => {
  const router = useRouter();
  const [selectedJob, setSelectedJob] = useState();

  const { data, isLoading, error, refetch } = useFetch(
    'search', { 
      query: 'iOS developer jobs Remote',
      num_pages: '1',
    }
  )

  const handleCardPress = (item) => {
    router.push(`/job-details/${item.job_id}`);
    setSelectedJob(item.job_id);
  };

  console.log(data);
  
  return (
    <View styles={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Shop Local</Text>
        <TouchableOpacity>
          <Text style={styles.headerBtn}>Show All</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.cardsContainer}>
        {isLoading ? (
          <ActivityIndicator size="large" colors={COLORS.primary} />
        ) : (
          <FlatList 
          data={data}
          renderItem={({ item }) => (
            <PopularProductCard 
            item={item} 
            selectedJob={selectedJob}
            handleCardPress={handleCardPress}
            />
        )}
          keyExtractor={(item) => item?.job_id}
          contentContainerStyle={{ columnGap: SIZES.medium }}
          horizontal
          showsHorizontalScrollIndicator={false}
        /> 
        )} 
      </View>

    </View>
  )
}

export default Popularproducts