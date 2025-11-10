using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NodesImplementation.Structures
{
    public class LinkedListParent<T>
    {
        private LinkedListNode<T>? firstNode;

        public void Add(T Object)
        {
            this.firstNode = new LinkedListNode<T>(Object, previousNode: firstNode, nextNode: null);
        }
    }
}