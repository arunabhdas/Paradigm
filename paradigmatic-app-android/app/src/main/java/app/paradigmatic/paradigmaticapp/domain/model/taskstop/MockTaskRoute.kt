package app.paradigmatic.paradigmaticapp.domain.model.taskstop

import app.paradigmatic.paradigmaticapp.ui.routetaskmanager.data.Address


data class MockTaskRoute (
    val id: Int,
    val title: String,
    val description: String? = null,
    val originRangeStartTime: String,
    val originRangeEndTime: String,
    val destinationRangeStartTime: String,
    val destinationRangeEndTime: String,
    val originAddress: Address,
    val destinationAddress: Address

)

val MockTaskRouteList = listOf(

    MockTaskRoute(
        1,
        "Loadout from RONA Hamilton (Rymal Road)",
        "1245 Rymal Rd E, Hamilton, ON L8W 3N1",
        " 12:30\nPM",
        "17:30",
        "12:30 PM",
        "3:30 PM",
        Address(
            "250 Front St. West",
            "7th floor",
            "7",
                "Toronto",
            "ON",
            "Canada",
            "M5H 3V5",
            0.0,
            0.0,
            "None"
            ),
        Address(
            "250 Front St. West",
            "7th floor",
            "7",
            "Toronto",
            "ON",
            "Canada",
            "M5H 3V5",
            0.0,
            0.0,
            "None"
        ),
    ),
    MockTaskRoute(
        2,
        "Delivery to Moxies Restaurant, Scarborough",
        "1220 Markham Rd, Scarborough, ON M1H 3B4",
        "1:30\nPM",
        "2024, 02, 01, 10, 55",
        "1:30 PM",
        "4:30 PM",
        Address(
            "Random Street",
            "Unit",
            "Number",
            "City",
            "Province",
            "Country",
            "PostalCode",
            0.0,
            0.0,
            "Info"
        ),
        Address(
            "Random Street",
            "Unit",
            "Number",
            "City",
            "Province",
            "Country",
            "PostalCode",
            0.0,
            0.0,
            "Info"
        )
    ),


    MockTaskRoute(
        2,
        "Delivery to Flying Saucer, Niagara Falls",
        "6768 Lundy's Ln, Niagara Falls, ON L2G 1V5",
        "2:30\nPM",
        "2024, 02, 01, 10, 55",
        "2:30 PM",
        "5:30 PM",
        Address(
            "Random Street",
            "Unit",
            "Number",
            "City",
            "Province",
            "Country",
            "PostalCode",
            0.0,
            0.0,
            "Info"
        ),
        Address(
            "Random Street",
            "Unit",
            "Number",
            "City",
            "Province",
            "Country",
            "PostalCode",
            0.0,
            0.0,
            "Info"
        )
    ),

    MockTaskRoute(
        2,
        "Delivery to The Olive Press, Oakville",
        "2322 Dundas St W, Oakville, ON L6M 4J3",
        "3:30\nPM",
        "2024, 02, 01, 10, 55",
        "3:30 PM",
        "5:30 PM",
        Address(
            "Random Street",
            "Unit",
            "Number",
            "City",
            "Province",
            "Country",
            "PostalCode",
            0.0,
            0.0,
            "Info"
        ),
        Address(
            "Random Street",
            "Unit",
            "Number",
            "City",
            "Province",
            "Country",
            "PostalCode",
            0.0,
            0.0,
            "Info"
        )
    ),

)