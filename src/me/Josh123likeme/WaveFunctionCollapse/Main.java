package me.Josh123likeme.WaveFunctionCollapse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Main {

	public static Random random = new Random();
	
	private static boolean showGUI;
	
	public static int[][] board;
	private static boolean[][] placedTiles;
	private static ArrayList<Integer>[][] boardEntropies;
	
	public static ArrayList<Integer[]> badTiles = new ArrayList<Integer[]>();
	public static ArrayList<Integer[]> consideredTiles = new ArrayList<Integer[]>();
	
	private static Game game;
	
	public static void main(String[] args) {
		
		Tiles.loadTiles();
		
		showGUI = Tiles.showGUI;
		
		if (showGUI) game = new Game();
		
		BufferedImage complete = waveFunctionCollapse();
		
		try {
			ImageIO.write(complete, "png", new File(System.getProperty("user.dir") + "/output.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("complete");	
		
	}
	
	@SuppressWarnings("unchecked")
	private static BufferedImage waveFunctionCollapse() {
		
		//init
		board = new int[Tiles.height][Tiles.width];
		placedTiles = new boolean[Tiles.height][Tiles.width];
		boardEntropies = new ArrayList[Tiles.height][Tiles.width];
		
		for (int y = 0; y < placedTiles.length; y++) {
			
			for (int x = 0; x < placedTiles[0].length; x++) {
				
				placedTiles[y][x] = false;
				
			}
			
		}
		
		for (int y = 0; y < Tiles.height; y++) {
			
			for (int x = 0; x < Tiles.width; x++) {
				
				ArrayList<Integer> possibleValues = new ArrayList<Integer>();
				
				for (int i = 0; i < Tiles.getNumberOfTiles(); i++) {
					
					possibleValues.add(i);
					
				}
				
				boardEntropies[y][x] = possibleValues;
				
			}
			
		}
		
		int ranX = random.nextInt(board[0].length);
		int ranY = random.nextInt(board.length);
		
		board[ranY][ranX] = random.nextInt(Tiles.getNumberOfTiles());
		placedTiles[ranY][ranX] = true;
		
		recalculateEntropies(ranX, ranY);
		
		long last = System.currentTimeMillis();
		
		//start collapse
		do {
			
			badTiles.removeAll(badTiles);
			consideredTiles.removeAll(consideredTiles);
			
			//select random place with lowest entropy
			int lowestEntropy = 1000;
			
			for (int y = 0; y < boardEntropies.length; y++) {
				
				for (int x = 0; x < boardEntropies[0].length; x++) {
					
					if (placedTiles[y][x]) continue;
					
					if (boardEntropies[y][x].size() == 0) {
						
						badTiles.add(new Integer[] {x, y});
						
						continue;
						
					}
					
					if (boardEntropies[y][x].size() < lowestEntropy) {
						
						lowestEntropy = boardEntropies[y][x].size();
						
					}
					
				}
				
			}
			
			ArrayList<Integer> lowestX = new ArrayList<Integer>();
			ArrayList<Integer> lowestY = new ArrayList<Integer>();
			
			for (int y = 0; y < boardEntropies.length; y++) {
				
				for (int x = 0; x < boardEntropies[0].length; x++) {
					
					if (placedTiles[y][x]) continue;
					if (boardEntropies[y][x].size() <= 0) continue;
					
					if (boardEntropies[y][x].size() == lowestEntropy) {
						
						lowestX.add(x);
						lowestY.add(y);
						
						consideredTiles.add(new Integer[] {x, y});
						
					}
					
				}
				
			}
			
			if (lowestX.size() == 0) break;
			
			int choice = random.nextInt(lowestX.size());
			
			int lowX = lowestX.get(choice);
			int lowY = lowestY.get(choice);
			
			//place random tile
			board[lowY][lowX] = boardEntropies[lowY][lowX].get(random.nextInt(boardEntropies[lowY][lowX].size()));
			placedTiles[lowY][lowX] = true;
			
			recalculateEntropies(lowX, lowY);
			
			if (System.currentTimeMillis() - last > 1000) {
				
				last = System.currentTimeMillis();
				
				System.out.println((double) Math.round(percentComplete() * 10000) / 100 + "%");	
				
			}
			
		}
		while (percentComplete() != 1d);
		
		consideredTiles.removeAll(consideredTiles);
		
		BufferedImage finishedBoard = new BufferedImage(Tile.tileSize * Tiles.width, Tile.tileSize * Tiles.height, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < Tiles.height; y++) {
			
			for (int x = 0; x < Tiles.width; x++) {
				
				finishedBoard.getGraphics().drawImage(Tiles.getTile(board[y][x]).texture,
						x * Tile.tileSize, y * Tile.tileSize, Tile.tileSize, Tile.tileSize, null);
				
			}
			
		}
		
		BufferedImage red = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		
		red.setRGB(0, 0, (255 << 24) | (255 << 16) | (0 << 8) | 0);
		
		for (Integer[] tile : badTiles) {
			
			finishedBoard.getGraphics().drawImage(red, tile[0] * Tile.tileSize, tile[1] * Tile.tileSize, Tile.tileSize, Tile.tileSize, null);
			
		}
		
		return finishedBoard;
		
	}
	
	private static void recalculateEntropies(int x, int y) {
		
		if (y - 1 >= 0) {
			
			@SuppressWarnings("unchecked")
			ArrayList<Integer> entropy = (ArrayList<Integer>) boardEntropies[y - 1][x].clone();
			
			for (Integer item : entropy) {
				
				if (!Tiles.getTile(board[y][x]).NorthEntropy.contains(item)) boardEntropies[y - 1][x].remove(item);
				
			}
			
		}
		
		if (x + 1 < boardEntropies[0].length) {
			
			@SuppressWarnings("unchecked")
			ArrayList<Integer> entropy = (ArrayList<Integer>) boardEntropies[y][x + 1].clone();
			
			for (Integer item : entropy) {
				
				if (!Tiles.getTile(board[y][x]).EastEntropy.contains(item)) boardEntropies[y][x + 1].remove(item);
				
			}
			
		}
		
		if (y + 1 < boardEntropies.length) {
			
			@SuppressWarnings("unchecked")
			ArrayList<Integer> entropy = (ArrayList<Integer>) boardEntropies[y + 1][x].clone();
			
			for (Integer item : entropy) {
				
				if (!Tiles.getTile(board[y][x]).SouthEntropy.contains(item)) boardEntropies[y + 1][x].remove(item);
		
			}
			
		}

		if (x - 1 >= 0) {
			
			@SuppressWarnings("unchecked")
			ArrayList<Integer> entropy = (ArrayList<Integer>) boardEntropies[y][x - 1].clone();
			
			for (Integer item : entropy) {
				
				if (!Tiles.getTile(board[y][x]).WestEntropy.contains(item)) boardEntropies[y][x - 1].remove(item);
		
			}
			
		}
		
	}
	
	private static double percentComplete() {
		
		int numberOfPlacedTiles = 0;
		
		for (int y = 0; y < Tiles.height; y++) {
			
			for (int x = 0; x < Tiles.width; x++) {
				
				if (placedTiles[y][x]) numberOfPlacedTiles++;
				
			}
			
		}
		
		return (double) numberOfPlacedTiles / (Tiles.height * Tiles.width);
		
	}
	
}
