package me.Josh123likeme.WaveFunctionCollapse;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tile {
	
	public static int tileSize;
	
	public final BufferedImage texture;
	public final int id;
	
	public final int North;
	public final int East;
	public final int South;
	public final int West;
	
	public List<Integer> NorthEntropy = new ArrayList<Integer>();
	public List<Integer> EastEntropy = new ArrayList<Integer>();
	public List<Integer> SouthEntropy = new ArrayList<Integer>();
	public List<Integer> WestEntropy = new ArrayList<Integer>();
	
	public Tile(int id, BufferedImage texture, String sideConfiguration) {
		
		tileSize = texture.getWidth();
		
		this.id = id;
		this.texture = texture;
		
		String[] sides = sideConfiguration.split(",");
		
		North = Integer.parseInt(sides[0]);
		East = Integer.parseInt(sides[1]);
		South = Integer.parseInt(sides[2]);
		West = Integer.parseInt(sides[3]);
		
	}
	
}
