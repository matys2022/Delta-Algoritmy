using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Models.Tuples
{
    public struct SortablePair<T> where T : IConvertible, IComparable
    {
        public decimal subv;
        public T value;
        public SortablePair(decimal subv, T value)
        {
            this.subv = subv;
            this.value = value;
        }
    }
}