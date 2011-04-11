package melmac.core.world;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RobotInfoTest
{

    public RobotInfoTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getPosition method, of class RobotInfo.
     */
    @Test
    public void testGetPosition()
    {
        System.out.println("getPosition");
        RobotInfo instance = new RobotInfo(new Point(1, 2), new Point(0, 0), true, new Point(-1, -2));
        Point result = instance.getPosition();
        assertEquals(1, result.getX());
        assertEquals(2, result.getY());
    }

    /**
     * Test of getDirectionVector method, of class RobotInfo.
     */
    @Test
    public void testGetDirectionVector()
    {
        System.out.println("getDirectionVector");
        RobotInfo instance = new RobotInfo(new Point(1, 2), new Point(0, 0), true, new Point(-1, -2));
        Point result = instance.getDirection();
        assertEquals(-1, result.getX());
        assertEquals(-2, result.getY());
    }

    /**
     * Test of getAbsoluteAngle method, of class RobotInfo.
     */
    @Test
    public void testGetAbsoluteAngle()
    {
        System.out.println("getAbsoluteAngle");
        RobotInfo instance = new RobotInfo(new Point(1, 2), new Point(0, 0), true, new Point(1, 0));
        double expResult = 90;
        double result = instance.getAbsoluteAngle();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetAbsoluteAngle2()
    {
        System.out.println("getAbsoluteAngle2");
        RobotInfo instance = new RobotInfo(new Point(1, 2), new Point(0, 0), true, new Point(1, -1));
        double expResult = 45;
        double result = instance.getAbsoluteAngle();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getRelativeAngleTo method, of class RobotInfo.
     */
    @Test
    public void testGetRelativeAngleTo()
    {
        System.out.println("getRelativeAngleTo");
        Point point = new Point(1, 0);
        RobotInfo instance = new RobotInfo(new Point(1, 2), new Point(0, 0), true, new Point(1, -1));
        double expResult = -45;
        double result = instance.getRelativeAngleTo(point);
        assertEquals(expResult, result, 0.0);
    }
}
