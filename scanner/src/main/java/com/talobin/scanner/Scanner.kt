package com.talobin.scanner

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import com.talobin.music.Parser
import com.talobin.scanner.model.FileResult
import com.talobin.scanner.model.ObjectResult
import com.talobin.scanner.model.Progress
import com.talobin.scanner.model.ScanOutput
import io.reactivex.Observable
import uk.co.dolphin_com.rscore.ConvertOptions
import uk.co.dolphin_com.rscore.RScore
import uk.co.dolphin_com.rscore.ex.NoNotesException
import uk.co.dolphin_com.rscore.ex.TooManyStaffsException
import java.io.*
import java.util.*
import java.util.Scanner


/**
 * Singleton class to parse MusicXML
 */
object Scanner {
    private val publicImageFilenameSuffix = "Hai_"
    private val scoresDirectory = "/Camera/Scores"
    private val TAG = Scanner::class.java.simpleName

    init {
        System.loadLibrary("stlport_shared")
        System.loadLibrary("ReadScoreLib")

    }

    /**
     * Scan a music sheet and write result to files. Call getMidiFilePublic and getXmlFilePublic afterwards to get the files.
     * @property filePath the path to the xml file.
     * @return An observable to represent the progress and end result of the scan.
     */
    fun scanBitmapToFile(bm: Bitmap, context: Context): Observable<ScanOutput> {
        return Observable.create { observableEmitter ->

            try {
                val bitmapWidth = bm.getWidth()
                val bitmapHeight = bm.getHeight()
                val pixels = IntArray(bitmapWidth * bitmapHeight)
                bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight())
                val midiFile = getMidiFile(context)
                val xmlFile = getXmlFile(context)
                Log.d(TAG, "File" + xmlFile.path)
                midiFile.delete()
                xmlFile.delete()
                val rscore_opt = ConvertOptions("PlayScore", false, false)
                val theScore = RScore.Convert(
                    pixels,
                    bitmapWidth,
                    bitmapHeight,
                    bm.getWidth(),
                    RScore.Orientation.ROT0,
                    {
                        observableEmitter.onNext(Progress(it.progress_percent))
                        true
                    },
                    midiFile,
                    xmlFile,
                    rscore_opt
                )
                if (midiFile.exists()) {
                    observableEmitter.onNext(Progress(100))

                }

                if (theScore.getNumBars() < 0) {
                    observableEmitter.onError(NoNotesException("Failed! Found no notes!"))
                } else {
                    val resultXMLFile = copyFilesToPublicSpace(
                        context,
                        System.currentTimeMillis().toString()
                    )
                    removeBadLines(resultXMLFile)
                    observableEmitter.onNext(FileResult(resultXMLFile))
                }
            } catch (e: TooManyStaffsException) {
                observableEmitter.onError(e)
            }

            observableEmitter.onComplete()
        }
    }

    /**
     * Scan a music sheet and write object in memory.
     * @property filePath the path to the xml file.
     * @return An observable to represent the progress and end result of the scan.
     */
    fun scanBitmapToMemory(bm: Bitmap, context: Context): Observable<ScanOutput> {
        return return Observable.create { observableEmitter ->

            try {
                val bitmapWidth = bm.getWidth()
                val bitmapHeight = bm.getHeight()
                val pixels = IntArray(bitmapWidth * bitmapHeight)
                bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight())
                val midiFile = getMidiFile(context)
                val xmlFile = getXmlFile(context)
                Log.d(TAG, "File" + xmlFile.path)
                midiFile.delete()
                xmlFile.delete()
                val rscore_opt = ConvertOptions("PlayScore", false, false)
                val theScore = RScore.Convert(
                    pixels,
                    bitmapWidth,
                    bitmapHeight,
                    bm.getWidth(),
                    RScore.Orientation.ROT0,
                    {
                        Log.e(TAG, "Calling onNext" + it.progress_percent)
                        observableEmitter.onNext(Progress(it.progress_percent))
                        true
                    },
                    midiFile,
                    xmlFile,
                    rscore_opt
                )
                if (midiFile.exists()) {
                    Log.e(TAG, "Calling onNext 100!!!")
                    observableEmitter.onNext(Progress(100))

                }

                if (theScore.getNumBars() < 0) {
                    Log.e(TAG, "Calling onError ")
                    observableEmitter.onError(NoNotesException("Failed! Found no notes!"))
                } else {
                    val tempXMLFile = copyFilesToPublicSpace(context, "temp")
                    removeBadLines(tempXMLFile)
                    Log.e(TAG, "XML File: " + tempXMLFile.absolutePath)
                    val result = Parser.parseFile(tempXMLFile)
                    tempXMLFile.delete()
                    if (result != null) {
                        observableEmitter.onNext(ObjectResult(result))
                        val pitches = Parser.extractPitchList(0, result)
                        Log.d(TAG, "Got data$pitches")
                    }

                }
            } catch (e: TooManyStaffsException) {
                Log.e(TAG, "Calling onError ")
                observableEmitter.onError(e)
            }

            observableEmitter.onComplete()
        }
    }


    /**
     * There are 3 extra lines in the file that will make the scanner throw error.
     * We need to remove it to be able to successfully scan the XML file.
     */
    @Throws(IOException::class)
    private fun removeBadLines(file: File) {
        val lines = LinkedList<String>()
        val reader = Scanner(FileInputStream(file), "UTF-8")
        while (reader.hasNextLine())
            lines.add(reader.nextLine())
        reader.close()
        lines.removeAt(1)
        lines.removeAt(1)
        lines.removeAt(1)
        val writer = BufferedWriter(FileWriter(file, false))
        for (line in lines) {
            writer.write(line)
            writer.newLine()
        }

        writer.flush()
        writer.close()
    }

    private fun copyFilesToPublicSpace(context: Context, fileName: String): File {
        val midiFrom = getMidiFile(context)
        val xmlFrom = getXmlFile(context)
//        val midiTo = getMidiFilePublic(fileName)
        val xmlTo = getXmlFilePublic(fileName)
//        copyFile(midiFrom, midiTo)
        copyFile(xmlFrom, xmlTo)
        MediaScannerConnection.scanFile(
            context,
//            arrayOf(midiTo.toString(), xmlTo.toString()),
            arrayOf(xmlTo.toString()),
            null, null
        )
        return xmlTo
    }


    public fun getMidiFile(context: Context): File {
        return File(context.filesDir.toString() + "/currentMidi")
    }

    public fun getXmlFile(context: Context): File {
        return File(context.filesDir.toString() + "/currentXml")
    }

    fun getMidiFilePublic(str: String): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .absolutePath + (scoresDirectory + "/" + publicImageFilenameSuffix + str + ".mid")
        )
    }

    fun getXmlFilePublic(str: String): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .absolutePath + (scoresDirectory + "/" + publicImageFilenameSuffix + str + ".xml")
        )
    }

    @Throws(IOException::class)
    fun copyFile(src: File, dst: File) {
        val inChannel = FileInputStream(src).channel
        val outChannel = FileOutputStream(dst).channel
        try {
            inChannel!!.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel?.close()
            outChannel?.close()
        }
    }


}