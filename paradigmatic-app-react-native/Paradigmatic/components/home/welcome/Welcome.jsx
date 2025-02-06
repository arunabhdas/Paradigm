import { useState } from 'react'
import React from 'react'
import { 
  View, 
  Text,
  TextInput,
  TouchableOpacity,
  Image,
  FlatList 
} from 'react-native'

import { useRouter } from 'expo-router';

import styles from './welcome.style'
import { icons, SIZES, COLORS } from '../../../constants'

const supplierTypes = ["Ecommerce", "Gaming", "Travel", "Kids", "Education", "Fashion", "Sports", "Media", "Cars", "Pets", "Finance", "Fitness", "CPG"]

const Welcome = () => {
  const router = useRouter();
  const [activeSupplierType, setActiveSupplierType] = useState("Onshore");
  return (
  <View>
    <View style={styles.container}>
      <Text style={styles.userName}>Paradigmatic helps consumers and business find alternate suppliers for their products</Text>
      <Text style={styles.welcomeMessage}>Find alternative supplier and markets</Text>
    </View>

    <View style={styles.searchContainer}>
      <View style={styles.searchWrapper}>
        <TextInput
          style={styles.searchInput}
          value=""
          onChange={() => {}}
          placeholder="What are you looking for?"
          placeholderTextColor={COLORS.gray}
        />
      </View>

      <TouchableOpacity style={styles.searchBtn} onPress={() => {}}>
        <Image 
          source={icons.search}
          resizeMode="contain"
          style={styles.searchBtnImage}
        />
      </TouchableOpacity>
    </View>

    <Text style={styles.welcomeMessage}>Sectors</Text>
    <View style={styles.tabsContainer}>
      <FlatList 
        data={supplierTypes} 
        renderItem={({ item }) => ( 
        <TouchableOpacity 
          style={styles.tab(activeSupplierType, item)} 
          onPress={() => {
          setActiveSupplierType(item)
          router.push(`/search/${item}`)
          }}>
        <Text 
          style={styles.tabText(activeSupplierType, item)}>{item}
        </Text> 
        </TouchableOpacity>
        )} 
        keyExtractor={(item) => item}
        contentContainerStyle={{ columnGap: SIZES.small }}
        horizontal
        showsHorizontalScrollIndicator={false}
      />
    </View>
    
  </View>
  )
}

export default Welcome