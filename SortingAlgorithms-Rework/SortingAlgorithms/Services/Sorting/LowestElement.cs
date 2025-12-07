using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting
{
    public class LowestElement<T> where T : IConvertible, IComparable
    {
        
        public static (int index, SortablePair<T> value) Get(IEnumerable<SortablePair<T>> values)
        {
            // Set the first element as the minimum
            (int index, SortablePair<T> value) minValue = (index: 0, value : new SortablePair<T> (subv: values.First().subv, value: values.First().value));


            for (int i = 0; i < values.Count(); i++)
            {
                SortablePair<T> currentItem = values.ElementAt(i);

                if (currentItem.subv < minValue.value.subv)
                {
                    minValue.index = i;
                    minValue.value = currentItem;
                }
            }

            return minValue;
        }

    }
}