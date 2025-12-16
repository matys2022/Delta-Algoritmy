using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class QuickSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {

        private static double percentageDone = 0; 
        public QuickSort()
        {
            
        }


        public double getPercentage()
        {
            return percentageDone;
        }

        public List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {            

            percentageDone = 0; 

            Console.WriteLine("Quick Sort");

            return runSort(items, token);
        }

        public List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {
            int pivotPoints = 0;
            
            Stack<(int ia, int iz)> partitions = new Stack<(int ia, int iz)>([(ia: 0, iz: items.Count - 1)]);

            while(partitions.Count > 0)
            {

                (int ia, int iz) nextBunch = partitions.Peek();
                int ia =  nextBunch.ia ;
                int iz = nextBunch.iz;

                SortablePair<T> pivot = items[ia + (int)Math.Round((decimal)((iz - ia) / 2))];


                int i = ia - 1;
                int j = iz + 1;


                while(true)
                {

                    token?.ThrowIfCancellationRequested();

                    // Get the bounding indecies
                    do { i++; } while (items[i].subv < pivot.subv);
                    do { j--; } while (items[j].subv > pivot.subv);

                    if (i >= j)
                        break;

                    SortablePair<T> tmp = items[i];
                    items[i] = items[j];
                    items[j] = tmp;


                }
                
                pivotPoints++;
                partitions.Pop();

                if(pivotPoints / ((double)items.Count / 100) > percentageDone)
                {
                    percentageDone = ((double)pivotPoints) / (items.Count / 100);
                }

                if (ia < j)
                    partitions.Push((ia, j));

                if (j + 1 < iz)
                    partitions.Push((j + 1, iz));

            }

            return items;
        }
    }
}