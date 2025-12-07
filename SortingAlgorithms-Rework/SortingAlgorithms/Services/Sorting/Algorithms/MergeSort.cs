using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class MergeSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public MergeSort()
        {
            
        }

        public IList<SortablePair<T>> Sort(IList<SortablePair<T>> items)
        {
            IList<SortablePair<T>> collectionCopy = [.. items];
            
            return Split(collectionCopy);
        }

        private IList<SortablePair<T>> Split(IList<SortablePair<T>> collectionCopy)
        {
            if(collectionCopy.Count == 1)
                return collectionCopy;

            // Source - https://stackoverflow.com/a/10700594
            // Posted by Saeed Amiri, modified by community. See post 'Timeline' for change history
            // Retrieved 2025-12-07, License - CC BY-SA 3.0

            IList<SortablePair<T>> firstArray = collectionCopy.Take(collectionCopy.Count / 2).ToArray();
            IList<SortablePair<T>> secondArray = collectionCopy.Skip(collectionCopy.Count / 2).ToArray();

            if(firstArray.Count > 1)
            {
                firstArray = Split(firstArray);
            }
            if(secondArray.Count > 1)
            {
                secondArray = Split(secondArray);
            }
            

            return Merge(firstArray, secondArray);



        }

        private IList<SortablePair<T>> Merge(IList<SortablePair<T>> list1, IList<SortablePair<T>> list2)
        {
            IList<SortablePair<T>> largest = list1;
            IList<SortablePair<T>> smallest = list2;

            IList<SortablePair<T>> merged = new List<SortablePair<T>>(){};

            if(list1.Count < list2.Count)
            {
                largest = list2;
                smallest = list1;
            }

            
            int i = 0;
            int y = 0;
            
            while(i+y < largest.Count + smallest.Count)
            {

                if((smallest.Count - 1  < y) || (largest.Count - 1  >= i && largest[i].subv < smallest[y].subv))
                {
                    merged.Add(largest[i]);
                    i++;
                }
                else
                {
                    merged.Add(smallest[y]);
                    y++;
                }
                    
            }

            return merged;
        }

        
    }
}