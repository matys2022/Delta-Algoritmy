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
            
            Stack<(int ia, int iz)> ranges = new Stack<(int ia, int iz)>([(ia: 0, iz: collectionCopy.Count - 1)]);

            Random random = new Random();

            while(ranges.Count > 0)
            {
                // if (pivotPoints >= collectionCopy.Count)
                // {
                //     break;
                // }

                (int ia, int iz) nextBunch = ranges.Peek();
                int ia =  nextBunch.ia;
                int iz = nextBunch.iz;

                // Console.WriteLine($"{((double)pivotPoints) / ((double)collectionCopy.Count / 100)}");
                // if(pivotPoints%10 == 0)
                // Console.Clear();


                
                if(iz - ia >= 1)
                {
                    int randomPivotIndex = random.Next(ia, iz + 1); // .Next is exclusive on upper bound, so +1
        
                    // B. SWAP the chosen pivot to the END (iz) so Lomuto works
                    SortablePair<T> tempP = collectionCopy[randomPivotIndex];
                    collectionCopy[randomPivotIndex] = collectionCopy[iz];
                    collectionCopy[iz] = tempP;
                    
                    SortablePair<T> pivot = collectionCopy[iz];
                    int ix = -1 + ia;


                    for(int i = ia; i <= iz; i++) 
                    {
                        if(pivot.subv >= collectionCopy[i].subv)
                        {
                            ix++;
                            SortablePair<T> tmp = collectionCopy[ix];
                            collectionCopy[ix] = collectionCopy[i];
                            collectionCopy[i] = tmp;
                        }
                    }


                    pivotPoints++;
                    ranges.Pop();


                    if(ix - 1 > ia) // Check if left side has at least 2 elements
                        ranges.Push((ia: ia, iz: ix - 1));


                    if(ix + 1 < iz) // Check if right side has at least 2 elements
                        ranges.Push((ia: ix + 1, iz: iz));

                }

            }

            return collectionCopy;
        }
    }
}