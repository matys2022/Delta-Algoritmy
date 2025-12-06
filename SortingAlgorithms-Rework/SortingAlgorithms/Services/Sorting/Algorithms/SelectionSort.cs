using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class SelectionSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public SelectionSort()
        {
            
        }

        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items)
        {
            
            IList<(double subv, T value)> collectionCopy = [.. items];

            int lastIx = -1;

            for (int i = 0; i < collectionCopy.Count(); i++)
            {
                (int index, (double subv, T value) value) itemWithLowestValue = LowestElement<T>.Get(collectionCopy);

                (double subv, T value) currentItem = collectionCopy[lastIx + 1];

                collectionCopy[itemWithLowestValue.index] = currentItem;

                collectionCopy[lastIx + 1] = (subv: double.MaxValue, value: itemWithLowestValue.value.value);

                lastIx = i;
            }

            return collectionCopy;

        }
    }
}