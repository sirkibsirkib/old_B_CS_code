package stuff;

public class PageRow {
	private Page[] pages;
	private int numberOfPages;
	
	PageRow(){
		pages = new Page[4];
		numberOfPages = 0;
	}
	
	public void addPage(Page p){
		if(pages.length <= numberOfPages){
			doubleArraySize();
		}
		pages[numberOfPages] = p;
		numberOfPages++;
	}
	
	public void removePageAt(int index){
		for(int i = 0; i < numberOfPages; i++){
			pages[i].removeLinksTo(pages[index]);
		}
		for(int i = index; i < numberOfPages-1; i++){
			pages[i] = pages[i+1];
		}
		for(int i = index; i < numberOfPages; i++){
			pages[i].setGlobalIndex(i);
		}
		numberOfPages--;
		if(numberOfPages <= pages.length/2 && pages.length > 4){
			halveArraySize();
		}
	}
	
	private void movePageIntoPosition(int from, int to){
		//TODO user can choose what page becomes page 1
	}
	
	private void doubleArraySize(){
		Page[] pages2 = new Page[pages.length*2];
		for(int i = 0; i < numberOfPages; i++){
			pages2[i] = pages[i];
		}
		pages = pages2;
	}
	
	private void halveArraySize(){
		Page[] pages2 = new Page[pages.length/2];
		for(int i = 0; i < numberOfPages; i++){
			pages2[i] = pages[i];
		}
		pages = pages2;
	}
	
	public Page getPageAt(int index){
		return pages[index];
	}
	
	public int getNumberOfPages(){
		return numberOfPages;
	}
	
	private void addPageAt(Page p, int index){
		if(pages.length <= numberOfPages){
			doubleArraySize();
		}
		//pages[numberOfPages] = p;
		for(int i = numberOfPages; i > index+1; i--){
			pages[i] = pages[i-1];
			pages[i].setGlobalIndex(i);
		}
		pages[index] = p;
		p.setGlobalIndex(index);
		numberOfPages++;
	}
	
	private void setNumberOfPages(int n){
		numberOfPages = n;
	}
	
	public PageRow getCopy(){
		PageRow pr2 = new PageRow();
		for(int i = 0; i < numberOfPages; i++){
			pr2.addPage(pages[i]);
		}
		return pr2;
	}
	
	public int getIndex(Page p){
		for(int i = 0; i < pages.length; i++){
			if(pages[i] == p)
				return i;
		}
		return -1;
	}
	
	public OptionRow getAllUniqueOptions(){
		OptionRow uniques = new OptionRow();
		for(int i = 0; i < numberOfPages; i++){
			OptionRow thisPageOptions = pages[i].getOptionsOut();
			int numberOfOptions = thisPageOptions.getNumberOfOptions();
			for(int j = 0; j < numberOfOptions; j++){
				Option thisOption = thisPageOptions.getOption(j);
				if(!!uniques.has(thisOption))
					uniques.addOption(thisOption);
			}
		}
		return uniques;
	}
	
	public void shufflePages(){
		for(int i = 0; i < numberOfPages; i++){
			switchPages(i, Fun.rng(i));
		}
		for(int i = 0; i < numberOfPages; i++){
			pages[i].setGlobalIndex(i);
		}
	}
	
	private void switchPages(int a, int b){
		Page c = pages[a];
		pages[a] = pages[b];
		pages[b] = c;
	}

	public void movePageIntoIndex(Page p, int desiredIndex) {
		int pIndex = getIndex(p);
		switchPages(desiredIndex, pIndex);
		pages[desiredIndex].setGlobalIndex(desiredIndex);
		pages[pIndex].setGlobalIndex(pIndex);
	}

	public int countLooseEnds() {
		int looseEnds = 0;
		for(int i = 0; i < numberOfPages; i++){
			looseEnds += pages[i].countLooseEnds();
		}
		return looseEnds;
	}

	public Page getALoosePage() {
		for(int i = 0; i < numberOfPages; i++){
			if(pages[i].countLooseEnds() > 0)
				return pages[i];
		}
		return null;
	}
}
