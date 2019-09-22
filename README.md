MusicXML Android Comprehensive Library
========================
Comprehensive ultility library for Android to work with MusicXML.
The library include:

* A **parser** to parse MusicXML file/string to an in-memory Object. (v0.0.1)
* A **scanner** to scan any music sheet and turn it into MusicXML file/string(v0.0.1)
* A **converter** to convert back and forth between MusicXML and Midi(In Progress)
* A **player** to play MusicXML file/string/object and/or Midi file.(In Progress)


### What is MusicXML?
MusicXML is "The standard open format for exchanging digital sheet music".
Basically it is the JSON equivalent for any music data.

Officialy Music XML [website:](https://www.musicxml.com/)

### Setup


##### In your gradle, add the following dependency from `mavenCentral()`:

```
implementation 'com.talobin.musicxml:parser:0.0.1'
```
##### Read file permission:
If you want to parse a file. Add this to your AndroidManifest:
```
 <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 ```
##### Make sure you have the minimum Sdk Version:

1. AndroidManifest.xml:

    ```xml
    <uses-sdk android:minSdkVersion="15" />
    ```

2. Or build.gradle:

    ```
    defaultConfig {
        minSdkVersion 15
        ...
        }
    ```

##### Note: Before you build in release mode, make sure to adjust your proguard configuration by adding the following to `proguard.cnf`:

```
-keep class com.talobin.**
-keepclassmembers class com.talobin.** {
    *;
}
```

### Sample code  (See the app for an example)
####Parse MusicXML from a String 

JAVA

```java
Parser.INSTANCE.parseString("Your string that comforms MusicXML format");
```

KOTLIN

```kotlin
Parser.parseString("Your string that comforms MusicXML format");
```
####Parse MusicXML from a File 

JAVA

```java
Parser.INSTANCE.parseFile("path/to/your/file");
//Or
Parser.INSTANCE.parseFile(theFileItself);

```

KOTLIN

```kotlin
Parser.parseFile("path/to/your/file");
//Or
Parser.INSTANCE.parseFile(theFileItself);
```
####Scan a music sheet image and extract MusicXML

JAVA

```java
disposable = Scanner.INSTANCE.scanBitmap(this.bitmapFull, MainActivity.this)
                                         .subscribeOn(Schedulers.newThread())
                                         .doOnError(new Consumer<Throwable>() {
                                             @Override
                                             public void accept(Throwable throwable) throws Exception {
                                                 if (throwable instanceof ConvertFailedException) {
                                                     Log.e(TAG, "ConvertFailedException"+throwable);
                                                 } else if (throwable instanceof TooManyStaffsException) {
                                                     Log.e(TAG, "TooManyStaffsException"+throwable);
                                                 } else if (throwable instanceof RScoreException) {
                                                     Log.e(TAG, "RScoreException"+throwable);
                                                 } else if (throwable instanceof NoNotesException) {
                                                     Log.e(TAG, "NoNotesException"+throwable);
                                                 }
                                             }
                                         })
                                         .subscribe(new Consumer<ScanOutput>() {
                                             @Override
                                             public void accept(final ScanOutput output) throws Exception {
                                                 runOnUiThread(new Runnable() {
                                                     public void run() {
                                                         if (output instanceof Progress) {
                                                             if (((Progress) output).getCompletionPercent() == 100) {
                                                                 Log.d(TAG, "ScanOutput:" + output);
                                                             }
                                                             Log.d(TAG, "Progress:" + ((Progress) output).getCompletionPercent());
                                                         } else {
                                                             Log.d(TAG, "Score:" + theScore);
                                                         }
                                                     }
                                                 });
                                             }
                                         });

```

KOTLIN

```kotlin
disposable = Scanner.scanBitmap(bitmap, this@MainActivity)
            .subscribeOn(Schedulers.newThread()).doOnError(io.reactivex.functions.Consumer {
                fun accept(throwable: Throwable) {
                    if (throwable is ConvertFailedException) {
                        Log.e(TAG, "ConvertFailedException" + throwable)
                    } else if (throwable is TooManyStaffsException) {
                        Log.e(TAG, "TooManyStaffsException" + throwable)
                    } else if (throwable is RScoreException) {
                        Log.e(TAG, "RScoreException" + throwable)
                    } else if (throwable is NoNotesException) {
                        Log.e(TAG, "NoNotesException" + throwable)

                    }
                }
            }).subscribe(io.reactivex.functions.Consumer {
                fun accept(output: ScanOutput) {
                    runOnUiThread {
                        if (output is Progress) {
                            Log.d(TAG, "ScanOutput:${output.completionPercent}")
                        } else {
                            Log.d(TAG, "Score:$output")
                        }
                    }
                }
            })
```



### Contribution
Open a PR is the way to go :)
### Dependencies
As of Sept/15/2019, the project uses:

* org.jetbrains.kotlin:kotlin-stdlib:1.3.0
* com.tickaroo.tikxml:retrofit-converter:0.8.16-SNAPSHOT
* com.tickaroo.tikxml:annotation:0.8.16-SNAPSHOT
* com.tickaroo.tikxml:core:0.8.16-SNAPSHOT
* com.tickaroo.tikxml:processor:0.8.16-SNAPSHOT
* io.reactivex.rxjava2:rxkotlin:2.4.0
* io.reactivex.rxjava2:rxjava:2.2.12
* androidx.core:core-ktx:1.1.0
