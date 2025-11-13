using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace NodesImplementation.Structures.Generic
{
    public class LinkedListParent<T> : IEnumerable<T>
    {
        private LinkedListNode<T>? firstNode;
        private LinkedListNode<T>? lastNode;
        private int lenght;
        public int Lenght
        {
            get => lenght;
            set => lenght = value;
        }

        private void IncreaseLength(int len = 1)
        {
            lenght += len;
        }
        public void AddFirst(T Object)
        {
            firstNode = new LinkedListNode<T>(Object, rightNode: firstNode, leftNode: null);

            IncreaseLength();
            firstNode.actualizeIndex();
        }
        public void AddLast(T Object)
        {
            lastNode = new LinkedListNode<T>(Object, rightNode: null, leftNode: lastNode);

            IncreaseLength();
            lastNode.actualizeIndex();
        }
        public void AddAt(T Object, int index)
        {
            LinkedListNode<T>? xNode = GetNodeAt(index-1);
            LinkedListNode<T>? yNode = GetNodeAt(index);
            LinkedListNode<T> zNode = new LinkedListNode<T>(Object, rightNode: yNode, leftNode: xNode);

            if(xNode == null && yNode == null)
            throw new Exception("There is no element at or around the index");

            if(xNode != null)
            xNode.SetPreviousNode(zNode);
            if(yNode != null)
            yNode.SetNextNode(zNode);

            IncreaseLength();
            zNode.actualizeIndex();

        }
        public void RemoveLast()
        {
            if(lastNode == null)
                throw new Exception("Collection doesn't contain any elements");

            LinkedListNode<T>? xNode = lastNode.GetNextNode();
            if(xNode != null){
                xNode.SetPreviousNode(null);
            }
            else{
                firstNode = null;
            }

            lastNode = xNode;

        }
        public void RemoveFirst()
        {
            if(firstNode == null)
                throw new Exception("Collection doesn't contain any elements");

            LinkedListNode<T>? zNode = firstNode.GetPreviousNode();

            if(zNode != null){
                zNode.SetNextNode(null);
            }
            else{
                lastNode = null;
            }

            firstNode.actualizeIndex();

            firstNode = zNode;
            
        }

        public void Remove(T item)
        {
            RemoveAt(GetNode(item).getIndex());
        }

        public void RemoveAt(int index)
        {
            LinkedListNode<T>? foundNode = GetNodeAt(index);

            if(foundNode == null)
                throw new Exception($"No element found at specified index: {index}");

            LinkedListNode<T>? xNode = foundNode.GetNextNode();
            LinkedListNode<T>? zNode = foundNode.GetPreviousNode();

            if(xNode != null){
                xNode.SetPreviousNode(zNode);
            }else{
                firstNode = zNode;
            }
            if(zNode != null){
                zNode.SetNextNode(xNode);
            }
            else{
                lastNode = xNode;
            }


            if(xNode==null&&zNode!=null){
                zNode.actualizeIndex();
            }
            if((xNode != null && zNode != null) || (xNode!=null&&zNode==null)){
                xNode?.actualizeIndex(); 
            }
            
            
        }


        private LinkedListNode<T>? GetNodeAt(int index)
        {   
            if(firstNode == null)
            throw new Exception("Collection doesn't contain any elements");

            LinkedListNode<T>? currentNode = firstNode;

            while (currentNode.getIndex() != index)
            {
                if(currentNode != null){
                    currentNode = currentNode.GetPreviousNode();
                }

                if(currentNode == null){
                    throw new Exception("Element not found at specified index");
                }
            }

            return currentNode;
        }

        private LinkedListNode<T> GetNode(T node)
        {

            LinkedListNode<T>? currentNode = firstNode;

            if(currentNode == null)
            throw new Exception("Collection doesn't contain any elements");
            
            
            while(true){
                if(currentNode == null)
                {
                    throw new Exception("Collection doesn't contain this element");
                }

                if(currentNode.Value?.Equals(node) ?? true)
                {
                    return currentNode;
                }
                else
                {
                    currentNode = currentNode.GetPreviousNode();
                }
            }
        }

        public T ElementAt(int index)
        {
            LinkedListNode<T>? foundNode = GetNodeAt(index);

            if(foundNode == null)
            {
                throw new Exception($"No element has been found at specified index: {index}");
            }

            return foundNode.Value;

        }

        public override string ToString()
        {
            string converted = "";

            LinkedListNode<T>? currentNode = this.firstNode;

            converted = $"{currentNode?.Value?.ToString()}";

            while (currentNode?.GetPreviousNode() != null)
            {
                currentNode = currentNode.GetPreviousNode();
                converted = $"{converted}, {currentNode?.Value?.ToString()}";
            }

            return converted;
        }

        public IEnumerator<T> GetEnumerator()
        {
            throw new NotImplementedException();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }
    }
}