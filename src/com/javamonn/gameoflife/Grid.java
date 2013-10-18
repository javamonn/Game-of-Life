package com.javamonn.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.*;

public class Grid extends View {
	public static final int RUNNING = 1;
	public static final int PAUSE = 0;
	private Life _life;
	private long delay = 250;
	private Context con;
	private int state;
	private boolean isMainActivity;
	
	private int viewHeight;
	private int viewWidth;
	private int buf;
		
	private RefreshHandler _redrawHandler = new RefreshHandler();
	
	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			Grid.this.update();
			Grid.this.invalidate();
		}
		
		public void sleep(long delayMillis) {
			this.removeMessages(0);
			this.sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};
	
	public Grid(Context context, AttributeSet attrs) {
		super(context, attrs);
		state = PAUSE;
		con = context;
		buf = 10;
		delay = 5;
		this.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					int xLoc = (int)event.getX();
					int yLoc = (int)event.getY();
					if (xLoc >= 0 && yLoc >= 0 && xLoc < viewWidth && yLoc < viewHeight - 5) 
						fillCell(xLoc,yLoc);
					return true;
				}
			});
		
	}
	
	public void setMode(int mode) {
		if (mode == RUNNING) {
			state = RUNNING;
			update();
			return;
		}
		else if (mode == PAUSE) {
			state = PAUSE;
			//implement 
		}
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.lighter_gray));

        Paint cell = new Paint();
        cell.setColor(getResources().getColor(R.color.cell));
		

        // draw background
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		//draw border that seperates left from right pane
		Paint line = new Paint();
		line.setColor(getResources().getColor(R.color.cell));
		line.setStrokeWidth(10);
		if (!isMainActivity) canvas.drawLine(0, 0, 0, viewHeight, line);

		//int buf = 8;
		
		int gridHeight = _life.getHeight();
		int gridWidth = _life.getWidth();
		int gridCellSize = _life.getCellSize();
		
        // draw cells
        for (int h = 0; h < gridHeight; h++) {
            for (int w = 0; w < gridWidth; w++) {
                if (_life.getGrid()[h][w] != 0) {
                    canvas.drawRect(
                        (w * gridCellSize) + buf, 
                        h * gridCellSize, 
                        (w * gridCellSize) + (gridCellSize - 1) + buf,
                        (h * gridCellSize) + (gridCellSize - 1),
                        cell);
                }
            }
        }
    }

    private void update() {
    	if (state == RUNNING) {
    		_life.generateNextGeneration();
			_redrawHandler.sleep(1500 / delay);
    	}
    }
	
	/*  fills the square at the corresponding pixel values that got passed. 
	 *	Called by GridActivity for pre game to select the starting squares
	 *  that are "alive" conversion to indices happens here before updating
	 *   the grid in the Life Class
	 */
	
	public void fillCell(int xPixels, int yPixels) {
		int x = (xPixels - 4) / _life.getCellSize();
		int y = yPixels / _life.getCellSize();
		if (!isMainActivity) {
			_life.fill(x,y);
			invalidate();
		}
	}

    private void initGrid() {
		
        setFocusable(true);
    }
    
    public void buildGrid(int width, int height, boolean isMain) {
    	viewWidth = width;
    	viewHeight = height;
		isMainActivity = isMain;
    	_life = new Life(con, width, height);
		if(isMain) setMode(RUNNING);
    	initGrid();
    }
    
    public int getState() {
    	return state;
    }
	
	public void setDelay(int val) {
		//we don't want a 0 delay, wierd things happen
		if (val == 0) val++;
		delay = val;
	}
}
