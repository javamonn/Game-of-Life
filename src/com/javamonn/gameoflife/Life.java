package com.javamonn.gameoflife;
import android.content.Context;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.Point;
import java.util.Arrays;

public class Life {
	
    private int CELL_SIZE;
	//width is the number of cells wide 
    private int WIDTH;
	//height is the number of cells tall
    private int HEIGHT;

    private int[][] _lifeGrid;
    private Context _context;

    public Life(Context context, int width,int height) {
		CELL_SIZE = 16;
		WIDTH = width / CELL_SIZE;
		HEIGHT = height / CELL_SIZE;
		//WIDTH = 1280 / CELL_SIZE;
		//HEIGHT = 840 / CELL_SIZE;
		this._context = context;
		_lifeGrid = new int[HEIGHT][WIDTH];
        initializeGrid();
    }

    public int[][] getGrid() {
        return _lifeGrid;
    }

    public void initializeGrid() {
        resetGrid(_lifeGrid);

        _lifeGrid[8][(WIDTH / 2) - 1] = 1;
        _lifeGrid[8][(WIDTH / 2) + 1] = 1;
        _lifeGrid[9][(WIDTH / 2) - 1] = 1;
        _lifeGrid[9][(WIDTH / 2) + 1] = 1;
        _lifeGrid[10][(WIDTH / 2) - 1] = 1;
        _lifeGrid[10][(WIDTH / 2)] = 1;
        _lifeGrid[10][(WIDTH / 2) + 1] = 1;
    }

    public void generateNextGeneration() {
        int neighbours;
        int minimum = Integer.parseInt(PreferencesActivity
									   .getMinimumVariable(this._context));
        int maximum = Integer.parseInt(PreferencesActivity
									   .getMaximumVariable(this._context));
        int spawn = Integer.parseInt(PreferencesActivity
									 .getSpawnVariable(this._context));
									 
		minimum = 2;
		maximum = 3;
		spawn = 3;

        int[][] nextGenerationLifeGrid = new int[HEIGHT][WIDTH];

        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                neighbours = calculateNeighbours(h, w);

                if (_lifeGrid[h][w] != 0) {
                    if ((neighbours >= minimum) && (neighbours <= maximum)) {
                        nextGenerationLifeGrid[h][w] = neighbours;
                    }
					else nextGenerationLifeGrid[h][w] = 0;
                } else {
                    if (neighbours == spawn) {
                        nextGenerationLifeGrid[h][w] = spawn;
                    }
                }
            }
        }
        copyGrid(nextGenerationLifeGrid, _lifeGrid);
    }

    private void resetGrid(int[][] grid) {
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                grid[h][w] = 0;
            }
        }
    }
	
    private int calculateNeighbours(int y, int x) {
        int total = 0; 
		if (y - 1 >= 0 && y + 1 < HEIGHT && x - 1 >= 0 && x + 1 < WIDTH)
			for (int row = y - 1; row <= y + 1; row++) {
				for (int col = x - 1; col <= x + 1; col++) {
					if (!(row == y && col == x) && _lifeGrid[row][col] != 0) total++;
				}
			}
        return total;
    }

    private void copyGrid(int[][] source, int[][] destination) {
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                destination[h][w] = source[h][w];
            }
        }
    }
	
	public void fill(int x, int y) {
		_lifeGrid[y][x] = 1;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getCellSize() {
		return CELL_SIZE;
	}
}
