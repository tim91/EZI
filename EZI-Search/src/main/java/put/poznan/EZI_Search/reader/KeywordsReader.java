package put.poznan.EZI_Search.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class KeywordsReader extends AbstractReader {

	private String keywordFile;

	private String regexp = "txt";

	private static KeywordsReader keyReader;

	private KeywordsReader() {
	}

	public static KeywordsReader getInstance() {

		if (keyReader == null) {

			keyReader = new KeywordsReader();
		}
		return keyReader;
	}

	public Vector<String> readKeywordsFromFile() {

		Vector<String> keywords = new Vector<String>(10);

		File file = new File(this.keywordFile);

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				String keyword = br.readLine().trim()
						.replaceAll("[^a-zA-Z ]", "").toLowerCase();
				if (keyword.length() > 0) {
					keywords.add(keyword);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("No database available.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return keywords;
	}

	public String getKeywordFile() {
		return keywordFile;
	}

	public void setKeywordFile(String keywordFile) {
		this.keywordFile = keywordFile;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

}
