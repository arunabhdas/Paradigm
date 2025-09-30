package app.paradigmatic.paradigmaticapp.presentation.data.model

import com.google.gson.annotations.SerializedName

data class FetchRoutesResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("notes")
    val notes: Any,
    @SerializedName("route_assigned_to_user")
    val routeAssignedToUser: CustomUser,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("stops")
    val stops: List<Stop>,
    @SerializedName("title")
    val title: String
)

data class CustomUser(
    @SerializedName("date_joined")
    val dateJoined: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("groups")
    val groups: List<Any>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_deactivated")
    val isDeactivated: Boolean,
    @SerializedName("is_staff")
    val isStaff: Boolean,
    @SerializedName("is_superuser")
    val isSuperuser: Boolean,
    @SerializedName("last_login")
    val lastLogin: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("user_permissions")
    val userPermissions: List<Any>,
    @SerializedName("username")
    val username: String
)

data class Stop(
    @SerializedName("title")
    val title: String,
    @SerializedName("contact_user")
    val contactUser: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("destination_address")
    val destinationAddress: String,
    var destinationAddressLat: Double? = null,
    var destinationAddressLng: Double? = null,
    @SerializedName("destination_address_full")
    val destinationAddressFull: String,
    @SerializedName("destination_notes")
    val destinationNotes: String,
    @SerializedName("ordering_number")
    val orderingNumber: Int,
    @SerializedName("destination_tracking_number")
    val destinationTrackingNumber: String,
    @SerializedName("destination_window_range_start")
    val destinationWindowRangeStart: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("load_number")
    val loadNumber: String,
    @SerializedName("manifest_number")
    val manifestNumber: String,
    @SerializedName("notes")
    val notes: String,
    @SerializedName("origin_address")
    val originAddress: String,
    var originAddressLat: Double? = null,
    var originAddressLng: Double? = null,
    @SerializedName("origin_address_full")
    val originAddressFull: String,
    @SerializedName("origin_notes")
    val originNotes: String,
    @SerializedName("origin_tracking_number")
    val originTrackingNumber: String,
    @SerializedName("origin_window_range_start")
    val originWindowRangeStart: String,
    @SerializedName("proof_of_delivery_picture_1")
    val proofOfDeliveryPicture1: String,
    @SerializedName("proof_of_pickup_picture_1")
    val proofOfPickupPicture1: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)