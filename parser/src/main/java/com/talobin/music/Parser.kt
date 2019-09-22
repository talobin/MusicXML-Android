package com.talobin.music

import android.os.Environment
import android.util.Log
import com.talobin.music.parser.model.Pitch
import com.talobin.music.parser.model.ScorePartWise
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Singleton class to parse MusicXML
 */
object Parser {
    private val parsingEngine: TikXml

    private val TESTXMLSTRING =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><score-partwise version=\"1.1\"><!DOCTYPE score-partwise PUBLIC\n" +
                "\"-//Recordare//DTD MusicXML 1.1 Partwise//EN\"\n" +
                " \"http://www.musicxml.org/dtds/partwise.dtd\"><identification><encoding><software>PlayScore</software></encoding></identification></score-partwise>"

    private val TAG = Parser::class.java.simpleName

    init {
        parsingEngine = TikXml.Builder().exceptionOnUnreadXml(false).build()
    }


    //region Public APIs
    /**
     * Only for testing
     */
    fun parseTestXML() {
        try {
            val bufferedSource = sourceForFile("test.xml")
            val data = parsingEngine.read<ScorePartWise>(bufferedSource, ScorePartWise::class.java)
            val pitches = extractPitchList(0, data)
            Log.d("Hai", "Got data$pitches")
        } catch (e: Exception) {
            Log.e("HAI", "Ex ception:" + e.message)
        }

//        parseString(TESTXMLSTRING)

    }

    /**
     * Parse MusicXML from a file
     * @property filePath the path to the xml file.
     * @return An object that represents MusicXML data. Null if failed.
     */
    fun parseFile(filePath: String) {
        val file = File(filePath)
        parseFile(file)
    }

    /**
     * Parse MusicXML from a file
     * @property file the file that contains MusicXML data
     * @return An object that represents MusicXML data. Null if failed.
     */
    fun parseFile(file: File): ScorePartWise? {
        try {
            val bufferedSource = file.source().buffer()
            val data = parsingEngine.read<ScorePartWise>(bufferedSource, ScorePartWise::class.java)
//            Log.d(TAG, "Successfully parsed a file $data")
            return data
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse file. Exception:" + e.message)
            return null
        }
    }

    /**
     * Parse MusicXML from a string
     * @property xmlString string file that conforms MusicXML format
     * @return An object that represents MusicXML data. Null if failed.
     */
    fun parseString(xmlString: String): ScorePartWise? {
        try {
            val buffer = Buffer().writeUtf8(xmlString)
            val data = parsingEngine.read<ScorePartWise>(buffer, ScorePartWise::class.java)
            Log.d(TAG, "Successfully parsed a string $data")
            return data
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse String. Exception:" + e.message)
            return null
        }
    }
    //endregion


    //region Helper Functions
    @Throws(IOException::class)
    private fun sourceForFile(fileName: String): BufferedSource {
        return File(getResourcePath(fileName)).source().buffer()
    }

    /**
     * Get the resource path
     */
    private fun getResourcePath(fileName: String): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .absolutePath + ("/Camera/Scores" + "/" + fileName)
    }


    fun extractPitchList(partIndex: Int, scorePartWise: ScorePartWise): List<Pitch> {
        val result = ArrayList<Pitch>()
        if (scorePartWise.parts != null && scorePartWise.parts!!.size > partIndex) {
            val (_, measureList) = scorePartWise.parts!![partIndex]
            if (measureList != null && measureList.size > 0) {
                for (i in measureList.indices) {
                    val eachMesure = measureList[i]
                    if (eachMesure != null) {
                        val noteList = eachMesure.noteList
                        if (noteList != null && noteList.size > 0) {
                            for (j in noteList.indices) {
                                val (pitch) = noteList[j]
                                if (pitch != null) {
                                    result.add(pitch)
                                }
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    //endregion
}