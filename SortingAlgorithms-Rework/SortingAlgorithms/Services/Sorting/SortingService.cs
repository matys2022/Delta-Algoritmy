using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting
{
    public class SortingService<T>
    {
        
        public SortingService()
        {
            
        }

        public IEnumerable<T> Sort(IEnumerable<T> items, ISortingAlgorithm algorithm)
        {
            throw new NotImplementedException();
        }

        
    }
}