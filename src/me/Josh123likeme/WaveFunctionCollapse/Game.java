package me.Josh123likeme.WaveFunctionCollapse;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int INITIAL_WIDTH = 400, INITIAL_HEIGHT = 400;
	
	private Thread thread;
	private boolean running = false;
	
	public Game() {
		
		new Window(INITIAL_WIDTH, INITIAL_HEIGHT, "Wave Function Collapse", this);
		
	}
	
	public synchronized void start() {
		
		thread = new Thread(this);
		thread.start();
		running = true;
		
	}
	
	public synchronized void stop() {
		
		try 
		{
			thread.join();
			running = false;
		}
		
		catch(Exception e) {e.printStackTrace();}
		
	}
	
	public void run() {
		
		while (running) {
			
			paint();
			
		}
		
	}
	
	private void tick() {
		
	}

	public void paint() {
	
		int[][] board = Main.board;
		ArrayList<Integer[]> badTiles = Main.badTiles;
		ArrayList<Integer[]> consideredTiles = Main.consideredTiles;
		
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		if(bufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		//basic black background to stop flashing
		graphics.setColor(Color.black); 
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		//put rendering stuff here
		
		double tileSize = (getWidth() < getHeight()) ? getWidth() / board[0].length : getHeight() / board.length;
		
		for (int y = 0; y < board.length; y++) {
			
			for (int x = 0; x < board[0].length; x++) {
				
				graphics.drawImage(Tiles.getTile(board[y][x]).texture, (int) (x * tileSize), (int) (y * tileSize), (int) tileSize, (int) tileSize, null);
				
			}
			
		}
		
		graphics.setColor(Color.yellow);
		
		try {
			
			for (Integer[] tile : consideredTiles) {
				
				graphics.drawRect((int) (tile[0] * tileSize), (int) (tile[1] * tileSize), (int) tileSize - 1, (int) tileSize - 1);
				
			}
			
		} catch (ConcurrentModificationException e) { };
		
		graphics.setColor(Color.red);
		
		try {
			
			for (Integer[] tile : badTiles) {
				
				graphics.drawRect((int) (tile[0] * tileSize), (int) (tile[1] * tileSize), (int) tileSize - 1, (int) tileSize - 1);
				
			}
			
		} catch (ConcurrentModificationException e) { };
		
		//this pushes the graphics to the window
		bufferStrategy.show();
		
	}
	
}
