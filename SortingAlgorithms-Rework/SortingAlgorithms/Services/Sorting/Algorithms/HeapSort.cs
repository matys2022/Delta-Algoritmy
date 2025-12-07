using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.VisualBasic;
using SortingAlgorithms.Interfaces.Sorting;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class HeapSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {
        public HeapSort()
        {
            
        }

        private int collectionSize = 0;

        public IList<(double subv, T value)> Sort(IList<(double subv, T value)> items)
        {
            IList<(double subv, T value)> collectionCopy = [.. items];
            

            collectionSize = collectionCopy.Count;


            // Sort the tree
            int Ix = collectionSize / 2 - 1; 

            while (Ix >= 0)
            {
                PopHeap(collectionCopy, Ix);
                Ix--;
            }

            int tmpIx = 0;

            while (collectionSize > 1)
            {

                int LeftChildIx = tmpIx * 2 + 1;
                int RightChildIx = tmpIx * 2 + 2;

                // If parent node doesn't exist, or it doesn't have any children, it is not possible to further propagate the node.
                if(tmpIx >= collectionSize || (LeftChildIx >= collectionSize && RightChildIx >= collectionSize))
                {
                    tmpIx = 0;
                }
    
                PopHeap(collectionCopy, tmpIx);

                (double subv, T value) tmp = collectionCopy[0];
                
                collectionCopy[0] = collectionCopy[collectionSize - 1];
                collectionCopy[collectionSize - 1] = tmp;

                collectionSize --;

            }

            return collectionCopy;

        }


        private void PopHeap(IList<(double subv, T value)> collectionCopy, int tmpIx)
        {
            int LeftChildIx = tmpIx * 2 + 1;
            int RightChildIx = tmpIx * 2 + 2;

            if(tmpIx >= collectionSize || LeftChildIx >= collectionSize )
            {
                return;
            }

            int LargestValueIx = tmpIx;
            

            // Find and swap the max heap
            if(RightChildIx < collectionSize && collectionCopy[tmpIx].subv < collectionCopy[RightChildIx].subv && collectionCopy[LeftChildIx].subv < collectionCopy[RightChildIx].subv)
            {
                LargestValueIx = RightChildIx;

            }else if (collectionCopy[tmpIx].subv < collectionCopy[LeftChildIx].subv)
            {
                LargestValueIx = LeftChildIx;
            }
            
            // Do not swap if either children is smaller
            if(LargestValueIx == tmpIx)
            {
                return;
            }

            (double subv, T value) tmp = collectionCopy[tmpIx];
                
            collectionCopy[tmpIx] = collectionCopy[LargestValueIx];
            collectionCopy[LargestValueIx] = tmp;

            PopHeap(collectionCopy, LargestValueIx);


            
            
        }



    }
}