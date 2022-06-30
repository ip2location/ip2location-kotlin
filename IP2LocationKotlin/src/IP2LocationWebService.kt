import com.google.gson.*
import org.apache.commons.lang3.StringUtils
import java.net.URL
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * This class performs the lookup of IP2Location data from an IP address by querying the IP2Location Web Service.
 *
 *
 * Example usage scenarios:
 *
 *  * Redirect based on country
 *  * Digital rights management
 *  * Web log stats and analysis
 *  * Auto-selection of country on forms
 *  * Filter access from countries you do not do business with
 *  * Geo-targeting for increased sales and click-through
 *  * And much, much more!
 *
 * Copyright (c) 2002-2022 IP2Location.com
 *
 * @author IP2Location.com
 * @version 8.2.0
 */
class IP2LocationWebService {
    private var key = ""
    private var packageType = ""
    private var isSSL = true
    /**
     * This function initializes the params for the web service.
     */
    /**
     * This function initializes the params for the web service.
     */
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun open(aPIKey: String, packageName: String, useSSL: Boolean = true) {
        key = aPIKey
        packageType = packageName
        isSSL = useSSL
        checkParams()
    }

    /**
     * This function validates the API key and package params.
     */
    @Throws(IllegalArgumentException::class)
    private fun checkParams() {
        require(!(!pattern.matcher(key).matches() && key != "demo")) { "Invalid API key." }
        require(pattern2.matcher(packageType).matches()) { "Invalid package name." }
    }

    /**
     * This function to query IP2Location data.
     * @param ipAddress IP Address you wish to query
     * @return IP2Location data
     */
    @Throws(IllegalArgumentException::class, RuntimeException::class)
    fun ipQuery(ipAddress: String?): JsonObject {
        return ipQuery(ipAddress, arrayOf(), "en")
    }

    /**
     * This function to query IP2Location data.
     * @param ipAddress IP Address you wish to query
     * @param language Translation language
     * @return IP2Location data
     */
    @Throws(IllegalArgumentException::class, RuntimeException::class)
    fun ipQuery(ipAddress: String?, language: String?): JsonObject {
        return ipQuery(ipAddress, arrayOf(), language)
    }

    /**
     * This function to query IP2Location data.
     * @param ipAddress IP Address you wish to query
     * @param addOns List of addons
     * @param language Translation language
     * @return IP2Location data
     */
    @Throws(IllegalArgumentException::class, RuntimeException::class)
    fun ipQuery(
            ipAddress: String?,
            addOns: Array<String>,
            language: String?
    ): JsonObject {
        try {
            checkParams() // check here in case user haven't called Open yet
            val bf = StringBuffer()
            bf.append("http")
            if (isSSL) {
                bf.append("s")
            }
            bf.append("://api.ip2location.com/v2/?key=").append(key).append("&package=").append(packageType)
                    .append("&ip=").append(URLEncoder.encode(ipAddress, "UTF-8")).append("&lang=")
                    .append(URLEncoder.encode(language, "UTF-8"))
            if (addOns.isNotEmpty()) {
                bf.append("&addon=").append(URLEncoder.encode(StringUtils.join(addOns, ","), "UTF-8"))
            }
            val myUrl = bf.toString()
            val myJson = Http[URL(myUrl)]
            return JsonParser.parseString(myJson).asJsonObject
        } catch (ex: IllegalArgumentException) {
            throw ex
        } catch (ex2: Exception) {
            throw RuntimeException(ex2)
        }
    }

    /**
     * This function to check web service credit balance.
     * @return Credit balance
     */
    @Throws(IllegalArgumentException::class, RuntimeException::class)
    fun getCredit(): JsonObject {
        try {
            checkParams() // check here in case user haven't called Open yet
            val bf = StringBuffer()
            bf.append("http")
            if (isSSL) {
                bf.append("s")
            }
            bf.append("://api.ip2location.com/v2/?key=").append(key).append("&check=true")
            val myUrl = bf.toString()
            val myJson: String = Http[URL(myUrl)]
            return JsonParser.parseString(myJson).asJsonObject
        } catch (ex: IllegalArgumentException) {
            throw ex
        } catch (ex2: Exception) {
            throw RuntimeException(ex2)
        }
    }

    companion object {
        private val pattern = Pattern.compile("^[\\dA-Z]{10}$")
        private val pattern2 = Pattern.compile("^WS\\d+$")
    }
}