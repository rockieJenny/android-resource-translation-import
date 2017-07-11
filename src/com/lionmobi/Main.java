package com.lionmobi;


public class Main {
	
	public static void  runTask(String dataPath,String rootPath) throws Exception{
		new Translation().doTranslate(dataPath, rootPath);
	}
}
