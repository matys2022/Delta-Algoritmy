using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class BubbleSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public BubbleSort()
        {
            
        }

        public IList<SortablePair<T>> Sort(IList<SortablePair<T>> items)
        {
            IList<SortablePair<T>> collectionCopy = [.. items];

            int indexCap = 0;
            
            while (true)
            {
                int fieldSize = collectionCopy.Count() - indexCap;

                
                bool sorted = true;

                for(int i = 0; i < fieldSize - 1; i++)
                {
                    if(collectionCopy[i].subv > collectionCopy[i + 1].subv)
                    {
                        SortablePair<T> tmp = collectionCopy[i];
                        
                        collectionCopy[i] = collectionCopy[i + 1];
                        
                        collectionCopy[i+1] = tmp;

                        sorted = false;
                    }
                }

                indexCap++;

                
                // In case of already sorted field, this will end the cycle;
                // Either no change has been made or there is nothing to be moved around anymore;
                
                if(collectionCopy.Count() - indexCap == 0 || sorted)
                {
                    break;
                }
            }

            return collectionCopy;
            
        }
    }
}