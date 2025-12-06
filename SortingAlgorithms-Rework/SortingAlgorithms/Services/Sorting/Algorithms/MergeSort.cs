using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class MergeSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public MergeSort()
        {
            
        }

        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items)
        {
            throw new NotImplementedException();
        }
    }
}