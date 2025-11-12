using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NodesImplementation.Structures.Generic
{
    public class LinkedListNode<T>
    {
        private T Data;

        private int index;
        private LinkedListNode<T>? previousNode;
        private LinkedListNode<T>? nextNode;

        public LinkedListNode(T Object, LinkedListNode<T>? rightNode, LinkedListNode<T>? leftNode)
        {
            this.Data = Object;
            this.previousNode = rightNode;
            this.nextNode = leftNode;

        }

        public void actualizeIndex()
        {
            this.index = nextNode==null?0:nextNode.getIndex() + 1 ;

            if(previousNode != null)
            {
                previousNode.actualizeIndex();    
            }
        }
        
        public int getIndex()
        {
            return this.index;    
        }
        public LinkedListNode<T>? GetPreviousNode()
        {
            return previousNode;
        }
        public LinkedListNode<T>? GetNextNode()
        {
            return nextNode;
        }

        public void SetNextNode(LinkedListNode<T>? linkedListNode)
        {
            this.nextNode = linkedListNode;
        }
        public void SetPreviousNode(LinkedListNode<T>? linkedListNode)
        {
            this.previousNode = linkedListNode;
        }

        public T Value
        {
            get => Data;
            private set => Data = value;
        }
    }
}