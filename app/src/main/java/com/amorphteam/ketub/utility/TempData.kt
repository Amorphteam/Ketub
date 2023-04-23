package com.amorphteam.ketub.utility

import com.amorphteam.ketub.R
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc

class TempData {
    companion object {
        var bookArray = arrayListOf(
            BookModel(R.drawable.book_sample, 1, "Ejtehad1"),
            BookModel(R.drawable.book_sample, 2, "Ejtehad1"),
            BookModel(R.drawable.book_sample, 3, "Ejtehad2"),
            BookModel(R.drawable.book_sample, 4, "Ejtehad3"),
            BookModel(R.drawable.book_sample, 5, "Ejtehad4")
        )

        var mostRead = arrayListOf(
            MainToc(1, "إشكالية اللغة في الخطاب الديني "),
            MainToc(2, "قاعدة نفي خلوّ الوقائع من الحكم الشرعي وأثرها في علم أصول الفقه ـ القسم الثاني"),
            MainToc(3, "إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة"),
            MainToc(4, "الوَحْي الإلهيّ عند الفارابي"),
            MainToc(5, "إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة"),
            MainToc(6, "الوَحْي الإلهيّ عند الفارابي"),
            MainToc(7, "مباني فهم النصّ / في الفكر الأصوليّ للسيّد محمّد باقر الصدر")
        )


    }
}