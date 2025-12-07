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
        public SelectionSort()
        {
            
        }

        public IList<SortablePair<T>> Sort(IList<SortablePair<T>> items)
        {
            
            IList<SortablePair<T>> collectionCopy = [.. items];

            int lastIx = -1;

            for (int i = 0; i < collectionCopy.Count(); i++)
            {
                (int index, SortablePair<T> value) itemWithLowestValue = LowestElement<T>.Get(collectionCopy);

                SortablePair<T> currentItem = collectionCopy[lastIx + 1];

                collectionCopy[itemWithLowestValue.index] = currentItem;

                collectionCopy[lastIx + 1] = new SortablePair<T>(subv: double.MaxValue, value: itemWithLowestValue.value.value);

                lastIx = i;
            }

            return collectionCopy;

        }
    }
}