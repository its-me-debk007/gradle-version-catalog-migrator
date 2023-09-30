import util.*
import java.io.File


private val libraries = hashMapOf<String, Pair<String, String>>()
private val versions = hashMapOf<String, String>()
private val pluginIds = hashMapOf<String, String>()

fun setupToml() {
    File("output").mkdir()

    val file = File("./output/$FILE_NAME")
    if (!file.exists()) file.createNewFile()

    file.writeText("$VERSIONS\n")

    versions.forEach { (name, version) ->
        val statement = "$name = \"$version\""
        file.appendText(statement + '\n')
    }

    file.appendText("\n$LIBRARIES\n")

    libraries.forEach { (name, groupVersionPair) ->
        val statement = "$name = { group = \"${groupVersionPair.first}\", name = \"$name\"" +
                (if (groupVersionPair.second.isNotEmpty()) ", version.ref = \"${groupVersionPair.second}\" " else "") + "}"
        file.appendText(statement + '\n')
    }

    file.appendText("\n$PLUGINS\n")

    pluginIds.forEach { (name, id) ->
        val statement = "$name = { id = \"$id\", version.ref = \"$name\" }"
        file.appendText(statement + '\n')
    }
}

fun convertDependencies(inputString: String): String {
    val dependenciesOutput = mutableListOf<String>()
    libraries.clear()

    val inputList = inputString.split('\n')

    for (it in inputList) {
        val input = it.trim()

        if (input.isEmpty() || input[0] == '/') continue

        try {
            val firstColonIdx = input.indexOf(':')
            val lastColonIdx = input.lastIndexOf(':')
            val firstQuoteIdx = input.indexOf('"')
            val lastQuoteIdx = input.lastIndexOf('"')

            val group = input.substring(firstQuoteIdx + 1, firstColonIdx)
            var name = if (firstColonIdx != lastColonIdx) input.substring(
                firstColonIdx + 1,
                lastColonIdx
            )
            else input.substring(firstColonIdx + 1, lastQuoteIdx)

            if (libraries.containsKey(name)) name = "${group.replace('.', '-')}-$name"

            val groupVersionPair = if (firstColonIdx != lastColonIdx) {
                val formattedGroup = group.replace('.', '-')
                versions[formattedGroup] =
                    input.substring(lastColonIdx + 1, input.lastIndexOf('"'))

                group to formattedGroup
            } else group to ""

            libraries[name] = groupVersionPair

            val dependencyName = name.replace('-', '.')
            val dependency =
                input.substring(0, firstQuoteIdx) + "libs." + dependencyName + input.substring(
                    lastQuoteIdx + 1
                )

            dependenciesOutput.add(dependency + '\n')
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            return INVALID_SYNTAX
        }
    }

    return dependenciesOutput.toString()
}

fun convertPlugins(inputString: String): String {
    val inputList = inputString.split('\n')

    val pluginsOutput = StringBuilder()
    for (it in inputList) {
        val input = it.trim()

        if (input.isEmpty() || input[0] == '/') continue

        try {
            val firstQuoteIdx = input.indexOf('"')
            val secondQuoteIdx = input.indexOf('"', firstQuoteIdx + 1)
            val thirdQuoteIdx = input.indexOf('"', secondQuoteIdx + 1)
            val lastQuoteIdx = input.indexOf('"', thirdQuoteIdx + 1)

            val realName = input.substring(firstQuoteIdx + 1, secondQuoteIdx)
            val formattedName = realName.replace('.', '-')

            pluginIds[formattedName] = realName

            val version = input.substring(thirdQuoteIdx + 1, lastQuoteIdx)
            versions[formattedName] = version

            val pluginLine = "alias(libs.plugins.$realName)" + input.substring(lastQuoteIdx + 1)

            pluginsOutput.append(pluginLine + '\n')
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            return INVALID_SYNTAX
        }
    }
    pluginsOutput.append(COMMENT)
    return pluginsOutput.toString()
}
