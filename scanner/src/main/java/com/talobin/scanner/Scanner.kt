package com.talobin.scanner

import android.content.Context
import android.graphics.Bitmap
import com.talobin.scanner.model.EndResult
import com.talobin.scanner.model.Progress
import com.talobin.scanner.model.ScanOutput
import io.reactivex.Observable
import uk.co.dolphin_com.rscore.ConvertOptions
import uk.co.dolphin_com.rscore.RScore
import uk.co.dolphin_com.rscore.ex.NoNotesException
import uk.co.dolphin_com.rscore.ex.TooManyStaffsException
import java.io.File


/**
 * Singleton class to parse MusicXML
 */
object Scanner {


    init {
        System.loadLibrary("stlport_shared")
        System.loadLibrary("ReadScoreLib")

    }

    /**
     * Scan a music sheet
     * @property filePath the path to the xml file.
     * @return An object that represents MusicXML data. Null if failed.
     */
    fun scanBitmap(bm: Bitmap, context: Context): Observable<ScanOutput> {
        return Observable.create { observableEmitter ->

            try {
                val bitmapWidth = bm.getWidth()
                val bitmapHeight = bm.getHeight()
                val pixels = IntArray(bitmapWidth * bitmapHeight)
                bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight())
                val midiFile = getMidiFileLocal(context)
                val xmlFile = getXmlFileLocal(context)
                midiFile.delete()
                xmlFile.delete()
                val rscore_opt = ConvertOptions("Talobin", false, false)
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
                observableEmitter.onNext(EndResult(theScore))
                if (theScore.getNumBars() < 0) {
                    observableEmitter.onError(NoNotesException("Failed! Found no notes!"))

                }
            } catch (e: TooManyStaffsException) {
                observableEmitter.onError(e)
            }

            observableEmitter.onComplete()
        }
    }


    private fun getMidiFileLocal(context: Context): File {
        return File(context.filesDir.toString() + "/currentMidi")
    }

    private fun getXmlFileLocal(context: Context): File {
        return File(context.filesDir.toString() + "/currentXml")
    }


}