import java.io.File

object Converter {
    private const val FILE_NAME = "libs.versions.toml"
    private const val LIBRARIES = "[libraries]"
    private const val VERSIONS = "[versions]"
    private const val PLUGINS = "[plugins]"

    private val libraries = mutableListOf<String>()
    private val versions = mutableListOf<String>()
    private val pluginIds = mutableListOf<String>()
    private val plugins = mutableListOf<String>()
    private val dependencies = mutableListOf<String>()

    init {
        File("output").mkdir()

        File("./output/$FILE_NAME").let {
            if (!it.exists()) it.createNewFile()
        }

//        file.writeText("$VERSIONS\n")
//
//        versions.forEach { file.appendText(it + '\n') }

//        file.appendText("\n$LIBRARIES\n")
//
//        libraries.forEach { file.appendText(it + '\n') }
//
//        file.appendText("\n$PLUGINS\n")
//
//        pluginIds.forEach { file.appendText(it + '\n') }
//
//        plugins.forEach { file.appendText(it + '\n') }
    }

    fun convertDependencies(inputString: String): String {
        dependencies.clear()
//        versions.clear()
        libraries.clear()

        val inputList = inputString.split('\n')

        for (it in inputList) {
            val input = it.trim()

            if (input.isBlank()) break
            else if (input[0] == '/') continue

            try {
                val firstColonIdx = input.indexOf(':')
                val lastColonIdx = input.lastIndexOf(':')
                val firstQuoteIdx = input.indexOf('"')
                val lastQuoteIdx = input.lastIndexOf('"')

                val group = input.substring(firstQuoteIdx + 1, firstColonIdx)
                var name = if (firstColonIdx != lastColonIdx) input.substring(firstColonIdx + 1, lastColonIdx)
                else input.substring(firstColonIdx + 1, lastQuoteIdx)

                libraries.forEach {
                    if (name == it.substring(0, it.indexOf('=') - 1)) {
                        name = "${group.replace('.', '-')}-$name"
                        return@forEach
                    }
                }

                val implementation = if (firstColonIdx != lastColonIdx) {
                    val version = input.substring(lastColonIdx + 1, input.lastIndexOf('"'))

                    val versioning = "$name = \"$version\""
                    versions.add(versioning)

                    "$name = { group = \"$group\", name = \"$name\", version.ref = \"$name\" }"

                } else "$name = { group = \"$group\", name = \"$name\" }"

                libraries.add(implementation)

                val dependencyName = name.replace('-', '.')
                val dependency =
                    input.substring(0, firstQuoteIdx) + "libs." + dependencyName + input.substring(lastQuoteIdx + 1)

                dependencies.add(dependency)

            } catch (e: Exception) {
                println("ERROR: ${e.message}")
            }
        }

        var output = ""
        dependencies.forEach { output += it + '\n' }

        return output
    }

    fun convertPlugins(inputString: String): String {
        plugins.clear()
        val inputList = inputString.split('\n')

        for (it in inputList) {
            val input = it.trim()

            if (input.isBlank()) break
            else if (input[0] == '/') continue

            try {
                val firstQuoteIdx = input.indexOf('"')
                val secondQuoteIdx = input.indexOf('"', firstQuoteIdx + 1)
                val thirdQuoteIdx = input.indexOf('"', secondQuoteIdx + 1)
                val lastQuoteIdx = input.indexOf('"', thirdQuoteIdx + 1)

                val realName = input.substring(firstQuoteIdx + 1, secondQuoteIdx)
                val formattedName = realName.replace('.', '-')

                val plugin = "$formattedName = { id = \"$realName\", version.ref = \"$formattedName\" }"
                pluginIds.add(plugin)

                val version = input.substring(thirdQuoteIdx + 1, lastQuoteIdx)

                val versioning = "$formattedName = \"$version\""
                versions.add(versioning)

                val x = "alias(libs.plugins.$realName)" + input.substring(lastQuoteIdx + 1)
                plugins.add(x)

            } catch (e: Exception) {
                println("ERROR: ${e.message}")
            }
        }

        var output = ""
        plugins.forEach { output += it + '\n' }

        return output
    }
}