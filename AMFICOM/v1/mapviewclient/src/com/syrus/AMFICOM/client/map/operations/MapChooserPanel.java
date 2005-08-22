/*
 * Название: $Id: MapChooserPanel.java,v 1.14 2005/08/22 12:34:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.operations;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.util.Log;

/**
 * панель выбора вида карты
 * @version $Revision: 1.14 $, $Date: 2005/08/22 12:34:38 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapChooserPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

//	JButton selectButton = new JButton();

	/**
	 * список возможных видов карты
	 */
	private JComboBox combo = new JComboBox();
	
	/**
	 * окно карты
	 */
	private MapConnection connection;
	
	/**
	 * По умолчанию
	 */
	public MapChooserPanel(MapConnection connection)
	{
		jbInit();
		setMapConnection(connection);
	}

	private void jbInit()
	{
		this.setToolTipText(LangModelMap.getString("ChooseMap"));
		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));
//		this.selectButton.setText(LangModelGeneral.getString("Button.OK"));
//		this.selectButton.addActionListener(new ActionListener()
//			{
//				public void actionPerformed(ActionEvent e)
//				{
//					mapSelected();
//				}
//			});
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.combo, constraints);

//		constraints.gridx = 0;
//		constraints.gridy = 1;
//		constraints.gridwidth = 1;
//		constraints.gridheight = 1;
//		constraints.weightx = 0.0;
//		constraints.weighty = 0.0;
//		constraints.anchor = GridBagConstraints.CENTER;
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.insets = new Insets(5, 5, 5, 5);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.add(this.selectButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(Box.createVerticalGlue(), constraints);
	}

	/**
	 * обновить список возможных видов карты
	 */
	public synchronized void refreshMapList() {
		this.combo.removeAllItems();
		if (this.connection == null) {
			return;
		}

		List<String> availableViews;
		try {
			availableViews = this.connection.getAvailableViews();
		} catch(MapDataException e) {
			Log.debugMessage("Cannot get views: " + e.getMessage(), Level.SEVERE);
			Log.debugException(e, Level.SEVERE);
			return;
		}

		for (final String view : availableViews) {
			this.combo.addItem(view);
		}
		final String currentMap = this.connection.getView();
		if (null == currentMap) {
			if (this.combo.getModel().getSize() != 0) {
				this.combo.setSelectedIndex(0);
				changeMap();
			}
		} else
			this.combo.setSelectedItem(currentMap);
	}
	
	public void setMapConnection(MapConnection connection) 
	{
		this.connection = connection;
		refreshMapList();
	}

	/**
	 * обработка выбора нового вида карты
	 * смена вида осуществляется в отдельном thread'е
	 */
	public void mapSelected()
	{
		if(this.connection == null)
			return;
		
		Thread t = new Thread(new Runnable() 
		{
			public void run() 
			{
				changeMap();
			}
		});
		t.start();
	}

	void changeMap() 
	{
		String name = (String )this.combo.getSelectedItem();
		String previousView = this.connection.getView(); 
		try
		{
			this.connection.setView(name);
			this.connection.connect();
		}
		catch(MapConnectionException e)
		{
			Log.debugMessage("Cannot change view: " + e.getMessage(), Level.SEVERE);
			Log.debugException(e, Level.SEVERE);
			this.connection.setView(previousView);
		}
	}
}
