/*
 * Название: $Id: MapChooserPanel.java,v 1.2 2004/10/04 16:04:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.ReusedGridBagConstraints;

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
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/04 16:04:43 $
 * @author $Author: krupenn $
 * @see
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
	private MapFrame mmf;
	
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

	/**
	 * Метод jbInit
	 * 
	 * 
	 * @exception Exception
	 */
	private void jbInit()
	{
		this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		selectButton.setText(LangModel.getString("Ok"));
		selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					mapSelected();
				}
			});
		this.add(combo, ReusedGridBagConstraints.get(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(selectButton, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.add(Box.createVerticalGlue(), ReusedGridBagConstraints.get(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	}

	/**
	 * обработка выбора нового вида карты
	 * смена вида осуществляется в отдельном thread'е
	 */
	private void mapSelected()
	{
		if(mmf == null)
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
		combo.removeAllItems();
		if(mmf == null)
			return;

		for(Iterator it = mmf.getMapViewer().getAvailableViews().iterator(); it.hasNext();)
		{
			combo.addItem(it.next());
		}
		String currentMap = mmf.getMapViewer().getConnection().getView();
		if (null == currentMap) 
		{
			if(combo.getModel().getSize() != 0)
			{
				combo.setSelectedIndex(0);
				changeMap();
			}
		}
		else
			combo.setSelectedItem(currentMap);
	}
	
	public void setMapFrame(MapFrame mmf) 
	{
		this.mmf = mmf;
		refreshMapList();
	}

	public MapFrame getMapFrame() 
	{
		return mmf;
	}

	private void changeMap() 
	{
		String name = (String )combo.getSelectedItem();
		mmf.getMapViewer().setView(name);
	}
}
