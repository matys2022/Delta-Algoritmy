using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Services.Sorting;
using SortingAlgorithms.Services.Sorting.Algorithms;

namespace SortingAlgorithms
{
    public class App
    {
        public App()
        {

        }

        public void Run()
        {

            string DoubleSample = @"
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
 
            string IntSample = @"
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
                -466282490
                1749769646
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
            
            // string StringSample = @"strawberry
            //     vinegar
            //     lemon
            //     papaya
            //     ugli
            //     vinegar
            //     nectarine
            //     honeydew
            //     quince
            //     orange
            //     papaya
            //     orange
            //     apple
            //     mango
            //     honeydew
            //     grape
            // ";
            
            string StringSample = @"qj
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




            IEnumerable<double> ParsedDoubleSample = DoubleSample.
            Split(new char[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries).
            Select(x => { 
                if (double.TryParse(x.Trim(), NumberStyles.Float, CultureInfo.InvariantCulture, out double y)) 
                    { return y; } 
                else 
                    { Console.WriteLine(x); throw new Exception("One or more input values are invalid"); } 
                });

            SortingService<double> SortingDoubleService = new SortingService<double>();

            Console.WriteLine("Double Sorted");

            IEnumerable<double> DoubleSampleSorted = SortingDoubleService.Sort(ParsedDoubleSample, new InsertionSort<double>());
            
            double DoublePrev = double.MinValue;

            foreach (double item in DoubleSampleSorted)
            {
                if(DoublePrev <= item)
                {
                    DoublePrev = item;
                }
                else
                {
                    throw new Exception("Not sorted");
                }
                Console.WriteLine($"{item}");
            }
            





            IEnumerable<int> ParsedIntSample = IntSample.
            Split(new char[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries).
            Select(x => { 
                if (int.TryParse(x.Trim(), out int y)) 
                    { return y; } 
                else 
                    { Console.WriteLine(x); throw new Exception("One or more input values are invalid"); } 
                });

            SortingService<int> SortingIntService = new SortingService<int>();
            
            Console.WriteLine("Int Sorted");

            IEnumerable<int> IntSampleSorted = SortingIntService.Sort(ParsedIntSample, new InsertionSort<int>());
            
            int IntPrev = int.MinValue;

            foreach (int item in IntSampleSorted)
            {
                if(IntPrev <= item)
                {
                    IntPrev = item;
                }
                else
                {
                    throw new Exception("Not sorted");
                    
                }
                Console.WriteLine($"{item}");
            }




            IEnumerable<string> ParsedStringSample = StringSample.
            Split(new char[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries).Select(x => x.Trim());

            SortingService<string> SortingStringService = new SortingService<string>();
            
            IEnumerable<string> StringSampleSorted = SortingStringService.Sort(ParsedStringSample, new InsertionSort<string>());

            Console.WriteLine("Strings Sorted");
            
            int maxLenght = 0;
            foreach (string item in StringSampleSorted)
            {
                string strItem = item.ToString()??"";
                if(strItem.Length > maxLenght)
                maxLenght = strItem.Length; 
            }



            double StringPrev = 0;

            foreach (string parsed in StringSampleSorted)
            {
                double StringSum = 0;
                for (int i = 0; i < parsed.Length; i++)
                {
                    StringSum += ((double)parsed[i]) / Math.Pow(256, (i+1));

                }

                if(StringPrev <= StringSum)
                {
                    StringPrev = StringSum;
                }
                else
                {
                    throw new Exception("Not sorted");
                }
                // Console.WriteLine($"{parsed}");
            }

        }
    }
}