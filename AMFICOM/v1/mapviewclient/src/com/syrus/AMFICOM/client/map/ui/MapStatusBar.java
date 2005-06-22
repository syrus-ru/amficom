/*
 * $Id: MapStatusBar.java,v 1.10 2005/06/22 13:21:53 krupenn Exp $
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

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/06/22 13:21:53 $
 * @module mapviewclient_v1
 */
public final class MapStatusBar extends JPanel {

	private JLabel latitudeLabel = new JLabel();
	JTextField latitudeTextField = new JTextField();
	private JLabel longitudeLabel = new JLabel();
	JTextField longitudeField = new JTextField();
	private JLabel scaleLabel = new JLabel();
	JTextField scaleField = new JTextField();

	private static Dimension fieldSize = new Dimension(120, 24);
	final NetMapViewer netMapViewer;

	public MapStatusBar(NetMapViewer netMapViewer) {
		super();
		this.netMapViewer = netMapViewer;
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
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
							MapStatusBar.this.netMapViewer.setScale(scale);
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
						MapStatusBar.this.netMapViewer.setCenter(
							new DoublePoint(lon, lat));
					} catch(Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		};
		this.latitudeTextField.addKeyListener(longlatKeyListener);
		this.longitudeField.addKeyListener(longlatKeyListener);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.longitudeLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(1, 5, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.longitudeField, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1, 10, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.latitudeLabel, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(1, 5, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.latitudeTextField, constraints);

		constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(1, 40, 1, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.scaleLabel, constraints);

		constraints.gridx = 5;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(1, 5, 1, 300);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.scaleField, constraints);
	}
}


