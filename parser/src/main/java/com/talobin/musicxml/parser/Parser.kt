package com.talobin.musicxml.parser

import android.os.Environment
import android.util.Log
import com.talobin.musicxml.parser.model.Pitch
import com.talobin.musicxml.parser.model.ScorePartWise
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

    private val xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><score-partwise version=\"1.1\"><identification><encoding><software>PlayScore</software></encoding></identification></score-partwise>"

    init {
        parsingEngine = TikXml.Builder().exceptionOnUnreadXml(false).build()
    }


    //region Public APIs
    /**
     * Only for testing
     */
    fun parseTestXML() {
        try {
            val bufferedSource = sourceForFile("text.xml")
            val data = parsingEngine.read<ScorePartWise>(bufferedSource, ScorePartWise::class.java)
            val pitches = extractPitchList(0, data)
            Log.d("Hai", "Got data$pitches")
        } catch (e: Exception) {
            Log.e("HAI", "Ex ception:" + e.message)
        }

        parseString(xmlString)

    }

    /**
     * Parse MusicXML from a file
     * @property filePath the path to the xml file.
     */
    fun parseFile(filePath: String) {
        val file = File(filePath)
        parseFile(file)
    }

    /**
     * Parse MusicXML from a file
     * @property file the file that contains MusicXML data
     */
    fun parseFile(file: File) {
        try {
            val bufferedSource = file.source().buffer()
            val data = parsingEngine.read<ScorePartWise>(bufferedSource, ScorePartWise::class.java)
            Log.d("Hai", "Got data$data")
        } catch (e: Exception) {
            Log.e("HAI", "Exception:" + e.message)
        }
    }

    fun parseString(xmlString: String) {
        try {
            val buffer = Buffer().writeUtf8(xmlString)
            val data = parsingEngine.read<ScorePartWise>(buffer, ScorePartWise::class.java)
            Log.d("Hai", "Got data$data")
        } catch (e: Exception) {
            Log.e("HAI", "Exception:" + e.message)
        }
    }
    //endregion


    //region Helper Functions
    @Throws(IOException::class)
    private fun sourceForFile(filePath: String): BufferedSource {
        return File(getResourcePath(filePath)).source().buffer()
    }

    /**
     * Get the resource path
     */
    private fun getResourcePath(resPath: String): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .absolutePath + ("/Camera/Scores" + "/" + "test.xml")
    }


    private fun extractPitchList(partIndex: Int, scorePartWise: ScorePartWise): List<Pitch> {
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