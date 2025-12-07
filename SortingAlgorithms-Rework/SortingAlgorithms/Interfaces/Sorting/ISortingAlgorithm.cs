using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Interfaces.Sorting
{
    public interface ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public IList<SortablePair<T>> Sort(IList<SortablePair<T>> items);
    }
}