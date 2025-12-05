using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class InsertionSort<T> : ISortingAlgorithm<T> where T : IConvertible, ISpanFormattable, IComparable
    {
        public InsertionSort()
        {
            
        }

        public IEnumerable<(double subv, T value)> Sort(IEnumerable<(double subv, T value)> items)
        {
            throw new NotImplementedException();
        }
    }
}