/*
 * Название: $Id: LayersPanel.java,v 1.1 2005/03/02 12:30:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Operations;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

/**
 * панель управления отображением слоев
 * @version $Revision: 1.1 $, $Date: 2005/03/02 12:30:40 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class LayersPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	/**
	 * панель списка слоев
	 */
	private JPanel layersPanel = new JPanel();
	
	/**
	 * панель заголовка
	 */
	private JPanel titlePanel = new JPanel();

	/**
	 * окно карты
	 */
	private MapFrame mapFrame;
	
	/**
	 * список слоев
	 */
	private List tableData = new LinkedList();

	/**
	 * обработчик изменения видимости слоя
	 */
	private ActionListener actionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				LayerVisibilityCheckBox cb = (LayerVisibilityCheckBox )e.getSource();
				SpatialLayer sl = cb.getSpatialLayer();
				sl.setVisible(cb.isSelected());
			}
		};

	/**
	 * По умолчанию
	 */
	public LayersPanel()
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
		this.setToolTipText(LangModelMap.getString("ConfigureTopologicalLayers"));

		this.setLayout(new BorderLayout());

		this.setSize(new Dimension(370, 629));

		this.titlePanel.setLayout(this.gridBagLayout1);
		this.layersPanel.setLayout(this.gridBagLayout1);

		GridBagConstraints gridbagconstraints = new GridBagConstraints();

		ImageIcon ii = new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/seesymbs.gif"));
		JLabel jlabel = new JLabel(ii);
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		this.titlePanel.add(jlabel, gridbagconstraints);

		Component box = Box.createHorizontalBox();
		box.setSize(32, 1);
		gridbagconstraints.gridx = 1;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		this.titlePanel.add(box, gridbagconstraints);

		JLabel jlabel2 = new JLabel(LangModelMap.getString("Layers"));
		gridbagconstraints.gridx = 2;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 2;
		this.titlePanel.add(jlabel2, gridbagconstraints);

		Component strut = Box.createHorizontalStrut(5);
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 1;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 3;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		this.titlePanel.add(strut, gridbagconstraints);

		JSeparator jseparator = new JSeparator();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 2;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 3;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 2;
		this.titlePanel.add(jseparator, gridbagconstraints);

		this.add(this.titlePanel, BorderLayout.NORTH);
		this.add(new JScrollPane(this.layersPanel), BorderLayout.CENTER);
	}

	/**
	 * установка окна карты -> перерисовка или обнуление списка слоев
	 */
	public void setMapFrame(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
		if(mapFrame != null)
		{
			updateList();
		}
		else
		{
			clearList();
		}
	}

	/**
	 * очистить панель со списком слоев
	 */
	public void clearList()
	{
		this.tableData.clear();
		this.layersPanel.removeAll();
	}
	
	/**
	 * обновить окно со списком слоев
	 */
	public void updateList()
	{
		this.tableData.clear();
		
		try
		{
			for(Iterator it = this.mapFrame.getMapViewer().getLayers().iterator(); it.hasNext();)
			{
				SpatialLayer sl = (SpatialLayer )it.next();
				this.tableData.add(sl);
			}
		}
		catch(MapDataException e)
		{
			System.out.println("cannot get layers");
			e.printStackTrace();
		}
		
		layoutLayerRows();
	}
	
	/**
	 * разместить графические элемента управления отображением слоев на панели 
	 * списка слоев
	 */
	public void layoutLayerRows()
	{
		this.layersPanel.removeAll();

		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		Component imageLabel;
		SpatialLayer sl;

		int i = 0;
		
		for(Iterator it = this.tableData.iterator(); it.hasNext();)
		{
			sl = (SpatialLayer )it.next();
		
			imageLabel = sl.getLayerImage();

			LayerVisibilityCheckBox jcheckbox = new LayerVisibilityCheckBox(sl);
			jcheckbox.setSelected(sl.isVisible());
			jcheckbox.addActionListener(this.actionListener);

			jcheckbox.setBackground(this.layersPanel.getBackground());
			jcheckbox.setAlignmentY(0.5F);
			jcheckbox.setAlignmentX(0.8F);
			gridbagconstraints.gridx = 0;
			gridbagconstraints.gridy = i;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.weighty = 0.0D;
			gridbagconstraints.gridwidth = 1;
			gridbagconstraints.gridheight = 1;
			gridbagconstraints.fill = 0;
			this.layersPanel.add(jcheckbox, gridbagconstraints);

			if(imageLabel != null)
			{
				imageLabel.setBackground(this.layersPanel.getBackground());
				gridbagconstraints.gridx = 1;
				gridbagconstraints.gridy = i;
				gridbagconstraints.weightx = 0.0D;
				gridbagconstraints.weighty = 0.0D;
				gridbagconstraints.gridwidth = 1;
				gridbagconstraints.gridheight = 1;
				gridbagconstraints.fill = 0;
				this.layersPanel.add(imageLabel, gridbagconstraints);
			}

			JLabel nameLabel = new JLabel(" " + sl.getName());
			gridbagconstraints.gridx = 2;
			gridbagconstraints.gridy = i;
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.weighty = 1.0D;
			gridbagconstraints.gridwidth = 1;
			gridbagconstraints.gridheight = 1;
			gridbagconstraints.fill = 2;
			this.layersPanel.add(nameLabel, gridbagconstraints);
			i++;
		}
		
		this.revalidate();
	}
	
	public MapFrame getMapFrame() 
	{
		return this.mapFrame;
	}

	/**
	 * чекбокс отображения слоя
	 */
	private class LayerVisibilityCheckBox extends JCheckBox
	{
		SpatialLayer sl;
		
		public LayerVisibilityCheckBox(SpatialLayer sl)
		{
			super();
			this.sl = sl;
		}
		
		public SpatialLayer getSpatialLayer()
		{
			return this.sl;
		}
	}
}

