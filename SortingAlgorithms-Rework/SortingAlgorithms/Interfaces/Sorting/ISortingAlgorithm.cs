using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Interfaces.Sorting
{
    public interface ISortingAlgorithm<T> where T : IConvertible, ISpanFormattable, IComparable
    {
        public IEnumerable<(double subv, T value)> Sort(IEnumerable<(double subv, T value)> items);
    }
}