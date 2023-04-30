package com.amorphteam.ketub.utility

import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexFirstChildItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexGroupItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexSecondChildItem
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc
import com.amorphteam.ketub.ui.search.model.SearchModel

class TempData {
    companion object {

        var mostRead = arrayListOf(
            MainToc(1, "إشكالية اللغة في الخطاب الديني "),
            MainToc(2, "قاعدة نفي خلوّ الوقائع من الحكم الشرعي وأثرها في علم أصول الفقه ـ القسم الثاني"),
            MainToc(3, "إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة"),
            MainToc(4, "الوَحْي الإلهيّ عند الفارابي"),
            MainToc(5, "إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة"),
            MainToc(6, "الوَحْي الإلهيّ عند الفارابي"),
            MainToc(7, "مباني فهم النصّ / في الفكر الأصوليّ للسيّد محمّد باقر الصدر")
        )

        var searchResult = arrayListOf(
            SearchModel(1,29, "نصوص معاصرة",  "إشكالية اللغة في الخطاب الديني "),
            SearchModel(2, 13,"الاجتهاد والتجديد","قاعدة نفي خلوّ الوقائع من الحكم الشرعي وأثرها في علم أصول الفقه ـ القسم الثاني"),
            SearchModel(3, 43,"نصوص معاصرة","إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة"),
            SearchModel(4,75,"الاجتهاد والتجديد", "الوَحْي الإلهيّ عند الفارابي"),
            SearchModel(5,66,"نصوص معاصرة", "إله العرفان وإله الفلسفة / اختلافاتٌ جوهريّة"),
            SearchModel(6,103,"الاجتهاد والتجديد", "الوَحْي الإلهيّ عند الفارابي"),
            SearchModel(7,547,"نصوص معاصرة", "مباني فهم النصّ / في الفكر الأصوليّ للسيّد محمّد باقر الصدر")
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

        val indexItems = listOf(
            IndexGroupItem(
                1,"إشكالية اللغة في الخطاب الديني","نصوص معاصرة",
                listOf(
                    IndexFirstChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد", listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    )),
                    IndexFirstChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    )),
                    IndexFirstChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    ))
                )
            ),
            IndexGroupItem(
                2,"مباني فهم النصّ / في الفكر الأصوليّ للسيّد محمّد باقر الصدر","الاجتهاد والتجديد",
                listOf(
                    IndexFirstChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    )),
                    IndexFirstChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    ))
                )
            ),
            IndexGroupItem(
                3,"قاعدة نفي خلوّ الوقائع من الحكم الشرعي وأثرها في علم أصول الفقه ـ القسم الثاني","نصوص معاصرة",
                listOf(
                    IndexFirstChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    )),
                    IndexFirstChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    )),
                    IndexFirstChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    )),
                    IndexFirstChildItem(4,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد",listOf(
                        IndexSecondChildItem(1,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(2,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد"),
                        IndexSecondChildItem(3,"إشكالية اللغة في الخطاب الديني","الاجتهاد والتجديد")
                    ))
                )
            )
        )
    }
}