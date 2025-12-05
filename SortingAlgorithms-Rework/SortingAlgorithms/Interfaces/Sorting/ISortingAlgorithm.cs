using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Interfaces.Sorting
{
    public interface ISortingAlgorithm 
    {
        public IEnumerable<double> Sort(IEnumerable<double> items);
    }
}