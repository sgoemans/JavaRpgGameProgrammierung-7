package de.neuromechanics;

import java.awt.Canvas;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.HashSet;

public class Game implements Runnable {
	public static final int FPS = 60;
	public static final long maxLoopTime = 1000 / FPS;
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 640;

	public Screen screen;
	Player player;
	Level level;
	KeyManager keyManager;
	private Camera gameCamera;
	BufferStrategy bs;
	Graphics g;

	public static void main(String[] arg) {
		Game game = new Game();
		new Thread(game).start();
	}
	@Override
	public void run() {
		long timestamp;
		long oldTimestamp;

		screen = new Screen("Game", SCREEN_WIDTH, SCREEN_HEIGHT);
		keyManager = new KeyManager();
		screen.getFrame().addKeyListener(keyManager);

		TileSet[] tileSet = new TileSet[1];
		// Ground layer tileset with blocking tiles
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HashSet hs = new HashSet(Arrays.asList(0, 1, 2, 12, 14, 24, 25, 26));
		tileSet[0] = new TileSet("/tiles/rpg.png", 12 /*sizeX*/, 12/*sizeY*/, 3 /*border*/, hs);
		
		level = new Level(this, "/level/level1.txt", tileSet);
		SpriteSheet playerSprite = new SpriteSheet("/sprites/player.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
		player = new Player(this, level, 320, 320, playerSprite);
		gameCamera = new Camera(level.getSizeX(), level.getSizeY());
		while(true) {
			oldTimestamp = System.currentTimeMillis();
			update();
			timestamp = System.currentTimeMillis();
			if(timestamp-oldTimestamp > maxLoopTime) {
				System.out.println("Wir sind zu spät!");
				continue;
			}
			render();
			timestamp = System.currentTimeMillis();
			System.out.println(maxLoopTime + " : " + (timestamp-oldTimestamp));
			if(timestamp-oldTimestamp <= maxLoopTime) {
				try {
					Thread.sleep(maxLoopTime - (timestamp-oldTimestamp) );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	void update() {
		keyManager.update();
		player.setMove(getInput());
		player.update();
	}
	void render() {
		Canvas c = screen.getCanvas();
		bs = c.getBufferStrategy();
		if(bs == null){
			screen.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		level.renderMap(g);
		player.render(g);

		bs.show();
		g.dispose();
	}
	private Point getInput(){
		int xMove = 0;
		int yMove = 0;
		if(keyManager.up)
			yMove = -1;
		if(keyManager.down)
			yMove = 1;
		if(keyManager.left)
			xMove = -1;
		if(keyManager.right)
			xMove = 1;
		return new Point(xMove, yMove);
	}
	public Camera getGameCamera(){
		return gameCamera;
	}
}