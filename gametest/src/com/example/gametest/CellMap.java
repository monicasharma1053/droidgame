package com.example.gametest;


import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
 
/**
 * The SurfaceView, on which we draw the map.
 */
public class CellMap extends SurfaceView implements SurfaceHolder.Callback {
    public static int _cellSize = 60;
 
    private HashMap<Integer, HashMap<Integer, Cell>> _mapCells = new HashMap<Integer, HashMap<Integer, Cell>>();
    private MapThread _mapThread;
    private Paint paint = new Paint();
 
    // variables we will use often
    private HashMap<Integer, Cell> _row;
 
    // map size in cells
    public static int _mapSize = 10;
    
 // Offset to the upper left corner of the map
    private int _xOffset = 0;
    private int _yOffset = 0;
     
    // last touch point
    private int _xTouch = 0;
    private int _yTouch = 0;
     
    // scrolling active?
    private boolean _isMoving = false;
 
    /**
     * Constructor, fills the map on creation.
     * 
     * @param context
     */
    public CellMap(Context context) {
        super(context);
 
        // fill the map with cells
        int id = 0;
        for (int i = 0; i < _mapSize; i++) {
            _row = new HashMap<Integer, Cell>();
            for (int j = 0; j < _mapSize; j++) {
                _row.put(j, new Cell(id++));
            }
            _mapCells.put(i, _row);
        }
 
        
        // register the view to the surfaceholder
        getHolder().addCallback(this);
 
        // set the thread - not yet started
        _mapThread = new MapThread(this);
 
        // secure the view is focusable
        setFocusable(true);
    }
 
    /**
     * Draw what we want to see.
     */
    @Override
    public void onDraw(Canvas canvas) {
    	   int x = 0;
    	    int y = 0;
    	 
    	    for (int i = 0; i < _mapSize; i++) {
    	        // get the row
    	        _row = _mapCells.get(i);
    	 
    	        // calculate the correct y for the row
    	        y = i * _cellSize - _yOffset;
    	        // go through the row
    	        for (int j = 0; j < _mapSize; j++) {
    	            // calculate the x coordinate for the columns
    	            x = j * _cellSize - _xOffset;
    	 
    	            _row.get(j).draw(canvas, paint, x, y);
    	        }
    	    }
    	}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
 
    /**
     * Called when surface created.
     * Starts the thread if not already running.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!_mapThread.isAlive()) {
            _mapThread = new MapThread(this);
        }
        _mapThread._run = true;
        _mapThread.start();
    }
 
    /**
     * Called when surface destroyed
     * Stops the thread.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        _mapThread._run = false;
        while (retry) {
            try {
                _mapThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }
    
    /**
     * Handle touch event on the map.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // touch down
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // start of a new event, reset the flag
            _isMoving = false;
            // store the current touch coordinates for scroll calculation
            _xTouch = (int) event.getX();
            _yTouch = (int) event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // touch starts moving, set the flag
            _isMoving = true;
     
            // get the new offset
            _xOffset += _xTouch - (int) event.getX();
            _yOffset += _yTouch - (int) event.getY();
     
            // secure, that the offset is never out of view bounds
            if (_xOffset < 0) {
                _xOffset = 0;
            } else if (_xOffset > _mapSize * _cellSize - getWidth()) {
                _xOffset = _mapSize * _cellSize - getWidth();
            }
            if (_yOffset < 0) {
                _yOffset = 0;
            } else if (_yOffset > _mapSize * _cellSize - getHeight()) {
                _yOffset = _mapSize * _cellSize - getHeight();
            }
     
            // store the last position
            _xTouch = (int) event.getX();
            _yTouch = (int) event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch released
            if (!_isMoving) {
                // calculate the touched cell
                int column = (int) Math.ceil((_xOffset + event.getX()) / _cellSize) - 1;
                int row = (int) Math.ceil((_yOffset + event.getY()) / _cellSize) - 1;
                Cell cell = _mapCells.get(row).get(column);
                // show the id of the touched cell
                Toast.makeText(getContext(), "Cell id #" + cell._id, Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}