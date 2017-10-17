package crafting;

import primativeRows.Row;

public class Recipe implements Comparable{
	private RecipeBook rb;
	private String name;
	private String[] formula;
	private String[] types;
	
	Recipe(RecipeBook rb, String name, String[] formula, String[] types){
		this.rb = rb;
		this.name = name;
		this.formula = formula;
		this.types = types;
	}
	
	public String[] getTypes(){
		return types;
	}
	
	public String[] getFormula(){
		return formula;
	}
	
	boolean hasType(String query){
		if(name.equals(query))
			return true;
		for(int i = 0; i < types.length; i++){
			if(types[i].equals(query)){
				return true;
			}
				
			Recipe queryRecipe = rb.get(query);
			if(queryRecipe != null && queryRecipe.hasType(query))
				return true;
		}
		return false;
	}
	
	boolean canBuild(String[] components){
		if(formula ==  null)
			return false;
		for(int i = 0; i < formula.length; i++){
			if(components.length < i || !acceptableComponentAt(components[i], formula[i])){
				return false;
			}
		}
		return true;
	}
	
	public Row<String> scavenge(){
		if(formula ==  null)
			return new Row<>();
		Row<String> result = new Row<>();
		for(int i = 0; i < formula.length; i++){
			if(rb.has(formula[i]))
				result.add(formula[i]);
		}
		return result;
	}
	
	private boolean acceptableComponentAt(String component, String requirement){
		if(requirement.equals(component))
			return true;
		if(component.equals(""))
			return false;
		Recipe r =  rb.get(component);
		if(r == null){
			return false;
		}return r.hasType(r.getName());
	}

	public String getName() {
		return name;
	}
	
	public int compareTo(Object other) {
		if(!(other instanceof Recipe))
			return -1;
		Recipe otherRecipe = (Recipe) other;
		if(otherRecipe.getName().equals(name))
			return 0;
		return -1;
	}
}
