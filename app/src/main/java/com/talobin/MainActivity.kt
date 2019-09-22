package com.talobin

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.talobin.music.Parser
import com.talobin.scanner.Scanner
import com.talobin.scanner.model.FileResult
import com.talobin.scanner.model.ObjectResult
import com.talobin.scanner.model.Progress
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.dolphin_com.rscore.ex.ConvertFailedException
import uk.co.dolphin_com.rscore.ex.NoNotesException
import uk.co.dolphin_com.rscore.ex.RScoreException
import uk.co.dolphin_com.rscore.ex.TooManyStaffsException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//                Parser.parseTestXML()
        val myDrawable = resources.getDrawable(R.drawable.testsheet)
        val bitmap = (myDrawable as BitmapDrawable).bitmap
        //  Scan to Memory
        disposable = Scanner.scanBitmapToMemory(bitmap, this@MainActivity)
            .subscribeOn(Schedulers.computation())
            .doOnError {
                when (it) {
                    is ConvertFailedException ->
                        Log.e("Hai", "ConvertFailedException" + it)
                    is TooManyStaffsException ->
                        Log.e("Hai", "TooManyStaffsException" + it)
                    is RScoreException ->
                        Log.e("Hai", "RScoreException" + it)
                    is NoNotesException ->
                        Log.e("Hai", "NoNotesException" + it)
                }
            }
            .doOnComplete { Log.d("Hai", "Completed!") }
            .doOnNext {
                when (it) {
                    is Progress ->
                        Log.d("Hai", "ScanOutput:${it.completionPercent}")
                    is ObjectResult -> {
                        val pitches = Parser.extractPitchList(0, it.score)
                        Log.d("Hai", "Got data from Memory$pitches");

                    }
                }
            }
            .subscribe()


        //  Scan to FIle
        disposable = Scanner.scanBitmapToFile(bitmap, this@MainActivity).delay(15, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .doOnError {
                when (it) {
                    is ConvertFailedException ->
                        Log.e("Hai", "ConvertFailedException" + it)
                    is TooManyStaffsException ->
                        Log.e("Hai", "TooManyStaffsException" + it)
                    is RScoreException ->
                        Log.e("Hai", "RScoreException" + it)
                    is NoNotesException ->
                        Log.e("Hai", "NoNotesException" + it)
                }
            }
            .doOnComplete { Log.d("Hai", "Completed!") }
            .doOnNext {
                when (it) {
                    is Progress ->
                        Log.d("Hai", "ScanOutput:${it.completionPercent}")
                    is FileResult -> {
                        val result = Parser.parseFile(it.file)
                        if (result != null) {
                            val pitches = Parser.extractPitchList(0, result)
                            Log.d("Hai", "Got data from File$pitches")
                        }
                        it.file.delete()
                    }
                }
            }.subscribe()


    }


}
