package com.example.wassalniDR.data

data class TripDetails(
    val _id: String,
    val start: String,
    val destination: String,
    val startTime: Long,
    val endTime: Long,
    val price: Double,
    val availableSeats: Int = 14,
    val driverId: String = "kfhga45y6u7t",
    val driverName: String = "ahmed Nabil",
    val stations: List<Station>,
    var passengers: List<Passenger>
)

data class Station(
    val _id: String,
    var isArrived: Boolean,
    val location: Location,
    val name: String,
    val time: Long,
    val rideStation: Boolean = true
)

data class Location(
    val lat: Double,
    val lng: Double
)

var stations = listOf(
    Station(
        _id = "1", isArrived = true, location = Location(29.343262, 31.203258),
        name = "الواسطي", time = 1679238000
    ),
    Station(
        _id = "1", isArrived = false, location = Location(29.327294, 31.197011),
        name = "الشادر", time = 1679238120
    ),
    Station(
        _id = "1", isArrived = false, location = Location(29.302335, 31.186068),
        name = "قمن العروس", time = 1679238300
    ),
    Station(
        _id = "1", isArrived = false, location = Location(29.282451, 31.189469),
        name = "كوم ادريجه", time = 1679238600
    ),
    Station(
        _id = "1", isArrived = false, location = Location(29.256526, 31.193339),
        name = "بني حدير", time = 1679238840
    ), Station(
        _id = "1", isArrived = false, location = Location(29.238141, 31.191408),
        name = "الميمون", time = 1679239200
    ), Station(
        _id = "1", isArrived = false, location = Location(29.220333, 31.181162),
        name = "اشمنت", time = 1679239380
    ), Station(
        _id = "1", isArrived = false, location = Location(29.145256, 31.137614),
        name = "مركز ناصر", time = 1679239800
    ), Station(
        _id = "1", isArrived = false, location = Location(29.049163, 31.118871),
        name = "كلية النهضة", time = 1679241600, rideStation = false
    ), Station(
        _id = "1", isArrived = false, location = Location(29.039685, 31.132564),
        name = "كلية الهندسة", time = 1679241780, rideStation = false
    ), Station(
        _id = "1", isArrived = false, location = Location(29.038052, 31.122425),
        name = "كلية التربية", time = 1679241900, rideStation = false
    ), Station(
        _id = "1", isArrived = false, location = Location(29.043484, 31.109482),
        name = "تعليم صناعي", time = 1679242020, rideStation = false
    )
)

var passengers = listOf(
    Passenger(
        id = "12341",
        name = "abdo mostafa",
        point = 1,
        numOfSeat = 1,
        hasPaid = false,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12342",
        name = "ahmed nabil",
        point = 0,
        numOfSeat = 2,
        hasPaid = true,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12343",
        name = "Hossam",
        point = 0,
        numOfSeat = 1,
        hasPaid = false,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12344",
        name = "eslam abbas",
        point = 0,
        numOfSeat = 2,
        hasPaid = true,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12345",
        name = "mostafa zarea",
        point = 2,
        numOfSeat = 1,
        hasPaid = false,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12346",
        name = "mostafa nayef",
        point = 2,
        numOfSeat = 1,
        hasPaid = false,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12347",
        name = "mokhtar",
        point = 6,
        numOfSeat = 1,
        hasPaid = true,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12348",
        name = "dr.abdelrahman mamdouh",
        point = 8,
        numOfSeat = 1,
        hasPaid = false,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "12349",
        name = "omar shaban",
        point = 0,
        numOfSeat = 1,
        hasPaid = true,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "123410",
        name = "mahmoud ramadan",
        point = 6,
        numOfSeat = 1,
        hasPaid = false,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "123411",
        name = "zizo farghly",
        point = 2,
        numOfSeat = 1,
        hasPaid = true,
        hasCome = 0,
        hasArrived = false
    ),
    Passenger(
        id = "123412",
        name = "mohamed badway",
        point = 1,
        numOfSeat = 1,
        hasPaid = true,
        hasCome = 0,
        hasArrived = false
    )

)
val fullTrip = TripDetails(
    _id = "12345", start = "الواسطي", destination = "تعليم صناعي",
    startTime = 1678647025, endTime = 1678653720, driverId = "63ebe5b95d2b62799228d412",
    price = 5.5, availableSeats = 14,  stations = stations,
    passengers = passengers

)
