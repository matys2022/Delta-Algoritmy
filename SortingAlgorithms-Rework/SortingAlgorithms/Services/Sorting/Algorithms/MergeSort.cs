using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class MergeSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public MergeSort()
        {
            
        }

        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items)
        {
            IList<(double subv, T value)> collectionCopy = [.. items];
            
            return Split(collectionCopy);
        }

        public IList<(double subv, T value)> Split(IList<(double subv, T value)> collectionCopy)
        {
            if(collectionCopy.Count == 1)
                return collectionCopy;

            // Source - https://stackoverflow.com/a/10700594
            // Posted by Saeed Amiri, modified by community. See post 'Timeline' for change history
            // Retrieved 2025-12-07, License - CC BY-SA 3.0

            IList<(double subv, T value)> firstArray = collectionCopy.Take(collectionCopy.Count / 2).ToArray();
            IList<(double subv, T value)> secondArray = collectionCopy.Skip(collectionCopy.Count / 2).ToArray();

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

        public IList<(double subv, T value)> Merge(IList<(double subv, T value)> list1, IList<(double subv, T value)> list2)
        {
            IList<(double subv, T value)> largest = list1;
            IList<(double subv, T value)> smallest = list2;

            IList<(double subv, T value)> merged = new List<(double subv, T value)>(){};

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