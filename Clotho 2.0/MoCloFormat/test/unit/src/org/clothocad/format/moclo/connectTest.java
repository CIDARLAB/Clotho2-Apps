package org.clothocad.format.moclo;

import java.util.ArrayList;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.NucSeq;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Plasmid;
import org.clothocore.api.data.Vector;
import org.junit.Test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ernstl
 */
public class connectTest {
    
    public connectTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class connect.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        connect instance = new connect();
        instance.init();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkPart method, of class connect.
     */
    @Test
    public void testCheckPart() {
        System.out.println("checkPart");
        
        System.out.println(Format.retrieveByName("org-clothocad-format-moclo-connect"));

        Format partFormat = Format.retrieveByName("org-clothocad-format-moclo-connect");

                
        Part p = Part.generateBasic(
                "TestPart",
                "TestPart",  
                "GGTCTCAGGAGATGTCTTCXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXGAAGACGTTACTAGAGACC",  
                partFormat,  
                null);
        
        connect instance = new connect();
        boolean expResult = false;
        boolean result = instance.checkPart(p);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkVector method, of class connect.
     */
    @Test
    public void testCheckVector() {
        System.out.println("checkVector");
        Vector v = null;
        connect instance = new connect();
        boolean expResult = false;
        boolean result = instance.checkVector(v);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkComposite method, of class connect.
     */
    @Test
    public void testCheckComposite() {
        System.out.println("checkComposite");
        ArrayList<Part> composition = null;
        Object additionalRequirements = null;
        connect instance = new connect();
        boolean expResult = false;
        boolean result = instance.checkComposite(composition, additionalRequirements);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkPlasmid method, of class connect.
     */
    @Test
    public void testCheckPlasmid() {
        System.out.println("checkPlasmid");
        Part p = null;
        Vector v = null;
        Object additionalRequirements = null;
        connect instance = new connect();
        boolean expResult = false;
        boolean result = instance.checkPlasmid(p, v, additionalRequirements);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateBasicPartSequence method, of class connect.
     */
    @Test
    public void testGenerateBasicPartSequence() {
        System.out.println("generateBasicPartSequence");
        Part objPart = null;
        connect instance = new connect();
        NucSeq expResult = null;
        NucSeq result = instance.generateBasicPartSequence(objPart);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateCompositeSequence method, of class connect.
     */
    @Test
    public void testGenerateCompositeSequence() {
        System.out.println("generateCompositeSequence");
        ArrayList<Part> composition = null;
        Object additionalRequirements = null;
        connect instance = new connect();
        NucSeq expResult = null;
        NucSeq result = instance.generateCompositeSequence(composition, additionalRequirements);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generatePlasmidSequence method, of class connect.
     */
    @Test
    public void testGeneratePlasmidSequence() {
        System.out.println("generatePlasmidSequence");
        Plasmid p = null;
        connect instance = new connect();
        NucSeq expResult = null;
        NucSeq result = instance.generatePlasmidSequence(p);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateSequencingRegion method, of class connect.
     */
    @Test
    public void testGenerateSequencingRegion() {
        System.out.println("generateSequencingRegion");
        Plasmid p = null;
        connect instance = new connect();
        NucSeq expResult = null;
        NucSeq result = instance.generateSequencingRegion(p);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
