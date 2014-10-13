package put.poznan.EZI_Search.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import put.poznan.EZI_Search.Stemmer.Stemmer;

public abstract class AbstractReader {

	protected Stemmer stemmer;
	
	public AbstractReader() {
		stemmer = new Stemmer();
	}
	
	
	protected List<File> getFiles(String baseDir, String suffix)
	{	
		File folder = new File(baseDir);
		File[] listOfFiles = folder.listFiles();

		List<File> files = new ArrayList<File>(listOfFiles.length);
		
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().endsWith(suffix)) {
		    	files.add(file);
		    }
		}
		
		return files;
	}
}
