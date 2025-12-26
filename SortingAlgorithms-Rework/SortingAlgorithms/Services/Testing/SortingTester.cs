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

            Console.WriteLine();
            Console.WriteLine(new string('–', 10));
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

                SortData<T>(loader, algorithm, path_to_file, data, log, timeout);
                
            }
        }
        

        public void SortData<T>(FileLoader loader, Func<ISortingAlgorithm<T>> algorithm, string? path_to_file = null, string? data = null,  bool log = false, TimeSpan? timeout = null) where T : IComparable, IConvertible
        {
            IEnumerable<T> ParsedSample;

            if(typeof(T) == typeof(long))
            {
                ParsedSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (long.TryParse(x.Trim(), out long y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine($"{x} : One or more input values are invalid"); throw new Exception("One or more input values are invalid"); }  
                    });
                
                sortInt(ParsedSample, algorithm, timeout);
            } else if(typeof(T) == typeof(decimal))
            {
                ParsedSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (decimal.TryParse(x.Trim(), NumberStyles.Float, CultureInfo.InvariantCulture, out decimal y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine($"{x} : One or more input values are invalid"); throw new Exception("One or more input values are invalid"); } 
                    });
                
                sortDecimal(ParsedSample, algorithm, timeout);
            } else if(typeof(T) == typeof(uint))
            {
                ParsedSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (uint.TryParse(x.Trim(), out uint y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine($"{x} : One or more input values are invalid"); throw new Exception("One or more input values are invalid"); }  
                    });
                
                sortInt(ParsedSample, algorithm, timeout);
            } else if(typeof(T) == typeof(int))
            {
                ParsedSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (int.TryParse(x.Trim(), out int y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine($"{x} : One or more input values are invalid"); throw new Exception("One or more input values are invalid"); }  
                    });
                
                sortInt(ParsedSample, algorithm, timeout);
            } else if(typeof(T) == typeof(string))
            {
                ParsedSample = loader.load(path_to_file, data).Select(x => (T)(object)x.Trim());

                sortString(ParsedSample, algorithm, timeout);
                
            } else if (typeof(T) == typeof( double))
            {
                ParsedSample = loader.load(path_to_file, data).
                Select(x => { 
                    if (double.TryParse(x.Trim(), NumberStyles.Float, CultureInfo.InvariantCulture, out double y)) 
                        { return (T)(object)y; } 
                    else 
                        { Console.WriteLine($"{x} : One or more input values are invalid"); throw new Exception("One or more input values are invalid"); }  
                    });

                sortDouble(ParsedSample, algorithm, timeout);


            } else
                throw new Exception("Unknown object type.");
        }

        public void sortInt<D>(IEnumerable<D> ParsedIntSample, Func<ISortingAlgorithm<D>> algorithm, TimeSpan? timeout) where D : IConvertible, IComparable
        {
            IEnumerable<D> IntSampleSorted = sortingService.Sort(ParsedIntSample, algorithm, timeout);
            if(timeout == null)
            {
                long IntPrev = long.MinValue;

                foreach (D item in IntSampleSorted)
                {
                    if(typeof(long) == typeof(D))
                    {
                        if(IntPrev <= (long)(object)item)
                        {
                            IntPrev = (long)(object)item;
                        }
                        else
                        {
                            // throw new Exception("Not sorted");
                            
                        }
                        // Console.WriteLine($"{item}");
                    }
                    
                }
            }
        }



        public void sortDouble<D>(IEnumerable<D> ParsedDoubleSample, Func<ISortingAlgorithm<D>> algorithm, TimeSpan? timeout) where D : IConvertible, IComparable
        {

            IEnumerable<D> DoubleSampleSorted = sortingService.Sort(ParsedDoubleSample, algorithm, timeout);
            
            double DoublePrev = double.MinValue;
            
            if(timeout == null)
            {
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
        }
        
        public void sortDecimal<D>(IEnumerable<D> ParsedDecimalSample, Func<ISortingAlgorithm<D>> algorithm, TimeSpan? timeout) where D : IConvertible, IComparable
        {

            IEnumerable<D> DecimalSampleSorted = sortingService.Sort(ParsedDecimalSample, algorithm, timeout);
            
            if(timeout == null)
            {
                decimal DecimalPrev = decimal.MinValue;

                foreach (D item in DecimalSampleSorted)
                {
                    if(DecimalPrev <= (decimal)(object)item)
                    {
                        DecimalPrev = (decimal)(object)item;
                    }
                    else
                    {
                        throw new Exception("Not sorted");
                    }
                    // Console.WriteLine($"{item}");
                }    
            }
            
        }

        public void sortString<D>(IEnumerable<D> ParsedStringSample, Func<ISortingAlgorithm<D>> algorithm, TimeSpan? timeout) where D : IConvertible, IComparable
        {
            
            IEnumerable<D> StringSampleSorted = sortingService.Sort<D>(ParsedStringSample, algorithm, timeout);
            if(timeout == null)
            {
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
}
