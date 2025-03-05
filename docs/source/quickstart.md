# Quickstart

## Dependencies

This library requires IP2Location BIN database to function. You may
download the BIN database at

-   IP2Location LITE BIN Data (Free): <https://lite.ip2location.com>
-   IP2Location Commercial BIN Data (Comprehensive):
    <https://www.ip2location.com>

## Requirements ##
Intellij IDEA: <https://www.jetbrains.com/idea/>

## Sample Codes

### Query geolocation information from BIN database

You can query the geolocation information from the IP2Location BIN database as below:

```kotlin
import kotlin.jvm.JvmStatic
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val strIPAddress = "8.8.8.8"

            // querying with the BIN file
            val dbPath = "/usr/data/IP-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ZIPCODE-TIMEZONE-ISP-DOMAIN-NETSPEED-AREACODE-WEATHER-MOBILE-ELEVATION-USAGETYPE-ADDRESSTYPE-CATEGORY-DISTRICT-ASN.BIN"

            var loc = IP2Location()

            // this is to initialize with a BIN file
            val useMMF = true
            loc.open(dbPath, useMMF)

            // this is to initialize with byte array
            // val binPath: Path = Paths.get(dbPath)
            // val binFileBytes = Files.readAllBytes(binPath)
            // loc.open(binFileBytes)

            var rec = loc.ipQuery(strIPAddress)

            when (rec.status) {
                "OK" -> println(rec)
                "EMPTY_IP_ADDRESS" -> println("IP address cannot be blank.")
                "INVALID_IP_ADDRESS" -> println("Invalid IP address.")
                "MISSING_FILE" -> println("Invalid database path.")
                "IPV6_NOT_SUPPORTED" -> println("This BIN does not contain IPv6 data.")
                else -> println("Unknown error." + rec.status)
            }
            loc.close()

        } catch (e: Exception) {
            println(e)
            e.printStackTrace(System.out)
        }
    }
}
```

### Processing IP address using IP Tools class

You can manupulate IP address, IP number and CIDR as below:

```kotlin
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val tools = IPTools()

            println(tools.isIPV4("60.54.166.38"))
            println(tools.isIPV6("2600:1f18:45b0:5b00:f5d8:4183:7710:ceec"))
            println(tools.ipV4ToDecimal("60.54.166.38"))
            println(tools.ipV6ToDecimal("2600:118:450:5b00:f5d8:4183:7710:ceec"))
            println(tools.decimalToIPV4(BigInteger("1010214438")))
            println(tools.decimalToIPV4(BigInteger("770")))
            println(tools.decimalToIPV6(BigInteger("50510686025047391022278667396705210092")))
            println(tools.decimalToIPV6(BigInteger("977677717377287979008")))
            println(tools.ipV4ToDecimal("0.0.166.38"))

            println(tools.compressIPV6("0000:0000:0000:0035:0000:FFFF:0000:0000"))
            println(tools.compressIPV6("::0035:0000:FFFF:0000:0000"))
            println(tools.compressIPV6("0120:F000:0000:0035:0000:FFFF:0000:0000"))
            println(tools.compressIPV6("0120:F000:0002:0035:0090:FFFF:0000:0000"))
            println(tools.compressIPV6("::"))
            println(tools.compressIPV6("233::"))
            println(tools.compressIPV6("::233:0:0"))
            println(tools.compressIPV6("00FF:0E00:2000:0035:0009:FFFF:10:0000"))
            println(tools.expandIPV6(tools.compressIPV6("0500:6001:00FE:35:0000:FFFF:0000:0000")))
            val stuff = tools.ipV4ToCIDR("10.0.0.0", "10.10.2.255")
            stuff?.forEach(System.out::println)

            val stuff2 =
                tools.ipV6ToCIDR("2001:4860:4860:0000:0000:0000:0000:8888", "2001:4860:4860:0000:eeee:ffff:ffff:ffff")
            stuff2?.forEach(System.out::println)

            val stuff3 = tools.cIDRToIPV4("10.123.80.0/12")
            stuff3?.forEach(System.out::println)
            val stuff4 = tools.cIDRToIPV6("2002:1234::abcd:ffff:c0a8:101/62")
            stuff4?.forEach(System.out::println)
        } catch (e: Exception) {
            println(e)
            //e.printStackTrace(System.out)
            throw e
        }
    }
}
```

### List down country information

You can query country information for a country from IP2Location Country Information CSV file as below:

```kotlin
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val cc = Country("./IP2LOCATION-COUNTRY-INFORMATION.CSV")

            val ccResult: Map<String, String?>? = cc.getCountryInfo("US")
            println(ccResult.toString())

            val ccResults: List<Map<String, String?>> = cc.getCountryInfo()
            println(ccResults.toString())
        } catch (e: Exception) {
            println(e)
            //e.printStackTrace(System.out)
            throw e
        }
    }
}
```

### List down region information

You can get the region code by country code and region name from IP2Location ISO 3166-2 Subdivision Code CSV file as below:

```kotlin
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val reg = Region("./IP2LOCATION-ISO3166-2.CSV")

            val regionCode: String? = reg.getRegionCode("US", "California")

            println(regionCode)
        } catch (e: Exception) {
            println(e)
            //e.printStackTrace(System.out)
            throw e
        }
    }
}
```

