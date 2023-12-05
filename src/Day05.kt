data class MapEntry(
    val srcStart: Long,
    val length: Long,
    val dstStart: Long
)

data class SeedEntry(
    val start: Long,
    val length: Long
)

fun List<MapEntry>.get(num: Long): Long =
    this.find { num >= it.srcStart && num < it.srcStart + it.length }?.let {
        (num - it.srcStart) + it.dstStart
    } ?: num


fun main() {

    fun part1(input: List<String>): Long {
        var seeds = emptyList<Long>()
        val seedToSoilMap = mutableListOf<MapEntry>()
        val soilToFertilizerMap = mutableListOf<MapEntry>()
        val fertilizerToWaterMap = mutableListOf<MapEntry>()
        val waterToLightMap = mutableListOf<MapEntry>()
        val lightToTemperatureMap = mutableListOf<MapEntry>()
        val temperatureToHumidityMap = mutableListOf<MapEntry>()
        val humidityToLocationMap = mutableListOf<MapEntry>()
        var currentMap = seedToSoilMap
        input.forEach { line ->
            if (line.isNotEmpty()) {
                when {
                    line.startsWith("seeds:") -> {
                        seeds = line.substringAfter("seeds: ").split(" ").map { it.toLong() }
                    }

                    line == "seed-to-soil map:" -> {
                        currentMap = seedToSoilMap
                    }

                    line == "soil-to-fertilizer map:" -> {
                        currentMap = soilToFertilizerMap
                    }

                    line == "fertilizer-to-water map:" -> {
                        currentMap = fertilizerToWaterMap
                    }

                    line == "water-to-light map:" -> {
                        currentMap = waterToLightMap
                    }

                    line == "light-to-temperature map:" -> {
                        currentMap = lightToTemperatureMap
                    }

                    line == "temperature-to-humidity map:" -> {
                        currentMap = temperatureToHumidityMap
                    }

                    line == "humidity-to-location map:" -> {
                        currentMap = humidityToLocationMap
                    }

                    else -> {
                        val numbers = line.split(" ").map { it.toLong() }
                        val dstStart = numbers[0]
                        val srcStart = numbers[1]
                        val length = numbers[2]
                        currentMap.add(MapEntry(srcStart, length, dstStart))
                    }
                }
            }
        }
        return seeds.minOf { seed ->
            seedToSoilMap.get(seed).let { soil ->
                soilToFertilizerMap.get(soil).let { fertilizer ->
                    fertilizerToWaterMap.get(fertilizer).let { water ->
                        waterToLightMap.get(water).let { light ->
                            lightToTemperatureMap.get(light).let { temp ->
                                temperatureToHumidityMap.get(temp).let { humidity ->
                                    humidityToLocationMap.get(humidity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun part2(input: List<String>): Long {
        var seeds = emptyList<SeedEntry>()
        val seedToSoilMap = mutableListOf<MapEntry>()
        val soilToFertilizerMap = mutableListOf<MapEntry>()
        val fertilizerToWaterMap = mutableListOf<MapEntry>()
        val waterToLightMap = mutableListOf<MapEntry>()
        val lightToTemperatureMap = mutableListOf<MapEntry>()
        val temperatureToHumidityMap = mutableListOf<MapEntry>()
        val humidityToLocationMap = mutableListOf<MapEntry>()
        var currentMap = seedToSoilMap
        input.forEach { line ->
            if (line.isNotEmpty()) {
                when {
                    line.startsWith("seeds:") -> {
                        seeds = line.substringAfter("seeds: ")
                            .split(" ")
                            .map { it.toLong() }
                            .chunked(2)
                            .map { SeedEntry(it[0], it[1]) }
                    }

                    line == "seed-to-soil map:" -> {
                        currentMap = seedToSoilMap
                    }

                    line == "soil-to-fertilizer map:" -> {
                        currentMap = soilToFertilizerMap
                    }

                    line == "fertilizer-to-water map:" -> {
                        currentMap = fertilizerToWaterMap
                    }

                    line == "water-to-light map:" -> {
                        currentMap = waterToLightMap
                    }

                    line == "light-to-temperature map:" -> {
                        currentMap = lightToTemperatureMap
                    }

                    line == "temperature-to-humidity map:" -> {
                        currentMap = temperatureToHumidityMap
                    }

                    line == "humidity-to-location map:" -> {
                        currentMap = humidityToLocationMap
                    }

                    else -> {
                        val numbers = line.split(" ").map { it.toLong() }
                        val dstStart = numbers[0]
                        val srcStart = numbers[1]
                        val length = numbers[2]
                        currentMap.add(MapEntry(srcStart, length, dstStart))
                    }
                }
            }
        }
        var min = Long.MAX_VALUE

        seeds.forEach {
             repeat(it.length.toInt()) { index ->
                seedToSoilMap.get(it.start + index).let { soil ->
                    soilToFertilizerMap.get(soil).let { fertilizer ->
                        fertilizerToWaterMap.get(fertilizer).let { water ->
                            waterToLightMap.get(water).let { light ->
                                lightToTemperatureMap.get(light).let { temp ->
                                    temperatureToHumidityMap.get(temp).let { humidity ->
                                        humidityToLocationMap.get(humidity).let {
                                            if(it < min) min = it
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return min
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
