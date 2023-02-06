/*
package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.tokens.LineBreakToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class TestIndent
{
    @Test
    fun indentAngleBrackets()
    {
        val inputTokens = arrayListOf(
            SpecialToken("{"),
            LineBreakToken(newLine),
            UnknownToken("runApp"),
            SpecialToken("("),
            UnknownToken("const MyApp"),
            SpecialToken("("),
            SpecialToken(")"),
            SpecialToken(")"),
            SpecialToken(";"),
            LineBreakToken(newLine),
            SpecialToken("}"),
            LineBreakToken(newLine),
            UnknownToken("END")
        )
        val expectedOutputText = "void main()$newLine" +
                "{$newLine" +
                "    runApp(const MyApp());$newLine" +
                "}$newLine" +
                "END"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun closingBraceShouldDecreaseIndentationAtTextEnd()
    {
        val inputTokens = arrayListOf(
            UnknownToken("void main"),
            SpecialToken("("),
            SpecialToken(")"),
            LineBreakToken(newLine),
            SpecialToken("{"),
            LineBreakToken(newLine),
            UnknownToken("runApp"),
            SpecialToken("("),
            UnknownToken("const MyApp"),
            SpecialToken("("),
            SpecialToken(")"),
            SpecialToken(")"),
            SpecialToken(";"),
            LineBreakToken(newLine),
            SpecialToken("}")
        )
        val expectedOutputText = "void main()$newLine" +
                "{$newLine" +
                "    runApp(const MyApp());$newLine" +
                "}"

        val indenter = Indenter()
        val actualOutputText = indenter.indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }

    @Test
    fun doNotIndentEmptyLines()
    {
        val inputTokens = arrayListOf(
            UnknownToken("Text"), WhiteSpaceToken(" "), SpecialToken.OPENING_ANGLE_BRACKET, LineBreakToken(newLine),
            WhiteSpaceToken("    "), UnknownToken("Text"), LineBreakToken(newLine),
            LineBreakToken(newLine),
            WhiteSpaceToken("    "), UnknownToken("Text"), LineBreakToken(newLine)
        )
        val expectedOutputText = "Text {$newLine" +
                "    Text$newLine" +
                newLine +
                "    Text$newLine"

        val actualOutputText = Indenter().indent(inputTokens)

        assertThat(actualOutputText, equalTo(expectedOutputText))
    }
}
*/
