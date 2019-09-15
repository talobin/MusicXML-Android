package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Software(
        @TextContent
        val description: String?
)
