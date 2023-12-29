Start with a complete, valid board (filled with 81 numbers).

Make a list of all 81 cell positions and shuffle it randomly.

As long as the list is not empty, take the next position from the list and remove the number from the related cell.

Test uniqueness using a fast backtracking solver. My solver is - in theory - able to count all solutions, but for testing uniqueness, it will stop immediately when it finds more than one solution.

If the current board has still just one solution, goto step 3) and repeat.

If the current board has more than one solution, undo the last removal (step 3), and continue step 3 with the next position from the list

Stop when you have tested all 81 positions.


This gives you not only unique boards, but boards where you cannot remove any more numbers without destroying the uniqueness of the solution.


---------------------------------------------------------------------------------------------------------------------------------------------
Of course, this is only the second half of the algorithm. The first half is to find a complete valid board first (randomly filled!) It works very similar, but "in the other direction":

Start with an empty board.

Add a random number at one of the free cells (the cell is chosen randomly, and the number is chosen randomly from the list of numbers valid for this cell according to the SuDoKu rules).

Use the backtracking solver to check if the current board has at least one valid solution. If not, undo step 2 and repeat with another number and cell. Note that this step might produce full valid boards on its own, but those are in no way random.

Repeat until the board is completely filled with numbers.
