/*
 * Название: $Id: MapChooserPanel.java,v 1.5 2005/02/10 11:48:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Setup;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * панель выбора вида карты
 * @version $Revision: 1.5 $, $Date: 2005/02/10 11:48:39 $
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
	public MapChooserPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit()
	{
		this.setLayout(this.gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		this.selectButton.setText(LangModel.getString("Ok"));
		this.selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mapSelected();
				}
			});
		this.add(this.combo, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.selectButton, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(Box.createVerticalGlue(), com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
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

		for(Iterator it = this.mapFrame.getMapViewer().getAvailableViews().iterator(); it.hasNext();)
		{
			this.combo.addItem(it.next());
		}
		String currentMap = this.mapFrame.getMapViewer().getConnection().getView();
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
		this.mapFrame.getMapViewer().setView(name);
	}
}
