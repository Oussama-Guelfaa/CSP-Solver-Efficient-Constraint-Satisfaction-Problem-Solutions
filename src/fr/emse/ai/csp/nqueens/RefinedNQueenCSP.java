/**
 * RefinedNQueenCSP.java
 *
 * This class implements a refined model for the N-Queens problem as required
 * in Section 3.2 of the TP. This model is much more efficient than the brute
 * force approach by significantly reducing the domain size.
 *
 * Key improvements in this model:
 * - Each variable represents a column on the board (not a queen)
 * - The domain is just the row where a queen can be placed (N possibilities instead of N²)
 * - This automatically satisfies the constraint that no two queens can be in the same column
 * - The remaining constraints only check for queens in the same row or diagonal
 *
 * The performance difference between this model and the brute force approach
 * is dramatic, especially for larger board sizes.
 *
 * @author Oussama GUELFAA
 * @date 19-05-2025
 */
package fr.emse.ai.csp.nqueens;

import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.Constraint;
import fr.emse.ai.csp.core.Domain;
import fr.emse.ai.csp.core.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a refined model for the N-Queens problem.
 * In this model, each variable represents a column, and its domain is the row where a queen can be placed.
 * This automatically satisfies the constraint that no two queens can be in the same column.
 */
public class RefinedNQueenCSP extends CSP {
    private int size;

    /**
     * Creates a new N-Queens problem with the refined approach.
     * @param n The size of the board and number of queens
     */
    public RefinedNQueenCSP(int n) {
        this.size = n;

        // Create variables, one for each column
        for (int i = 0; i < n; i++) {
            Variable var = new Variable("Q" + (i + 1));
            addVariable(var);
        }

        // Define domains - each queen can be placed in any row of its column
        Domain domain = new Domain(getAllPossibleRows(n));
        for (Variable var : getVariables()) {
            setDomain(var, domain);
        }

        // Add constraints - no two queens can be in the same row or diagonal
        addConstraint(new QueensConstraint(getVariables()));
    }

    /**
     * Creates an array of integers from 1 to n, representing the possible row positions.
     */
    private Integer[] getAllPossibleRows(int n) {
        Integer[] rows = new Integer[n];
        for (int i = 0; i < n; i++) {
            rows[i] = i + 1;
        }
        return rows;
    }

    /**
     * Constraint that ensures no two queens threaten each other.
     * Since queens are already in different columns, we only need to check rows and diagonals.
     */
    private static class QueensConstraint implements Constraint {
        private List<Variable> variables;

        public QueensConstraint(List<Variable> variables) {
            this.variables = new ArrayList<>(variables);
        }

        @Override
        public List<Variable> getScope() {
            return variables;
        }

        @Override
        public boolean isSatisfiedWith(fr.emse.ai.csp.core.Assignment assignment) {
            // Check if any two queens threaten each other
            for (int i = 0; i < variables.size(); i++) {
                Variable var1 = variables.get(i);
                Integer row1 = (Integer) assignment.getAssignment(var1);
                if (row1 == null) {
                    continue; // Skip unassigned variables
                }

                // Check against other queens
                for (int j = i + 1; j < variables.size(); j++) {
                    Variable var2 = variables.get(j);
                    Integer row2 = (Integer) assignment.getAssignment(var2);
                    if (row2 == null) {
                        continue; // Skip unassigned variables
                    }

                    // Check if queens are in the same row
                    if (row1.equals(row2)) {
                        return false;
                    }

                    // Check if queens are on the same diagonal
                    int col1 = i + 1; // Column of first queen (1-based)
                    int col2 = j + 1; // Column of second queen (1-based)

                    // Queens are on the same diagonal if the difference in rows
                    // equals the difference in columns
                    if (Math.abs(row1 - row2) == Math.abs(col1 - col2)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
