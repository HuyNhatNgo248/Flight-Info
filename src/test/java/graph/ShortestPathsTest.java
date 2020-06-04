package graph;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.*;

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

    /**
     * Placeholer test case. Write your own tests here.  Be sure to include
     * the @Test annotation above each test method, or JUnit will ignore it and
     * not run it as a test case.
     */
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

        assertTrue(true);
        assertEquals(2 + 2, 4);
    }

    @Test
    //Test if file is in the correct directory and can be found.
    public void test01ValidFilePath() {
        String fn0 = getGraphResource("Simple0.txt");
        String fn1 = getGraphResource("Simple1.txt");
        String fn2 = getGraphResource("Simple2.txt");
        String fn3 = getGraphResource("FakeCanada.txt");
        try {
            Graph simple1 = ShortestPaths.parseGraph("basic", fn0);
            Graph simple2 = ShortestPaths.parseGraph("basic", fn1);
            Graph simple3 = ShortestPaths.parseGraph("basic", fn2);
            Graph simple4 = ShortestPaths.parseGraph("basic", fn3);
        } catch (FileNotFoundException e) {
            fail("Could not find file");
            return;
        }
    }

    @Test
    //Test for corresponding shortest path length given origin and dest for simple0 file.
    public void test02DjikstraSimple0ShortstPathLength() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("Simple0.txt");

        try {
            Graph simple0 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple0.getNode("A");
            sh.compute(origin);
            assertEquals(0.0, sh.shortestPathLength(simple0.getNode("A")), 0.0001);
            assertEquals(2.0, sh.shortestPathLength(simple0.getNode("C")), 0.0001);
            assertEquals(1.0, sh.shortestPathLength(simple0.getNode("B")), 0.0001);

            Node origin1 = simple0.getNode("B");
            sh.compute(origin1);
            assertEquals(0.0, sh.shortestPathLength(simple0.getNode("B")), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, sh.shortestPathLength(simple0.getNode("C")), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, sh.shortestPathLength(simple0.getNode("A")), 0.0001);

            Node origin2 = simple0.getNode("C");
            sh.compute(origin2);
            assertEquals(2.0, sh.shortestPathLength(simple0.getNode("B")), 0.0001);
            assertEquals(0.0, sh.shortestPathLength(simple0.getNode("C")), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, sh.shortestPathLength(simple0.getNode("A")), 0.0001);
        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test shortest path in term of linkedlist arrayList
    public void test03DjikstraSimple0ShortstPath() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("Simple0.txt");
        LinkedList<Node> ans = new LinkedList<>();
        try {
            Graph simple0 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple0.getNode("A");
            sh.compute(origin);
            //shortest path from A to B
            ans = initializeList("A B", simple0);
            assertEquals(ans, sh.shortestPath(simple0.getNode("B")));

            //shortest path from A to C
            ans = initializeList("A C", simple0);
            assertEquals(ans, sh.shortestPath(simple0.getNode("C")));

            //shortest path from A to A
            ans = initializeList("A", simple0);
            assertEquals(ans, sh.shortestPath(simple0.getNode("A")));

            origin = simple0.getNode("B");
            sh.compute(origin);
            //shortest path from B to B
            ans = initializeList("B", simple0);
            assertEquals(ans, sh.shortestPath(simple0.getNode("B")));

            //shortest path from B to C
            assertNull(sh.shortestPath(simple0.getNode("C")));

            //shortest path from B to A
            assertNull(sh.shortestPath(simple0.getNode("A")));

        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test for corresponding shortest path length given origin and dest for simple1 file.
    public void test04DjikstraSimple1ShortstPathLength() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("Simple1.txt");

        try {
            Graph simple1 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple1.getNode("A");
            sh.compute(origin);
            assertEquals(0.0, sh.shortestPathLength(simple1.getNode("A")), 0.0001);
            assertEquals(4.0, sh.shortestPathLength(simple1.getNode("D")), 0.0001);
            assertEquals(2.0, sh.shortestPathLength(simple1.getNode("C")), 0.0001);
            assertEquals(5.0, sh.shortestPathLength(simple1.getNode("S")), 0.0001);
            assertEquals(1.0, sh.shortestPathLength(simple1.getNode("B")), 0.0001);

            origin = simple1.getNode("B");
            sh.compute(origin);
            assertEquals(4.0, sh.shortestPathLength(simple1.getNode("D")), 0.0001);
            assertEquals(5.0, sh.shortestPathLength(simple1.getNode("S")), 0.0001);
            assertEquals(13.0, sh.shortestPathLength(simple1.getNode("A")), 0.0001);
            assertEquals(0.0, sh.shortestPathLength(simple1.getNode("B")), 0.0001);
            assertEquals(10.0, sh.shortestPathLength(simple1.getNode("C")), 0.0001);

            origin = simple1.getNode("C");
            sh.compute(origin);
            assertEquals(2.0, sh.shortestPathLength(simple1.getNode("D")), 0.0001);
            assertEquals(3.0, sh.shortestPathLength(simple1.getNode("S")), 0.0001);
            assertEquals(3.0, sh.shortestPathLength(simple1.getNode("A")), 0.0001);
            assertEquals(4.0, sh.shortestPathLength(simple1.getNode("B")), 0.0001);
            assertEquals(0.0, sh.shortestPathLength(simple1.getNode("C")), 0.0001);
        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test shortest path in term of linkedlist arrayList
    public void test05DjikstraSimple1ShortstPath() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("Simple1.txt");
        LinkedList<Node> ans;
        try {
            Graph simple1 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple1.getNode("A");
            sh.compute(origin);
            //shortest path from A to B
            ans = initializeList("A B", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("B")));
            //shortest path from A to C
            ans = initializeList("A C", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("C")));
            //shortest path from A to A
            ans = initializeList("A C D", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("D")));
            //shortest path from A to S
            ans = initializeList("A C D S", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("S")));


            origin = simple1.getNode("B");
            sh.compute(origin);
            //shortest path from B to B
            ans = initializeList("B", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("B")));
            //shortest path from B to C
            ans = initializeList("B D S C", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("C")));
            //shortest path from B to D
            ans = initializeList("B D", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("D")));
            //shortest path from B to s
            ans = initializeList("B D S", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("S")));
            //shortest path from B to A
            ans = initializeList("B D S C A", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("A")));

        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test for corresponding shortest path length given origin and dest for simple2 file.
    public void test06DjikstraSimple2ShortstPathLength() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("Simple2.txt");

        try {
            Graph simple2 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple2.getNode("D");
            sh.compute(origin);
            assertEquals(1.0, sh.shortestPathLength(simple2.getNode("H")), 0.0001);
            assertEquals(0.0, sh.shortestPathLength(simple2.getNode("D")), 0.0001);
            assertEquals(5.0, sh.shortestPathLength(simple2.getNode("E")), 0.0001);
            assertEquals(8.0, sh.shortestPathLength(simple2.getNode("F")), 0.0001);
            assertEquals(9.0, sh.shortestPathLength(simple2.getNode("I")), 0.0001);
            assertEquals(11.0, sh.shortestPathLength(simple2.getNode("J")), 0.0001);

            origin = simple2.getNode("H");
            sh.compute(origin);
            assertEquals(9.0, sh.shortestPathLength(simple2.getNode("I")), 0.0001);
            assertEquals(12.0, sh.shortestPathLength(simple2.getNode("G")), 0.0001);
            assertEquals(11.0, sh.shortestPathLength(simple2.getNode("C")), 0.0001);
            assertNull(sh.shortestPath(simple2.getNode("A")));


            origin = simple2.getNode("G");
            sh.compute(origin);
            assertNull(sh.shortestPath(simple2.getNode("J")));
            assertNull(sh.shortestPath(simple2.getNode("F")));
            assertNull(sh.shortestPath(simple2.getNode("I")));
            assertNull(sh.shortestPath(simple2.getNode("H")));
        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test shortest path in term of linkedlist arrayList
    public void test07DjikstraSimple2ShortstPath() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("Simple2.txt");
        LinkedList<Node> ans;
        try {
            Graph simple2 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple2.getNode("A");
            sh.compute(origin);
            //shortest path from A to B
            ans = initializeList("A E F B", simple2);
            assertEquals(ans, sh.shortestPath(simple2.getNode("B")));
            //shortest path from A to C
            ans = initializeList("A E F C", simple2);
            assertEquals(ans, sh.shortestPath(simple2.getNode("C")));
            //shortest path from A to A
            ans = initializeList("A E F I J", simple2);
            assertEquals(ans, sh.shortestPath(simple2.getNode("J")));
            //shortest path from A to S
            ans = initializeList("A E F I J G", simple2);
            assertEquals(ans, sh.shortestPath(simple2.getNode("G")));

            origin = simple2.getNode("B");
            sh.compute(origin);
            //shortest path from A to B
            ans = initializeList("B", simple2);
            assertEquals(ans, sh.shortestPath(simple2.getNode("B")));
            assertNull(sh.shortestPath(simple2.getNode("G")));
            assertNull(sh.shortestPath(simple2.getNode("F")));
            assertNull(sh.shortestPath(simple2.getNode("I")));
            assertNull(sh.shortestPath(simple2.getNode("J")));

        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test for corresponding shortest path length given origin and dest for FakeCanada file.
    public void test08DjikstraFakeCanadaShortstPathLength() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("FakeCanada.txt");

        try {
            Graph simple1 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple1.getNode("YUL");
            sh.compute(origin);
            assertEquals(94.0, sh.shortestPathLength(simple1.getNode("YOW")), 0.0001);
            assertEquals(320.0, sh.shortestPathLength(simple1.getNode("YYZ")), 0.0001);
            assertEquals(1995.0, sh.shortestPathLength(simple1.getNode("YYC")), 0.0001);
            assertEquals(2423.0, sh.shortestPathLength(simple1.getNode("YVR")), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, sh.shortestPathLength(simple1.getNode("YUR")), 0.0001);
        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }

    @Test
    //Test shortest path in term of linkedlist arrayList
    public void test09DjikstraFakeCanadaShortstPath() {
        ShortestPaths sh = new ShortestPaths();
        String fn0 = getGraphResource("FakeCanada.txt");
        LinkedList<Node> ans;
        try {
            Graph simple1 = ShortestPaths.parseGraph("basic", fn0);
            Node origin = simple1.getNode("YUL");
            sh.compute(origin);
            //shortest path from A to B
            ans = initializeList("YUL YOW", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("YOW")));
            //shortest path from A to C
            ans = initializeList("YUL YOW YYZ", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("YYZ")));
            //shortest path from A to A
            ans = initializeList("YUL YOW YYZ YYC", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("YYC")));
            //shortest path from A to S
            ans = initializeList("YUL YOW YYZ YYC YVR", simple1);
            assertEquals(ans, sh.shortestPath(simple1.getNode("YVR")));
            assertNull(sh.shortestPath(simple1.getNode("YUR")));

        } catch (FileNotFoundException ex) {
            fail("Could not find file");
            return;
        } catch (NumberFormatException ex) {
            fail("Wrong number type");
        }
    }


    /*
     * Initialize linkedlist with expected node that should be contained
     * in the final result
     * Pre: elements and graph aren't null
     */
    public LinkedList<Node> initializeList(String elements, Graph gr) {
        String[] e = elements.split(" ");
        LinkedList<Node> ans = new LinkedList<>();
        //pass elements into new LinkedList
        for (String n : e)
            ans.add(gr.getNode(n));
        return ans;
    }
}
