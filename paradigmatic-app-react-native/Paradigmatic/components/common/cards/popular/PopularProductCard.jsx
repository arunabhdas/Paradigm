import React from 'react'
import { View, Text, TouchableOpacity, Image } from 'react-native'

import styles from './popularproductcard.style';

import { checkImageURL } from '../../../../utils';

const PopularProductCard = ({ item, selectedJob, handleCardPress }) => {
  return (
    <TouchableOpacity
    style={styles.container(selectedJob, item)}
    onPress={() => handleCardPress(item)}
    >
      <TouchableOpacity style={styles.logoContainer(selectedJob, item)}>
        <Image
          source={{uri: checkImageURL(item.employer_logo) ? item.employer_logo : "https://t4.ftcdn.net/jpg/05/05/08/61/360_F_505086119_1Xy7u1D0gq6pFqzDpQ9uZ8yYwOwL1yjk.jpg"}}
          resizeMode="contain"
          style={styles.logoImage}
        />
      </TouchableOpacity>
      <Text style={styles.companyName} numberOfLines={1}>{item.employer_name}</Text>
      <View style={styles.infoContainer}>
        <Text style={styles.jobName} numberOfLines={1}>
          {item.job_title}
        </Text>
        <Text style={styles.location}>{item.job_country}</Text>
      </View>
    </TouchableOpacity>
  )
}

export default PopularProductCard