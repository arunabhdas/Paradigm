import React from 'react'
import { View, Text, TouchableOpacity, Image } from 'react-native'

import styles from './nearbytaskcard.style';

import { checkImageURL } from '../../../../utils';

const NearbyTaskCard = ({ item, handleNavigate }) => {
  return (
    <TouchableOpacity
    style={styles.container}
    onPress={() => handleNavigate}

    >
      <TouchableOpacity style={styles.logoContainer}>
        <Image
          source={{uri: checkImageURL(item.employer_logo) ? item.employer_logo : "https://t4.ftcdn.net/jpg/05/05/08/61/360_F_505086119_1Xy7u1D0gq6pFqzDpQ9uZ8yYwOwL1yjk.jpg"}}
          resizeMode="contain"
          style={styles.logoImage}
        />
      </TouchableOpacity>
      <Text style={styles.companyName} numberOfLines={1}>{item.employer_name}</Text>
      <View style={styles.infoContainer}>
        <Text style={styles.jobName} numberOfLines={1}>
          {item.jobName}
        </Text>
        <Text style={styles.jobType}>{item.job_employment_type}</Text>
      </View>
    </TouchableOpacity>
  )
}

export default NearbyTaskCard