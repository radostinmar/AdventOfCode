package y2023

import Day
import util.lcm
import java.util.*

object Day20 : Day(year = 2023) {

    private val signalQueue: Queue<Signal> = LinkedList()

    private enum class Pulse {
        LOW, HIGH
    }

    private data class Signal(
        val pulse: Pulse,
        val from: String,
        val to: String
    )

    private interface Module {
        val name: String
        val receivers: List<String>
        fun receive(pulse: Pulse, from: String)
    }

    private data class FlipFlop(
        override val name: String,
        override val receivers: List<String>
    ) : Module {
        private var on = false
        override fun receive(pulse: Pulse, from: String): Unit =
            if (pulse != Pulse.HIGH) {
                val newPulse = if (on) Pulse.LOW else Pulse.HIGH
                on = !on
                receivers.forEach {
                    signalQueue.add(Signal(newPulse, this.name, it))
                }
            } else Unit
    }

    private data class Conjunction(
        override val name: String,
        override val receivers: List<String>,
        val connected: List<String>
    ) : Module {

        val remembered = connected.associateWith {
            Pulse.LOW
        }.toMutableMap()

        override fun receive(pulse: Pulse, from: String) {
            remembered[from] = pulse
            val newPulse = if (remembered.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
            receivers.forEach {
                signalQueue.add(Signal(newPulse, this.name, it))
            }
        }
    }

    private data class Broadcast(
        override val name: String,
        override val receivers: List<String>
    ) : Module {
        override fun receive(pulse: Pulse, from: String): Unit =
            receivers.forEach {
                signalQueue.add(Signal(pulse, this.name, it))
            }
    }

    private fun generateModules(): List<Module> =
        lines.mapNotNull { line ->
            val module = line.substringBefore(" ->")
            val receivers = line.substringAfter("-> ").split(", ")
            when {
                module == "broadcaster" -> Broadcast(module, receivers)
                module.first() == '%' -> FlipFlop(module.drop(1), receivers)
                module.first() == '&' -> Conjunction(module.drop(1), receivers, emptyList())
                else -> null
            }
        }.let { temp ->
            temp.map { module ->
                if (module is Conjunction) {
                    val connected = temp.mapNotNull { if (module.name in it.receivers) it.name else null }
                    module.copy(connected = connected)
                } else {
                    module
                }
            }
        }

    override fun part1(): Any {
        val nameMap = generateModules().associateBy { it.name }
        var lowCount = 0
        var highCount = 0
        repeat(1000) {
            signalQueue.add(Signal(Pulse.LOW, "button", "broadcaster"))
            while (signalQueue.isNotEmpty()) {
                val current = signalQueue.poll()
                if (current.pulse == Pulse.LOW) lowCount++ else highCount++
                nameMap[current.to]?.receive(current.pulse, current.from)
            }
        }
        return lowCount * highCount
    }

    override fun part2(): Any {
        val modules = generateModules()
        val nameMap = modules.associateBy { it.name }
        val input = modules.first { "rx" in it.receivers } as Conjunction
        val buttonPressesToHigh = input.connected.associateWith<String, Int?> { null }.toMutableMap()
        var buttonPresses = 0
        while (true) {
            signalQueue.add(Signal(Pulse.LOW, "button", "broadcaster"))
            buttonPresses++
            while (signalQueue.isNotEmpty()) {
                val current = signalQueue.poll()
                nameMap[current.to]?.receive(current.pulse, current.from)
                if (current.to == input.name) {
                    val highKey = input.remembered.entries.find { it.value == Pulse.HIGH }?.key
                    if (highKey != null && buttonPressesToHigh[highKey] == null) {
                        buttonPressesToHigh[highKey] = buttonPresses
                        if (buttonPressesToHigh.values.all { it != null }) {
                            return buttonPressesToHigh.values.map { it!!.toLong() }.lcm()
                        }
                    }
                }
            }
        }
    }
}
