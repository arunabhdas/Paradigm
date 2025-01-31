package app.paradigmatic.paradigmaticapp.data.blescanner

object Xl8rProBeaconRepository {
    val xlr8ProBeacons = listOf(
        Xlr8ProBeacon(
            1,
            "Test1",
            "UUID1",
            "12345"
        ),
        Xlr8ProBeacon(
            2,
            "Test2",
            "UUID2",
            "12345"
        ),
        Xlr8ProBeacon(
            3,
            "Test3",
            "UUID3",
            "12345"
        ),
        Xlr8ProBeacon(
            4,
            "Test4",
            "UUID4",
            "12345"
        )

    )

    fun getXlr8ProBeacons(id: Int) = xlr8ProBeacons.firstOrNull { it.id == id }
}
