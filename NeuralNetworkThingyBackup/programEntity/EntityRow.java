package programEntity;

public class EntityRow {
	private Entity[] row;
	private int numberOfElements;
	
	public EntityRow(int maxElements){
		numberOfElements = 0;
		row = new Entity[maxElements];
	}
	
	public void add(Entity ent){
		row[numberOfElements] = ent;
		numberOfElements++;
	}
	
	private void shrinkArrayToFit(int wiggleRoom){
		Entity[] tempRow = new Entity[numberOfElements + wiggleRoom];
		for(int i = 0; i < numberOfElements; i++){
			tempRow[i] = row[i];
		}
		row = tempRow;
	}
	
	public void remove(int index){
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
		numberOfElements--;
	}
	
	public void removeAllPlayerShadow(){
		for(int i = 0; i < numberOfElements; i++){
			if(row[i] instanceof PlayerShadow)
				remove(i);
		}
	}
	
	//GETS
	
	public int getNumberOfElements(){
		return numberOfElements;
	}
	
	public Entity getElement(int index){
		return row[index];
	}
	
	public Entity getEntityAt(int x, int y){
		for(int i = 0; i < numberOfElements; i++){
			if(row[i].getX() == x && row[i].getY() == y){
				return row[i];
			}
		}
		return null;
	}
}
