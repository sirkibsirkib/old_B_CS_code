package stuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import ui.UIAuxiliaryMethods;

public class Bookwriter {
	//private String path = "C:/Users/Christopher/Desktop/adventureBook.txt";
	private int skippedLinks = 0;
	
	Bookwriter(String path, String fileName, PageRow pr) throws FileNotFoundException, UnsupportedEncodingException{
		File file = new File(path + fileName);
		
		file.getParentFile().mkdirs();
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		
		writePage(pr.getPageAt(0), writer);
		for(int i = 1; i < pr.getNumberOfPages(); i++){
			Page nextPage = pr.getPageAt(i);
			writer.println();
			writePage(nextPage, writer);
		}
		String endMessage = "Book successfully written to:\n" + path + fileName + "\n";
		if(skippedLinks > 0){
			endMessage += "A total of (" + skippedLinks + ") links were skipped.";
		}
		UIAuxiliaryMethods.showMessage(endMessage);
		writer.close();
	}
	
	private void writePage(Page thisPage, PrintWriter writer){
		writer.println("\t#" + (thisPage.getGlobalIndex()+1));
		writer.println(thisPage.getText());
		OptionRow outs = thisPage.getOptionsOut();
		for(int i = 0; i < outs.getNumberOfOptions(); i++){
			Option nextOut = outs.getOption(i);
			if(!nextOut.isLoose())
				writeOption(writer, nextOut, i);
			else
				skippedLinks++;
		}
	}

	private void writeOption(PrintWriter writer, Option o, int i) {
		String optionText = o.getText();
		if(optionText.contains("#"))
			optionText = optionText.replace("#", ""+(o.getLinksTo().getGlobalIndex()+1));
		else
			optionText = optionText.trim() + " (Turn to " + (o.getLinksTo().getGlobalIndex()+1) + ")";
		writer.println("\t" + optionText);
	}
}
