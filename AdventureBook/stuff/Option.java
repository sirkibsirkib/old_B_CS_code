package stuff;

public class Option {
	private String text;
	private Page linksTo, linksFrom;
	
	public Page getLinksTo() {
		return linksTo;
	}
	
	public Page getLinksFrom() {
		return linksFrom;
	}

	public void setLinksTo(Page linksTo) {
		this.linksTo = linksTo;
	}

	Option(String text, Page linksFrom){
		this.text = text;
		this.linksFrom = linksFrom;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public boolean isLoose() {
		return linksTo == null;
	}
}
