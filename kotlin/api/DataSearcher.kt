package api

class DataSearcher {
    fun filterData(data: List<Data>, conditions: List<Condition>): List<Data> {
        return data.filter { datum ->
            conditions.all { condition ->
                when (condition.key.lowercase()) {
                    "temperature" -> compare(datum.ta, condition.type, condition.value)
                    "wave_height" -> compare(datum.whSig, condition.type, condition.value)
                    "wind_speed" -> compare(datum.ws1, condition.type, condition.value)
                    "pressure" -> compare(datum.pa, condition.type, condition.value)
                    "humidity" -> compare(datum.hm, condition.type, condition.value)
                    "max_wave" -> compare(datum.whMax, condition.type, condition.value)
                    "significant_wave" -> compare(datum.whSig, condition.type, condition.value)
                    "average_wave" -> compare(datum.whAve, condition.type, condition.value)
                    "wave_period" -> compare(datum.wp, condition.type, condition.value)
                    "sensor1_direction" -> compare(datum.wd1?.toFloat(), condition.type, condition.value)
                    "sensor1_gust" -> compare(datum.ws1Gst, condition.type, condition.value)
                    "sensor2_direction" -> compare(datum.wd2?.toFloat(), condition.type, condition.value)
                    "sensor2_speed" -> compare(datum.ws2, condition.type, condition.value)
                    "sensor2_gust" -> compare(datum.ws2Gst, condition.type, condition.value)
                    "sea_temperature" -> compare(datum.tw, condition.type, condition.value)
                    "wave_direction" -> compare(datum.wo?.toFloat(), condition.type, condition.value)
                    else -> false
                }
            }
        }
    }

    private fun compare(value: Float?, type: String, target: Float): Boolean {
        if (value == null) return false // null 값은 조건을 충족하지 않음
        return when (type) {
            ">" -> value > target
            "<" -> value < target
            "==" -> value == target
            else -> false
        }
    }
}

// Condition 클래스 정의
// key: 필터링할 필드 이름 (영어로 입력)
// type: 비교 연산자 (>, <, ==)
// value: 비교할 값

data class Condition(
    val key: String,
    val type: String,
    val value: Float
)
