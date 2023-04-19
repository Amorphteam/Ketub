package com.amorphteam.ketub.utility

import com.amorphteam.ketub.R
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.library.model.BookModel
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc

class TempData {
    companion object {
        var bookArray = arrayListOf(
            BookModel(1, R.drawable.book_sample, "Ejtehad1"),
            BookModel(2, R.drawable.book_sample, "Ejtehad1"),
            BookModel(3, R.drawable.book_sample, "Ejtehad2"),
            BookModel(4, R.drawable.book_sample, "Ejtehad3"),
            BookModel(5, R.drawable.book_sample, "Ejtehad4")
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

        var bookMarkArray = arrayListOf(
            BookmarkModel(1,"إشكالية اللغة في الخطاب الديني","نصوص معاصرة"),
            BookmarkModel(2,"مباني فهم النصّ / في الفكر الأصوليّ للسيّد محمّد باقر الصدر","الاجتهاد والتجديد"),
            BookmarkModel(3,"قاعدة نفي خلوّ الوقائع من الحكم الشرعي وأثرها في علم أصول الفقه ـ القسم الثاني","نصوص معاصرة"),
            BookmarkModel(4,"إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة","الاجتهاد والتجديد"),
            BookmarkModel(5,"الوَحْي الإلهيّ عند الفارابي","نصوص معاصرة"),
            BookmarkModel(6,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
            BookmarkModel(7,"إشكالية اللغة في الخطاب الديني","نصوص معاصرة"),
            BookmarkModel(8,"الوَحْي الإلهيّ عند الفارابي","الاجتهاد والتجديد"),
            BookmarkModel(9,"إشكالية اللغة في الخطاب الديني","نصوص معاصرة")
        )


    }
}