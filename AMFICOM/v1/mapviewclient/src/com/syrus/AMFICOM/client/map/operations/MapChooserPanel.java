/*
 * Название: $Id: MapChooserPanel.java,v 1.8 2005/06/22 13:21:53 krupenn Exp $
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
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * панель выбора вида карты
 * @version $Revision: 1.8 $, $Date: 2005/06/22 13:21:53 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapChooserPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JButton selectButton = new JButton();

	/**
	 * список возможных видов карты
	 */
	private JComboBox combo = new JComboBox();
	
	/**
	 * окно карты
	 */
	private MapFrame mapFrame;
	
	/**
	 * По умолчанию
	 */
	public MapChooserPanel(MapFrame mapFrame)
	{
		jbInit();
		setMapFrame(mapFrame);
	}

	private void jbInit()
	{
		this.setToolTipText(LangModelMap.getString("ChooseMap"));
		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		this.selectButton.setText(LangModelGeneral.getString("Button.OK"));
		this.selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mapSelected();
				}
			});
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
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.add(this.selectButton, constraints);

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
	 * обработка выбора нового вида карты
	 * смена вида осуществляется в отдельном thread'е
	 */
	void mapSelected()
	{
		if(this.mapFrame == null)
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

	/**
	 * обновить список возможных видов карты
	 */
	public synchronized void refreshMapList() 
	{
		this.combo.removeAllItems();
		if(this.mapFrame == null)
			return;

		List availableViews;
		try
		{
			availableViews = this.mapFrame.getMapConnection().getAvailableViews();
		}
		catch(MapDataException e)
		{
			System.out.println("Cannot get views: " + e.getMessage());
			e.printStackTrace();
			return;
		} 

		for(Iterator it = availableViews.iterator(); it.hasNext();)
		{
			this.combo.addItem(it.next());
		}
		String currentMap = this.mapFrame.getMapConnection().getView();
		if (null == currentMap) 
		{
			if(this.combo.getModel().getSize() != 0)
			{
				this.combo.setSelectedIndex(0);
				changeMap();
			}
		}
		else
			this.combo.setSelectedItem(currentMap);
	}
	
	public void setMapFrame(MapFrame mmf) 
	{
		this.mapFrame = mmf;
		refreshMapList();
	}

	public MapFrame getMapFrame() 
	{
		return this.mapFrame;
	}

	void changeMap() 
	{
		String name = (String )this.combo.getSelectedItem();
		String previousView = this.mapFrame.getMapConnection().getView(); 
		try
		{
			this.mapFrame.getMapConnection().setView(name);
			this.mapFrame.getMapConnection().connect();
		}
		catch(MapConnectionException e)
		{
			System.out.println("Cannot change view: " + e.getMessage());
			e.printStackTrace();
			this.mapFrame.getMapConnection().setView(previousView);
		}
	}
}
