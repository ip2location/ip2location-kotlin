import kotlin.jvm.JvmStatic
import com.google.gson.JsonObject
import java.lang.Exception

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val strIPAddress = "8.8.8.8"

            // querying with the BIN file
            val dbPath = "C:/mydata/IPV6-COUNTRY-REGION-CITY-LATITUDE-LONGITUDE-ZIPCODE-TIMEZONE-ISP-DOMAIN-NETSPEED-AREACODE-WEATHER-MOBILE-ELEVATION-USAGETYPE.BIN"
            val useMMF = true

            var loc = IP2Location()
            loc.open(dbPath, useMMF)

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

            // querying with the web service
            val ws = IP2LocationWebService()
            val strAPIKey = "XXXXXXXXXX" // replace this with your IP2Location Web Service API key
            val strPackage = "WS24"
            val addOn = arrayOf("continent", "country", "region", "city", "geotargeting", "country_groupings", "time_zone_info")
            val strLang = "es"
            val boolSSL = true
            ws.open(strAPIKey, strPackage, boolSSL)
            var myResult: JsonObject = ws.ipQuery(strIPAddress, addOn, strLang)
            if (myResult["response"] == null) {
                // standard results
                println("country_code: " + if (myResult["country_code"] != null) myResult["country_code"].asString else "")
                println("country_name: " + if (myResult["country_name"] != null) myResult["country_name"].asString else "")
                println("region_name: " + if (myResult["region_name"] != null) myResult["region_name"].asString else "")
                println("city_name: " + if (myResult["city_name"] != null) myResult["city_name"].asString else "")
                println("latitude: " + if (myResult["latitude"] != null) myResult["latitude"].asString else "")
                println("longitude: " + if (myResult["longitude"] != null) myResult["longitude"].asString else "")
                println("zip_code: " + if (myResult["zip_code"] != null) myResult["zip_code"].asString else "")
                println("time_zone: " + if (myResult["time_zone"] != null) myResult["time_zone"].asString else "")
                println("isp: " + if (myResult["isp"] != null) myResult["isp"].asString else "")
                println("domain: " + if (myResult["domain"] != null) myResult["domain"].asString else "")
                println("net_speed: " + if (myResult["net_speed"] != null) myResult["net_speed"].asString else "")
                println("idd_code: " + if (myResult["idd_code"] != null) myResult["idd_code"].asString else "")
                println("area_code: " + if (myResult["area_code"] != null) myResult["area_code"].asString else "")
                println("weather_station_code: " + if (myResult["weather_station_code"] != null) myResult["weather_station_code"].asString else "")
                println("weather_station_name: " + if (myResult["weather_station_name"] != null) myResult["weather_station_name"].asString else "")
                println("mcc: " + if (myResult["mcc"] != null) myResult["mcc"].asString else "")
                println("mnc: " + if (myResult["mnc"] != null) myResult["mnc"].asString else "")
                println("mobile_brand: " + if (myResult["mobile_brand"] != null) myResult["mobile_brand"].asString else "")
                println("elevation: " + if (myResult["elevation"] != null) myResult["elevation"].asString else "")
                println("usage_type: " + if (myResult["usage_type"] != null) myResult["usage_type"].asString else "")
                println("credits_consumed: " + if (myResult["credits_consumed"] != null) myResult["credits_consumed"].asString else "")

                // continent addon
                if (myResult["continent"] != null) {
                    val continentObj = myResult.getAsJsonObject("continent")
                    println("continent => name: " + continentObj["name"].asString)
                    println("continent => code: " + continentObj["code"].asString)
                    val myArr = continentObj.getAsJsonArray("hemisphere")
                    println("continent => hemisphere: $myArr")
                    println("continent => translations: " + continentObj.getAsJsonObject("translations")[strLang].asString)
                }

                // country addon
                if (myResult["country"] != null) {
                    val countryObj = myResult.getAsJsonObject("country")
                    println("country => name: " + countryObj["name"].asString)
                    println("country => alpha3_code: " + countryObj["alpha3_code"].asString)
                    println("country => numeric_code: " + countryObj["numeric_code"].asString)
                    println("country => demonym: " + countryObj["demonym"].asString)
                    println("country => flag: " + countryObj["flag"].asString)
                    println("country => capital: " + countryObj["capital"].asString)
                    println("country => total_area: " + countryObj["total_area"].asString)
                    println("country => population: " + countryObj["population"].asString)
                    println("country => idd_code: " + countryObj["idd_code"].asString)
                    println("country => tld: " + countryObj["tld"].asString)
                    println("country => translations: " + countryObj.getAsJsonObject("translations")[strLang].asString)
                    val currencyObj = countryObj.getAsJsonObject("currency")
                    println("country => currency => code: " + currencyObj["code"].asString)
                    println("country => currency => name: " + currencyObj["name"].asString)
                    println("country => currency => symbol: " + currencyObj["symbol"].asString)
                    val languageObj = countryObj.getAsJsonObject("language")
                    println("country => language => code: " + languageObj["code"].asString)
                    println("country => language => name: " + languageObj["name"].asString)
                }

                // region addon
                if (myResult["region"] != null) {
                    val regionObj = myResult.getAsJsonObject("region")
                    println("region => name: " + regionObj["name"].asString)
                    println("region => code: " + regionObj["code"].asString)
                    println("region => translations: " + regionObj.getAsJsonObject("translations")[strLang].asString)
                }

                // city addon
                if (myResult["city"] != null) {
                    val cityObj = myResult.getAsJsonObject("city")
                    println("city => name: " + cityObj["name"].asString)
                    println("city => translations: " + cityObj.getAsJsonArray("translations").toString())
                }

                // geotargeting addon
                if (myResult["geotargeting"] != null) {
                    val geoObj = myResult.getAsJsonObject("geotargeting")
                    println("geotargeting => metro: " + geoObj["metro"].asString)
                }

                // country_groupings addon
                if (myResult["country_groupings"] != null) {
                    val myArr = myResult.getAsJsonArray("country_groupings")
                    if (myArr.size() > 0) {
                        for (x in 0 until myArr.size()) {
                            println("country_groupings => #" + x + " => acronym: " + myArr[x].asJsonObject["acronym"].asString)
                            println("country_groupings => #" + x + " => name: " + myArr[x].asJsonObject["name"].asString)
                        }
                    }
                }

                // time_zone_info addon
                if (myResult["time_zone_info"] != null) {
                    val tzObj = myResult.getAsJsonObject("time_zone_info")
                    println("time_zone_info => olson: " + tzObj["olson"].asString)
                    println("time_zone_info => current_time: " + tzObj["current_time"].asString)
                    println("time_zone_info => gmt_offset: " + tzObj["gmt_offset"].asString)
                    println("time_zone_info => is_dst: " + tzObj["is_dst"].asString)
                    println("time_zone_info => sunrise: " + tzObj["sunrise"].asString)
                    println("time_zone_info => sunset: " + tzObj["sunset"].asString)
                }
            } else {
                println("Error: " + myResult["response"].asString)
            }
            myResult = ws.getCredit()
            if (myResult["response"] != null) {
                println("Credit balance: " + myResult["response"].asString)
            }
        } catch (e: Exception) {
            println(e)
            e.printStackTrace(System.out)
        }
    }
}