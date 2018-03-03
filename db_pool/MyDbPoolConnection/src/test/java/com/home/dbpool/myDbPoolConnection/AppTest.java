package com.home.dbpool.myDbPoolConnection;


import junit.framework.TestCase;
import junit.framework.TestSuite;


import junit.framework.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
*/
    /**
     * Rigourous Test :-)
     */
    @Test
    public void testApp()
    {
    	App app = new App();
    	app.setActiveCon(15);
    	Assert.assertEquals(app.getUSerPwd(), "jerry001");
    	//Assert.assertEquals(app.getActiveConFromPojo(), app.getActiveCon());
    }
}
