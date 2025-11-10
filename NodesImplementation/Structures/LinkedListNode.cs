using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NodesImplementation.Structures
{
    public class LinkedListNode<T>
    {
        private T Data;
        private LinkedListNode<T>? previousNode;
        private LinkedListNode<T>? nextNode;

        public LinkedListNode(T Object, LinkedListNode<T>? previousNode, LinkedListNode<T>? nextNode)
        {
            this.Data = Object;
            this.previousNode = previousNode;
            this.nextNode = nextNode;

        }
    }
}