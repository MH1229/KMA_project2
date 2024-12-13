package api

import java.net.HttpURLConnection
import java.net.URL

class APIManager {
    fun fetchBuoyData(startTime: String, endTime: String, stationId: Int): List<Data> {
        val urlString = "https://apihub.kma.go.kr/api/typ01/url/kma_buoy2.php?" +
                "tm1=$startTime&tm2=$endTime&stn=$stationId&authKey=CH8XAAZ1Qxq_FwAGdcMa8w"

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        val response = connection.run {
            requestMethod = "GET"
            inputStream.bufferedReader().use { it.readText() }
        }

        return parseRawResponse(response)
    }

    private fun parseRawResponse(response: String): List<Data> {
        val lines = response.lines()
        val dataList = mutableListOf<Data>()

        for (line in lines) {
            // 주석 및 헤더 제외
            if (line.startsWith("#") || line.isBlank() || line.contains("YYMMDDHHMI")) continue

            val tokens = line.split(",")
            if (tokens.size >= 19) { // 데이터 필드 수 확인
                try {
                    dataList.add(
                        Data(
                            time = tokens[0].trim(),
                            location = tokens[1].trim(),
                            wd1 = tokens[2].toIntOrNull(),
                            ws1 = tokens[3].toFloatOrNull(),
                            ws1Gst = tokens[4].toFloatOrNull(),
                            wd2 = tokens[5].toIntOrNull(),
                            ws2 = tokens[6].toFloatOrNull(),
                            ws2Gst = tokens[7].toFloatOrNull(),
                            pa = tokens[8].toFloatOrNull(),
                            hm = tokens[9].toFloatOrNull(),
                            ta = tokens[10].toFloatOrNull(),
                            tw = tokens[11].toFloatOrNull(),
                            whMax = tokens[12].toFloatOrNull(),
                            whSig = tokens[13].toFloatOrNull(),
                            whAve = tokens[14].toFloatOrNull(),
                            wp = tokens[15].toFloatOrNull(),
                            wo = tokens[16].toIntOrNull(),
                            aqc = tokens[17].trim(),
                            mqc = tokens[18].trim()
                        )
                    )
                } catch (e: Exception) {
                    println("Error parsing line: $line - ${e.message}")
                }
            }
        }
        return dataList
    }
}
