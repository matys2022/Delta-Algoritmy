using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class BubbleSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public BubbleSort()
        {
            
        }

        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items)
        {
            IList<(double subv, T value)> collectionCopy = [.. items];

            int indexCap = 0;
            
            while (true)
            {
                int fieldSize = collectionCopy.Count() - indexCap;

                
                bool sorted = true;

                for(int i = 0; i < fieldSize - 1; i++)
                {
                    if(collectionCopy[i].subv > collectionCopy[i + 1].subv)
                    {
                        (double subv, T value) tmp = collectionCopy[i];
                        
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