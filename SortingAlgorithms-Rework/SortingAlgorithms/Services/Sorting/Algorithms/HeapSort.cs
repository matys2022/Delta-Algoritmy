using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.VisualBasic;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class HeapSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {

        private static double percentageDone = 0; 

        public HeapSort()
        {
            
        }


        public double getPercentage()
        {
            return percentageDone;
        }

        private int collectionSize = 0;

        public List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {
            percentageDone = 0; 
            Console.WriteLine("Heap Sort");
           

            return runSort(items, token);


        }

        private List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {
            
            collectionSize = items.Count;


            // Build max heap
            int Ix = collectionSize / 2 - 1; 

            double nextPrint = 0;
            while (Ix >= 0)
            {
                PopHeap(items, Ix, token);
                Ix--;
                
                if((collectionSize - Ix) * 100d / collectionSize/2 >= nextPrint){
                    percentageDone = (collectionSize - Ix) / ((double)collectionSize/100) / 2;
                    nextPrint += 0.5d;
                }
            }

            int tmpIx = 0;


            // Heapify the tree for every element
            nextPrint = 0;
            while (collectionSize > 1)
            {
                
                double per = ((items.Count - collectionSize) / ((double)items.Count/100) / 2) + 50;
                
                if(((items.Count - collectionSize) * 100d / items.Count) >= nextPrint){
                    percentageDone = per;
                    nextPrint += 0.5d;
                }

                int leftChildIx = tmpIx * 2 + 1;
                int rightChildIx = tmpIx * 2 + 2;

                // If parent node doesn't exist, or it doesn't have any children, it is not possible to further propagate the node.
                if(tmpIx >= collectionSize || (leftChildIx >= collectionSize && rightChildIx >= collectionSize))
                {
                    tmpIx = 0;
                }
    
                PopHeap(items, tmpIx, token);

                SortablePair<T> tmp = items[0];
                
                items[0] = items[collectionSize - 1];
                items[collectionSize - 1] = tmp;

                collectionSize --;

            }

            return items;
            
        }

        // Heapify
        private void PopHeap(IList<SortablePair<T>> items, int tmpIx, CancellationToken? token)
        {
            
            token?.ThrowIfCancellationRequested();

            int leftChildIx = tmpIx * 2 + 1;
            int rightChildIx = tmpIx * 2 + 2;

            if(tmpIx >= collectionSize || leftChildIx >= collectionSize )
            {
                return;
            }

            int largestValueIx = tmpIx;
            

            // Find and swap the max heap
            if(rightChildIx < collectionSize && items[tmpIx].subv < items[rightChildIx].subv && items[leftChildIx].subv < items[rightChildIx].subv)
            {
                largestValueIx = rightChildIx;

            }else if (items[tmpIx].subv < items[leftChildIx].subv)
            {
                largestValueIx = leftChildIx;
            }
            
            // Do not swap if either children is smaller
            if(largestValueIx == tmpIx)
            {
                return;
            }

            SortablePair<T> tmp = items[tmpIx];
                
            items[tmpIx] = items[largestValueIx];
            items[largestValueIx] = tmp;

            PopHeap(items, largestValueIx, token);

        }
    }
}