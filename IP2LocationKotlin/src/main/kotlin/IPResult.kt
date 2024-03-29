/**
 * This class is used to store the geolocation data that is returned by the IP2Location class.
 *
 * Copyright (c) 2002-2023 IP2Location.com
 *
 * @author IP2Location.com
 * @version 8.4.1
 */
class IPResult internal constructor(private var IPAddress: String) {
    var ipAddress: String? = IPAddress
    var countryShort: String? = null
    var countryLong: String? = null
    var region: String? = null
    var city: String? = null
    var iSP: String? = null
    var latitude = 0f
    var longitude = 0f
    var domain: String? = null
    var zIPCode: String? = null
    var netSpeed: String? = null
    var timeZone: String? = null
    var iDDCode: String? = null
    var areaCode: String? = null
    var weatherStationCode: String? = null
    var weatherStationName: String? = null
    var mCC: String? = null
    var mNC: String? = null
    var mobileBrand: String? = null
    var elevation = 0f
    var usageType: String? = null
    var addressType: String? = null
    var category: String? = null
    var district: String? = null
    var aSN: String? = null
    var aS: String? = null
    var status: String? = null
    var version = "Version 8.4.1"

    /**
     * This method to return all the fields.
     * @return the result in a formatted string.
     */
    override fun toString(): String {
        val nL = System.getProperty("line.separator")
        val buf = StringBuffer("IP2LocationRecord:$nL")
        buf.append("\tIP Address = $ipAddress$nL")
        buf.append("\tCountry Short = " + countryShort + nL)
        buf.append("\tCountry Long = " + countryLong + nL)
        buf.append("\tRegion = $region$nL")
        buf.append("\tCity = $city$nL")
        buf.append("\tISP = " + iSP + nL)
        buf.append("\tLatitude = $latitude$nL")
        buf.append("\tLongitude = $longitude$nL")
        buf.append("\tDomain = $domain$nL")
        buf.append("\tZipCode = " + zIPCode + nL)
        buf.append("\tTimeZone = " + timeZone + nL)
        buf.append("\tNetSpeed = " + netSpeed + nL)
        buf.append("\tIDDCode = " + iDDCode + nL)
        buf.append("\tAreaCode = " + areaCode + nL)
        buf.append("\tWeatherStationCode = " + weatherStationCode + nL)
        buf.append("\tWeatherStationName = " + weatherStationName + nL)
        buf.append("\tMCC = " + mCC + nL)
        buf.append("\tMNC = " + mNC + nL)
        buf.append("\tMobileBrand = " + mobileBrand + nL)
        buf.append("\tElevation = $elevation$nL")
        buf.append("\tUsageType = " + usageType + nL)
        buf.append("\tAddressType = " + addressType + nL)
        buf.append("\tCategory = " + category + nL)
        buf.append("\tDistrict = " + district + nL)
        buf.append("\tASN = " + aSN + nL)
        buf.append("\tAS = " + aS + nL)
        return buf.toString()
    }

    companion object {
        const val NOT_SUPPORTED = "Not_Supported"
    }

}