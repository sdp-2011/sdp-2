package melmac.core.world;

import melmac.core.utils.Angle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AngleTest
{

    public AngleTest()
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
     * Test of getAbsoluteAngle method, of class Angle.
     */
    @Test
    public void testGetAbsoluteAngle90Deg()
    {
        System.out.println("getAbsoluteAngle");
        Point vector = new Point(1, 0);
        double expResult = 90;
        double result = Angle.getAbsoluteAngle(vector);
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetAbsoluteAngle0Deg()
    {
        System.out.println("getAbsoluteAngle");
        Point vector = new Point(0, 1);
        double expResult = 180;
        double result = Angle.getAbsoluteAngle(vector);
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetAbsoluteAngle180Deg()
    {
        System.out.println("getAbsoluteAngle");
        Point vector = new Point(0, -1);
        double expResult = 0;
        double result = Angle.getAbsoluteAngle(vector);
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetAbsoluteAngle270Deg()
    {
        System.out.println("getAbsoluteAngle");
        Point vector = new Point(-1, 0);
        double expResult = 270;
        double result = Angle.getAbsoluteAngle(vector);
        assertEquals(expResult, result, 0.0);
    }
}
