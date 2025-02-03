import React from 'react'
import { View, Text, TouchableOpacity, FlatList, ActivityIndicator } 
from 'react-native'
import { useRouter } from 'expo-router';


import styles from './popularproducts.style'

import { COLORS, SIZES } from '../../../constants';
import { PopularProductCard } from '../../common/cards/popular/PopularProductCard';

const Popularproducts = () => {
  return (
    <View>
      <Text>Welcome to Popularproducts</Text>
    </View>
  )
}

export default Popularproducts