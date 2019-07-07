package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String {
    if (count > this.trim().length)
        return this.trim()
    return this.substring(0, count).trim() + "..."
}


fun String.stripHtml(): String {

    var str = this

    while (str.indexOf("<") != -1 && str.indexOf(">") != -1) {
        val toBeReplaced = str.substring(str.indexOf("<"), str.indexOf(">") + 1)
        str = str.replace(toBeReplaced, "")
    }
    str = str.replace("\\s+".toRegex()," ")

    return str.trim()
}