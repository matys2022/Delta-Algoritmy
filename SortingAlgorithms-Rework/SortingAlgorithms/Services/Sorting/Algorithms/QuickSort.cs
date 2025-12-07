using System;
using System.Collections.Generic;
using System.Linq;
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

        public IList<SortablePair<T>> Sort(IList<SortablePair<T>> items)
        {
            IList<SortablePair<T>> collectionCopy = [.. items];

            return Pivot(collectionCopy, 0, collectionCopy.Count - 1);
        }

        private IList<SortablePair<T>> Pivot(IList<SortablePair<T>> collectionCopy, int ia, int iz)
        {
            SortablePair<T> pivot = collectionCopy[iz];

            int ix = ia - 1;
            int iy = iz;


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
            
            if(iz - ia > 1)
            {
                if(ix - 1 > ia)
                Pivot(collectionCopy, ia, ix - 1 ); // Left side to the pivot

                if(iz > ix)
                Pivot(collectionCopy, ix + 1, iz ); // Right side to the pivot
            }

            return collectionCopy;

            
        }
    }
}