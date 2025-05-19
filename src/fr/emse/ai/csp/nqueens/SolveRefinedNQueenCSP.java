/**
 * SolveRefinedNQueenCSP.java
 *
 * This class solves the N-Queens problem using the refined model
 * implemented in RefinedNQueenCSP.java. It tests the performance of both
 * backtracking and min-conflicts strategies for increasing board sizes.
 *
 * For each board size (starting from 4 and increasing), the class:
 * 1. Creates a RefinedNQueenCSP instance
 * 2. Solves it using backtracking and reports steps and time
 * 3. Solves it using min-conflicts and reports steps and time
 * 4. Displays the solution as a chessboard
 *
 * The results demonstrate that the refined model is much more efficient
 * than the brute force approach, especially for larger board sizes.
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
import fr.emse.ai.csp.core.Variable;

/**
 * Solves the N-Queens problem using the refined model
 * for an increasing number of queens.
 */
public class SolveRefinedNQueenCSP {

    public static void main(String[] args) {
        System.out.println("Solving N-Queens Problem with Refined Approach");
        System.out.println("=============================================");

        // Start with 4 queens and increase by 1
        for (int n = 4; n <= 12; n++) {
            System.out.println("\n" + n + "-Queens Problem");
            System.out.println("------------------");

            // Create the CSP
            RefinedNQueenCSP queensCSP = new RefinedNQueenCSP(n);

            // Solve with backtracking
            System.out.println("\n1. Solving with Backtracking Strategy");
            solveWithBacktracking(queensCSP, n);

            // Solve with min-conflicts
            System.out.println("\n2. Solving with Min-Conflicts Strategy");
            solveWithMinConflicts(queensCSP, n, 10000);
        }
    }

    /**
     * Solves the CSP using the Backtracking strategy
     */
    private static void solveWithBacktracking(CSP csp, int n) {
        SolutionStrategy strategy = new BacktrackingStrategy();

        // Add a listener to count steps
        final int[] stepCounter = {0};
        strategy.addCSPStateListener(new CSPStateListener() {
            @Override
            public void stateChanged(Assignment assignment, CSP csp) {
                stepCounter[0]++;
                // Don't print every step to avoid too much output
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

        // Print the solution
        if (solution != null) {
            System.out.println("Solution found after " + stepCounter[0] + " steps");
            System.out.println("Time taken: " + timeTaken + "ms");
            printQueensSolution(solution, n);
        } else {
            System.out.println("No solution found after " + stepCounter[0] + " steps");
            System.out.println("Time taken: " + timeTaken + "ms");
        }
    }

    /**
     * Solves the CSP using the Min-Conflicts strategy
     */
    private static void solveWithMinConflicts(CSP csp, int n, int maxSteps) {
        SolutionStrategy strategy = new MinConflictsStrategy(maxSteps);

        // Add a listener to count steps
        final int[] stepCounter = {0};
        strategy.addCSPStateListener(new CSPStateListener() {
            @Override
            public void stateChanged(Assignment assignment, CSP csp) {
                stepCounter[0]++;
                // Don't print every step to avoid too much output
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

        // Print the solution
        if (solution != null) {
            System.out.println("Solution found after " + stepCounter[0] + " steps");
            System.out.println("Time taken: " + timeTaken + "ms");
            printQueensSolution(solution, n);
        } else {
            System.out.println("No solution found after " + maxSteps + " steps");
            System.out.println("Time taken: " + timeTaken + "ms");
        }
    }

    /**
     * Prints the N-Queens solution as a chessboard
     */
    private static void printQueensSolution(Assignment solution, int n) {
        // Print the chessboard
        System.out.println("\nChessboard representation:");

        // Create an empty board
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }

        // Place queens on the board
        for (int col = 0; col < n; col++) {
            Variable var = solution.getVariables().get(col);
            Integer row = (Integer) solution.getAssignment(var);
            if (row != null) {
                board[row - 1][col] = 'Q';
            }
        }

        // Print the board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(" " + board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
