package app.paradigmatic.paradigmaticapp.ui.chargingstations.model


import com.google.gson.annotations.SerializedName

data class StationsResponse(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val next_page_token: String,
    @SerializedName("results")
    val chargingStations: List<ChargingStation>,
    @SerializedName("status")
    val status: String

)

data class Geometry(
    @SerializedName("location")
    val location: Location,
    @SerializedName("viewport")
    val viewport: Viewport
)

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Northeast(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean
)

data class PlusCode(
    @SerializedName("compound_code")
    val compoundCode: String,
    @SerializedName("global_code")
    val globalCode: String
)

data class ChargingStation(
    @SerializedName("business_status")
    val businessStatus: String,
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String,
    @SerializedName("icon_mask_base_uri")
    val iconMaskBaseUri: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCode,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("types")
    val types: List<String>,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    @SerializedName("vicinity")
    val vicinity: String
)

data class Southwest(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Viewport(
    @SerializedName("northeast")
    val northeast: Northeast,
    @SerializedName("southwest")
    val southwest: Southwest
)