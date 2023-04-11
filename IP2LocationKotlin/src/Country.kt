import org.apache.commons.csv.CSVFormat
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * This class parses country information CSV and returns the country information.
 *
 * Copyright (c) 2002-2023 IP2Location.com
 *
 * @author IP2Location.com
 * @version 8.4.0
 */
class Country(CSVFile: String) {
    private val records: MutableMap<String?, Map<String, String?>> = HashMap()

    /**
     * This function gets the country information for the supplied country code.
     *
     * @param CountryCode ISO-3166 country code
     * @return Map
     */
    @Throws(IOException::class)
    fun getCountryInfo(CountryCode: String?): Map<String, String?>? {
        return if (records.isEmpty()) {
            throw IOException("No record available.")
        } else {
            records.getOrDefault(CountryCode, null)
        }
    }

    /**
     * This function gets the country information for all countries.
     *
     * @return List
     */
    @Throws(IOException::class)
    fun getCountryInfo(): List<Map<String, String?>> {
        val results: MutableList<Map<String, String?>> = ArrayList()
        if (records.isEmpty()) {
            throw IOException("No record available.")
        } else {
            for ((_, value) in records) {
                results.add(value)
            }
        }
        return results
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
     * This constructor reads the country information CSV and store the parsed info.
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
                var gotCountryCode = false
                for ((y, col) in row.withIndex()) {
                    val col2 = col.trim('"')
                    if (col2 == "country_code") {
                        gotCountryCode = true
                    }
                    header.add(y, col2)
                }
                if (!gotCountryCode) {
                    throw IOException("Invalid country information CSV file.")
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
                records[countryCode] = dataRow
            }
        }
    }
}