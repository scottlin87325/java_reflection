package com.example.serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileWriterUtil {
	private void writeToFile(List<String> data, String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			for (String line : data) {
				writer.write(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mergeFiles(String headerFile, List<String> iniFile, String footerFile, String outputFile) {
		List<String> mergedLines = new ArrayList<>();
		FileUtil read = new FileUtil();
		List<String> headerLines;
		List<String> footerLines;
		
		headerLines = read.readFile(headerFile, 0);
		footerLines = read.readFile(footerFile, 0);
		mergedLines.addAll(headerLines);
		mergedLines.addAll(iniFile);
		mergedLines.addAll(footerLines);
		writeToFile(mergedLines, outputFile);
	}
}
