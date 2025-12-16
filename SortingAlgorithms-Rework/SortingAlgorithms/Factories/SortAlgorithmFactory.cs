using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using SortingAlgorithms.Services.Sorting.Algorithms;

namespace SortingAlgorithms.Factories
{
    public class SortAlgorithmFactory<T> where T : IComparable, IConvertible
    {
        public SortAlgorithmFactory()
        {
            
        }

        public BubbleSort<T> CreateBubbleSort()
        {
            return new BubbleSort<T>();
        }
        public HeapSort<T> CreateHeapSort()
        {
            return new HeapSort<T>();
        }
        public InsertionSort<T> CreateInsertionSort()
        {
            return new InsertionSort<T>();
        }
        public MergeSort<T> CreateMergeSort()
        {
            return new MergeSort<T>();
        }
        public QuickSort<T> CreateQuickSort()
        {
            return new QuickSort<T>();
        }
        public RadixSort<T> CreateRadixSort()
        {
            return new RadixSort<T>();
        }
        public SelectionSort<T> CreateSelectionSort()
        {
            return new SelectionSort<T>();
        }

    }
}