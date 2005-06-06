/*
 * $Id: MapStatusBar.java,v 1.6 2005/06/06 12:20:36 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:20:36 $
 * @module mapviewclient_v1
 */
public final class MapStatusBar extends JPanel {
	LogicalNetLayer logicalNetLayer;

	private JLabel latitudeLabel = new JLabel();
	JTextField latitudeTextField = new JTextField();
	private JLabel longitudeLabel = new JLabel();
	JTextField longitudeField = new JTextField();
	private JLabel scaleLabel = new JLabel();
	JTextField scaleField = new JTextField();

	private static Dimension fieldSize = new Dimension(120, 24);

	public MapStatusBar(LogicalNetLayer logicalNetLayer) {
		this();
		setLogicalNetLayer(logicalNetLayer);
	}

	public MapStatusBar() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer) {
		this.logicalNetLayer = logicalNetLayer;
	}

	public void showLatLong(double latitude, double longitude) {
		try {
			this.latitudeTextField.setText(
				MapPropertiesManager.getCoordinatesFormat().format(latitude));
			this.longitudeField.setText(
				MapPropertiesManager.getCoordinatesFormat().format(longitude));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void showScale(double scale) {
		try {
			this.scaleField.setText(
				MapPropertiesManager.getScaleFormat().format(scale));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		// this.setPreferredSize(new Dimension(-1,-1));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setLayout(new GridBagLayout());

		this.latitudeLabel.setText(LangModelMap.getString("Latitude"));

		this.latitudeTextField.setText("0.0000");
		this.latitudeTextField.setSize(fieldSize);
		this.latitudeTextField.setPreferredSize(fieldSize);
		this.latitudeTextField.setMaximumSize(fieldSize);
		this.latitudeTextField.setMinimumSize(fieldSize);

		this.longitudeLabel.setText(LangModelMap.getString("Longitude"));

		this.longitudeField.setText("0.0000");
		this.longitudeField.setSize(fieldSize);
		this.longitudeField.setPreferredSize(fieldSize);
		this.longitudeField.setMaximumSize(fieldSize);
		this.longitudeField.setMinimumSize(fieldSize);

		this.scaleLabel.setText(LangModelMap.getString("Scale"));

		this.scaleField.setText("0.0");
		this.scaleField.setSize(fieldSize);
		this.scaleField.setPreferredSize(fieldSize);
		this.scaleField.setMaximumSize(fieldSize);
		this.scaleField.setMinimumSize(fieldSize);

		this.scaleField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						double scale = Double.parseDouble(
								MapStatusBar.this.scaleField.getText());
						if(scale > 0) {
							MapStatusBar.this.logicalNetLayer.setScale(scale);
							MapStatusBar.this.logicalNetLayer.repaint(true);
						}
					} catch(Exception ex) {
						System.out.println("Wring number format");
					}
				}
			}
		});

		KeyListener longlatKeyListener = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						double lon = Double.parseDouble(
							MapStatusBar.this.longitudeField.getText());
						double lat = Double.parseDouble(
							MapStatusBar.this.latitudeTextField.getText());
						MapStatusBar.this.logicalNetLayer.setCenter(
							new DoublePoint(lon, lat));
						MapStatusBar.this.logicalNetLayer.repaint(true);
					} catch(Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		};
		this.latitudeTextField.addKeyListener(longlatKeyListener);
		this.longitudeField.addKeyListener(longlatKeyListener);

		this.add(this.longitudeLabel, ReusedGridBagConstraints.get(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,0,1,0),0,0));
		this.add(this.longitudeField, ReusedGridBagConstraints.get(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1,5,1,0),0,0));
		this.add(this.latitudeLabel, ReusedGridBagConstraints.get(2,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,10,1,0),0,0));
		this.add(this.latitudeTextField, ReusedGridBagConstraints.get(3,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1,5,1,0),0,0));

		this.add(this.scaleLabel, ReusedGridBagConstraints.get(4,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,40,1,0),0,0));
		this.add(this.scaleField, ReusedGridBagConstraints.get(5,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1,5,1,300),0,0));
	}
}


