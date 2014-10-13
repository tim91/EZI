package put.poznan.EZI_Search.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractReader {

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
