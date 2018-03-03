package com.home.dbpool.myDbPoolConnection.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	private static Properties prop = new Properties();
	private static InputStream input = null;
	
	static {
		
		try {
			
			//input = new FileInputStream("common-config.properties");
			input = ReadProperties.class.getClassLoader().getResourceAsStream("common-config.properties");
			
			if (input !=null ) {
				// load a properties file
				prop.load(input);
				
				System.out.println( "-------- PASS :  common-config.properties file loaded into memory ---------"  + prop.getProperty("DRIVER") );
			} else {
				
				System.err.println( "-------- NO ways.. --------" );
			}
			
		} catch( Exception exe) {
			
			exe.printStackTrace();
			System.out.println( "-------- ERROR :  properties file read -------------" + exe.getMessage());
			throw new ExceptionInInitializerError(exe + "ERROR------");
		} finally {
			
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static String getValue(String name) {
		
		if (name !=null && name!="") {
			
			System.out.println( "-------- Return value is : ----------" +  prop.getProperty(name) ); 
			return prop.getProperty(name);
		} else {
			
			System.out.println( "-------- Return value is : ----------" + " NULL" ); 
			return null;
		}
		
	}
}
