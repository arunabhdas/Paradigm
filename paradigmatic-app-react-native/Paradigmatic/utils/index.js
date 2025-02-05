export const checkImageURL = (url) => {
    if (!url) return false;
    
    // Debug log
    console.log('Checking URL:', url);
    
    try {
        // Allow any URL that starts with http or https
        if (url.startsWith('http://') || url.startsWith('https://')) {
            console.log('Valid http(s) URL');
            return true;
        }
        
        // Check if it's a data URL for images
        if (url.startsWith('data:image/')) {
            console.log('Valid data URL');
            return true;
        }

        console.log('URL validation failed');
        return false;
    } catch (e) {
        console.log('Error checking URL:', e.message);
        return false;
    }
};