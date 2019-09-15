package com.talobin

import com.talobin.musicxml.parser.Parser
import com.talobin.musicxml.parser.model.Identification
import com.talobin.musicxml.parser.model.ScorePartWise
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parseInCorrectString() {
        val wrongString = ""
        val result = Parser.parseString(wrongString)
        assertNull("Result is : $result", result)
    }

    @Test
    fun parseEmptyString() {
        val emptyString =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><score-partwise></score-partwise>"
        val result: ScorePartWise?
        result = Parser.parseString(emptyString)
        assertNotNull("Result is : $result", result)
        val emptyObject = ScorePartWise(null, null, null, null)
        assertEquals(emptyObject, result)
    }

    @Test
    fun parseStringVersionIsCorrect() {
        val versionString = "12345"
        val stringWithVersion =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><score-partwise version=\"$versionString\"></score-partwise>"
        val result: ScorePartWise?
        result = Parser.parseString(stringWithVersion)
        assertNotNull("Result is : $result", result)
        val emptyObject = ScorePartWise(versionString, null, null, null)
        assertEquals(emptyObject, result)
    }

    @Test
    fun parseStringIdentificationIsCorrect() {
        val targetObject =
            ScorePartWise(null, Identification(null, "TheCreator", null), null, null)
        val stringWithVersion =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><score-partwise><identification><creator>${targetObject.identification?.creator}</creator></identification></score-partwise>"
        val result: ScorePartWise?
        result = Parser.parseString(stringWithVersion)
        assertNotNull("Result is : $result", result)
        assertEquals(targetObject, result)
    }
}
