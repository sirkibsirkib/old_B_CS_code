package stuff;

public class OptionRow {
	private Option[] options;
	private int numberOfOptions;
	
	public int getNumberOfOptions() {
		return numberOfOptions;
	}
	
	public void setLinksToAt(int index, Page link){
		options[index].setLinksTo(link);
	}
	
	public Page getLinksToAt(int index){
		return options[index].getLinksTo();
	}
	
	public String getTextAt(int index){
		return options[index].getText();
	}
	
	public Option getOption(int index){
		return options[index];
	}

	OptionRow(){
		options = new Option[4];
		numberOfOptions = 0;
	}
	
	public void addOption(Option p){
		if(options.length <= numberOfOptions){
			doubleArraySize();
		}
		options[numberOfOptions] = p;
		numberOfOptions++;
	}
	
	public void removeOptionAt(int index){
		for(int i = index; i < numberOfOptions-1; i++){
			options[i] = options[i+1];
		}
		numberOfOptions--;
		if(options.length >= numberOfOptions/2 && options.length > 4){
			halveArraySize();
		}
	}
	
	private void doubleArraySize(){
		Option[] options2 = new Option[options.length*2];
		for(int i = 0; i < numberOfOptions; i++){
			options2[i] = options[i];
		}
		options = options2;
	}
	
	private void halveArraySize(){
		Option[] options2 = new Option[options.length/2];
		for(int i = 0; i < numberOfOptions; i++){
			options2[i] = options[i];
		}
		options = options2;
	}

	public void pruneDead() {
		for(int i = 0; i < numberOfOptions; i++){
			if(options[i].getLinksTo() == null)
				removeOptionAt(i);
		}
	}

	public void findAndDelete(Option opt) {
		for(int i = 0; i < numberOfOptions; i++){
			if(options[i] == opt){
				removeOptionAt(i);
			}
		}
	}
	
	public Page findLooseEndOption(){
		for(int i = 0; i < numberOfOptions; i++){
			if(options[i].getLinksFrom() != null && options[i].getLinksTo() == null)
				return options[i].getLinksFrom();
		}
		return null;
	}

	public boolean has(Option o) {
		for(int i = 0; i < numberOfOptions; i++){
			if(options[i] == o)
				return true;
		}
		return false;
	}

	public void removeLinksTo(Page p) {
		for(int i = 0; i < numberOfOptions; i++){
			if(options[i].getLinksTo() == p){
				options[i].setLinksTo(null);
			}
		}
	}
}
