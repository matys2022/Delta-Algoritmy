using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class SelectionSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {

        private static double percentageDone = 0; 

        

        public SelectionSort()
        {
            
        }
        public double getPercentage()
        {
            return percentageDone;
        }

        public  List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {
            percentageDone = 0;    

            Console.WriteLine("Selection Sort");

            return runSort(items, token);

        }

        private List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {
            int lastIx = -1;

            double nextPrint = 0;


            for (int i = 0; i < items.Count - 1; i++)
            {

                token?.ThrowIfCancellationRequested();

                if((i * 100 / (double)items.Count) >= nextPrint){
                    percentageDone = i / ((double)items.Count/100);
                    nextPrint += 0.5f;
                }

                int minIndex = lastIx + 1;



                for (int j = lastIx + 1; j < items.Count; j++)
                {
                    SortablePair<T> element = items[j];

                    if (element.subv < items[minIndex].subv)
                    {
                        minIndex = j;
                    }
                }

                SortablePair<T> currentItem = items[lastIx + 1];

                items[lastIx + 1] = items[minIndex];
                
                items[minIndex] = currentItem;

                

                lastIx = i;

            }

            return items;
        }
    }
}