package dev.eggnstone.plugins.jetbrains.dartformat.indenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter

class SingleBlockIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val lineSplitter = LineSplitter()
    }

    override fun indentPart(part: IPart): String
    {
        if (part !is SingleBlock)
            throw DartFormatException("Unexpected non-SingleBlock type.")

        val singleBlock: SingleBlock = part

        val header = indentHeader(singleBlock.header)

        //DotlinLogger.log("parts: ${Tools.toDisplayStringForParts(singleBlock.parts)}")

        val blockIndenter = BlockIndenter(spacesPerLevel)
        val indentedBody = blockIndenter.indentParts(singleBlock.parts, spacesPerLevel)

        @Suppress("UnnecessaryVariable")
        val result = header + indentedBody + singleBlock.footer

        return result
    }

    fun indentHeader(header: String): String
    {
        if (DotlinTools.isEmpty(header))
            throw DartFormatException("Unexpected empty header.")

        if (!DotlinTools.endsWith(header,"{"))
            throw DartFormatException("Unexpected header end: " + Tools.toDisplayString(DotlinTools.substring(header, header.length - 1)))

        val shortenedHeader = DotlinTools.substring(header, 0, header.length - 1)

        val headerLines = lineSplitter.split(shortenedHeader, true)
        if (headerLines.isEmpty())
            return "{"

        var result = headerLines[0]

        var startIndex = 1

        // Fix annotations
        while (startIndex < headerLines.size)
        {
            if (DotlinTools.startsWith(headerLines[startIndex - 1],"@")
                || DotlinTools.startsWith(headerLines[startIndex - 1],"//")
            )
            {
                result += headerLines[startIndex]
                startIndex++
                continue
            }

            break
        }

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until headerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val headerLine = headerLines.get(i) // workaround for dotlin
            DotlinLogger.log("headerLine #$i: ${Tools.toDisplayString(headerLine)}")

            var pad = ""
            if ( DotlinTools.startsWith(headerLine,"//")
                || ( DotlinTools.startsWith(headerLine,"async ") || DotlinTools.trim(headerLine) == "async")
            )
            {
                // no padding for "async...", "//..."
            }
            else
            {
                if (DotlinTools.isBlank(headerLine))
                    TODO("untested")
                else
                    pad = DotlinTools.getSpaces(spacesPerLevel)
            }

            result += pad + headerLine
        }

        //result += "{"

        // TODO: find a better solution
        val endsWithWhitespace = DotlinTools.isNotEmpty(result) && Tools.isWhitespace(DotlinTools.substring(result, result.length - 1))
        result += if (endsWithWhitespace) "{" else " {"

        return result
    }
}
