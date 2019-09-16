package com.talobin

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.talobin.musicxml.parser.Parser
import com.talobin.scanner.Scanner
import com.talobin.scanner.model.Progress
import com.talobin.scanner.model.ScanOutput
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.dolphin_com.rscore.ex.ConvertFailedException
import uk.co.dolphin_com.rscore.ex.NoNotesException
import uk.co.dolphin_com.rscore.ex.RScoreException
import uk.co.dolphin_com.rscore.ex.TooManyStaffsException


class MainActivity : AppCompatActivity() {
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Parser.parseTestXML()
        val myDrawable = resources.getDrawable(R.drawable.testsheet)
        val bitmap = (myDrawable as BitmapDrawable).bitmap
        disposable = Scanner.scanBitmap(bitmap, this@MainActivity)
            .subscribeOn(Schedulers.newThread()).doOnError(io.reactivex.functions.Consumer {
                fun accept(throwable: Throwable) {
                    if (throwable is ConvertFailedException) {
                        Log.e("Hai", "ConvertFailedException" + throwable)
                    } else if (throwable is TooManyStaffsException) {
                        Log.e("Hai", "TooManyStaffsException" + throwable)
                    } else if (throwable is RScoreException) {
                        Log.e("Hai", "RScoreException" + throwable)
                    } else if (throwable is NoNotesException) {
                        Log.e("Hai", "NoNotesException" + throwable)

                    }
                }
            }).subscribe(io.reactivex.functions.Consumer {
                fun accept(output: ScanOutput) {
                    runOnUiThread {
                        if (output is Progress) {
                            Log.d("Hai", "ScanOutput:${output.completionPercent}")
                        } else {
                            Log.d("Hai", "Score:$output")
                        }
                    }
                }
            })


    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
