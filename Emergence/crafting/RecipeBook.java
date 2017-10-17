package crafting;

import primativeRows.Row;

public class RecipeBook {
	Node top;
	Row<Recipe> reAr;
	
	RecipeBook(){
		reAr = new Row<>();
	}
	
	public void add(Recipe r){
		reAr.add(r);
		if(top == null)
			top = new Node(r);
		else
			tryAddAt(r, top);
	}
	
	public boolean has(String name){
		if(top == null)
			return false;
		return (tryGet(name, top) != null);
	}
	
	public Recipe get(String name){
		if(top == null)
			return null;
		return tryGet(name, top);
	}
	
	public String build(String... components){
		for(int i = 0; i < reAr.getNumberOfElements(); i++){
			Recipe next = reAr.getElement(i);
			if(next.canBuild(components))
				return next.getName();
		}
		return "NOTHING";
	}
	
	private void tryAddAt(Recipe add, Node where){
		int compare = add.getName().compareTo(where.data.getName());
		if(compare < 0){
			if(where.left == null)
				where.left = new Node(add);
			else
				tryAddAt(add, where.left);
		}else{
			if(where.right == null)
				where.right = new Node(add);
			else
				tryAddAt(add, where.right);
		}
	}
	
	private Recipe tryGet(String name, Node where){
		int compare = name.compareTo(where.data.getName());
		if(compare == 0)
			return where.data;
		if(compare < 0){
			if(where.left == null)
				return null;
			return tryGet(name, where.left);
		}else{
			if(where.right == null)
				return null;
			return tryGet(name, where.right);
		}
	}
	
	class Node{
		Recipe data;
	    Node left,
	         right;

	    public Node(Recipe d) {
	        this(d, null, null);
	    }

	    public Node(Recipe data, Node left, Node right) {
	        this.data = data == null ? null : data;
	        this.left = left;
	        this.right = right;
	    }
	}
}
