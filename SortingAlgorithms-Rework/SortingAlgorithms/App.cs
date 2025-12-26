using System;
using System.Collections.Generic;
using System.Diagnostics.Metrics;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Services.Sorting;
using SortingAlgorithms.Services.Sorting.Algorithms;
using SortingAlgorithms.Services.Testing;

namespace SortingAlgorithms
{
    public class App
    {
        public App()
        {

        }

        public void Run()
        {
            TimeSpan timeSpan = TimeSpan.FromSeconds(3600);

            SortingTester SortingDoubleService = new SortingTester();


            SortingDoubleService.TestAll<int>(Globals.TestingConstants.rando_1M_cela_cisla, null, false, timeSpan);
            SortingDoubleService.TestAll<int>(Globals.TestingConstants.random_integers_10M, null, false, timeSpan);
            
            SortingDoubleService.TestAll<uint>(Globals.TestingConstants.integers_0_to_4294967295, null, false, timeSpan);

            SortingDoubleService.TestAll<decimal>(Globals.TestingConstants.random_10M_interval, null, false, timeSpan);
            
            SortingDoubleService.TestAll<string>(Globals.TestingConstants.random_words_1M, null, false, timeSpan);
            SortingDoubleService.TestAll<string>(Globals.TestingConstants.random_words_10M, null, false, timeSpan);
            

            



        }
    }
}
