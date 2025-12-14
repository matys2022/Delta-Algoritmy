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
        public QuickSort()
        {
            
        }

        public List<SortablePair<T>> Sort(List<SortablePair<T>> items)
        {
            List<SortablePair<T>> collectionCopy = items;
            
            Console.WriteLine("Quick Sort");

            int pivotPoints = 0;
            
            Stack<(int ia, int iz)> partitions = new Stack<(int ia, int iz)>([(ia: 0, iz: collectionCopy.Count - 1)]);

            // int ix = (int)Math.Ceiling(((decimal) collectionCopy.Count - 1 )/ 2);

            while(partitions.Count > 0)
            {

                (int ia, int iz) nextBunch = partitions.Peek();
                int ia =  nextBunch.ia ;
                int iz = nextBunch.iz;

                SortablePair<T> pivot = collectionCopy[ia + ((iz - ia) >> 1)];


                int i = ia - 1;
                int j = iz + 1;


                while(true)
                {

                    // Get the bounding elements
                    do { i++; } while (collectionCopy[i].subv < pivot.subv);
                    do { j--; } while (collectionCopy[j].subv > pivot.subv);

                    if (i >= j)
                        break;

                    SortablePair<T> tmp = collectionCopy[i];
                    collectionCopy[i] = collectionCopy[j];
                    collectionCopy[j] = tmp;

                }


                
                    

                
                pivotPoints++;
                partitions.Pop();

                if (ia < j)
                    partitions.Push((ia, j));

                if (j + 1 < iz)
                    partitions.Push((j + 1, iz));

            }

            return collectionCopy;
        }
    }
}