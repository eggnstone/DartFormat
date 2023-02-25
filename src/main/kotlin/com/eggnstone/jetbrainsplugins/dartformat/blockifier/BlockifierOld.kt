package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.blocks.*

class BlockifierOld
{
    companion object
    {
        const val debug = false
    }

    fun printBlocks(blocks: List<IBlock>)
    {
        for (block in blocks)
        {
            println("Block: ${block::class.simpleName}")
            println("  $block")
        }
    }

    fun blockify(text: String): List<IBlock>
    {
        var state = BlockifierStateOld()

        for (c in text)
        {
            if (debug)
                println("${Tools.toDisplayString2(c)} ${state.currentType} ${Tools.toDisplayString2(state.currentText)}")

            if (state.currentType != AreaType.Unknown)
            {
                state = when (state.currentType)
                {
                    AreaType.ClassBody -> handleInClassBody(c, state)
                    AreaType.ClassHeader -> handleInClassHeader(c, state)
                    AreaType.CurlyBracket -> handleInCurlyBrackets(c, state)
                    AreaType.Whitespace -> handleInWhitespace(c, state)
                    else -> throw DartFormatException("Unhandled BlockType: ${state.currentType}")
                }
                continue
            }

            if (Tools.isWhitespace(c))
            {
                if (state.currentText.isEmpty())
                {
                    state.currentType = AreaType.Whitespace
                    if (debug)
                        println("  -> ${state.currentType}")

                    state.currentText += c
                    continue
                }

                if (state.currentText == "class" || state.currentText == "abstract class")
                {
                    state.currentType = AreaType.ClassHeader
                    if (debug)
                        println("  -> ${state.currentType}")
                }

                state.currentText += c
                continue
            }

            if (c == '{')
            {
                if (state.currentText.isEmpty())
                {
                    state.currentType = AreaType.CurlyBracket
                    if (debug)
                        println("  -> ${state.currentType}")

                    state.currentText += c
                    continue
                }
            }

            if (c == ';')
            {
                state.blocks += ExpressionBlock(state.currentText + c)
                state.currentText = ""
                continue
            }

            state.currentText += c
        }

        if (state.currentText.isNotEmpty())
        {
            if (state.currentType == AreaType.CurlyBracket)
                state.blocks += CurlyBracketBlock(arrayListOf(UnknownBlock(state.currentText)))
            else if (state.currentType == AreaType.Unknown)
                state.blocks += UnknownBlock(state.currentText)
            else if (state.currentType == AreaType.Whitespace)
                state.blocks += WhitespaceBlock(state.currentText)
            else
                throw DartFormatException("Unhandled BlockType at end of text: ${state.currentType}")
        }

        return state.blocks
    }

    private fun handleInClassBody(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (c == '}')
        {
            state.currentType = AreaType.Unknown
            if (debug)
                println("  -> ${state.currentType}")

            val innerText = state.currentText.substring(1)
            val innerBlocks = blockify(innerText)
            state.blocks += ClassBlock(state.currentClassHeader, innerBlocks)
            state.currentClassHeader = ""
            state.currentText = ""
            return state
        }

        state.currentText += c
        return state
    }

    private fun handleInClassHeader(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (c == '{')
        {
            state.currentType = AreaType.ClassBody
            if (debug)
                println("  -> ${state.currentType}")

            state.currentClassHeader = state.currentText
            state.currentText = ""
        }

        state.currentText += c
        return state
    }

    private fun handleInCurlyBrackets(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (debug)
            println("  handleInCurlyBrackets: ${Tools.toDisplayString2(c)} ${Tools.toDisplayString2(state.currentText)}")

        if (c != '}')
        {
            state.currentText += c
            return state
        }

        state.currentType = AreaType.Unknown
        if (debug)
            println("  -> ${state.currentType}")

        state.blocks += CurlyBracketBlock(arrayListOf(UnknownBlock(state.currentText.substring(1))))
        state.currentText = ""
        return state
    }

    private fun handleInWhitespace(c: Char, state: BlockifierStateOld): BlockifierStateOld
    {
        if (Tools.isWhitespace(c))
        {
            state.currentText += c
            return state
        }

        state.currentType = AreaType.Unknown
        if (debug)
            println("  -> ${state.currentType}")

        if (state.currentText.isNotEmpty())
            state.blocks += WhitespaceBlock(state.currentText)

        state.currentText = c.toString()
        return state
    }
}
