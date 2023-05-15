package com.amorphteam.ketub.utility

import com.amorphteam.ketub.model.*

class StyleBookPreferences {
     var fontName: FontName = FontName.FONT0
     var lastSeenIndex = 0
     var theme: Theme = Theme.BASE
     var quickStyle: QuickStyle = QuickStyle.DEFAULT
     var fontSize: FontSize = FontSize.SIZE1
     var lineSpace: LineSpace = LineSpace.SPACE1
     var readerColor: BkColor = BkColor.NONE
     var lastSeenPercent = 0f

     fun getClasses(): String? {
          return fontSize.name + " " +
                  fontName.name + " " +
                  lineSpace.name + " " +
                  readerColor.name + " " +
                  theme.name
     }
}