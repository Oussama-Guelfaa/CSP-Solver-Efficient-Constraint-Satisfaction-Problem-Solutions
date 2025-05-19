/**
 * NaturalNQueenCSP.java
 *
 * This class implements the brute force approach for the N-Queens problem
 * as required in Section 3.1 of the TP. In this approach, each variable
 * represents a queen, and its domain is all possible cells on the chessboard.
 *
 * The implementation includes:
 * - A constructor that creates N variables (one for each queen)
 * - A domain consisting of all possible cells on the NxN board
 * - A constraint that ensures no two queens threaten each other
 *
 * This approach has a large domain size (N² cells for each queen), which
 * makes it inefficient for larger board sizes.
 *
 * @author Oussama GUELFAA
 * @date 19-05-2025
 */
package fr.emse.ai.csp.nqueens;

import fr.emse.ai.csp.core.CSP;
import fr.emse.ai.csp.core.Constraint;
import fr.emse.ai.csp.core.Domain;
import fr.emse.ai.csp.core.Variable;
import fr.emse.ai.csp.core.ChessBoardCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the N-Queens problem using a brute force approach.
 * Each variable represents a queen, and its domain is all possible cells on the chessboard.
 */
public class NaturalNQueenCSP extends CSP {
    private int size;

    /**
     * Creates a new N-Queens problem with the brute force approach.
     * @param n The size of the board and number of queens
     */
    public NaturalNQueenCSP(int n) {
        this.size = n;

        // Create variables, one for each queen
        for (int i = 0; i < n; i++) {
            Variable var = new Variable("Q" + (i + 1));
            addVariable(var);
        }

        // Define domains - each queen can be placed in any cell of the board
        Domain domain = new Domain(getAllPossibleCells(n));
        for (Variable var : getVariables()) {
            setDomain(var, domain);
        }

        // Add constraints - no two queens can threaten each other
        addConstraint(new QueensConstraint(getVariables()));
    }

    /**
     * Creates an array of all possible cells on the chessboard.
     */
    private ChessBoardCell[] getAllPossibleCells(int n) {
        ChessBoardCell[] cells = new ChessBoardCell[n * n];
        int index = 0;
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                cells[index++] = new ChessBoardCell(row, col);
            }
        }
        return cells;
    }

    /**
     * Constraint that ensures no two queens threaten each other.
     * Two queens threaten each other if they share the same row, column, or diagonal.
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
                ChessBoardCell cell1 = (ChessBoardCell) assignment.getAssignment(var1);
                if (cell1 == null) {
                    continue; // Skip unassigned variables
                }

                // Check against other queens
                for (int j = i + 1; j < variables.size(); j++) {
                    Variable var2 = variables.get(j);
                    ChessBoardCell cell2 = (ChessBoardCell) assignment.getAssignment(var2);
                    if (cell2 == null) {
                        continue; // Skip unassigned variables
                    }

                    // Check if queens are in the same row
                    if (cell1.getL() == cell2.getL()) {
                        return false;
                    }

                    // Check if queens are in the same column
                    if (cell1.getC() == cell2.getC()) {
                        return false;
                    }

                    // Check if queens are on the same diagonal
                    if (cell1.samediagonal(cell2.getL(), cell2.getC())) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
