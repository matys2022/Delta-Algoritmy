using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class QuickSort<T> : ISortingAlgorithm<T> where T : IConvertible, ISpanFormattable, IComparable
    {
        public QuickSort()
        {
            
        }

        public IEnumerable<(double subv, T value)> Sort(IEnumerable<(double subv, T value)> items)
        {
            throw new NotImplementedException();
        }
    }
}