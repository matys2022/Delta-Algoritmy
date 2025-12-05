using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting
{
    public class SortingService<T> where T : IConvertible, ISpanFormattable, IComparable
    {
        
        public SortingService()
        {
            int ff;
        }

        public IEnumerable<T> Sort(IEnumerable<T> items, ISortingAlgorithm<T> algorithm) 
        {
            
            List<(double subv, T value)> values = new List<(double subv, T value)>();

            if(typeof(T) != typeof(double)){
                foreach (T item in items)
                {
                    double sum = 0;
                    string? parsed = item.ToString();
                    if(parsed != null)
                    {
                        
                        foreach (char c in parsed)
                        {
                            sum += (double)c;
                            
                        }
                    }
                    else
                    {
                        throw new ArgumentNullException();
                    }
                    
                    values.Add((subv: sum, value: item));

                }

            }
            else
            {
                foreach (T item in items)
                {
                    if(typeof(T) == typeof(double)){
                        values.Add((subv: double.Parse(item.ToString()??""), value: item));
                    }
                }
            }


            List<(double subv, T value)> sorted_values = algorithm.Sort(values).ToList();    
            
            return sorted_values.Select(x=>x.value);
            
        }

        
    }
}