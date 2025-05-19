/**
 * MapColoringDemo.java
 *
 * This class implements the example from Section 2.2 of the TP to demonstrate
 * how to solve the Australia map coloring problem using the BacktrackingStrategy.
 * It shows how to use the CSPStateListener interface to trace the solving process
 * step by step, displaying the evolution of the assignment.
 *
 * The code creates a MapCSP instance, attaches a listener to track progress,
 * solves the CSP using backtracking, and displays the solution along with
 * the time taken to find it.
 *
 * @author Oussama GUELFAA
 * @date 19-05-2025
 */
package fr.emse.ai.csp.australia;

import fr.emse.ai.csp.core.Assignment;
import fr.emse.ai.csp.core.BacktrackingStrategy;
import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.CSPStateListener;

/**
 * Demonstrates how to solve the Australia map coloring problem
 * using the BacktrackingStrategy as shown in the TP.
 */
public class MapColoringDemo {

    public static void main(String[] args) {
        // Create the CSP as shown in the TP
        MapCSP map = new MapCSP();
        BacktrackingStrategy bts = new BacktrackingStrategy();
        bts.addCSPStateListener(new CSPStateListener() {
            @Override
            public void stateChanged(Assignment assignment, CSP csp) {
                System.out.println("Assignment evolved : " + assignment);
            }

            @Override
            public void stateChanged(CSP csp) {
                System.out.println("CSP evolved : " + csp);
            }
        });

        double start = System.currentTimeMillis();
        Assignment sol = bts.solve(map);
        double end = System.currentTimeMillis();
        System.out.println(sol);
        System.out.println("Time to solve = " + (end - start));
    }
}
