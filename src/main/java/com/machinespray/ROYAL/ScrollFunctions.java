package com.machinespray.ROYAL;

import java.util.ArrayList;

public class ScrollFunctions {
	public static ArrayList<Object[]> scrollRunnable = new ArrayList<Object[]>();
	public static void initRunnable(){
		scrollRunnable.add(new Object[]{
				new Runnable(){
					@Override
					public void run(){
						//TODO
					}
				},"Stinking Cloud"
		});
	}
}
