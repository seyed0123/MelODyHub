#pragma once

#include "vector.cpp"
#include "miscellaneous.cpp"

/*
Got the idea from:
https://github.com/shaunlebron/pacman-mazegen
Algorithm used: randomfill
*/


using Maze = Vector2d;
using Connection = VectorMap;


bool isValidPosition(int x, int y, int width, int height);
bool canNewBlockFit(Maze& maze, int x, int y, int width, int height);
void addWallBlock(Maze& maze, int x, int y);
Vector2d getPossibleStarts(Maze& maze);
Connection getConnections(Maze& maze, Vector2d& possibleStarts);
void addConnection(Maze& maze, Connection& connections, Vector2d& possibleStarts, int x, int y, int dx, int dy);
void connect(Maze& maze, Connection& connections, Vector2d& possibleStarts, int x, int y, int x0, int y0);
bool isWallBlockFilled(Maze& maze, int x, int y);
void expand(Maze& maze, Connection& connections, Vector2d& visited, int x, int y);
void expandWall(Maze& maze, Connection& connections, int x, int y);
void printMaze(Maze maze);
Maze createEmptyMazeWithGhost(int width, int height);
bool addRandomBlock(Maze& maze);
Maze removeLastAxis(Maze maze);
Maze addPointsToMaze(Maze maze);
Maze addGhostHouse(Maze maze);
Maze addSymmetryToMaze(Maze maze);
Maze createRandomMaze(int width, int height);
bool isDesirable(Maze maze);
Maze addPowerPoints(Maze maze);
Maze createDesirableMaze(int width, int height);


// Prevents from accessing points outside of the maze dimension
bool isValidPosition(int x, int y, int width, int height) {
    if(x < 0 || x >= width || y < 0 || y >= height) {
        return false;
    } else {
        return true;
    }
}

// Scans all empty 4x4 squares
bool canNewBlockFit(Maze& maze, int x, int y, int width, int height) {
    if(isValidPosition(x, y, width, height) && isValidPosition(x+3, y+3, width, height)) {
        for(int j = y; j < y+4; j++) {
            for(int i = x; i < x+4; i++) {
                if(maze[j][i] != 0) {
                    return false;
                }
            }
        }
        return true;
    } else {
        return false;
    }
}

// Adds a 2x2 wall block inside a 4x4 square
void addWallBlock(Maze& maze, int x, int y) {
        for(int i = x+1; i <= x+2; i++) {
            for(int j = y+1; j <= y+2; j++) {
                maze.change(j, i, 1);
            }
        }
}

// Get all points corresponding to empty 4x4 squares
Vector2d getPossibleStarts(Maze& maze) {
    Vector2d possibleStarts;
    for(int y = 0; y < maze.size(); y++) {
        for(int x = 0; x < maze[0].size(); x++) {
            if(canNewBlockFit(maze, x, y, maze[0].size(), maze.size())) {
                Vector1d position;
                position.push_back(x);
                position.push_back(y);
                possibleStarts.push_back(position);
            }
        }
    }
    return possibleStarts;
}

Connection getConnections(Maze& maze, Vector2d& possibleStarts) {
    Connection connections;         
    for(int i = 0; i < possibleStarts.size(); i++) {
        Vector1d position = possibleStarts[i];
        int x = position[0];
        int y = position[1];
        for(int y0 = 0; y0 < 4; y0++) {
            if(!isValidPosition(x-1, y+y0, maze[0].size(), maze.size())) {
                continue;
            }
            if(maze[y+y0][x-1] == 1) {
                addConnection(maze, connections, possibleStarts, x, y, 1, 0);
                break;
            }
        }
        for(int y0 = 0; y0 < 4; y0++) {
            if(!isValidPosition(x+4, y+y0, maze[0].size(), maze.size())) {
                continue;
            }
            if (maze[y+y0][x+4] == 1) {
                addConnection(maze, connections, possibleStarts, x, y, -1 ,0);
                break;
            }
        }
        for(int x0 = 0; x0 < 4; x0++) {
            if(!isValidPosition(x+x0, y-1, maze[0].size(), maze.size())) {
                continue;
            }
            if (maze[y-1][x+x0] == 1) {
                addConnection(maze, connections, possibleStarts, x, y, 0, 1);
                break;
            }
        }
        for(int x0 = 0; x0 < 4; x0++) {
            if(!isValidPosition(x+x0, y+4, maze[0].size(), maze.size())) {
                continue;
            }
            if (maze[y+4][x+x0] == 1) {
                addConnection(maze, connections, possibleStarts, x, y, 0, -1);
                break;
            }
        }
    }
    return connections;
}

// moving two points to the desired direction
void addConnection(Maze& maze, Connection& connections, Vector2d& possibleStarts, int x, int y, int dx, int dy) {
    connect(maze, connections, possibleStarts, x, y, x+dx, y+dy);
    connect(maze, connections, possibleStarts, x, y, x+2*dx, y+2*dy);
}

void connect(Maze& maze, Connection& connections, Vector2d& possibleStarts, int x, int y, int x0, int y0) {
    Vector1d source;
    source.push_back(x);
    source.push_back(y);
    Vector1d destination;
    destination.push_back(x0);
    destination.push_back(y0);
    
    if(possibleStarts.contains(destination)) {
        connections.add(destination, source);
    }
}

bool isWallBlockFilled(Maze& maze, int x, int y) {
    for(int dy = 1; dy < 3; dy++) {
        for(int dx = 1; dx < 3; dx++) {
            if(!isValidPosition(x+dx, y+dy, maze[0].size(), maze.size())) {
                return false;
            }

            if(maze[y+dy][x+dx] != 1) {
                return false;
            }
        }
    }
    return true;
}

void expand(Maze& maze, Connection& connections, Vector2d& visited, int x, int y) {
    Vector1d source;
    source.push_back(x);
    source.push_back(y);
    if(visited.contains(source)) {
        return;
    }

    visited.push_back(source);
    if(connections.keyExists(source)) {
        Vector2d connection = connections.getValue(source);
        for(int i = 0; i < connection.size(); i++) {
            int x0 = connection[i][0];
            int y0 = connection[i][1];

            addWallBlock(maze, x0, y0);
            expand(maze, connections, visited, x0, y0);
        }
    }
}

void expandWall(Maze& maze, Connection& connections, int x, int y) {
    Vector2d visited;
    expand(maze, connections, visited, x, y);
}

void printMaze(Maze maze) {
    for(int i = 0; i < maze.size(); i++) {
        for(int j = 0; j < maze[i].size(); j++) {
            if(maze[i][j] == 0) {
                std::cout << ' ';
            } else if(maze[i][j] == 1) {
                std::cout << '|';
            } else if(maze[i][j] == 2) {
                std::cout << '.';
            } else if(maze[i][j] == -1) {
                std::cout << '-';
            }
        }
        std::cout << std::endl;
    }
}

Maze createEmptyMazeWithGhost(int width, int height) {
    Maze maze;
    Vector1d topWall;
    for(int j = 0; j < width; j++) {
        topWall.push_back(1);
    }
    maze.push_back(topWall);

    // Creating ghost house, in the middle
    for(int i = 1; i < (height-5)/2 - 1; i++) {
        Vector1d row;
        row.push_back(1);
        for(int j = 1; j < width-1; j++) {
            row.push_back(0);
        }
        row.push_back(0);
        maze.push_back(row);
    }

    for(int i = (height-5)/2 - 1; i < (height-5)/2 + 4; i++) {
        Vector1d row;
        row.push_back(1);
        for(int j = 1; j < width-6; j++) {
            row.push_back(0);
        }
      for(int j = width-6; j < width; j++) {
            row.push_back(1);
        }
        maze.push_back(row);
    }

    for(int i = (height-5)/2 + 4; i < height-1; i++) {
        Vector1d row;
        row.push_back(1);
        for(int j = 1; j < width-1; j++) {
            row.push_back(0);
        }
        row.push_back(0);
        maze.push_back(row);
    }

    Vector1d bottomWall;
    for(int j = 0; j < width; j++) {
        bottomWall.push_back(1);
    }
    maze.push_back(bottomWall);

    return maze;
}

bool addRandomBlock(Maze& maze) {
    Vector2d possibleStarts = getPossibleStarts(maze);
    Connection connections = getConnections(maze, possibleStarts);
    if(possibleStarts.size() == 0) {
        return false;
    } else {
        Vector1d position = possibleStarts[getRandomInt(0, possibleStarts.size()-1)];
        int x = position[0];
        int y = position[1];

        for(int i = x+1; i <= x+2; i++) {
            for(int j = y+1; j <= y+2; j++) {
                maze.change(j, i, 1);
            }
        }

        expandWall(maze, connections, x, y);

        return true;
    }
}

Maze removeLastAxis(Maze maze) {
    Maze newMaze;
    for(int i = 0; i < maze.size(); i++) {
        Vector1d row;
        for(int j = 0; j < maze[i].size()-1; j++) {
            row.push_back(maze[i][j]);
        }
        newMaze.push_back(row);
    }
    return newMaze;
}

Maze addPointsToMaze(Maze maze) {
    for(int y = 0; y < maze.size(); y++) {
        for(int x = 0; x < maze[0].size(); x++) {
            if(maze[y][x] == 0) {
                maze.change(y, x, 2);
            }
        }
    }
    return maze;
}

Maze addGhostHouse(Maze maze) {
    int height = maze.size();
    int width = maze[0].size();
    maze.change((height-5)/2 - 1, width-2, -1);
    maze.change((height-5)/2 - 1, width-1, -1);
    for(int i = (height-5)/2; i < (height-5)/2 + 3; i++) {
        for(int j = width-3; j < width; j++) {
            maze.change(i, j, 0);
        }
    }
    return maze;
}

Maze addSymmetryToMaze(Maze maze) {
    Maze symmetricalMaze;
    for(int i = 0; i < maze.size(); i++) {
        Vector1d row;
        for(int j = 0; j < maze[0].size(); j++) {
            row.push_back(maze[i][j]);
        }
        // for(int j = maze[0].size()-2; j < maze[0].size(); j++) {
        //     row.push_back(maze[i][j]);
        // }
        for(int j = maze[0].size()-1; j >= 0; j--) {
            row.push_back(maze[i][j]);
        }
        symmetricalMaze.push_back(row);
    }
    return symmetricalMaze;
}

Maze createRandomMaze(int width, int height) {
    width = width/2 + 1;
    Maze maze = createEmptyMazeWithGhost(width, height);
    while(addRandomBlock(maze)) {
        continue;
    }
    maze = removeLastAxis(maze);
    maze = addPointsToMaze(maze);
    maze = addGhostHouse(maze);
    maze = addSymmetryToMaze(maze); 
    return maze;
}

// Check whether the maze is desirable
bool isDesirable(Maze maze) {
    for(int j = 1; j < maze[0].size() - 1; j++) {
        for(int i = 1; i < maze.size()-1; i++) {
            if(maze[i][j] == 2) {
                // Prevents path with width or height more than one
                if(maze[i][j+1] == 2 && maze[i+1][j] == 2 && maze[i+1][j+1] == 2) {
                    return false;
                }

                // Prevents dead-ends
                int wallCount = 0;
                if(maze[i][j+1] == 1) {
                    wallCount++;
                }
                if(maze[i][j-1] == 1) {
                    wallCount++;
                }
                if(maze[i+1][j] == 1) {
                    wallCount++;
                }
                if(maze[i-1][j] == 1) {
                    wallCount++;
                }
                if(wallCount >= 3) {
                    return false;
                }   
            } else if(maze[i][j] == 1) {
                if(maze[i][j+1] == 2 && maze[i][j-1] == 2) {
                    return false;
                }
            }
        }
    }
    return true;
}

Maze addPowerPoints(Maze maze) {
    int width = maze[0].size();
    int height = maze.size();

    maze.change(1, 1, 3);
    maze.change(height-2, 1, 3);
    maze.change(1, width-2, 3);
    maze.change(height-2, width-2, 3);

    return maze;
}


// Keep creating random mazes until one that holds our standards get created
Maze createDesirableMaze(int width, int height) {
    while(true) {
        Maze maze = createRandomMaze(width, height);
        if(!isDesirable(maze)) {
            maze = addPowerPoints(maze);
            return maze;
        }
    }
}