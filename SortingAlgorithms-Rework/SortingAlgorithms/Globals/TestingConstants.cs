using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Globals
{
    public class TestingConstants
    {
            public static string DoubleSample = @"
                -0.69211
                4.47629
                6.92908
                4.32203
                9.23176
                -8.04610
                -6.70487
                -2.85488
                8.95787
                5.15179
                3.83709
                5.80844
                4.86452
                9.33070
                4.34180
                5.62008
            ";

            
            // Sorted
            // -8,0461
            // -6,70487
            // -2,85488
            // -0,69211
            // 3,83709
            // 4,32203
            // 4,3418
            // 4,47629
            // 4,86452
            // 5,15179
            // 5,62008
            // 5,80844
            // 6,92908
            // 8,95787
            // 9,23176
            // 9,3307
 
            public static string IntSamplePartial = @"
                20
                -29
                43
                -14
                36
                -13
                -13
                64
                11
                -14
                -46
                17
                -17
                53
                19
                -15
            ";
            public static string IntSampleFull = @"
                2015922585
                -296785545
                432769592
                -141982132
                368342189
                -1375308651
                -1378577977
                645670780
                1171295925
                -1454574731
                0
                -466282490
                1749769646
                1
                -1770420192
                532756948
                -1957045239
                -1574246914
            ";



            
            // Sorted
            // -1957045239
            // -1770420192
            // -1574246914
            // -1454574731
            // -1378577977
            // -1375308651
            // -466282490
            // -296785545
            // -141982132
            // 368342189
            // 432769592
            // 532756948
            // 645670780
            // 1171295925
            // 1749769646
            // 2015922585
            
            public static string StringSampleWords = @"strawberry
                vinegar
                lemon
                papaya
                ugli
                vinegar
                nectarine
                honeydew
                quince
                orange
                papaya
                orange
                apple
                mango
                honeydew
                grape
            ";
            
            public static string StringSampleLetters = @"
                qj
                zu
                bz
                cg
                md
                es
                md
                yo
                qy
                av
                dx
                ku
                et
                ox
                fb
                dl
                na
                qu
                kn
                jg
                nk
                qx
                tq
                kx
                xb
                jk
                ep
                qd
                kb
                ul
                rg
                ou
                gc
                hc
                bc
                dw
                gn
                ds
                dg
                np
                qa
                gn
                yq
                qc
                wa
                uv
                it
                df
                sx
            ";
            public static bool debug = false;
            // public static bool debug = false;
            public static string prefix = "../../../";
            public static string integers_0_to_4294967295 = (debug ? prefix : "") + "./source/integers_0_to_4294967295.txt";
            public static string rando_1M_cela_cisla = (debug ? prefix : "") + "./source/rando_1M_cela_cisla.txt";
            public static string random_10M_interval = (debug ? prefix : "") + "./source/random_10M_interval.txt";
            public static string random_integers_10M = (debug ? prefix : "") + "./source/random_integers_10M.txt";
            public static string random_words_1M = (debug ? prefix : "") + "./source/random_words_1M.txt";
            public static string random_words_10M = (debug ? prefix : "") + "./source/random_words_10M.txt";
    }
}