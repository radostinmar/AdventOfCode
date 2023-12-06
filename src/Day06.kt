object Day06 : Day() {

    override fun part1(): Number {
        val times = lines[0].substringAfter("Time:").trim().split("\\s+".toRegex()).toInts()
        val records = lines[1].substringAfter("Distance:").trim().split("\\s+".toRegex()).toInts()
        return times.zip(records).map { (time, record) ->
            (0..time).count {holdTime ->
                val distance = holdTime * (time - holdTime)
                distance > record
            }
        }.product
    }

    override fun part2(): Number {
        val time = lines[0].substringAfter("Time:").replace(" ", "").toLong()
        val record = lines[1].substringAfter("Distance:").replace(" ", "").toLong()
        return (0..time).count { holdTime ->
            val distance = holdTime * (time - holdTime)
            distance > record
        }
    }
}
