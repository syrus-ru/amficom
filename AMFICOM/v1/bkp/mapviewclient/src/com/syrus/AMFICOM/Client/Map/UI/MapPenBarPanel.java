/**
 * $Id: MapPenBarPanel.java,v 1.4 2004/12/30 16:17:48 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.PhysicalLinkType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JPanel;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/30 16:17:48 $
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
		List els = LinkTypeController.getPens(logicalNetLayer.getContext());
		penComboBox.setContents(els, false);
		penComboBox.setSelected(logicalNetLayer.getPen());
	}

	private void penSelected(ActionEvent e)
	{
		if(logicalNetLayer == null)
			return;
		PhysicalLinkType mpe = (PhysicalLinkType)penComboBox.getSelectedObjectResource();
		logicalNetLayer.setPen(mpe);
	}
}

