import org.apache.commons.lang3.StringUtils
import java.math.BigInteger
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

/**
 * This class contains IP address related tools.
 *
 *
 * Example usage scenarios:
 *
 *  * Checks if the IP address is an IPv4
 *  * Checks if the IP address is an IPv6
 *  * Converts IPv4 to decimal
 *  * Converts IPv6 to decimal
 *  * Converts decimal to IPv4
 *  * Converts decimal to IPv6
 *  * Returns compressed form of IPv6
 *  * Returns expanded form of IPv6
 *  * Converts IPv4 range to CIDR
 *  * Converts IPv6 range to CIDR
 *  * Converts CIDR to IPv4 range
 *  * Converts CIDR to IPv6 range
 *
 * Copyright (c) 2002-2025 IP2Location.com
 *
 * @author IP2Location.com
 * @version 8.5.0
 */
class IPTools {
    /**
     * This function checks if the string contains an IPv4 address.
     *
     * @param IPAddress IP Address to check
     * @return Boolean
     */
    fun isIPV4(IPAddress: String?): Boolean {
        val result: Boolean = try {
            val ia = InetAddress.getByName(IPAddress)
            ia is Inet4Address
        } catch (ex: UnknownHostException) {
            false
        }
        return result
    }

    /**
     * This function checks if the string contains an IPv6 address.
     *
     * @param IPAddress IP Address to check
     * @return Boolean
     */
    fun isIPV6(IPAddress: String?): Boolean {
        val result: Boolean = try {
            val ia = InetAddress.getByName(IPAddress)
            ia is Inet6Address
        } catch (ex: UnknownHostException) {
            false
        }
        return result
    }

    /**
     * This function converts IPv4 to IP number.
     *
     * @param IPAddress IP Address you wish to convert
     * @return BigInteger
     */
    fun ipV4ToDecimal(IPAddress: String?): BigInteger? {
        if (!isIPV4(IPAddress)) {
            return null
        }
        val result: BigInteger? = try {
            val ia = InetAddress.getByName(IPAddress)
            val byteArr = ia.address
            BigInteger(1, byteArr)
        } catch (ex: UnknownHostException) {
            null
        }
        return result
    }

    /**
     * This function converts IPv6 to IP number.
     *
     * @param IPAddress IP Address you wish to convert
     * @return BigInteger
     */
    fun ipV6ToDecimal(IPAddress: String?): BigInteger? {
        if (!isIPV6(IPAddress)) {
            return null
        }
        val result: BigInteger? = try {
            val ia = InetAddress.getByName(IPAddress)
            val byteArr = ia.address
            BigInteger(1, byteArr)
        } catch (ex: UnknownHostException) {
            null
        }
        return result
    }

    /**
     * This function converts IP number to IPv4.
     *
     * @param IPNum IP number you wish to convert
     * @return String
     * @throws UnknownHostException If unable to convert byte array to IP address
     */
    @Throws(UnknownHostException::class)
    fun decimalToIPV4(IPNum: BigInteger): String? {
        if (IPNum < BigInteger.ZERO || IPNum > MAX_IPV4_RANGE) {
            return null
        }
        var byteArr = IPNum.toByteArray()
        if (byteArr.size > 4) {
            // strip sign byte
            byteArr = Arrays.copyOfRange(byteArr, byteArr.size - 4, byteArr.size)
        } else if (byteArr.size < 4) {
            val pad = ByteArray(4 - byteArr.size) // byte array with default zero values
            val tmp = pad.copyOf(pad.size + byteArr.size)
            System.arraycopy(byteArr, 0, tmp, pad.size, byteArr.size)
            byteArr = tmp
        }
        val ia = InetAddress.getByAddress(byteArr)
        return ia.hostAddress
    }

    /**
     * This function converts IP number to IPv6.
     *
     * @param IPNum IP number you wish to convert
     * @return String
     * @throws UnknownHostException If unable to convert byte array to IP address
     */
    @Throws(UnknownHostException::class)
    fun decimalToIPV6(IPNum: BigInteger): String? {
        if (IPNum < BigInteger.ZERO || IPNum > MAX_IPV6_RANGE) {
            return null
        }
        var byteArr = IPNum.toByteArray()
        if (byteArr.size > 16) {
            byteArr = Arrays.copyOfRange(byteArr, byteArr.size - 16, byteArr.size)
        } else if (byteArr.size < 16) {
            val pad = ByteArray(16 - byteArr.size) // byte array with default zero values
            val tmp = pad.copyOf(pad.size + byteArr.size)
            System.arraycopy(byteArr, 0, tmp, pad.size, byteArr.size)
            byteArr = tmp
        }
        val ia = InetAddress.getByAddress(byteArr)
        return ia.hostAddress
    }

    /**
     * This function returns the compressed form of the IPv6.
     *
     * @param IPAddress IP Address you wish to compress
     * @return String
     */
    fun compressIPV6(IPAddress: String?): String? {
        if (!isIPV6(IPAddress)) {
            return null
        }
        var result: String?
        try {
            val ia = InetAddress.getByName(IPAddress)
            result = ia.hostAddress

            // compress the zeroes
            if (PATTERN.matcher(result).find()) {
                result = result.replaceFirst(PATTERN.toString().toRegex(), "::")
            } else if (PATTERN2.matcher(result).find()) {
                result = result.replaceFirst(PATTERN2.toString().toRegex(), "::")
            } else if (PATTERN3.matcher(result).find()) {
                result = result.replaceFirst(PATTERN3.toString().toRegex(), "::")
            }
            result = result!!.replaceFirst("::0$".toRegex(), "::") // special case
        } catch (ex: UnknownHostException) {
            result = null
        }
        return result
    }

    /**
     * This function returns the expanded form of the IPv6.
     *
     * @param IPAddress IP Address you wish to expand
     * @return String
     */
    fun expandIPV6(IPAddress: String?): String? {
        if (!isIPV6(IPAddress)) {
            return null
        }
        var result: String?
        try {
            val ia = InetAddress.getByName(IPAddress)
            val byteArr = ia.address
            val strArr = arrayOfNulls<String>(byteArr.size)
            var x = 0
            while (x < byteArr.size) {
                strArr[x] = String.format("%02x", byteArr[x])
                x++
            }
            result = java.lang.String.join("", *strArr)
            result = result.replace("(.{4})".toRegex(), "$1:")
            result = result.substring(0, result.length - 1)
        } catch (ex: UnknownHostException) {
            result = null
        }
        return result
    }

    /**
     * This function returns the CIDR for an IPv4 range.
     *
     * @param IPFrom Starting IP of the range
     * @param IPTo   Ending IP of the range
     * @return List of strings
     * @throws UnknownHostException If unable to convert byte array to IP address
     */
    @Throws(UnknownHostException::class)
    fun ipV4ToCIDR(IPFrom: String?, IPTo: String?): List<String>? {
        if (!isIPV4(IPFrom) || !isIPV4(IPTo)) {
            return null
        }
        var startIP = ipV4ToDecimal(IPFrom)!!.longValueExact()
        val endIP = ipV4ToDecimal(IPTo)!!.longValueExact()
        val result: MutableList<String> = ArrayList()
        while (endIP >= startIP) {
            var maxSize: Long = 32
            while (maxSize > 0) {
                val mask = 2.0.pow(32.0).toLong() - 2.0.pow((32 - (maxSize - 1)).toDouble()).toLong()
                val maskBase = startIP and mask
                if (maskBase != startIP) {
                    break
                }
                maxSize -= 1
            }
            val x = ln((endIP - startIP + 1).toDouble()) / ln(2.0)
            val maxDiff = 32L - floor(x).toLong()
            if (maxSize < maxDiff) {
                maxSize = maxDiff
            }
            val ip = decimalToIPV4(BigInteger(startIP.toString()))
            result.add("$ip/$maxSize")
            startIP += 2.0.pow((32 - maxSize).toDouble()).toLong()
        }
        return result
    }

    private fun ipToBinary(IPAddress: String?): String? {
        if (!isIPV6(IPAddress)) {
            return null
        }
        var result: String?
        try {
            val ia = InetAddress.getByName(IPAddress)
            val byteArr = ia.address
            val strArr = arrayOfNulls<String>(byteArr.size)
            var x = 0
            while (x < byteArr.size) {
                strArr[x] = String.format("%8s", Integer.toBinaryString(byteArr[x].toInt() and 0xFF)).replace(' ', '0')
                x++
            }
            result = java.lang.String.join("", *strArr)
        } catch (ex: UnknownHostException) {
            result = null
        }
        return result
    }

    @Throws(UnknownHostException::class)
    private fun binaryToIP(Binary: String): String? {
        if (!BIN_PATTERN_FULL.matcher(Binary).matches()) {
            return null
        }
        val m = BIN_PATTERN.matcher(Binary)
        val byteArr = ByteArray(16)
        var part: String
        var x = 0
        while (m.find()) {
            part = m.group(1)
            byteArr[x] = part.toInt(2).toByte() // parse as int first to bypass overflow issue
            x++
        }
        val ia = InetAddress.getByAddress(byteArr)
        return ia.hostAddress
    }

    /**
     * This function returns the CIDR for an IPv6 range.
     *
     * @param IPFrom Starting IP of the range
     * @param IPTo   Ending IP of the range
     * @return List of strings
     * @throws UnknownHostException If unable to convert byte array to IP address
     */
    @Throws(UnknownHostException::class)
    fun ipV6ToCIDR(IPFrom: String, IPTo: String?): List<String>? {
        if (!isIPV6(IPFrom) || !isIPV6(IPTo)) {
            return null
        }
        var ipFromBin = ipToBinary(IPFrom)
        var ipToBin = ipToBinary(IPTo)
        if (ipFromBin == null || ipToBin == null) {
            return null
        }
        val result: MutableList<String> = ArrayList()
        var networkSize = 0
        var shift: Int
        var unPadded: String
        var padded: String
        val networks: MutableMap<String, Int> = TreeMap()
        var n: Int
        var values: List<Int>?
        if (ipFromBin.compareTo(ipToBin) == 0) {
            result.add("$IPFrom/128")
            return result
        }
        if (ipFromBin > ipToBin) {
            val tmp: String = ipFromBin
            ipFromBin = ipToBin
            ipToBin = tmp
        }
        do {
            if (ipFromBin!![ipFromBin.length - 1] == '1') {
                unPadded = ipFromBin.substring(networkSize, 128)
                padded = String.format("%-128s", unPadded).replace(' ', '0') // pad right
                networks[padded] = 128 - networkSize
                n = ipFromBin.lastIndexOf('0')
                ipFromBin = (if (n == 0) "" else ipFromBin.substring(0, n)) + "1"
                ipFromBin = String.format("%-128s", ipFromBin).replace(' ', '0') // pad right
            }
            if (ipToBin!![ipToBin.length - 1] == '0') {
                unPadded = ipToBin.substring(networkSize, 128)
                padded = String.format("%-128s", unPadded).replace(' ', '0') // pad right
                networks[padded] = 128 - networkSize
                n = ipToBin.lastIndexOf('1')
                ipToBin = (if (n == 0) "" else ipToBin.substring(0, n)) + "0"
                ipToBin = String.format("%-128s", ipToBin).replace(' ', '1') // pad right
            }
            if (ipToBin < ipFromBin) {
                continue
            }
            values = listOf(ipFromBin.lastIndexOf('0'), ipToBin.lastIndexOf('1'))
            shift = 128 - Collections.max(values)
            unPadded = ipFromBin.substring(0, 128 - shift)
            ipFromBin = String.format("%128s", unPadded).replace(' ', '0')
            unPadded = ipToBin.substring(0, 128 - shift)
            ipToBin = String.format("%128s", unPadded).replace(' ', '0')
            networkSize += shift
            if (ipFromBin.compareTo(ipToBin) == 0) {
                unPadded = ipFromBin.substring(networkSize, 128)
                padded = String.format("%-128s", unPadded).replace(' ', '0') // pad right
                networks[padded] = 128 - networkSize
            }
        } while (ipFromBin!! < ipToBin!!)
        for ((key, value) in networks) {
            result.add(compressIPV6(binaryToIP(key)) + "/" + value)
        }
        return result
    }

    /**
     * This function returns the IPv4 range for a CIDR.
     *
     * @param CIDR The CIDR address to convert to range
     * @return Array of strings
     * @throws UnknownHostException If unable to convert byte array to IP address
     */
    @Throws(UnknownHostException::class)
    fun cIDRToIPV4(CIDR: String): Array<String?>? {
        if (!CIDR.contains("/")) {
            return null
        }
        val ip: String
        val prefix: Int
        val arr = CIDR.split("/".toRegex()).toTypedArray()
        val ipStart: String?
        val ipEnd: String?

        if (arr.size != 2 || !isIPV4(arr[0]) || !PREFIX_PATTERN.matcher(arr[1]).matches() || arr[1].toInt() > 32) {
            return null
        }
        ip = arr[0]
        prefix = arr[1].toInt()
        var ipStartLong: Long = ipV4ToDecimal(ip)!!.longValueExact()
        ipStartLong = ipStartLong and (-1L shl 32 - prefix)
        ipStart = decimalToIPV4(BigInteger(ipStartLong.toString()))
        val total: Long = 1L shl 32 - prefix
        var ipEndLong: Long = ipStartLong + total - 1
        if (ipEndLong > 4294967295L) {
            ipEndLong = 4294967295L
        }
        ipEnd = decimalToIPV4(BigInteger(ipEndLong.toString()))
        return arrayOf(ipStart, ipEnd)
    }

    /**
     * This function returns the IPv6 range for a CIDR.
     *
     * @param CIDR The CIDR address to convert to range
     * @return Array of strings
     * @throws UnknownHostException If unable to convert byte array to IP address
     */
    @Throws(UnknownHostException::class)
    fun cIDRToIPV6(CIDR: String): Array<String?>? {
        if (!CIDR.contains("/")) {
            return null
        }
        val ip: String
        val prefix: Int
        val arr = CIDR.split("/".toRegex()).toTypedArray()
        if (arr.size != 2 || !isIPV6(arr[0]) || !PREFIX_PATTERN2.matcher(arr[1]).matches() || arr[1].toInt() > 128) {
            return null
        }
        ip = arr[0]
        prefix = arr[1].toInt()

        val parts: List<String>? = expandIPV6(ip)?.split(":")

        val bitStart: String = StringUtils.repeat('1', prefix) + StringUtils.repeat('0', 128 - prefix)
        val bitEnd: String = StringUtils.repeat('0', prefix) + StringUtils.repeat('1', 128 - prefix)

        val chunkSize = 16

        val floors: Array<String> =
            bitStart.split("(?<=\\G.{$chunkSize})".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val ceilings: Array<String> =
            bitEnd.split("(?<=\\G.{$chunkSize})".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val startIP: ArrayList<String> = ArrayList(8)
        val endIP: ArrayList<String> = ArrayList(8)

        for (x in 0..7) {
            startIP.add(Integer.toHexString((parts?.get(x)?.toInt(16) ?: 0) and floors[x].toInt(2)))
            endIP.add(Integer.toHexString((parts?.get(x)?.toInt(16) ?: 0) or ceilings[x].toInt(2)))
        }

        val hexStartAddress: String? = expandIPV6(StringUtils.join(startIP, ":"))
        val hexEndAddress: String? = expandIPV6(StringUtils.join(endIP, ":"))
        return arrayOf(hexStartAddress, hexEndAddress)
    }

    companion object {
        private val MAX_IPV4_RANGE = BigInteger("4294967295")
        private val MAX_IPV6_RANGE = BigInteger("340282366920938463463374607431768211455")
        private val PATTERN = Pattern.compile("^(0:){2,}")
        private val PATTERN2 = Pattern.compile(":(0:){2,}")
        private val PATTERN3 = Pattern.compile("(:0){2,}$")
        private val BIN_PATTERN_FULL = Pattern.compile("^([01]{8}){16}$")
        private val BIN_PATTERN = Pattern.compile("([01]{8})")
        private val PREFIX_PATTERN = Pattern.compile("^[0-9]{1,2}$")
        private val PREFIX_PATTERN2 = Pattern.compile("^[0-9]{1,3}$")
    }
}