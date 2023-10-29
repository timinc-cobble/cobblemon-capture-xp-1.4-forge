package us.timinc.mc.cobblemon.capturexp.config

import com.google.gson.GsonBuilder
import us.timinc.mc.cobblemon.capturexp.CaptureXP
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class CaptureXPConfig {
    val multiplier = 1.0

    class Builder {
        companion object {
            fun load() : CaptureXPConfig {
                val gson = GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create()

                var config = CaptureXPConfig()
                val configFile = File("config/${CaptureXP.MOD_ID}.json")
                configFile.parentFile.mkdirs()

                if (configFile.exists()) {
                    try {
                        val fileReader = FileReader(configFile)
                        config = gson.fromJson(fileReader, CaptureXPConfig::class.java)
                        fileReader.close()
                    } catch (e: Exception) {
                        println("Error reading config file")
                    }
                }

                val pw = PrintWriter(configFile)
                gson.toJson(config, pw)
                pw.close()

                return config
            }
        }
    }
}