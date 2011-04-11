/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package melmac.simulator;

import org.junit.Assert;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author karel_evzen
 */
public class BodyTest
{

    public BodyTest()
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
    World world = null;

    @Before
    public void setUp()
    {
        world = new World(new Vector2f(), 1);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of setMaxMovementSpeed method, of class Player.
     */
    @Test
    public void testRotation()
    {
        System.out.println("testRotation");

        Body instance = new Body(new Box(10, 10), 100);
        instance.setRotation((float) Math.PI);

        float rot = instance.getRotation();

        Assert.assertEquals(Math.PI, rot, 0.01);
    }

    @Test
    public void testRotationDirection()
    {
        System.out.println("testRotationDirection");

        Body instance = new Body(new Box(10, 10), 100);
        instance.setRotation(-(float) Math.PI);

        float rot = instance.getRotation();

        Assert.assertEquals(-Math.PI, rot, 0.01);
    }

    @Test
    public void testRotationRange()
    {
        System.out.println("testRotationRange");

        Body instance = new Body(new Box(10, 10), 100);
        instance.setRotation(3 * (float) Math.PI);

        float rot = instance.getRotation();

        Assert.assertEquals(3 * Math.PI, rot, 0.01);
    }

    @Test
    public void testRotationSpeed()
    {
        System.out.println("testRotationSpeed");

        Body instance = new Body(new Box(10, 10), 100);
        instance.adjustAngularVelocity((float) Math.PI);

        world.add(instance);

        world.step(1);

        float rot = instance.getRotation();

        Assert.assertEquals(Math.PI, rot, 0.01);
    }

    @Test
    public void testRotationSpeedRange()
    {
        System.out.println("testRotationSpeedRange");

        Body instance = new Body(new Box(10, 10), 100);
        instance.adjustAngularVelocity((float) Math.PI);

        world.add(instance);

        world.step(3);

        float rot = instance.getRotation();

        Assert.assertEquals(3 * Math.PI, rot, 0.01);
    }
}
