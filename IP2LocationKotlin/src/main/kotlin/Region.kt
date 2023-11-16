import org.apache.commons.csv.CSVFormat
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * This class parses region information CSV and returns the region code.
 *
 * Copyright (c) 2002-2023 IP2Location.com
 *
 * @author IP2Location.com
 * @version 8.4.1
 */
class Region(CSVFile: String) {
    private val records: MutableMap<String?, MutableList<Map<String, String?>>> = HashMap()

    /**
     * This function gets the region code for the supplied country code and region name.
     *
     * @param CountryCode ISO-3166 country code
     * @param RegionName  Region name
     * @return String
     */
    @Throws(IOException::class)
    fun getRegionCode(CountryCode: String?, RegionName: String): String? {
        if (records.isEmpty()) {
            throw IOException("No record available.")
        } else {
            if (!records.containsKey(CountryCode)) {
                return null
            }
            val items: MutableList<Map<String, String?>>? = records[CountryCode]
            for (item in items!!) {
                if (item["name"].equals(RegionName, ignoreCase = true)) {
                    return item["code"]
                }
            }
        }
        return null
    }

    private fun readCSV(fr: FileReader): List<MutableList<String>> =
        CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.build().parse(fr).map {
            val mList: MutableList<String> = mutableListOf()
            for (x in it) {
                mList.add(x)
            }
            mList
        }.toList()

    /**
     * This constructor reads the region information CSV and store the parsed info.
     */
    init {
        val file = File(CSVFile)
        if (!file.exists()) {
            throw IOException("The CSV file '$CSVFile' is not found.")
        }
        val fr = FileReader(file)
        if (fr.read() == -1) {
            throw IOException("Unable to read '$CSVFile'.")
        }
        val header: MutableList<String> = mutableListOf()
        val data = readCSV(fr)
        for ((x, row) in data.withIndex()) {
            if (x == 0) {
                var gotSubDivisionName = false
                for ((y, col) in row.withIndex()) {
                    var col2 = col.trim('"')
                    if (col2 == "subdivision_name") {
                        gotSubDivisionName = true
                        col2 = "name"
                    }
                    header.add(y, col2)
                }
                if (!gotSubDivisionName) {
                    throw IOException("Invalid region information CSV file.")
                }
            } else {
                val dataRow: MutableMap<String, String> = mutableMapOf()
                var countryCode = ""
                for ((y, col) in row.withIndex()) {
                    if (header[y] == "country_code") {
                        countryCode = col
                    }
                    dataRow[header[y]] = col
                }
                if (!records.containsKey(countryCode)) {
                    records[countryCode] = mutableListOf()
                }
                records[countryCode]!!.add(dataRow)
            }
        }

    }
}