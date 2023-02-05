package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.SpecialToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CoverageForSpecialToken
{
    @Test
    fun testHashCode() = assertThat(SpecialToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(SpecialToken("a").toString(), equalTo("Special(a)"))
}
