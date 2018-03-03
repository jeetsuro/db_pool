package com.home.dbpool.myDbPoolConnection.utility;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;

// // https://kodejava.org/how-do-i-create-a-database-connection-pool/

public class PooledDataBaseUtility {

	private static volatile PooledDataBaseUtility datasource =null;
    private PoolingDataSource poolingDataSource = null;
    private static GenericObjectPool connectionPool = null;
    private static Object mutex = new Object();
    
    private PooledDataBaseUtility( ) throws Exception {
    	
    	setUpPooled();
    	 System.out.println(" PooledDataBaseUtility set-up completed..\n");
    }
    
    public static PooledDataBaseUtility getInstance() throws Exception {
  	    	
    	PooledDataBaseUtility result = datasource; // accessing the data from Volatile instance variable
    	
    	if (result == null) { // As when result is NULL, only then will consider multi-thread sync issue, 
    		
    		// https://www.journaldev.com/171/thread-safety-in-java-singleton-classes-with-example-code
        	synchronized (mutex) {
        		
                if (result == null) {
                	
                	result = datasource = new PooledDataBaseUtility();
                    System.out.println(" PooledDataBaseUtility instance created..\n");
                }
        	}
    	}
    	
    	return result;
    }
    
	private void setUpPooled()  {
		
        // Load JDBC Driver class.
        try {
        	
			Class.forName(ReadProperties.getValue(UtilityValue.DRIVER)).newInstance();
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                      
        String tempURL= ReadProperties.getValue( UtilityValue.DB_PROTOCOL )+ "://" + 
    			ReadProperties.getValue( UtilityValue.DB_ADDR )+ "/" +  
    			ReadProperties.getValue( UtilityValue.DB_NAME);
        
        System.out.println( " Mysql DB url : "  + tempURL );
        
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                tempURL,
                ReadProperties.getValue(UtilityValue.DB_USER),
                ReadProperties.getValue(UtilityValue.DB_PASSWORD));
        
        PoolableConnectionFactory poolableConnectionFactory =
        	     new PoolableConnectionFactory(connectionFactory, null);
        
        // Creates an instance of GenericObjectPool that holds our pool of connections object.
        connectionPool = new GenericObjectPool(poolableConnectionFactory);
        connectionPool.setMinIdle(10);
        connectionPool.setMaxIdle(25);
        connectionPool.setMaxTotal(30);
        connectionPool.setMaxWaitMillis(180);
        
        poolingDataSource = new PoolingDataSource(connectionPool);
	}
	    
    public Connection getConnection() throws SQLException {
    	
    	Connection con = null;
    	try {
    		
    		con = poolingDataSource.getConnection();
    	} catch ( SQLException sexe ) {
    		
    		System.out.println( "FAIL : DB connection issue "  + sexe.getMessage() );
    		throw sexe;
    	}
        return con;
    }
    
    public void printStatus() {
        System.out.println("Max   : " + connectionPool.getMaxTotal() + "; " +
                "Active: " + connectionPool.getNumActive() + "; " +
                "Idle  : " + connectionPool.getNumIdle());
    }
    
    public int getActiveConnectionCount() {
    	
    	return connectionPool.getNumActive();
    }
}
