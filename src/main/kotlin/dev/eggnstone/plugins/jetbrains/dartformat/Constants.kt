package dev.eggnstone.plugins.jetbrains.dartformat

class Constants
{
    companion object
    {
        const val DEBUG_FORMAT_ACTION = true
        const val DEBUG_NOTIFICATION_TOOLS = false
        const val DEBUG_SETTINGS_DIALOG = false

        const val REPO_NAME_DART_FORMAT = "DartFormat"
        const val REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN = "DartFormatJetBrainsPlugin"

        const val WAIT_INTERVAL_IN_MILLIS = 100
        const val WAIT_FOR_FORMAT_IN_SECONDS = 10
        const val WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS = 60
        const val WAIT_FOR_EXTERNAL_DART_FORMAT_RESPONSE_IN_SECONDS = 5
    }
}
