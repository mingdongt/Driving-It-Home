package com.unimelb.swen30006.partc.controllers;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.*;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;

import SensingSystem.ColourTest;
import SensingSystem.Grid;


/**
 * The AI controller for use in integrating your systems with the simulation.
 * @author Mathew Blair <mathew.blair@unimelb.edu.au>
 * @version 1.0
 */
public class AIController extends Controller {
	
	// The interfaces used to update the world
	private ISensing sensor;
	private IPlanning planner;
	private IPerception classifier;
	private Graphics2D g2;
    Color[][] colMap = null;
    ColourTest test= null;
    public int count;
	
	//public AIController(Car car, ISensing sensor, IPlanning planner, IPerception classifier) {
	public AIController(Car car, ISensing sensor) {
		
		super(car);
		//tl=new Test();
		// Modify this as you like to instantiate your interface
		this.sensor=sensor;
		//this.planner=planner;
		//this.classifier=classifier;
		test = new ColourTest();
	}

	@Override
	public void update(float delta) {
		
		count++;
		
		// First update sensing
		sensor.update(this.car.getPosition(), delta, World.VISIBILITY_RADIUS);	
	
		//Vector2[][] velMap = sensor.getVelocityMap();
		//boolean[][] spaceMap = sensor.getSpaceMap();
		colMap = sensor.getColourMap();
		
		if (colMap != null && count > 10){
			//test = new ColourTest();
			test.updateColMap(colMap);
			count = 0;
		}	
	}
}
