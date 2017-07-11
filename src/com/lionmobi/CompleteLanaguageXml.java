package com.lionmobi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CompleteLanaguageXml {

	public void complete(String targetXml, String key, String value)
			throws Exception {
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt");
		//BufferedReader br = new BufferedReader(new FileReader(targetXml));
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(targetXml),"UTF-8"));  
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
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
				new FileOutputStream(targetXml), "utf-8");
		outputStreamWriter.write(sb.toString());
		byte[] bytes = sb.toString().getBytes();
		StringBuilder byttt = new StringBuilder();
		for (byte b : bytes) {
			byttt.append((int) b);
		}
	
		outputStreamWriter.close();
	}
}
