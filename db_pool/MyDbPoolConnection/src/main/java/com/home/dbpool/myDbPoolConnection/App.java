package com.home.dbpool.myDbPoolConnection;

import com.home.dbpool.myDbPoolConnection.utility.*;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private Connection con= null;
	private PreparedStatement ps= null;
	private ResultSet rs= null;
	
	private String pwd=null;
	
	private int activeCon=15;
	private int activeConFromPojo=0;
	
	private PooledDataBaseUtility dbUtility = null;
	
	public App() {
		
		basicInit();
	}
	
	
	private void basicInit () {
		
        try {
        	
        	dbUtility = PooledDataBaseUtility.getInstance();
        	
        	if ( (con=dbUtility.getConnection()) !=null ) {
        		
        		System.out.print(" PASS" + con );
        		
        		dbUtility.printStatus();
        	} else {
        		
        		System.out.print("FAIL : Connection not created" + con );
        		return;
        	}
        	
        	final String sql = "Select password from User_Account where User_Name = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, "jerry");
            rs = pstm.executeQuery();
            
            if (rs.next()) {
                pwd = rs.getString("Password");
            }
            
            dbUtility.printStatus();
                        
            if (rs!=null) {
            	
            	rs.close();
            	if ( con!=null ) {
            		con.close();
            	}
            }
            
            dbUtility.printStatus();
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	public void multiThreadSetup () {
		
		final int tempCount = this.activeCon;
		System.out.println(" tempCount " + tempCount + "\n");
		
		try {
			

        	final String sql = "Select password from User_Account where User_Name = ?";

            String pwd1=null;
            
            // Create new Thread 
            new Thread(new Runnable() {

            	//Connection con1=null;
            	List<Connection> conList = new ArrayList();
            	List<String> resultVal = new ArrayList();
            	
				public void run() {
					
					try {
						
						// create list of con
						
						for ( int i =0; i< tempCount ; i++ ) {
							
							conList.add(dbUtility.getConnection());
							 System.out.println(" Connection  no " + i + "\n");
							dbUtility.printStatus();
						}
						
			            PreparedStatement pstm1 =null;
			            ResultSet rs1= null;
			            
						for ( int i =0; i< tempCount ; i++ ) {
							
							pstm1 = conList.get(i).prepareStatement(sql);
				            pstm1.setString(1, "jerry");
				            rs1 = pstm1.executeQuery();
				            
				            if ( rs1.next() ) {
				            	resultVal.add (rs1.getString("Password"));
				            }
						}
						
			            dbUtility.printStatus();
			            
			            System.out.println("\n SIZE of Con array   : " + conList.size()+ "; " +
			                    " SIZE of password array: " + resultVal.size() );
			        	
			            activeConFromPojo=dbUtility.getActiveConnectionCount();
			            
			    		for ( int i =0; i< tempCount ; i++ ) {
			    		
			                if ( conList.get(i) != null ) {
			                	conList.get(i).close();
			                }
			    		}
			            
					} catch( Exception exe) {
						
						exe.printStackTrace();
					}
				}
            	
            } ).start();
			
		} catch (Exception exe) {
			
			
		}
	}
	
    public static void main( String[] args )
    {
        System.out.println( "\n\n Hello World! " + new App().getUSerPwd());
        
    }
    
    
    public String getUSerPwd() {
    	
    	return pwd;
    }
    
    public int getActiveCon() {
    	
    	return this.activeCon;
    }
    
	public void setActiveCon(int activeCon) {
		
		this.activeCon = activeCon;
		
		 multiThreadSetup();
	}
	
    public int getActiveConFromPojo() {
    	
    	return activeConFromPojo;
    }
}
