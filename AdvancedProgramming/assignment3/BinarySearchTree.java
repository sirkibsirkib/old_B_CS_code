package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> implements BinarySearchTreeInterface<E>{
	private Node root;
	
	public void insert(E x) {
		root = insert(root, x);
	}
	
	private Node insert(Node root, E x){
		if(root == null)	return new Node(x);
		
		if(x.compareTo(root.data) < 0) //left
			root.left = insert(root.left, x);
		else
			root.right = insert(root.right, x);
		return root;
	}
	
	public void remove(E x) {
		root = remove(root, x);
	}
	
	public Node remove(Node root, E x){
		if(root == null)
			throw new Error("removing nonexistant data!");
		if(x.compareTo(root.data) < 0){ //left
			root.left = remove(root.left, x);
		}else if(x.compareTo(root.data) > 0){
			root.right = remove(root.right, x);
		}else{ //0 or 1 child
			if(root.left == null)
				root = root.right;
			else if(root.right == null)
				root = root.left;
			else{//2 children
				root.data = smallest(root.right);
				root.right = remove(root.right, root.data);
			}
		}
		return root;
	}
	
	public E smallest(){
		return smallest(root);
	}
	
	private E smallest(Node root) {
		return root.left == null ? root.data : smallest(root.left);
	}
	
	public E largest(){
		return largest(root);
	}
	
	private E largest(Node root) {
		return root.right == null ? root.data : largest(root.right);
	}

	private class Node{
		E data;
		Node left, right;
		
		private Node(E data, Node left, Node right){
			this.data = data;
			this.left = left;
			this.right = right;
		}
		
		private Node(E data){
			this(data, null, null);
		}
		
		private Node(){
			this(null, null, null);
		}
	}
	
	public boolean contains(E e) {
		return containsRec(root, e);
	}
	private boolean containsRec(Node root, E e){
		if(root == null)
			return false;
		int comparison = e.compareTo(root.data);
		if(comparison < 0)
			return containsRec(root.left, e);
		if(comparison > 0)
			return containsRec(root.right, e);
		return comparison == 0;
	}
	
	public int numberOfNodes() {
		return numberOfNodesRec(root);
	}
	private int numberOfNodesRec(Node p){
		if(p == null)
			return 0;
		return numberOfNodesRec(p.left) + numberOfNodesRec(p.right) + 1;
	}
	
	public int height() {
		return heightRec(root);
	}
	
	private int heightRec(Node p){
		if(p == null){
			return -1;
		}
		return 1 + Math.max(heightRec(p.left), heightRec(p.right));
	}
	
	public BinarySearchTreeInterface<E> copy() {
		BinarySearchTree<E> copy = new BinarySearchTree<>();
		copy.root = copy(root);
		return (BinarySearchTreeInterface<E>) copy;
	}
	
	private Node copy(Node root){
		if(root == null)
			return null;
		return new Node(root.data, copy(root.left), copy(root.right));
	}
	
	private void inOrder(BSTIterator<E> it, Node root){
		if(root == null)
			return;
		inOrder(it, root.left);
		it.addToEnd(root.data);
		inOrder(it, root.right);
	}
	
	private void reverseInOrder(BSTIterator<E> it, Node root){
		if(root == null)
			return;
		reverseInOrder(it, root.right);
		it.addToEnd(root.data);
		reverseInOrder(it, root.left);
	}
	
	public Iterator<E> ascendingIterator() {
		BSTIterator<E> it = new BSTIterator<>();
		inOrder(it, root);
		return it;
	}
	
	public Iterator<E> descendingIterator() {
		BSTIterator<E> it = new BSTIterator<>();
		reverseInOrder(it, root);
		return it;
	}
	
	private class BSTIterator<E extends Comparable<E>> implements Iterator<E>{
		private List<E> ls;
		private int pointer;
		
		BSTIterator(){
			ls = new ArrayList<>();
			pointer = 0;
		}
		
		private void addToEnd(E e){
			ls.add(e);
		}
		
		@Override
		public boolean hasNext() {
			return pointer < ls.size();
		}

		@Override
		public E next() {
			E nextData = ls.get(pointer);
			pointer++;
			return nextData;
		}
	}
}
