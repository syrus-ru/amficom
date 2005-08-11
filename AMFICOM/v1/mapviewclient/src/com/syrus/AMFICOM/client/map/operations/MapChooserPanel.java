/*
 * ��������: $Id: MapChooserPanel.java,v 1.9 2005/08/11 12:43:31 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
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
 * ������ ������ ���� �����
 * @version $Revision: 1.9 $, $Date: 2005/08/11 12:43:31 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class MapChooserPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JButton selectButton = new JButton();

	/**
	 * ������ ��������� ����� �����
	 */
	private JComboBox combo = new JComboBox();
	
	/**
	 * ���� �����
	 */
	private MapFrame mapFrame;
	
	/**
	 * �� ���������
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
	 * ��������� ������ ������ ���� �����
	 * ����� ���� �������������� � ��������� thread'�
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
	 * �������� ������ ��������� ����� �����
	 */
	public synchronized void refreshMapList() {
		this.combo.removeAllItems();
		if (this.mapFrame == null) {
			return;
		}

		List<String> availableViews;
		try {
			availableViews = this.mapFrame.getMapConnection().getAvailableViews();
		} catch (MapDataException e) {
			System.out.println("Cannot get views: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		for (final String view : availableViews) {
			this.combo.addItem(view);
		}
		final String currentMap = this.mapFrame.getMapConnection().getView();
		if (null == currentMap) {
			if (this.combo.getModel().getSize() != 0) {
				this.combo.setSelectedIndex(0);
				changeMap();
			}
		} else
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
