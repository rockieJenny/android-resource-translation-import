package com.lionmobi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class Translation {
	private Map<Integer, String> languageMapper = new HashMap<Integer, String>();
	private static final String PATH_FORMATTER = "\\values-%s\\strings.xml";
	private String ROOT;
	private CompleteLanaguageXml xmlCompletor;

	public void doTranslate(String filePath, String targetDirectory)
			throws FileNotFoundException {

		ROOT = findRootPath(targetDirectory);
		xmlCompletor = new CompleteLanaguageXml();

		ExcelUtils.resolve(new RowHandler() {

			@Override
			public Boolean handleRow(Row row) {
				try {
					if (row.getRowNum() == 0) {
						mapLanguage(row.getCells());
					} else {
						matchKey(row.getCells());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			}

		}, new FileInputStream(new File(filePath)));
	}

	/**
	 * 
	 * @param cells
	 * @throws Exception
	 */
	private void matchKey(List<Cell> cells) throws Exception {
		// 0 represent key
		if (cells.size() < languageMapper.size()) {
			return;
		}
		for (int i = 1; i < cells.size(); i++) {
			String language = languageMapper.get(i);
			if (language == null) {
				continue;
			}
			String targetXml = ROOT + String.format(PATH_FORMATTER, language);
			if ("en".equals(language)) {
				targetXml = ROOT + "\\values\\strings.xml";
			}

			String key = cells.get(0).getContent().trim();
			// should not trim or toLowerCase
			String value = cells.get(i).getContent();
			xmlCompletor.complete(targetXml, key, value);
			System.out.println(language + "  -  " + key + "  -   " + value);
		}
	}

	/**
	 * 
	 * @param cells
	 * @throws Exception
	 */
	private void mapLanguage(List<Cell> cells) throws Exception {
		for (int i = 0; i < cells.size(); i++) {
			Cell column = cells.get(i);
			String titleName = column.getContent().trim();
			String language = retrieveLanguage(titleName);
			if (language == null) {
				continue;
			}
			languageMapper.put(i, language);
		}

	}

	/**
	 * 
	 * @param tille
	 * @return
	 * @throws Exception
	 */
	private String retrieveLanguage(String title) throws Exception {
		if ("".equals(title)) {
			return null;
		}
		title = title.toLowerCase().trim();
		StringBuilder sb = new StringBuilder();
		File langaugeConfig = new File(new File("").getAbsolutePath()
				+ "/conf/language.cfg");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(langaugeConfig));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		JSONObject json = JSONObject.fromObject(sb.toString());

		if (json.containsKey(title)) {
			return title;
		}
		Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String key = iterator.next().trim().toLowerCase();
			String values = json.getString(key);
			String[] list = values.split(",");
			for (String o : list) {
				if (title.equals(o.trim().toLowerCase())) {
					return key;
				}
			}
		}
		return null;
	}

	private String findRootPath(String directory) {
		File root = new File(directory);
		if (!root.isDirectory()) {
			return null;
		} else if (root.getAbsolutePath().endsWith(
				"src" + File.separator + "main" + File.separator + "res")) {
			return root.getAbsolutePath();
		}
		File[] listFiles = root.listFiles();
		for (File file : listFiles) {
			String path = findRootPath(file.getAbsolutePath());
			if (path != null) {
				return path;
			}
		}
		return null;
	}
}
