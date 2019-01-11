package myFirstGame;
import java.awt.BorderLayout;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//sound libraries..
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
public class GameBoard extends JFrame{
	public static int count = 30;
	public static int boardWidth = 1000;
	public static int boardHeight = 800;
	
	static ScheduledThreadPoolExecutor executer = new ScheduledThreadPoolExecutor(5);
	
	public static boolean keyHeld = false;
	public static int keyHeldCode;
	
	//arrayList that holds torpedos..
	public static ArrayList<PhotonTorpedo> torpedos = new ArrayList<PhotonTorpedo>();
	
	//String thrustFile = "file: ./src/thrust.au";
	
	
	public static void main(String[] args) {
		new GameBoard();
		
		
	}
	
public GameBoard() {
	this.setSize(boardWidth, boardHeight);
	this.setTitle("Game kabir");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	addKeyListener(new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 87) {
				System.out.println("Forward");
				
				keyHeldCode = e.getKeyCode();
				keyHeld = true;
			
				}else if(e.getKeyCode() == 83) {
				System.out.println("Backward");
				
				keyHeldCode = e.getKeyCode();
				keyHeld = true;
			}
			
				else if(e.getKeyCode() == 68) {
					System.out.println("Rotate right");
					
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
			
				else if(e.getKeyCode() == 65) {
					System.out.println("Rotate left");
					
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
			
			//check for enter key pressed.. and fire torpedo if yes.
				else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					torpedos.add(new PhotonTorpedo(GameDrawingPanel.kabirGod.getShipNoseX(), GameDrawingPanel.kabirGod.getShipNoseY(), GameDrawingPanel.kabirGod.getRotationAngle()));
				
				}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keyHeld = false;
		}
		
	});
	
	GameDrawingPanel gamePanel = new GameDrawingPanel(); 
	this.add(gamePanel, BorderLayout.CENTER);
	
	
	executer.scheduleAtFixedRate(new RepaintTheBoard(this), 0L, 20L, TimeUnit.MILLISECONDS);
	//this.setResizable(false);
	//this.setLocation(400, 100);
	this.setVisible(true);
}

	
	class RepaintTheBoard implements Runnable{
		
		GameBoard theBoard;
		
		public RepaintTheBoard(GameBoard theBoard) {
			this.theBoard = theBoard;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			theBoard.repaint();
		}
	}
	
	public static class GameDrawingPanel extends JComponent{
		public static  ArrayList<Rock> rocks = new ArrayList<Rock>();
		int[] polyXArray = Rock.sPolyXArray;
		int[] polyYArray = Rock.sPolyYArray;
	    static SpaceShip kabirGod = new SpaceShip();
		int width = GameBoard.boardWidth;
		int height = GameBoard.boardHeight;
		
		public GameDrawingPanel() {
			for(int i = 0;i< count;i++) {
				int randomStartXPos = (int)(Math.random()*(GameBoard.boardWidth -40) + 1);
				int randomStartYPos = (int)(Math.random()*(GameBoard.boardHeight-40) + 1);
			
				rocks.add(new Rock(Rock.getPolyXArray(randomStartXPos), Rock.getPolyYArray(randomStartYPos), 12, 0,0));
				Rock.rocks= rocks;
			}
			
			 
		}
		
	public void paint(Graphics g) {
		Graphics2D graphicSettings = (Graphics2D)g;
		
		AffineTransform identity = new AffineTransform();
		graphicSettings.setColor(Color.BLACK);
		graphicSettings.fillRect(0, 0, getWidth(), getHeight());
		
		graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphicSettings.setPaint(Color.WHITE);
		
		for(Rock rock : rocks) {
			
		if(rock.onScreen) {
			
			rock.move(kabirGod, GameBoard.torpedos);
			graphicSettings.draw(rock);graphicSettings.setPaint(Color.GRAY);
			graphicSettings.fill(rock);
		}
	}	
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 68) {
			kabirGod.increaseRotationAngle();
			System.out.println("Ship angle: " + kabirGod.getRotationAngle());
		}else 
			if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 65) {
				kabirGod.decreaseRotationAngle();
				System.out.println("Ship angle: " + kabirGod.getRotationAngle());
		}else 
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 87) {
			kabirGod.setMovingAngle(kabirGod.getRotationAngle());
			
			kabirGod.increaseXVelocity(kabirGod.shipXMoveAngle(kabirGod.getMovingAngle())*0.1);
			kabirGod.increaseYVelocity(kabirGod.shipYMoveAngle(kabirGod.getMovingAngle())*0.1);
	}else
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 83) {
			//kabirGod.setMovingAngle(kabirGod.getRotationAngle());
			if(kabirGod.getXVelocity() >= 0)
			kabirGod.decreaseXVelocity(kabirGod.shipXMoveAngle(kabirGod.getMovingAngle())*0.1);
			else kabirGod.setXVelocity(0);
			if(kabirGod.getYVelocity() >= 0)
			kabirGod.decreaseYVelocity(kabirGod.shipYMoveAngle(kabirGod.getMovingAngle())*0.1);
			else kabirGod.setYVelocity(0);
	}
	
		
			
		kabirGod.move();
		//graphicSettings.setTransform(identity);
		graphicSettings.translate(kabirGod.getXCenter(), kabirGod.getYCenter());
		graphicSettings.rotate(Math.toRadians(kabirGod.getRotationAngle()));
		graphicSettings.setColor(Color.RED);
		graphicSettings.draw(kabirGod);graphicSettings.fill(kabirGod);
		
		//draw torpedos
		graphicSettings.setColor(Color.GREEN);
		for(PhotonTorpedo torpedo : GameBoard.torpedos) {
			torpedo.move();
			
			if(torpedo.onScreen) {
				graphicSettings.setTransform(identity);
				graphicSettings.translate(torpedo.getXCenter(), torpedo.getYCenter());
				graphicSettings.draw(torpedo);
				graphicSettings.fill(torpedo);
				
			}
		
		
		
		}
	
	}
		
	
		
		
	}
	
	
}
