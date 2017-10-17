package stuff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import ui.UIAuxiliaryMethods;

public class SLBook {
	static final String NULL = "NULL";
	String fileName = "bookSave.txt";
	
	//SAVE
	public void save(PageRow pr, String path, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		int numPages = pr.getNumberOfPages();
		
		File file = new File(path + fileName);
//		if(!file.exists() || file.isDirectory())
//			return; //TODO not optimal solution
		file.getParentFile().mkdirs();
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		
		writer.println(numPages);
		for(int i = 0; i < pr.getNumberOfPages(); i++){
			writePage(writer, pr.getPageAt(i), i, pr);
		}
		UIAuxiliaryMethods.showMessage("Progress saved to:\n" + path + fileName);
		writer.close();
	}
	
	private void writePage(PrintWriter writer, Page p, int index, PageRow pr){
		writer.print(p.getText());
		if(p.getText().length() == 0)
			writer.print(NULL);
		OptionRow optionsOut = p.getOptionsOut();
		for(int i = 0; i < optionsOut.getNumberOfOptions(); i++){
			Option o = optionsOut.getOption(i);
			writeOption(writer, pr, o);
		}
		writer.println();
	}

	private void writeOption(PrintWriter writer, PageRow pr, Option o) {
		if(o.getLinksTo() == null)
			writer.print("\t9999999\t" + o.getText());
		else
			writer.print("\t" + pr.getIndex(o.getLinksTo()) + "\t" + o.getText());
		if(o.getText().length() == 0)
			writer.print(NULL);
	}
	
	
	
	
	
	
//	//NEW POPUP LOAD
//	public PageRow popupLoad(String path) throws IOException{
//		UIAuxiliaryMethods.askUserForInput();
//	    return loadPageRowFromString();
//	}
//	
//	private PageRow loadPageRowFromString(){
//		PageRow pr = new PageRow();
//		Scanner dataScanner = new Scanner(System.in);
//		if(dataScanner.hasNext()){
//			int numberOfPages = dataScanner.nextInt();
//			dataScanner.nextLine();
//			
//			for(int i = 0; i < numberOfPages; i++){
//				pr.addPage(new Page(i));
//			}
//			for(int i = 0; i < numberOfPages; i++){
//				populatePageData(pr.getPageAt(i), dataScanner.nextLine(), pr);
//			}
//			dataScanner.close();
//			return pr;
//		}else{
//			System.out.println("nothing");
//			return null;
//		}
//	}

	//OLD PATHLOAD
	public PageRow pathLoad(String path, String fileName) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(path + fileName));
		}catch(FileNotFoundException e){
			UIAuxiliaryMethods.showMessage(path + fileName);
			return null;
		}
		String everything;
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } finally {
	    	UIAuxiliaryMethods.showMessage("Progress loaded from:\n" + path + fileName);
	        br.close();
	    } 
	    return pathNameLoadFromString(everything);
	}
	
	private PageRow pathNameLoadFromString(String data){
		PageRow pr = new PageRow();
		
		Scanner dataScanner = new Scanner(data);
		int numberOfPages = dataScanner.nextInt();
		dataScanner.nextLine();
		
		for(int i = 0; i < numberOfPages; i++){
			pr.addPage(new Page(i));
		}
		for(int i = 0; i < numberOfPages; i++){
			populatePageData(pr.getPageAt(i), dataScanner.nextLine(), pr);
		}
		dataScanner.close();
		return pr;
	}
	
	
	
	
	
	
	//BOTH LOADS
	private void populatePageData(Page p, String data, PageRow pr){
		Scanner pageScanner = new Scanner(data);
		pageScanner.useDelimiter("\t");
		String pageText = pageScanner.next().replace(NULL, "");
		p.setText(pageText);
		while(pageScanner.hasNext())
			populatePageWithOption(p, pageScanner.next(), pageScanner.next().replace(NULL, ""), pr);
		pageScanner.close();
	}
	
	private void populatePageWithOption(Page p, String index, String text, PageRow pr){
		int linkRef = Integer.parseInt(index);
		
		Option o = new Option(text, p);
		p.addOptionOut(o);
		if(linkRef != 9999999){
			o.setLinksTo(pr.getPageAt(linkRef));
			pr.getPageAt(linkRef).addOptionIn(o);
		}
	}
}
