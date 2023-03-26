package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class MasterSplitter : ISplitter
{
    override val name = "Master"

    fun splitAll(inputText: String): List<IPart>
    {
        val splitResult = split(inputText)
        if (StringWrapper.isNotEmpty(splitResult.remainingText))
        {
            TODO("MasterSplitter.splitAll")
            DotlinLogger.log("MasterSplitter.splitAll")
            DotlinLogger.log("  parts:         ${Tools.toDisplayStringForParts(splitResult.parts)}")
            DotlinLogger.log("  remainingText: ${Tools.toDisplayStringShort(splitResult.remainingText)}")
            throw DartFormatException("splitResult.remainingText.isNotEmpty()")
        }

        return splitResult.parts
    }

    override fun split(inputText: String, params: SplitParams, inputCurrentIndent: Int): SplitResult
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("MasterSplitter.split: ${Tools.toDisplayStringShort(inputText)}")

        val parts = mutableListOf<IPart>()

        var currentIndent = inputCurrentIndent
        var remainingText = inputText
        while (StringWrapper.isNotEmpty(remainingText))
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("===> ${Tools.toDisplayStringShort(remainingText)}")

            val splitter = getSplitter(remainingText)
            @Suppress("FoldInitializerAndIfToElvis")
            if (splitter == null)
                return SplitResult(remainingText, parts)

            if (DotlinLogger.isEnabled) DotlinLogger.log("Calling '${splitter.name}' splitter ...")
            val splitResult = splitter.split(remainingText, params, currentIndent)
            val consumedText = StringWrapper.substring(remainingText, 0, remainingText.length - splitResult.remainingText.length)
            val lastConsumedLine = Tools.getLastLine(consumedText)

            if (DotlinLogger.isEnabled) DotlinLogger.log("Old currentIndent: $currentIndent")

            if (StringWrapper.isEmpty(lastConsumedLine))
            {
                currentIndent = 0
            }
            else
            {
                if (consumedText.contains("\n") || consumedText.contains("\r"))
                    currentIndent = lastConsumedLine.length
                else
                    currentIndent += lastConsumedLine.length
            }

            if (DotlinLogger.isEnabled)
            {
                DotlinLogger.log("Result from '${splitter.name}' splitter:")
                DotlinLogger.log("  parts:              ${Tools.toDisplayStringForParts(splitResult.parts)}")
                DotlinLogger.log("  remainingText:      ${Tools.toDisplayStringShort(splitResult.remainingText)}")
                DotlinLogger.log("  > consumedText:     ${Tools.toDisplayStringShort(consumedText)}")
                DotlinLogger.log("  > lastConsumedLine: ${Tools.toDisplayStringShort(lastConsumedLine)}")
                DotlinLogger.log("  > currentIndent:    $currentIndent")
            }

            remainingText = splitResult.remainingText
            //parts += result.parts // dotlin
            parts.addAll(splitResult.parts)
        }

        return SplitResult("", parts)
    }

    fun getSplitter(inputText: String): ISplitter?
    {
        if (StringWrapper.isEmpty(inputText))
            throw DartFormatException("Unexpected empty text.")

        @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
        val c = inputText.get(0).toString() // workaround for dotlin for: for (c in text)

        if (Tools.isWhitespace(c))
            return WhitespaceSplitter()

        if (c != "}")
            return TextSplitter()

        return null
    }
}
