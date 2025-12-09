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
        public RadixSort()
        {
            
        }

        public IList<SortablePair<T>> Sort(IList<SortablePair<T>> items)
        {
            List<SortablePair<T>> collectionCopy = new List<SortablePair<T>>([.. items]);


            return CountingSort(collectionCopy);
        }

        private List<SortablePair<T>> CountingSort(List<SortablePair<T>> items)
        {
            List<SortablePair<T>> collectionOutput = new List<SortablePair<T>>(items);
            
            int maxLenght = GetMax(collectionOutput);
            
            for (int j = maxLenght - 1; j >= 0; j--)
            {

                List<SortablePair<T>>[] sumArr = new List<SortablePair<T>>[20];
            
                for(int i = 0; i < sumArr.Length; i++)
                {
                    sumArr[i] = new List<SortablePair<T>>();
                }


                for (int i = 0; i < collectionOutput.Count; i++)
                {
                    double currentItem = collectionOutput[i].Subv;

                    string subsidary_value = Math.Abs(currentItem)
                    .ToString().Replace(",", String.Empty)
                    .PadLeft(maxLenght, '0');
                    
                    // Add on index defined by the decimal interpretation of the ascii char
                    // To do that subtract 48 to get 0 for zero and so on
                    sumArr[((int)subsidary_value[j]) - 48 + (currentItem < 0 ? 10 : 0) ].Add(collectionOutput[i]);
                }

                collectionOutput = new List<SortablePair<T>>();
                
                for (int i = 19; i >= 10; i--)
                {
                    collectionOutput.AddRange(sumArr[i]);
                }
                for (int i = 0; i < 10; i++)
                {
                    collectionOutput.AddRange(sumArr[i]);
                }
                

            }


            return collectionOutput;
        }

        private static int GetMax(List<SortablePair<T>> items)
        {
            int max = 0;
            foreach(SortablePair<T> item in items)
            {
                int len = Math.Abs(item.Subv).ToString().Length;
                if(len > max)
                max = len;
            }
            return max;
        }
        
    }
}