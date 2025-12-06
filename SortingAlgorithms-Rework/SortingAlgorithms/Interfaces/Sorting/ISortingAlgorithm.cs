using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Interfaces.Sorting
{
    public interface ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items);
    }
}