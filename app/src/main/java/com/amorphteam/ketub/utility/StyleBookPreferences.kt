package com.amorphteam.ketub.utility

import com.amorphteam.ketub.model.BkColor
import com.amorphteam.ketub.model.FontName
import com.amorphteam.ketub.model.FontSize
import com.amorphteam.ketub.model.LineSpace

class StyleBookPreferences {
     var fontName: FontName = FontName.FONT1
     var lastSeenIndex = 0
     var fontSize: FontSize = FontSize.SIZE1
     var lineSpace: LineSpace = LineSpace.SPACE1
     var readerColor: BkColor = BkColor.NONE
     var lastSeenPercent = 0f
}