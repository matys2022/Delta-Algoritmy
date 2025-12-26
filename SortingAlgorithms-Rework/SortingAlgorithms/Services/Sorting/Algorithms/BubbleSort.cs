using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using SortingAlgorithms.Interfaces.Sorting;
using SortingAlgorithms.Models.Tuples;

namespace SortingAlgorithms.Services.Sorting.Algorithms
{
    public class BubbleSort<T> : ISortingAlgorithm<T> where T : IConvertible, IComparable
    {

        private static double percentageDone = 0; 

        public BubbleSort()
        {
            
        }


        public double getPercentage()
        {
            return percentageDone;
        }

        public List<SortablePair<T>> Sort(List<SortablePair<T>> items, CancellationToken? token)
        {
            percentageDone = 0; 
            Console.WriteLine("Bubble Sort");
            


            return runSort(items, token);
        }
        
        private List<SortablePair<T>> runSort(List<SortablePair<T>> items, CancellationToken? token)
        {

            int indexCap = 0;

            while (true)
            {
                token?.ThrowIfCancellationRequested();

                int fieldSize = items.Count - indexCap;

                bool sorted = true;

                for(int i = 0; i < fieldSize - 1; i++)
                {
                    if(items[i].subv > items[i + 1].subv)
                    {
                        SortablePair<T> tmp = items[i];
                        
                        items[i] = items[i + 1];
                        
                        items[i+1] = tmp;

                        sorted = false;
                    }
                }

                indexCap++;

                double currentProgress =  (double)indexCap/(items.Count/100);


                percentageDone = currentProgress;
                    
                

                // In case of already sorted field, this will end the cycle;
                // Either no change has been made or there is nothing to be moved around anymore;
                
                if(items.Count - indexCap == 0 || sorted)
                {
                    break;
                }

                
            }
            return items;
        }
    }
}