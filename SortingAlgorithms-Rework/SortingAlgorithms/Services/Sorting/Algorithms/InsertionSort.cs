using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class InsertionSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {

        private static double percentageDone = 0; 
        public InsertionSort()
        {
            
        }


        public double getPercentage()
        {
            return percentageDone;
        }

        public List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {
            percentageDone = 0; 
            Console.WriteLine("Insertion Sort");

            return runSort(items, token);
        }

        private List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {

            for(int i = 0; i < items.Count() - 1; i++)
            {
                int tmpIx = i;
                SortablePair<T> previousItem = items[i];

                // if((i * 100d / items.Count) >= nextPrint){
                percentageDone = i / ((double)items.Count/100);
                    // nextPrint += 0.5d;
                // }

                while (true)
                {
                     token?.ThrowIfCancellationRequested();
                    
                    if(previousItem.subv > items[tmpIx + 1].subv)
                    {

                        items[tmpIx] = items[tmpIx + 1];
                        items[tmpIx + 1] = previousItem;

                        if(tmpIx-1 >= 0)
                            {previousItem = items[tmpIx-1];}
                        else
                            {break;}

                        tmpIx--;
                    }
                    else
                    {
                        break;
                    }
                    
                }
            }

            return items;
        }
    }
}