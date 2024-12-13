package api

import java.io.File

class FileManager {
    fun saveData(data: List<Data>, conditions: List<Condition>, fileName: String) {
        val file = File(fileName)
        file.bufferedWriter().use { writer ->
            // 헤더 생성: 조건 키를 기준으로 헤더 작성
            val headers = conditions.map { condition ->
                when (condition.key.lowercase()) {
                    "temperature" -> "기온"
                    "wave_height" -> "파고"
                    "wind_speed" -> "풍속"
                    "pressure" -> "해면기압"
                    "humidity" -> "습도"
                    "max_wave" -> "최대파고"
                    "significant_wave" -> "유의파고"
                    "average_wave" -> "평균파고"
                    "wave_period" -> "파주기"
                    "sensor1_direction" -> "센서1 풍향"
                    "sensor1_gust" -> "센서1 GUST"
                    "sensor2_direction" -> "센서2 풍향"
                    "sensor2_speed" -> "센서2 풍속"
                    "sensor2_gust" -> "센서2 GUST"
                    "sea_temperature" -> "해수면 온도"
                    "wave_direction" -> "파향"
                    else -> condition.key
                }
            }

            writer.write("시간, 관측소, ${headers.joinToString(", ")}")
            writer.newLine()

            // 데이터 작성: 조건 키와 일치하는 데이터만 작성
            data.forEach { datum ->
                val selectedValues = conditions.map { condition ->
                    when (condition.key.lowercase()) {
                        "temperature" -> datum.ta?.toString() ?: ""
                        "wave_height" -> datum.whSig?.toString() ?: ""
                        "wind_speed" -> datum.ws1?.toString() ?: ""
                        "pressure" -> datum.pa?.toString() ?: ""
                        "humidity" -> datum.hm?.toString() ?: ""
                        "max_wave" -> datum.whMax?.toString() ?: ""
                        "significant_wave" -> datum.whSig?.toString() ?: ""
                        "average_wave" -> datum.whAve?.toString() ?: ""
                        "wave_period" -> datum.wp?.toString() ?: ""
                        "sensor1_direction" -> datum.wd1?.toString() ?: ""
                        "sensor1_gust" -> datum.ws1Gst?.toString() ?: ""
                        "sensor2_direction" -> datum.wd2?.toString() ?: ""
                        "sensor2_speed" -> datum.ws2?.toString() ?: ""
                        "sensor2_gust" -> datum.ws2Gst?.toString() ?: ""
                        "sea_temperature" -> datum.tw?.toString() ?: ""
                        "wave_direction" -> datum.wo?.toString() ?: ""
                        else -> ""
                    }
                }
                writer.write("${datum.time}, ${datum.location}, ${selectedValues.joinToString(", ")}")
                writer.newLine()
            }
        }
    }
}
