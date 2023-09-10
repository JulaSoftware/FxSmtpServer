package de.julasoftware.fxsmtp.core

import de.julasoftware.fxsmtp.model.ConfigModel
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Representer
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists


class Configuration {
    private val logger = LoggerFactory.getLogger(Configuration::class.java)
    private val defaultConfig = "/defaultConfig.yaml"
    private val userConfig = Path.of(System.getProperty("user.home"), "fxSmtpConfig.yaml")
    private var yaml: Yaml
    var loadedConfig: ConfigModel
        private set

    init {
        val configStream = loadConfig()

        val constructor = Constructor(ConfigModel::class.java, LoaderOptions())
        val dumperOptions = DumperOptions()
        dumperOptions.defaultScalarStyle = DumperOptions.ScalarStyle.SINGLE_QUOTED
        dumperOptions.isPrettyFlow = true
        dumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK

        yaml = Yaml(constructor, Representer(dumperOptions), dumperOptions)

        loadedConfig = yaml.load(configStream)

        Files.createDirectories(Path.of(loadedConfig.email.folder))
    }

    private fun loadConfig(): InputStream {
        logger.info("loading config")
        logger.debug("looking for {}", userConfig)
        if (userConfig.exists()) {
            return FileInputStream(userConfig.toFile())
        }

        logger.debug("loading default config")
        return javaClass.getResourceAsStream(defaultConfig)!!
    }

    fun saveToUserConfig() {
        val fos = FileWriter(userConfig.toFile())
        fos.write(yaml.dumpAs(loadedConfig, Tag.MAP, DumperOptions.FlowStyle.BLOCK))
        fos.close()
    }

    companion object {
        @Volatile
        private var instance: Configuration? = null

        fun instance(): Configuration {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Configuration()
                    }
                }
            }

            return instance!!
        }
    }
}