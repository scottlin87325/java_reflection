package com.example.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileReaderUtil {
	public Map<String, String> readFileAndSplit(String fileName) {
		List<String> lines = new ArrayList<>();
		FileUtil fileUtil = new FileUtil();
		
		lines = fileUtil.readFile(fileName, 1);
		
		return fileUtil.stringSplit(lines);
	}
}
