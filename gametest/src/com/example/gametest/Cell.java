package com.example.gametest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cell {
	    public int _id = 0;
	    public int _backgroundColor = Color.MAGENTA;
	 
	    /**
	     * Konstruktor.
	     * @param id
	     */
	    public Cell(int id) {
	        _id = id;
	    }
	 
	    /**
	     * Draw the cell
	     *  
	     * @param canvas Canvas to draw on.
	     * @param paint Color of the "pencil".
	     * @param x X coordinate.
	     * @param y Y coordinate.
	     */
	    public void draw(Canvas canvas, Paint paint, int x, int y) {
	        paint.setColor(_backgroundColor);
	        canvas.drawRect(x, y, x + CellMap._cellSize, y + CellMap._cellSize, paint);
	 
	        paint.setColor(Color.BLACK);
	        canvas.drawText("" + _id, x + 1, y + 10, paint);
	    }
	}
	