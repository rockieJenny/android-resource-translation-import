package com.lionmobi;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		new Translation().doTranslate("C:\\documents\\translate.xlsx", "C:\\works\\spaces\\security\\app\\src\\main\\res");
	}
	
	public static void  runTask(String dataPath,String rootPath) throws FileNotFoundException{
		new Translation().doTranslate(dataPath, rootPath);
	}
}
