/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package melmac.core.utils;

import melmac.core.world.Point;
import melmac.core.world.WorldState;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author karel_evzen
 */
public class PathTest
{

    public PathTest()
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
     * Test of isInAWay method, of class Path.
     */
    @Test
    public void testIsInAWaySimple()
    {
        System.out.println("isInAWay");
        Point source = new Point(1, 1);
        Point destination = new Point(3, 1);
        Point obstacle = new Point(2, 1);
        int width = 1;
        boolean expResult = true;
        boolean result = Path.isInAWay(source, destination, obstacle, width);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsInAWayComplexOut()
    {
        System.out.println("isInAWay");
        Point source = new Point(1, 1);
        Point destination = new Point(10, 10);
        Point obstacle = new Point(3, 1);
        int width = 1;
        boolean expResult = false;
        boolean result = Path.isInAWay(source, destination, obstacle, width);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsInAWayComplexIn()
    {
        System.out.println("isInAWay");
        Point source = new Point(1, 1);
        Point destination = new Point(10, 10);
        Point obstacle = new Point(3, 1);
        int width = 2;
        boolean expResult = true;
        boolean result = Path.isInAWay(source, destination, obstacle, width);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsInAWaySuperComplexIn()
    {
        System.out.println("isInAWay");
        Point source = new Point(1, 1);
        Point destination = new Point(10, 5);
        Point obstacle = new Point(8, 5);
        int width = 1;
        boolean expResult = true;
        boolean result = Path.isInAWay(source, destination, obstacle, width);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsInAWaySuperComplexOut()
    {
        System.out.println("isInAWay");
        Point source = new Point(1, 1);
        Point destination = new Point(10, 5);
        Point obstacle = new Point(9, 6);
        int width = 1;
        boolean expResult = false;
        boolean result = Path.isInAWay(source, destination, obstacle, width);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsInAWayOut()
    {
        System.out.println("isInAWay");
        Point source = new Point(1, 1);
        Point destination = new Point(10, 10);
        Point obstacle = new Point(11, 11);
        int width = 5;
        boolean expResult = false;
        boolean result = Path.isInAWay(source, destination, obstacle, width);
        assertEquals(expResult, result);
    }

    /**
     * Test of isBallInTheWay method, of class Path.
     */
    @Test
    public void testIsBallInTheWay()
    {
        System.out.println("isBallInTheWay");
        WorldState state = null;
        Point target = null;
        boolean expResult = false;
        boolean result = Path.isBallInTheWay(state, target);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBallInPossession method, of class Path.
     */
    @Test
    public void testIsBallInPossession()
    {
        System.out.println("isBallInPossession");
        WorldState state = null;
        boolean expResult = false;
        boolean result = Path.isBallInPossession(state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBallInOppPossession method, of class Path.
     */
    @Test
    public void testIsBallInOppPossession()
    {
        System.out.println("isBallInOppPossession");
        WorldState state = null;
        boolean expResult = false;
        boolean result = Path.isBallInOppPossession(state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of areWeGoalkeeper method, of class Path.
     */
    @Test
    public void testAreWeGoalkeeper()
    {
        System.out.println("areWeGoalkeeper");
        WorldState state = null;
        boolean expResult = false;
        boolean result = Path.areWeGoalkeeper(state);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
