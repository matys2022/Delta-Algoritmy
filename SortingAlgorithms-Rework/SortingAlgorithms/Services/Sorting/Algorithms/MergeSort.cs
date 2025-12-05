using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class MergeSort<T> : ISortingAlgorithm<T> where T : IConvertible, ISpanFormattable, IComparable
    {
        public MergeSort()
        {
            
        }

        public IEnumerable<(double subv, T value)> Sort(IEnumerable<(double subv, T value)> items)
        {
            throw new NotImplementedException();
        }
    }
}