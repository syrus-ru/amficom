/*
 * Название: $Id: LayersPanel.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Setup;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
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
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @author $Author: krupenn $
 * @see
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
	private MapFrame mmf;
	
	/**
	 * список слоев
	 */
	private List tableData = new LinkedList();

	/**
	 * обработчик изменения видимости слоя
	 */
	private ActionListener al = new ActionListener()
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

	/**
	 * Метод jbInit
	 * 
	 * 
	 * @exception Exception
	 */
	private void jbInit()
	{
		this.setLayout(new BorderLayout());

		this.setSize(new Dimension(370, 629));

		titlePanel.setLayout(gridBagLayout1);
		layersPanel.setLayout(gridBagLayout1);

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
		titlePanel.add(jlabel, gridbagconstraints);

		JLabel jlabel2 = new JLabel(LangModelMap.getString("Layers"));
		gridbagconstraints.gridx = 2;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 1;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 2;
		titlePanel.add(jlabel2, gridbagconstraints);

		Component strut = Box.createHorizontalStrut(5);
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 1;
		gridbagconstraints.weightx = 0.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 3;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 0;
		titlePanel.add(strut, gridbagconstraints);

		JSeparator jseparator = new JSeparator();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 2;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 0.0D;
		gridbagconstraints.gridwidth = 3;
		gridbagconstraints.gridheight = 1;
		gridbagconstraints.fill = 2;
		titlePanel.add(jseparator, gridbagconstraints);

		this.add(titlePanel, BorderLayout.NORTH);
		this.add(new JScrollPane(layersPanel), BorderLayout.CENTER);
	}

	/**
	 * установка окна карты -> перерисовка или обнуление списка слоев
	 */
	public void setMapFrame(MapFrame mmf)
	{
		this.mmf = mmf;
		if(mmf != null)
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
		tableData.clear();
		layersPanel.removeAll();
	}
	
	/**
	 * обновить окно со списком слоев
	 */
	public void updateList()
	{
		tableData.clear();
		
		for(Iterator it = mmf.getMapViewer().getLayers().iterator(); it.hasNext();)
		{
			SpatialLayer sl = (SpatialLayer )it.next();
			tableData.add(sl);
		}
		
		layoutLayerRows();
	}
	
	/**
	 * разместить графические элемента управления отображением слоев на панели 
	 * списка слоев
	 */
	public void layoutLayerRows()
	{
		layersPanel.removeAll();

		GridBagConstraints gridbagconstraints = new GridBagConstraints();
		Component imageLabel;
		SpatialLayer sl;

		int i = 0;
		
		for(Iterator it = tableData.iterator(); it.hasNext();)
		{
			sl = (SpatialLayer )it.next();
		
			imageLabel = sl.getLayerImage();

			LayerVisibilityCheckBox jcheckbox = new LayerVisibilityCheckBox(sl);
			jcheckbox.setSelected(sl.isVisible());
			jcheckbox.addActionListener(al);

			jcheckbox.setBackground(layersPanel.getBackground());
			jcheckbox.setAlignmentY(0.5F);
			jcheckbox.setAlignmentX(0.8F);
			gridbagconstraints.gridx = 0;
			gridbagconstraints.gridy = i;
			gridbagconstraints.weightx = 0.0D;
			gridbagconstraints.weighty = 0.0D;
			gridbagconstraints.gridwidth = 1;
			gridbagconstraints.gridheight = 1;
			gridbagconstraints.fill = 0;
			layersPanel.add(jcheckbox, gridbagconstraints);

			if(imageLabel != null)
			{
				imageLabel.setBackground(layersPanel.getBackground());
				gridbagconstraints.gridx = 1;
				gridbagconstraints.gridy = i;
				gridbagconstraints.weightx = 0.0D;
				gridbagconstraints.weighty = 0.0D;
				gridbagconstraints.gridwidth = 1;
				gridbagconstraints.gridheight = 1;
				gridbagconstraints.fill = 0;
				layersPanel.add(imageLabel, gridbagconstraints);
			}

			JLabel nameLabel = new JLabel(" " + sl.getName());
			gridbagconstraints.gridx = 2;
			gridbagconstraints.gridy = i;
			gridbagconstraints.weightx = 1.0D;
			gridbagconstraints.weighty = 1.0D;
			gridbagconstraints.gridwidth = 1;
			gridbagconstraints.gridheight = 1;
			gridbagconstraints.fill = 2;
			layersPanel.add(nameLabel, gridbagconstraints);
			i++;
		}
		
		this.revalidate();
	}
	
	public MapFrame getMapFrame() 
	{
		return mmf;
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

