package com.lionmobi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CompleteLanaguageXml {

	public void complete(String targetXml, String key, String value)
			throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(targetXml));
		String line = null;
		boolean found = false;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			String prefix = key.split("_")[0];
			if (line.matches(" name=\"" + prefix)) {
				sb.append(String.format("<string name=\"%s\">%s</string>", key,
						value) + "\r\n");
				found = true;
			}
			if (line.trim().equals("</resources>")) {
				if (!found) {
					sb.append(String.format("<string name=\"%s\">%s</string>",
							key, value) + "\r\n");
				}
			}
			sb.append(line + "\r\n");
		}

		br.close();
		FileWriter fileWrite = new FileWriter(new File(targetXml));
		fileWrite.write(sb.toString());
		fileWrite.close();
	}
}
