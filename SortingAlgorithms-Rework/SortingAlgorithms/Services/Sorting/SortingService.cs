using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting
{
    public class SortingService<T> where T : IConvertible, IComparable
    {
        
        public SortingService()
        {

        }

        public IEnumerable<T> Sort(IEnumerable<T> items, ISortingAlgorithm<T> algorithm) 
        {
            
            List<(double subv, T value)> values = new List<(double subv, T value)>();

            // Double - Maybe decimal?
            if(typeof(T) == typeof(double)){
                
                foreach (T item in items)
                {
                    if(typeof(T) == typeof(double)){
                        values.Add((subv: double.Parse(item.ToString()??""), value :item));
                    }
                }

            }
            
            // Int
            else if(typeof(T) == typeof(int))
            {
                foreach (T item in items)
                {
                    values.Add( (subv: double.Parse(item.ToString()??""), value : item)); 
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
                    double sum = 0;
                    string? parsed = item.ToString();
                    if(parsed != null)
                    {
                        for (int i = 0; i < parsed.Length; i++)
                        {
                            sum += ((double)parsed[i]) / Math.Pow(256, (i+1));

                        }
                    }
                    else
                    {
                        throw new ArgumentNullException();
                    }
                    
                    values.Add((subv: sum, value: item));

                }
            }


            List<(double subv, T value)> sorted_values = algorithm.Sort(values).ToList();    
            
            return sorted_values.Select(x=>x.value);
            
        }

        
    }
}