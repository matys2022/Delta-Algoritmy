using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting
{
    public class SortingService<T> where T : IConvertible, IComparable
    {
        
        public SortingService()
        {

        }

        public IEnumerable<T> Sort(IEnumerable<T> items, ISortingAlgorithm<T> algorithm) 
        {
            
            List<SortablePair<T>> values = new List<SortablePair<T>>();

            // Double - Maybe decimal?
            if(typeof(T) == typeof(double)){
                
                foreach (T item in items)
                {
                    if(typeof(T) == typeof(double)){
                        values.Add(new SortablePair<T>(subv: double.Parse(item.ToString()??""), value :item));
                    }
                }

            }
            
            // Int
            else if(typeof(T) == typeof(int))
            {
                foreach (T item in items)
                {
                    values.Add( new SortablePair<T>(subv: double.Parse(item.ToString()??""), value : item)); 
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
                    
                    values.Add(new SortablePair<T>(subv: sum, value: item));

                }
            }


            List<SortablePair<T>> sorted_values = algorithm.Sort(values).ToList();    
            
            return sorted_values.Select(x=>x.value);
            
        }

        
    }
}