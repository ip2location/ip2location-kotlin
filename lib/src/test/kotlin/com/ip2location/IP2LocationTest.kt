package com.ip2location

import kotlin.test.Test
import kotlin.test.assertTrue

class IP2LocationTest {
    @Test
    fun testUS() {
        val strIPAddress = "8.8.8.8"
        val resourceUrl = javaClass.getResource("/IP2LOCATION-LITE-DB1.BIN")
        val file = java.io.File(resourceUrl!!.toURI())
        val dbPath = file.absolutePath
        val loc = IP2Location()

        val useMMF = false
        loc.open(dbPath, useMMF)
        val rec = loc.ipQuery(strIPAddress)
        if (rec.status == "OK") {
            assertTrue { rec.countryShort == "US" }
        }
    }
}
