package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /** Placeholer test case. Write your own tests here.  Be sure to include
     * the @Test annotation above each test method, or JUnit will ignore it and
     * not run it as a test case. */
    @Test
    public void test00Nothing() {
        String fn = getGraphResource("Simple1.txt");
        Graph simple1;
        try {
          simple1 = ShortestPaths.parseGraph("basic", fn);
        } catch (FileNotFoundException e) {
          fail("Could not find graph Simple1.txt");
          return;
        }

        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addNeighbor(a, b)



        assertTrue(true);
        assertEquals(2+2, 4);
    }
}
