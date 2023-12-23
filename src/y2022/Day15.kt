package y2022

import Day
import util.Point
import util.manhattan
import util.pTo
import util.toInts
import kotlin.math.abs

object Day15 : Day(year = 2022) {

    data class SensorBeacon(
        val sensor: Point,
        val beacon: Point,
        val manhattan: Int
    )

    private fun getSensorBeacons(): List<SensorBeacon> =
        lines.map {
            val (sensorX, sensorY, beaconX, beaconY) =
                "Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)".toRegex().find(it)!!.groupValues.drop(1)
                    .toInts()
            val sensor = sensorX pTo sensorY
            val beacon = beaconX pTo beaconY
            val manhattan = manhattan(sensor, beacon)
            SensorBeacon(sensor, beacon, manhattan)
        }

    override fun part1(): Any {
        val targetY = 2000000
        val sensorBeacons = getSensorBeacons()
        val beaconToImpossibleRange = sensorBeacons.map {
            val yDistance = abs(targetY - it.sensor.y)
            val maxXDistance = it.manhattan - yDistance
            if (maxXDistance >= 0) {
                it.beacon to ((it.sensor.x - maxXDistance)..(it.sensor.x + maxXDistance)).toList()
            } else {
                it.beacon to emptyList()
            }
        }
        val existingBeacons = sensorBeacons.map(SensorBeacon::beacon)
        return beaconToImpossibleRange.flatMap(Pair<Point, List<Int>>::second).distinct().filter {
            it pTo targetY !in existingBeacons
        }.size
    }

    private fun findPoint(sensorBeacons: List<SensorBeacon>): Point? {
        val limit = 4000000
        for (x in 0..limit) {
            var y = 0
            while (y <= limit) {
                val point = x pTo y
                val sensor = sensorBeacons.find {
                    manhattan(it.sensor, point) <= it.manhattan
                } ?: return point
                y = sensor.sensor.y + sensor.manhattan - abs(x - sensor.sensor.x) + 1
            }
        }
        return null
    }

    override fun part2(): Any =
        findPoint(getSensorBeacons())?.let {
            (it.x.toLong() * 4000000) + it.y.toLong()
        } ?: -1
}