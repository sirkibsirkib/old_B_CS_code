package stuff;

public class Page {
	private String text = "";
	private OptionRow optionsOut,
		optionsIn;
	private int globalIndex;
	
	Page(int globalIndex){
		optionsOut = new OptionRow();
		optionsIn = new OptionRow();
		this.globalIndex = globalIndex;
	}
	
	public OptionRow getOptionsOut() {
		return optionsOut;
	}
	public OptionRow getOptionsIn() {
		return optionsIn;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public void addOptionOut(Option o) {
		optionsOut.addOption(o);
	}
	
	public void addOptionIn(Option o) {
		optionsIn.addOption(o);
	}

	public int getGlobalIndex() {
		return globalIndex;
	}
	
	public void setGlobalIndex(int index){
		globalIndex = index;
	}
	
	public void pruneDeadIncoming(){
		optionsIn.pruneDead();
	}

	public void findAndDelete(Option opt) {
		optionsOut.findAndDelete(opt);
	}
	
	public boolean hasOptionTo(Page p){
		for(int i = 0; i < optionsOut.getNumberOfOptions(); i++){
			if(optionsOut.getOption(i).getLinksTo() == p)
				return true;
		}
		return false;
	}
	
	public int getNumberOfLooseEnds(){
		int looseEnds = 0;
		for(int i = 0; i < optionsOut.getNumberOfOptions(); i++){
			if(optionsOut.getOption(i).getLinksTo() == null)
				looseEnds++;
		}
		return looseEnds;
	}

	public void reset() {
		text = "";
		optionsOut = new OptionRow();
		optionsIn = new OptionRow();
	}

	public void removeLinksTo(Page p) {
		optionsOut.removeLinksTo(p);
	}

	public int countLooseEnds() {
		int looseEnds = 0;
		for(int i = 0; i < optionsOut.getNumberOfOptions(); i++){
			if(optionsOut.getOption(i).isLoose())
				looseEnds++;
		}
		return looseEnds;
	}
}
