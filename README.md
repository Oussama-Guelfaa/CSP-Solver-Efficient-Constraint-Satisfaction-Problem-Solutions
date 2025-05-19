# Constraint Satisfaction Problems (CSP) - TP

This repository contains my implementation of the Constraint Satisfaction Problems (CSP) TP, based on Chapter 6 of "Artificial Intelligence: A Modern Approach" (AIMA).

## Guide to My TP Answers

This README file explains my answers to each section of the TP. For each section, I've indicated:
- Which files I created to answer the questions
- How to run the code
- The results and my analysis

### Quick Navigation:
- **Section 1 (Modeling Problem)**: Explained in Section 1 below, using the existing `MapCSP.java` file
- **Section 2 (Solving)**: Implemented in `MapColoringDemo.java` (Section 2 below)
- **Section 3 (Modeling CSPs: Exercises)**:
  - Brute Force Approach: `NaturalNQueenCSP.java` and `SolveNaturalNQueenCSP.java`
  - Refined Model: `RefinedNQueenCSP.java` and `SolveRefinedNQueenCSP.java`
  - Comparison: `CompareNQueenModels.java`

## 1. Modeling Problem

### 1.1 Classes of the Framework

The CSP framework is based on the following core classes:

- **CSP**: The main class representing a Constraint Satisfaction Problem. It contains:
  - Variables: A set of variables to be assigned values
  - Domains: A set of possible values for each variable
  - Constraints: Rules that restrict the allowed combinations of values

- **Variable**: Represents a variable in the CSP, identified by a name.

- **Domain**: Represents the set of possible values a variable can take.

- **Constraint**: An interface that defines constraints between variables. The main method is `isSatisfiedWith(Assignment)` which checks if an assignment satisfies the constraint.

- **Assignment**: Represents a mapping of variables to values. It can be:
  - Complete: All variables have assigned values
  - Consistent: All constraints are satisfied
  - Solution: Both complete and consistent

### 1.2 An Example of CSP Modeling - Australia Map Coloring

The Australia map coloring problem is modeled as follows:

- **Variables**: The states and territories of Australia (WA, NT, Q, NSW, V, SA, T)
- **Domain**: Three colors (RED, GREEN, BLUE)
- **Constraints**: Adjacent regions must have different colors (implemented as `NotEqualConstraint`)

The `MapCSP` class extends the `CSP` class and defines:
1. Variables for each state/territory
2. A domain of three colors for each variable
3. Constraints between adjacent regions

## 2. Solving

### 2.1 Classes of the Framework

To code a CSP solver, one should extend the `SolutionStrategy` abstract class. It essentially defines a `solve()` method that returns an assignment for a given CSP. It also provides some useful methods to trace step-by-step the solving process, using the `CSPStateListener` interface.

An example of solution strategy is the backtracking algorithm (class `BacktrackingStrategy`). When an assignment is done, the `fireStateChanged(assignment, csp)` method is triggered, so that another class listening to this one can trace, step-by-step, the solving process.

### 2.2 An Example of CSP Solving

To solve a CSP, you only have to call the `solve()` method. As requested in the TP, I've implemented the example code in the `MapColoringDemo.java` file:

```java
// This is the code I implemented in MapColoringDemo.java
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
```

When I executed this code, it displayed the following output:

```
Assignment evolved : {NSW=RED}
Assignment evolved : {NSW=RED, WA=RED}
Assignment evolved : {NSW=RED, WA=RED, NT=RED}
Assignment evolved : {NSW=RED, WA=RED, NT=GREEN}
...
Assignment evolved : {NSW=RED, WA=GREEN, NT=RED, Q=GREEN, SA=BLUE, V=GREEN, T=RED}
{NSW=RED, WA=GREEN, NT=RED, Q=GREEN, SA=BLUE, V=GREEN, T=RED}
Time to solve = 22.0
```

The output shows the step-by-step evolution of the assignment as the backtracking algorithm explores the search space. The final solution assigns RED to NSW, GREEN to WA, RED to NT, GREEN to Q, BLUE to SA, GREEN to V, and RED to T.

## 3. Modeling CSPs: Exercises

### 3.1 Brute Force Approach

The natural idea in modeling the N-Queens problem is to consider the cells of the chessboard as being the domain of the variable. The `ChessBoardCell` class manages the line and column parameters of a cell.

As required in the TP, I implemented:

- A class called `NaturalNQueenCSP.java` corresponding to this approach, where:
  - Each variable represents a queen
  - The domain is all possible cells on the chessboard (N×N cells)
  - The constraint ensures no two queens threaten each other (same row, column, or diagonal)

- A class called `SolveNaturalNQueenCSP.java` which solves the problem for an increasing number of queens (starting with 4 queens and increasing by 1)

#### Results for Brute Force Approach

For a 4×4 board with backtracking:
```
Solution found
Steps: 274
Time: 4ms
```

For an 8×8 board with backtracking:
```
Solution found
Steps: 1,660,100
Time: 1213ms
```

For the min-conflicts strategy with 10,000 steps:
- 4×4 board: Solution found in 201 steps, 5ms
- 6×6 board: No solution found within 10,000 steps
- 8×8 board: No solution found within 10,000 steps

#### Conclusion for Brute Force Approach

The brute force approach works well for small board sizes (4x4, 5x5), but becomes increasingly inefficient as the board size grows. For an 8x8 board, the backtracking algorithm required over 1.6 million steps and took more than 1 second to find a solution.

The min-conflicts heuristic struggled with the brute force model and often failed to find a solution within 10,000 steps for larger board sizes.

### 3.2 Refining the Model

As noted in the TP, the previous model for the N-Queen problem has a domain that is quickly too big to be efficient. Each queen's domain is too large. For an NxN board, there are N^2 possible placements for each queen, making the algorithms inefficient for large N.

Following the TP's suggestion to propose a smarter solution, I implemented:

- A class called `RefinedNQueenCSP.java` with a more efficient model where:
  - Each variable represents a column on the board
  - The domain of each variable is just the row where a queen can be placed (N possibilities instead of N^2)
  - This automatically satisfies the constraint that no two queens can be in the same column
  - The remaining constraints only check that no two queens are in the same row or diagonal

- A class called `SolveRefinedNQueenCSP.java` to solve the problem with this refined model

- A class called `CompareNQueenModels.java` to directly compare both approaches

#### Results for Refined Model

For a 4×4 board with backtracking:
```
Solution found
Steps: 26
Time: 0ms
```

For an 8×8 board with backtracking:
```
Solution found
Steps: 876
Time: 1ms
```

For the min-conflicts strategy with 10,000 steps:
- 4×4 board: Solution found in 64 steps, 1ms
- 6×6 board: Solution found in 1,289 steps, 4ms
- 8×8 board: Solution found in 4,761 steps, 13ms

#### Comparison Results

I implemented a `CompareNQueenModels` class to directly compare the performance of both approaches. Here are the results:

For an 8x8 board with backtracking:
- Brute Force: 1,660,100 steps, 1213ms
- Refined: 876 steps, 1ms
- Improvement: 1895.09x fewer steps, 1213.00x faster

For an 8x8 board with min-conflicts:
- Brute Force: No solution found within 10,000 steps
- Refined: Solution found in 4,761 steps, 13ms

#### Conclusion for Refined Model

The refined model is dramatically more efficient than the brute force approach. By reducing the domain size from N^2 to N and automatically satisfying one of the constraints (no two queens in the same column), the search space is significantly reduced.

This demonstrates the importance of carefully modeling a CSP. A good model can make the difference between a problem that is practically unsolvable and one that can be solved efficiently.

The min-conflicts strategy also performs much better with the refined model, finding solutions for larger board sizes where it failed with the brute force approach.

## 4. Summary of My TP Answers

In this TP, I have successfully completed all three required sections:

### Section 1: Modeling Problem
I analyzed the existing CSP framework and the Australia map coloring example to understand how CSPs are modeled. The key classes (CSP, Variable, Domain, Constraint, Assignment) work together to represent and solve constraint satisfaction problems.

### Section 2: Solving
I implemented the example code from the TP in `MapColoringDemo.java` to demonstrate how to use the `SolutionStrategy` class and the `CSPStateListener` interface to solve a CSP and trace the solving process step by step.

### Section 3: Modeling CSPs: Exercises
I implemented two different approaches to the N-Queens problem:

1. **Brute Force Approach** (`NaturalNQueenCSP.java`):
   - Each queen can be placed anywhere on the board (N² possibilities)
   - Performance degrades quickly as N increases (1.6 million steps for 8×8)
   - Min-conflicts often fails to find a solution for larger boards

2. **Refined Model** (`RefinedNQueenCSP.java`):
   - Each queen is assigned to a specific column (N possibilities)
   - Dramatically better performance (876 steps vs 1.6 million for 8×8)
   - Min-conflicts works well with this model

The comparison (`CompareNQueenModels.java`) clearly demonstrates how proper modeling can make a CSP much more efficient to solve. By reducing the domain size from N² to N and automatically satisfying one constraint, the refined model achieves orders of magnitude better performance.

This TP has taught me the importance of:
- Carefully modeling problems as CSPs
- Choosing appropriate algorithms based on problem characteristics
- Reducing domain sizes whenever possible
- Designing models that automatically satisfy some constraints

The CSP framework is a powerful tool for solving a wide range of problems, and the modeling choices can dramatically affect solution efficiency.

## How to Run the Code

### Compilation

First, compile all the Java files:

```
mkdir -p bin
find src -name "*.java" | xargs javac -d bin
```

### Running Each Section

#### Section 2 (Solving)
To run the Australia map coloring example with the code from Section 2.2:

```
java -cp bin fr.emse.ai.csp.australia.MapColoringDemo
```

This will show the step-by-step evolution of the assignment as described in the TP.

#### Section 3 (Modeling CSPs: Exercises)

To run the N-Queens problem with the brute force approach (Section 3.1):

```
java -cp bin fr.emse.ai.csp.nqueens.SolveNaturalNQueenCSP
```

To run the N-Queens problem with the refined approach (Section 3.2):

```
java -cp bin fr.emse.ai.csp.nqueens.SolveRefinedNQueenCSP
```

To compare the performance of both approaches (most informative):

```
java -cp bin fr.emse.ai.csp.nqueens.CompareNQueenModels
```

The comparison will show the dramatic performance difference between the two modeling approaches, demonstrating how the refined model is much more efficient.
