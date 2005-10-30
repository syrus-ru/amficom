/*-
 * $$Id: MapChooserPanel.java,v 1.21 2005/10/30 14:48:56 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.operations;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Log;

/**
 * панель выбора вида карты
 * 
 * @version $Revision: 1.21 $, $Date: 2005/10/30 14:48:56 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapChooserPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

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
		this.setToolTipText(I18N.getString(MapEditorResourceKeys.TITLE_CHOOSE_MAP));
		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));
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
			Log.debugMessage("Cannot get views: " + e.getMessage(), Level.SEVERE); //$NON-NLS-1$
			Log.debugMessage(e, Level.SEVERE);
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
		
		changeMap();
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
			Log.debugMessage("Cannot change view: " + e.getMessage(), Level.SEVERE); //$NON-NLS-1$
			Log.debugMessage(e, Level.SEVERE);
			this.connection.setView(previousView);
			try {
				this.connection.connect();
			} catch(MapConnectionException e1) {
				e1.printStackTrace();
			}
		}
	}
}
