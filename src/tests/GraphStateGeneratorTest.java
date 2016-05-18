package tests;

import smartCars.GraphState;
import smartCars.GraphStateGenerator;
import org.junit.Test;
import smartCars.SvgGenerator;
import java.io.File;

/**
 * Created by tc on 18/05/16.
 */
public class GraphStateGeneratorTest {
    @Test
    public void test() {
        int a = 20;
        int b = 1;
        GraphState graphState = GraphStateGenerator.GraphStateGenerator(a,b);
        File output = new File("media/output/test.svg");
        new SvgGenerator(graphState, output);
    }
}
