package com.home.dbpool.myDbPoolConnection.utility;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.beans.PropertyVetoException;
import java.io.IOException;

// https://www.javatips.net/blog/dbcp-connection-pooling-example

// https://kodejava.org/how-do-i-create-a-database-connection-pool/

public class DataBaseUtility {

    private static volatile DataBaseUtility datasource =null;   
    private static BasicDataSource ds =null;
	 
    private static Object mutex = new Object();
    
	private DataBaseUtility( ) throws IOException, SQLException, PropertyVetoException, Exception {
		        
		setUpBasic();
	}
	
	private void setUpBasic() throws Exception {
		
        ds = new BasicDataSource();
        
        ds.setDriverClassName(	ReadProperties.getValue(UtilityValue.DRIVER)	);
        ds.setUsername(	ReadProperties.getValue(UtilityValue.DB_USER)	);
        ds.setPassword(	ReadProperties.getValue(UtilityValue.DB_PASSWORD)	);
        ds.setUrl(	ReadProperties.getValue( UtilityValue.DB_PROTOCOL )+ "://" + 
        			ReadProperties.getValue( UtilityValue.DB_ADDR )+ "/" +  
        			ReadProperties.getValue( UtilityValue.DB_NAME));
        
        System.out.println(" URL :- " + ReadProperties.getValue( UtilityValue.DB_PROTOCOL )+ "://" + 
        					ReadProperties.getValue( UtilityValue.DB_ADDR )+ "/" +  
        					ReadProperties.getValue( UtilityValue.DB_NAME));
        
        // -- Following are Pooling options
        
        // MAX/ MIN idle connections that should be there in the connection pool
        ds.setMinIdle(5); 
        ds.setMaxIdle(20);
        
        // setInitialSize() that sets the initial size of the connection pool. 
        // These many connection will immediately be created and put to connection pool.
        ds.setInitialSize(5); 
        ds.setMaxTotal(20); // maximum size of the connection pool
        ds.setMaxOpenPreparedStatements(180);
	}
	
    public static DataBaseUtility getInstance() throws Exception {
    	
    	DataBaseUtility result = datasource;
    	
    	if (result == null) { // As when result is NULL, only then will consider multi-thread sync issue, 
    		
    		// https://www.journaldev.com/171/thread-safety-in-java-singleton-classes-with-example-code
        	synchronized (mutex) {
        		
                if (result == null) {
                	
                	result = datasource = new DataBaseUtility();
                    System.out.println(" DataBaseUtility instance created..\n");
                }
        	}
    		
    	}
    	return result;
    }
    
    public Connection getConnection() throws SQLException {
    	
    	Connection con=null;
    	try {
    		
    		con=ds.getConnection();
    	} catch ( SQLException sx) {
    		
    		System.out.println("FAIL : DB connection issue " + sx.getMessage() );
    		
    	} 
        return con;
    }
    
    public static void printStatus() {
        System.out.println("Max   : " + ds.getMaxTotal() + "; " +
                "Active: " + ds.getNumActive() + "; " +
                "Idle  : " + ds.getNumIdle());
    }

}
