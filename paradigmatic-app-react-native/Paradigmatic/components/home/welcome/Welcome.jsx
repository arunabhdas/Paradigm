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
import { icons, SIZES } from '../../../constants'

const supplierTypes = ["Onshore", "Nearshore", "Offshore"]

const Welcome = () => {
  const router = useRouter();
  const [activeSupplierType, setActiveSupplierType] = useState("Onshore");
  return (
  <View>
    <View style={styles.container}>
      <Text style={styles.userName}>Paradigmatic</Text>
      <Text style={styles.welcomeMessage}>Find alternative supplier and markets</Text>
    </View>

    <View style={styles.searchContainer}>
      <View style={styles.searchWrapper}>
        <TextInput
          style={styles.searchInput}
          value=""
          onChange={() => {}}
          placeholder="What are you looking for?"
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

    <View style={styles.tabsContainer}>
      <FlatList data={supplierTypes} renderItem={({ item }) => ( 
        <TouchableOpacity style={styles.tab(activeSupplierType, item)} onPress={() => {}}>
        <Text>{item}</Text> 
        </TouchableOpacity>
        )} 
      />
    </View>
    
  </View>
  )
}

export default Welcome