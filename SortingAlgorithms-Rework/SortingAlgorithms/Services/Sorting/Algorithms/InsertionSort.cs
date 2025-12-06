using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class InsertionSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public InsertionSort()
        {
            
        }

        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items)
        {
            IList<(double subv, T value)> collectionCopy = [.. items];

            for(int i = 0; i < collectionCopy.Count() - 1; i++)
            {
                int tmpIx = i;
                (double subv, T value) previousItem = collectionCopy[i];

                while (true)
                {
                    if(previousItem.subv > collectionCopy[tmpIx + 1].subv)
                    {

                        collectionCopy[tmpIx] = collectionCopy[tmpIx + 1];
                        collectionCopy[tmpIx + 1] = previousItem;

                        if(tmpIx-1 >= 0)
                            {previousItem = collectionCopy[tmpIx-1];}
                        else
                            {break;}

                        tmpIx--;
                    }
                    else
                    {
                        break;
                    }
                }
            }

            return collectionCopy;

        }
    }
}