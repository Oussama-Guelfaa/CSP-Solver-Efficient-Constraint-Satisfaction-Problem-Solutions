/**
 * CompareNQueenModels.java
 *
 * This class directly compares the performance of the brute force approach
 * and the refined model for solving the N-Queens problem. It provides a
 * clear demonstration of how proper modeling can dramatically improve
 * the efficiency of CSP solving.
 *
 * For each board size (4 to 8), the class:
 * 1. Creates both a NaturalNQueenCSP (brute force) and a RefinedNQueenCSP instance
 * 2. Solves both using backtracking and compares steps and time
 * 3. Solves both using min-conflicts and compares steps and time
 * 4. Calculates and displays the improvement factors
 *
 * The results show that the refined model is orders of magnitude more efficient,
 * especially for larger board sizes (e.g., 1895x fewer steps for 8-Queens).
 *
 * @author Oussama GUELFAA
 * @date 19-05-2025
 */
package fr.emse.ai.csp.nqueens;

import fr.emse.ai.csp.core.Assignment;
import fr.emse.ai.csp.core.BacktrackingStrategy;
import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.CSPStateListener;
import fr.emse.ai.csp.core.MinConflictsStrategy;
import fr.emse.ai.csp.core.SolutionStrategy;

/**
 * Compares the performance of the brute force and refined approaches
 * for solving the N-Queens problem.
 */
public class CompareNQueenModels {

    public static void main(String[] args) {
        System.out.println("Comparing N-Queens Problem Models");
        System.out.println("================================");

        // Compare for different board sizes
        for (int n = 4; n <= 8; n++) {
            System.out.println("\n" + n + "-Queens Problem");
            System.out.println("------------------");

            // Create the CSPs
            NaturalNQueenCSP bruteForceCSP = new NaturalNQueenCSP(n);
            RefinedNQueenCSP refinedCSP = new RefinedNQueenCSP(n);

            // Compare with backtracking
            System.out.println("\nBacktracking Strategy Comparison:");
            System.out.println("--------------------------------");

            System.out.println("1. Brute Force Approach:");
            PerformanceResult bruteForceResult = solveWithBacktracking(bruteForceCSP);

            System.out.println("\n2. Refined Approach:");
            PerformanceResult refinedResult = solveWithBacktracking(refinedCSP);

            // Print comparison
            System.out.println("\nComparison (Backtracking):");
            System.out.println("- Brute Force: " + bruteForceResult.steps + " steps, " + bruteForceResult.time + "ms");
            System.out.println("- Refined: " + refinedResult.steps + " steps, " + refinedResult.time + "ms");
            System.out.println("- Improvement: " +
                    String.format("%.2f", (double)bruteForceResult.steps / refinedResult.steps) + "x fewer steps, " +
                    String.format("%.2f", (double)bruteForceResult.time / refinedResult.time) + "x faster");

            // Compare with min-conflicts
            System.out.println("\nMin-Conflicts Strategy Comparison:");
            System.out.println("----------------------------------");

            System.out.println("1. Brute Force Approach:");
            PerformanceResult bruteForceMinResult = solveWithMinConflicts(bruteForceCSP, 10000);

            System.out.println("\n2. Refined Approach:");
            PerformanceResult refinedMinResult = solveWithMinConflicts(refinedCSP, 10000);

            // Print comparison
            System.out.println("\nComparison (Min-Conflicts):");
            if (bruteForceMinResult.solved && refinedMinResult.solved) {
                System.out.println("- Brute Force: " + bruteForceMinResult.steps + " steps, " + bruteForceMinResult.time + "ms");
                System.out.println("- Refined: " + refinedMinResult.steps + " steps, " + refinedMinResult.time + "ms");
                System.out.println("- Improvement: " +
                        String.format("%.2f", (double)bruteForceMinResult.steps / refinedMinResult.steps) + "x fewer steps, " +
                        String.format("%.2f", (double)bruteForceMinResult.time / refinedMinResult.time) + "x faster");
            } else {
                if (!bruteForceMinResult.solved) {
                    System.out.println("- Brute Force: No solution found");
                }
                if (!refinedMinResult.solved) {
                    System.out.println("- Refined: No solution found");
                }
            }
        }
    }

    /**
     * Solves the CSP using the Backtracking strategy and returns performance metrics
     */
    private static PerformanceResult solveWithBacktracking(CSP csp) {
        SolutionStrategy strategy = new BacktrackingStrategy();

        // Add a listener to count steps
        final int[] stepCounter = {0};
        strategy.addCSPStateListener(new CSPStateListener() {
            @Override
            public void stateChanged(Assignment assignment, CSP csp) {
                stepCounter[0]++;
            }

            @Override
            public void stateChanged(CSP csp) {
                // Not used in this example
            }
        });

        // Solve the CSP
        long startTime = System.currentTimeMillis();
        Assignment solution = strategy.solve(csp);
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        // Return performance metrics
        PerformanceResult result = new PerformanceResult();
        result.steps = stepCounter[0];
        result.time = timeTaken;
        result.solved = (solution != null);

        System.out.println(result.solved ? "Solution found" : "No solution found");
        System.out.println("Steps: " + result.steps);
        System.out.println("Time: " + result.time + "ms");

        return result;
    }

    /**
     * Solves the CSP using the Min-Conflicts strategy and returns performance metrics
     */
    private static PerformanceResult solveWithMinConflicts(CSP csp, int maxSteps) {
        SolutionStrategy strategy = new MinConflictsStrategy(maxSteps);

        // Add a listener to count steps
        final int[] stepCounter = {0};
        strategy.addCSPStateListener(new CSPStateListener() {
            @Override
            public void stateChanged(Assignment assignment, CSP csp) {
                stepCounter[0]++;
            }

            @Override
            public void stateChanged(CSP csp) {
                // Not used in this example
            }
        });

        // Solve the CSP
        long startTime = System.currentTimeMillis();
        Assignment solution = strategy.solve(csp);
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        // Return performance metrics
        PerformanceResult result = new PerformanceResult();
        result.steps = stepCounter[0];
        result.time = timeTaken;
        result.solved = (solution != null);

        System.out.println(result.solved ? "Solution found" : "No solution found");
        System.out.println("Steps: " + result.steps);
        System.out.println("Time: " + result.time + "ms");

        return result;
    }

    /**
     * Class to store performance metrics
     */
    private static class PerformanceResult {
        int steps;
        long time;
        boolean solved;
    }
}
