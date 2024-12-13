package main

import api.APIManager
import api.Data
import api.DataSearcher
import api.Condition

fun main() {
    val apiManager = APIManager()
    val dataSearcher = DataSearcher()

    // 사용자 입력
    print("검색할 시작 시간(예: 202307231200): ")
    val startTime = readLine() ?: ""
    print("검색할 종료 시간(예: 202307241200): ")
    val endTime = readLine() ?: ""
    print("관측소 ID(0: 모든 관측소): ")
    val stationId = readLine()?.toIntOrNull() ?: 0

    // API 데이터 가져오기
    val rawData = apiManager.fetchBuoyData(startTime, endTime, stationId)

    // 조건 입력 및 검색
    val conditions = mutableListOf<Condition>()
    while (true) {
        println("조건 입력 (종료: exit). 예) temperature > 20")
        print("조건 키(예: temperature, wave_height, wind_speed): ")
        val key = readLine() ?: ""
        if (key.lowercase() == "exit") break

        print("조건 타입(>, <, ==): ")
        val type = readLine() ?: ""
        print("조건 값: ")
        val value = readLine()?.toFloatOrNull() ?: 0f
        conditions.add(Condition(key.lowercase(), type, value))
    }

    // 조건 기반 데이터 검색
    val filteredData = dataSearcher.filterData(rawData, conditions)

    // 결과 출력
    if (filteredData.isEmpty()) {
        println("조건에 맞는 데이터가 없습니다.")
    } else {
        println("검색 결과:")
        filteredData.forEach { datum ->
            println("시간: ${datum.time}, 관측소: ${datum.location},")
            conditions.forEach { condition ->
                val value = when (condition.key) {
                    "temperature" -> datum.ta
                    "wave_height" -> datum.whSig
                    "wind_speed" -> datum.ws1
                    "pressure" -> datum.pa
                    "humidity" -> datum.hm
                    "max_wave" -> datum.whMax
                    "significant_wave" -> datum.whSig
                    "average_wave" -> datum.whAve
                    "wave_period" -> datum.wp
                    "sensor1_direction" -> datum.wd1
                    "sensor1_gust" -> datum.ws1Gst
                    "sensor2_direction" -> datum.wd2
                    "sensor2_speed" -> datum.ws2
                    "sensor2_gust" -> datum.ws2Gst
                    "sea_temperature" -> datum.tw
                    "wave_direction" -> datum.wo
                    else -> null
                }
                println("${condition.key.capitalize()}: $value")
            }
            println()
        }
    }
}
