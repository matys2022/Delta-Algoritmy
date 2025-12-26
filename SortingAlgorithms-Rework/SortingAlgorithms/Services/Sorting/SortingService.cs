using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Factories;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting
{
    public class SortingService
    {
        
        public SortingService()
        {

        }

        public IEnumerable<T> Sort<T>(IEnumerable<T> items, Func<ISortingAlgorithm<T>> algorithmFactory, TimeSpan? timeout)  where T : IConvertible, IComparable 
        {
            

            ISortingAlgorithm<T> sortingAlgorithm = algorithmFactory();

            List<SortablePair<T>> values = new List<SortablePair<T>>();

            // Double - Maybe decimal?
            if(typeof(T) == typeof(decimal)){
                
                foreach (T item in items)
                {
                    if(typeof(T) == typeof(decimal)){
                        values.Add(new SortablePair<T>(subv: decimal.Parse(item.ToString()??""), value :item));
                    }
                }

            }
            
            // Long / Int / UInt
            else if(typeof(T) == typeof(long) || typeof(T) == typeof(uint) || typeof(T) == typeof(int))
            {
                foreach (T item in items)
                {
                    values.Add( new SortablePair<T>(subv: decimal.Parse(item.ToString()??""), value : item)); 
                }
            }
            
            // Strings
            else
            {
                int maxLenght = 0;
                foreach (T item in items)
                {
                    string strItem = item.ToString()??"";
                    if(strItem.Length > maxLenght)
                    maxLenght = strItem.Length; 
                }

                foreach (T item in items)
                {
                    decimal sum = 0;
                    string? parsed = item.ToString();
                    if(parsed != null)
                    {
                        for (int i = 0; i < parsed.Length; i++)
                        {
                            sum += parsed[i] / (decimal)Math.Pow(256, (i+1));
                        }
                    }
                    else
                    {
                        throw new ArgumentNullException();
                    }
                    
                    values.Add(new SortablePair<T>(subv: sum, value: item));

                }
            }

            List<SortablePair<T>> sorted_values = new List<SortablePair<T>>();

            var sw = Stopwatch.StartNew();

            if(timeout != null)
            {
                using var cts = new CancellationTokenSource((TimeSpan)timeout);
                try
                {
                    sorted_values = sortingAlgorithm.Sort(values, cts.Token).ToList();    
                }catch(Exception e)
                {
                    Console.WriteLine(e.Message);
                }
                
                if (cts.Token.IsCancellationRequested == true)
                {
                    Console.WriteLine("Operation timed out.");
                    Console.WriteLine($"{sortingAlgorithm.getPercentage()}% Finished");
                }
                else
                {
                    Console.WriteLine($"Sort took {sw.Elapsed.TotalMilliseconds:F3} ms");
                }
            }
            else
            {
                sorted_values = sortingAlgorithm.Sort(values, null).ToList();
            }                    
            
            
            sw.Stop();

            return sorted_values.Select(x=>x.value);
            
        }

        
    }
}