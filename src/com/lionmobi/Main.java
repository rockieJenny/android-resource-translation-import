package com.lionmobi;

import java.util.concurrent.atomic.AtomicBoolean;


public class Main {
	
	
	public static void  runTask(String dataPath,String rootPath) throws Exception{
		new Translation().doTranslate(dataPath, rootPath);
		
	}
	
	public static void main(String[] args) {
		AtomicBoolean test = new AtomicBoolean();
		System.out.println(test.get());
		test.compareAndSet(true, true);
		System.out.println(test.get());
		if(test.getAndSet(true)){
			System.out.println(test.get());
		}
			
	}
}
