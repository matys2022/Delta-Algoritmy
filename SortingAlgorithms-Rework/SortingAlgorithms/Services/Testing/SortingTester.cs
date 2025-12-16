using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Factories;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Services.Loading;
using SortingAlgorithms.Services.Sorting;
using SortingAlgorithms.Services.Sorting.Algorithms;

namespace SortingAlgorithms.Services.Testing
{
    public class SortingTester
    {

        public SortingService sortingService;
        public SortingTester()
        {
            this.sortingService = new SortingService();
        }

        public void TestAll<T>(string? path_to_file = null, string? data = null,  bool log = false, TimeSpan? timeout = null) where T : IComparable, IConvertible
        {

            Console.WriteLine($"Dataset {path_to_file}");
            Console.WriteLine(new string('–', 10));
            
            SortAlgorithmFactory<T> algorithmFactory = new SortAlgorithmFactory<T>();

            List<Func<ISortingAlgorithm<T>>> algorithms = new List<Func<ISortingAlgorithm<T>>>(){
                algorithmFactory.CreateBubbleSort,
                algorithmFactory.CreateHeapSort,
                algorithmFactory.CreateInsertionSort,
                algorithmFactory.CreateMergeSort,
                algorithmFactory.CreateQuickSort,
                algorithmFactory.CreateRadixSort,
                algorithmFactory.CreateSelectionSort,
            };



            FileLoader loader = new FileLoader();
            

            foreach(Func<ISortingAlgorithm<T>> algorithm in algorithms)
            {
                Console.WriteLine();
                var sw = Stopwatch.StartNew();

                if(timeout != null)
                {
                    using var cts = new CancellationTokenSource((TimeSpan)timeout);

                    {
                        SortData<T>(loader, algorithm, path_to_file, data, log, cts.Token);
                    }
                    
                    if (cts.Token.IsCancellationRequested == true)
                    {
                        Console.WriteLine("Operation timed out.");
                        Console.WriteLine($"{algorithm().getPercentage()}% Finished");
                    }
                    else
                    {
                        Console.WriteLine($"Sort took {sw.Elapsed.TotalMilliseconds:F3} ms");
                    }
                }
                else
                {
                    SortData<T>(loader, algorithm, path_to_file, data, log);
                }
                sw.Stop();
            }
        }
        

        public async Task SortData<T>(FileLoader loader, Func<ISortingAlgorithm<T>> algorithm, string? path_to_file = null, string? data = null,  bool log = false, CancellationToken? token = null) where T : IComparable, IConvertible
        {
            if(typeof(T) == typeof(int))
            {
                IEnumerable<T> ParsedIntSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (int.TryParse(x.Trim(), out int y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine(x); throw new Exception("One or more input values are invalid"); } 
                    });
                
                await sortInt(ParsedIntSample, algorithm, token);
            } else if(typeof(T) == typeof(string))
            {
                IEnumerable<T> ParsedStringSample = loader.load(path_to_file, data).Select(x => (T)(object)x.Trim());

                await sortString(ParsedStringSample, algorithm, token);
                
            } else if (typeof(T) ==typeof( double))
            {
                IEnumerable<T> ParsedDoubleSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (double.TryParse(x.Trim(), NumberStyles.Float, CultureInfo.InvariantCulture, out double y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine(x); throw new Exception("One or more input values are invalid"); } 
                    });

                await sortDouble(ParsedDoubleSample, algorithm, token);


            } else
                throw new Exception("Unknown object type.");
        }

        public async Task sortInt<D>(IEnumerable<D> ParsedIntSample, Func<ISortingAlgorithm<D>> algorithm, CancellationToken? token) where D : IConvertible, IComparable
        {
            Console.WriteLine("Int Sorted");

            IEnumerable<D> IntSampleSorted = await sortingService.Sort(ParsedIntSample, algorithm, token);
            
            int IntPrev = int.MinValue;

            foreach (D item in IntSampleSorted)
            {
                if(typeof(int) == typeof(D))
                {
                    if(IntPrev <= (int)(object)item)
                    {
                        IntPrev = (int)(object)item;
                    }
                    else
                    {
                        // throw new Exception("Not sorted");
                        
                    }
                    // Console.WriteLine($"{item}");
                }
                
            }
        }



        public async Task sortDouble<D>(IEnumerable<D> ParsedDoubleSample, Func<ISortingAlgorithm<D>> algorithm, CancellationToken? token) where D : IConvertible, IComparable
        {

            Console.WriteLine("Double Sorted");

            IEnumerable<D> DoubleSampleSorted = await sortingService.Sort(ParsedDoubleSample, algorithm, token);
            
            double DoublePrev = double.MinValue;

            foreach (D item in DoubleSampleSorted)
            {
                if(DoublePrev <= (double)(object)item)
                {
                    DoublePrev = (double)(object)item;
                }
                else
                {
                    throw new Exception("Not sorted");
                }
                // Console.WriteLine($"{item}");
            }
        }
        

        public async Task sortString<D>(IEnumerable<D> ParsedStringSample, Func<ISortingAlgorithm<D>> algorithm, CancellationToken? token) where D : IConvertible, IComparable
        {

            Console.WriteLine("Strings Sorted");
            
            IEnumerable<D> StringSampleSorted = await sortingService.Sort<D>(ParsedStringSample, algorithm, token);

            int maxLenght = 0;
            foreach (D item in StringSampleSorted)
            {
                string strItem = item.ToString()??"";
                if(strItem.Length > maxLenght)
                maxLenght = strItem.Length; 
            }



            double StringPrev = 0;

            foreach (D parsed in StringSampleSorted)
            {
                double StringSum = 0;
                for (int i = 0; i < ((string)(object)parsed).Length; i++)
                {
                    StringSum += ((string)(object)parsed)[i] / Math.Pow(256, i+1);

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
