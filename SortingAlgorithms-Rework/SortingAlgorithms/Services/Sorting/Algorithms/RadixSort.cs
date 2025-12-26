using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class RadixSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {

        private static double percentageDone = 0; 

        private static double factor;

        public RadixSort()
        {
            
        }
        
        public double getPercentage()
        {
            return percentageDone;
        }
        public List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {
            percentageDone = 0; 
            factor = 0;

            Console.WriteLine("Radix Sort");

            return runSort(items, token);
        }

        private List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {
            
            (int maxLenght, int exp) = GetMax(items);

            factor = 100 / ((double)(maxLenght * items.Count));
                        
            for (int j = maxLenght - 1; j >= 0; j--)
            {

                List<SortablePair<T>>[] sumArr = new List<SortablePair<T>>[20];
            
                for(int i = 0; i < sumArr.Length; i++)
                {
                    sumArr[i] = new List<SortablePair<T>>();
                }


                for (int i = 0; i < items.Count; i++)
                {

                    token?.ThrowIfCancellationRequested();

                    decimal currentItem = decimal.Parse(Math.Abs(items[i].subv * (decimal)Math.Pow(10, exp)).ToString().PadLeft(10, '0').Substring(0, 10)) * (items[i].subv < 0 ? -1 : 1);

                    string subsidary_value = Math.Abs(currentItem)
                    .ToString().Replace(",", string.Empty)
                    .PadLeft(maxLenght, '0');
                    
                    // Add on index defined by the decimal interpretation of the ascii char
                    // To do that subtract 48 to get 0 for zero and so on
                    sumArr[((int)subsidary_value[j]) - 48 + (currentItem < 0 ? 10 : 0)].Add(items[i]);

                    percentageDone += factor;

                }

                items = new List<SortablePair<T>>();
                
                for (int i = 19; i >= 10; i--)
                {
                    items.AddRange(sumArr[i]);
                }
                for (int i = 0; i < 10; i++)
                {
                    items.AddRange(sumArr[i]);
                }
                

            }

            return items;


        }

        private static (int maxLenght, int exp) GetMax(List<SortablePair<T>> items)
        {
            (int maxLenght, int exp) = (0, 0);
            foreach(SortablePair<T> item in items)
            {
                string[] str = Math.Abs(item.subv).ToString().Split(",", StringSplitOptions.RemoveEmptyEntries);



                uint whole = uint.Parse(str[0]);
                int? floating = str.Length == 1 ? null : int.Parse(str[1].PadLeft(9, '0').Substring(0, 9));
                
                int len = (whole + (floating??0).ToString()).Length;
                
                if(len > maxLenght){
                    maxLenght = len;
                }
                
                int lenght = (floating.ToString()??"").Length;

                if(lenght > exp){
                    exp = str[1].Length;
                }
            }
            return (maxLenght, exp);
        }
        
    }
}