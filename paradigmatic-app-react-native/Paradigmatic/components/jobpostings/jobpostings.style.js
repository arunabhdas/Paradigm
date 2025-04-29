import { StyleSheet } from 'react-native';
import { COLORS, FONT, SIZES } from '../../constants';


const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f5f5f5',
    },
    scrollContainer: {
        paddingBottom: 20,
    },
    screenHeader: {
        fontSize: 24,
        fontWeight: 'bold',
        padding: 16,
        backgroundColor: '#fff',
        borderBottomWidth: 1,
        borderBottomColor: '#e0e0e0',
    },
    teamSection: {
        marginBottom: 16,
        marginTop: 8,
    },
    teamHeader: {
        fontSize: 18,
        fontWeight: 'bold',
        padding: 12,
        backgroundColor: '#e0e0e0',
        borderRadius: 4,
        marginHorizontal: 12,
    },
    jobCard: {
        backgroundColor: '#fff',
        padding: 16,
        marginHorizontal: 12,
        marginTop: 8,
        borderRadius: 8,
        borderLeftWidth: 4,
        borderLeftColor: '#4a90e2',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.2,
        shadowRadius: 2,
        elevation: 2,
    },
    jobTitle: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 4,
    },
    jobLocation: {
        fontSize: 14,
        color: '#666',
    },
});
