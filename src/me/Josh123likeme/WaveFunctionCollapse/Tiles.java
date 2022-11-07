package me.Josh123likeme.WaveFunctionCollapse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Tiles {

	private static List<Tile> tiles = new ArrayList<Tile>();
	
	public static boolean showGUI;
	public static int width;
	public static int height;
	
	public static Tile getTile(int id) {
		
		return tiles.get(id);
		
	}
	
	public static int getNumberOfTiles() {
		
		return tiles.size();
		
	}
	
	public static void loadTiles() {
		
		loadConfig();
		
		File folder = new File(System.getProperty("user.dir"));
		
		List<File> files = new ArrayList<File>();
		
		for (File file : folder.listFiles()) {
			
			if (file.isDirectory()) continue;
			
			if (file.getName().split("\\.")[1].equals("png")) {
				
				if (file.getName().equals("WFC-cfg.txt")) continue;
				
				if (file.getName().equals("output.png")) continue;
				
				System.out.println("found: " + file.getName());
				
				files.add(file);
				
			}
			
		}
		
		for (int i = 0; i < files.size(); i++) {
			
			try {
				tiles.add(new Tile(i, ImageIO.read(files.get(i)), files.get(i).getName().substring(0, files.get(i).getName().length() - 4)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		calculateEntropies();
		
	}
	
	private static void calculateEntropies() {
		
		for (Tile tile : tiles) {
			
			for (Tile tile2 : tiles) {
				
				if (tile.North == tile2.South) tile.NorthEntropy.add(tile2.id); 
				
			}
			for (Tile tile2 : tiles) {
				
				if (tile.East == tile2.West) tile.EastEntropy.add(tile2.id); 
				
			}
			for (Tile tile2 : tiles) {
	
				if (tile.South == tile2.North) tile.SouthEntropy.add(tile2.id); 
	
			}
			for (Tile tile2 : tiles) {
	
				if (tile.West == tile2.East) tile.WestEntropy.add(tile2.id); 
	
			}
			
		}
		
	}
	
	private static void loadConfig() {
		
		try {
			
            FileReader reader = new FileReader("WFC-cfg.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            List<String> lines = new ArrayList<String>();
            
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
            	
            	lines.add(line);
            	
            }
            reader.close();

            showGUI = lines.get(0).split("=")[1].equals("false") ? false : true;
            width = Integer.parseInt(lines.get(1).split("=")[1]);
            height = Integer.parseInt(lines.get(2).split("=")[1]);
 
        }
		catch (IOException e) {
            
        	System.out.println("Couldn't find config file, creating one now");
        	
        	File configFile = new File("WFC-cfg.txt");
        	try {
        		
				configFile.createNewFile();
				
				FileWriter writer = new FileWriter("WFC-cfg.txt");

				writer.write("showgui=\n");
				writer.write("width=\n");
				writer.write("height=\n");
				
				writer.close();
				
			} catch (IOException e1) {
				
				System.out.println("uh oh. Couldn't create a config file");
				
			}
        	
        	System.out.println("created a config file. please enter the parameters");
        	
        	System.exit(0);
            
        }
		catch (IndexOutOfBoundsException e) {
			
			System.out.println("seems like your config file is missing some entries\nadding them now");
			
			File configFile = new File("WFC-cfg.txt");
        	try {
        		
				configFile.createNewFile();
				
				FileWriter writer = new FileWriter("WFC-cfg.txt");

				writer.write("showgui=" + (showGUI ? "true" : "false") + "\n");
				writer.write("width=" + width + "\n");
				writer.write("height=" + height + "\n");
				
				writer.close();
				
			} catch (IOException e1) {
				
				System.out.println("uh oh. Couldn't create a config file");
				
			}
        	
        	System.out.println("created a config file. please enter the parameters");
        	
        	System.exit(0);
			
		}
		
	}
	
}
