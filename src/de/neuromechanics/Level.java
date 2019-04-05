package de.neuromechanics;

import java.awt.Graphics;

public class Level {
	private TileSet[] ts;
	private int sizeX, sizeY;
	private int[][][] tileMap;
	private Game game;

	public Level(Game game, String path, TileSet[] ts) {
		this.game = game;
		this.ts = ts;
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		sizeX = Utils.parseInt(tokens[0]);
		sizeY = Utils.parseInt(tokens[1]);
		tileMap = new int[1][sizeX][sizeY];
		int i = 2;
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){
				tileMap[0][x][y] = Utils.parseInt(tokens[i++]);
			}
		}
	}

	public void renderMap(Graphics g){
		for(int tileY = 0; tileY < sizeY; tileY++){
			for(int tileX = 0; tileX < sizeX; tileX++){
				ts[0].renderTile(g, tileMap[0][tileX][tileY], tileX * TileSet.TILEWIDTH - game.getGameCamera().getxOffset(),
						tileY * TileSet.TILEHEIGHT - game.getGameCamera().getyOffset());
			}
		}
	}
	public int[][] getTilesTouched(Creature player) {
		int[][] ret = new int[1][2];
		int numX = (player.entityX + Player.MARGIN_HORIZ) / player.width;
		int numY = (player.entityY + player.height - Player.MARGIN_VERT) / player.height;
		ret[0][0] = tileMap[0][numX][numY];
		if(ts[0].hs.contains(ret[0][0])) {
			ret[0][0] <<= 16;
		}
		numX = (player.entityX + player.width - Player.MARGIN_HORIZ) / player.width;
		ret[0][1] = tileMap[0][numX][numY];
		if(ts[0].hs.contains(ret[0][1])) {
			ret[0][1] <<= 16;
		}
		return ret;
	}
	public int getSizeX() {
		return sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}

}