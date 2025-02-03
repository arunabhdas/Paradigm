import { React, useState } from 'react'
import { View, Text, TouchableOpacity, FlatList, ActivityIndicator } 
from 'react-native'
import { useRouter } from 'expo-router';


import styles from './popularproducts.style'

import { COLORS, SIZES } from '../../../constants';
import PopularProductCard from '../../common/cards/popular/PopularProductCard';
const supplierTypes = ["Onions", "Tomato", "Potatoes", "Chilli Peppers", "Green Peppers"]

const Popularproducts = () => {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const error = false;
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
          data={supplierTypes}
          renderItem={({ item }) => {
            <PopularProductCard item={item} />
          }}
          keyExtractor={(item) => item}
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