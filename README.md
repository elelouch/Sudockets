# Sudockets
Multiplayer Sudoku Game.

### Interesting things about the project
- Quick sudoku solver and generator.
- Multiple players can connect to the server, it's modifiable (5 max.)
    - Feature implemented with TCP sockets and multithreading
- Simple and understandable UI made in Swing (
- Design patterns implemented, such as :
    - Observer
    - Singleton
    - AbstractFactory
- Has its own protocol for parsing received things.

### Known problems
- The solver has to check for uniqueness, since the early stopping  
variant is not implemented (get just one solution), it may lead the app
into a costly operation.
- The server can't open and close quickly, this is because 
the Maximum Segment Lifetime(MSL) which is a timeout of 2mins to get
the socket up again. This leads to a "address already in use bind".
