package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Articulations(@PropertyElement val staccato: String?)