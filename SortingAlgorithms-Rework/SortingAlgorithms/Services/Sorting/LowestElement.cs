using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Services.Sorting
{
    public class LowestElement<T>
    {
        
        public static (int index, (double subv, T value) value) Get(IEnumerable<(double subv, T value)> values)
        {
            // Set the first element as the minimum
            (int index, (double subv, T value) value) minValue = (index: 0, value : (subv: values.First().subv, values: values.First().value));


            for (int i = 0; i < values.Count(); i++)
            {
                (double subv, T value) currentItem = values.ElementAt(i);

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