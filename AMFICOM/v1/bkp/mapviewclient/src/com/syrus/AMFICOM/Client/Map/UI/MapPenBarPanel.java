/**
 * $Id: MapPenBarPanel.java,v 1.1 2004/09/16 10:39:53 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import java.util.Iterator;

import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/16 10:39:53 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPenBarPanel extends JPanel 
{
	final static int ELEMENT_DIMENSION = 30;
	private ObjectResourceComboBox penComboBox = new ObjectResourceComboBox();

	private LogicalNetLayer logicalNetLayer;

	public MapPenBarPanel(LogicalNetLayer logicalNetLayer)
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
		setLogicalNetLayer(logicalNetLayer);
	}
	
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		setProtoElements();
	}

	private void jbInit()
	{
		this.setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 27));
		setMinimumSize(new Dimension(150, 27));
		setMaximumSize(new Dimension(150, 27));
		penComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					penSelected(e);
				}
			});
		this.add(penComboBox, BorderLayout.CENTER);
	}

	public void setContext(ApplicationContext aContext)
	{
	}
	
	public void setProtoElements()
	{
		if(logicalNetLayer == null)
			return;
		List els = logicalNetLayer.getPens();
		penComboBox.setContents(els, false);
		penComboBox.setSelected(logicalNetLayer.getPen());
	}

	private void penSelected(ActionEvent e)
	{
		if(logicalNetLayer == null)
			return;
		MapLinkProtoElement mpe = (MapLinkProtoElement )penComboBox.getSelectedObjectResource();
		logicalNetLayer.setPen(mpe);
	}
}

