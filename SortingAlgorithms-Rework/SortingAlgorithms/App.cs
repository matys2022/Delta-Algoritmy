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

            // Globals.TestingConstants.debug = false;
            // Globals.TestingConstants.debug = true;


            SortingTester SortingDoubleService = new SortingTester();

            // SortingDoubleService.TestAll<int>(Globals.TestingConstants.rando_1M_cela_cisla);
            // SortingDoubleService.TestAll<string>(Globals.TestingConstants.random_words_1M, null, false, TimeSpan.FromSeconds(10));
            // SortingDoubleService.TestAll<string>(null, Globals.TestingConstants.StringSampleLetters);
            // SortingDoubleService.TestAll<int>(null, Globals.TestingConstants.IntSamplePartial);
            // SortingDoubleService.TestAll<double>(Globals.TestingConstants.random_10M_interval);
            

            // SortingDoubleService.TestAll<int>(Globals.TestingConstants.integers_0_to_4294967295, null, false, TimeSpan.FromSeconds(60));
            SortingDoubleService.TestAll<string>(Globals.TestingConstants.random_words_1M, null, false, TimeSpan.FromSeconds(3600));
            // SortingDoubleService.TestAll<double>(Globals.TestingConstants.random_10M_interval, null, false, TimeSpan.FromSeconds(60));
            



        }
    }
}