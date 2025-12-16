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

        private static double percentageDone = 0; 

        private static double factor;
        public MergeSort()
        {
            
        }

        public double getPercentage()
        {
            return percentageDone;
        }


        public List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {

            percentageDone = 0; 
            factor = 0;
            Console.WriteLine("Merge Sort");

            int n = (int)Math.Round(Math.Log(items.Count, 2));

            factor = 100d / (items.Count - 1);

            return runSort(items, token);
        }

        public List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {
            return Split(items, 0, items.Count, token);
        }

        private List<SortablePair<T>> Split(List<SortablePair<T>> items, int startIx, int endIx, CancellationToken? token)
        {

            token?.ThrowIfCancellationRequested();

            int range = endIx - startIx;

            if(range <= 1)
                return items;

            int mid = startIx + (int)Math.Round((double)(endIx - startIx) / 2);


            if(mid - startIx > 1)
            {
                Split(items, startIx, mid, token);
            }
            if(endIx - mid > 1)
            {
                Split(items, mid, endIx, token);
            }
            


            return Merge(items, (startIx: startIx, endIx: mid), (startIx: mid, endIx: endIx), token);
            


        }

        private List<SortablePair<T>> Merge(List<SortablePair<T>> items, (int startIx, int endIx) range1, (int startIx, int endIx) range2, CancellationToken? token)
        {
            (int startIx, int endIx, int diff) largest = (range1.startIx, range1.endIx, range1.endIx - range1.startIx);
            (int startIx, int endIx, int diff) smallest = (range2.startIx, range2.endIx, range2.endIx - range2.startIx);

            List<SortablePair<T>> merged = new List<SortablePair<T>>(){};

            if( largest.diff < smallest.diff )
            {
                (largest, smallest) = (smallest, largest);
            }

            int i = largest.startIx;
            int y = smallest.startIx;


            while (i < range1.endIx && y < range2.endIx)
            {
                
                token?.ThrowIfCancellationRequested();

                if((smallest.diff - 1  < y) || (largest.diff - 1  >= i && items[i].subv < items[y].subv))
                {
                    merged.Add(items[i]);
                    i++;
                }
                else
                {
                    merged.Add(items[y]);
                    y++;
                }
                    
            }

            percentageDone += factor;
            

            return merged;
        }

        
    }
}