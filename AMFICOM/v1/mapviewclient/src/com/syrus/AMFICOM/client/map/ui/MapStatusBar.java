/*
 * $Id: MapStatusBar.java,v 1.3 2005/05/25 16:46:23 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/05/25 16:46:23 $
 * @module mapviewclient_v1
 */
public final class MapStatusBar extends JPanel 
		implements ApplicationModelListener//, OperationListener
{
	private ApplicationModel aModel = null;

	private LogicalNetLayer logicalNetLayer;

	private JLabel latitudeLabel = new JLabel();
	JTextField latitudeTextField = new JTextField();
	private JLabel longitudeLabel = new JLabel();
	JTextField longitudeField = new JTextField();
	private JLabel scaleLabel = new JLabel();
	JTextField scaleField = new JTextField();

	private static Dimension fieldSize = new Dimension(120, 24);

	public MapStatusBar(LogicalNetLayer logicalNetLayer)
	{
		this();
		setLogicalNetLayer(logicalNetLayer);
	}
	
	public MapStatusBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void showLatLong (double latitude, double longitude)
	{
		try
		{
			this.latitudeTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(latitude));
			this.longitudeField.setText(MapPropertiesManager.getCoordinatesFormat().format(longitude));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void showScale (double scale)
	{
		try
		{
			this.scaleField.setText(MapPropertiesManager.getScaleFormat().format(scale));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void jbInit()
	{
//		this.setPreferredSize(new Dimension(-1,-1));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setLayout(new GridBagLayout());		
		
		this.latitudeLabel.setText(LangModelMap.getString("Latitude"));
		
		this.latitudeTextField.setText("0.0000");
		this.latitudeTextField.setPreferredSize(fieldSize);
		this.latitudeTextField.setMaximumSize(fieldSize);
		this.latitudeTextField.setMinimumSize(fieldSize);
		
		this.longitudeLabel.setText(LangModelMap.getString("Longitude"));
		
		this.longitudeField.setText("0.0000");
		this.longitudeField.setPreferredSize(fieldSize);
		this.longitudeField.setMaximumSize(fieldSize);
		this.longitudeField.setMinimumSize(fieldSize);

		this.scaleLabel.setText(LangModelMap.getString("Scale"));
		
		this.scaleField.setText("0.0");
		this.scaleField.setPreferredSize(fieldSize);
		this.scaleField.setMaximumSize(fieldSize);
		this.scaleField.setMinimumSize(fieldSize);

		this.scaleField.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e) 
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						try
						{
							double scale = Double.parseDouble(MapStatusBar.this.scaleField.getText());
							if(scale > 0)
							{
								getLogicalNetLayer().setScale(scale);
								getLogicalNetLayer().repaint(true);
							}
						}
						catch(Exception ex)
						{
							System.out.println("Wring number format");
						}
					}
				}
				public void keyReleased(KeyEvent e) {/*empty*/}
				public void keyTyped(KeyEvent e) {/*empty*/}
			});

		KeyListener longlatKeyListener = new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						try
						{
							double lon = Double.parseDouble(MapStatusBar.this.longitudeField.getText());
							double lat = Double.parseDouble(MapStatusBar.this.latitudeTextField.getText());
							getLogicalNetLayer().setCenter(
								new DoublePoint(lon, lat));
							getLogicalNetLayer().repaint(true);
						}
						catch(Exception ex)
						{
							System.out.println(ex.getMessage());
						}
					}
				}
				public void keyReleased(KeyEvent e) {/*empty*/}
				public void keyTyped(KeyEvent e) {/*empty*/}
			};
		this.latitudeTextField.addKeyListener(longlatKeyListener);
		this.longitudeField.addKeyListener(longlatKeyListener);

		this.add(this.longitudeLabel,
				new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,0,1,0),0,0));
		this.add(this.longitudeField,
				new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1,5,1,0),0,0));
		this.add(this.latitudeLabel,
				new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,10,1,0),0,0));
		this.add(this.latitudeTextField,
				new GridBagConstraints(3,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1,5,1,0),0,0));

		this.add(this.scaleLabel,
				new GridBagConstraints(4,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,40,1,0),0,0));
		this.add(this.scaleField,
				new GridBagConstraints(5,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1,5,1,300),0,0));
	}

	public void setModel(ApplicationModel a)
	{
	}

	public ApplicationModel getModel()
	{
		return this.aModel;
	}

	public void modelChanged(String e[])
	{
	}


	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}
}


