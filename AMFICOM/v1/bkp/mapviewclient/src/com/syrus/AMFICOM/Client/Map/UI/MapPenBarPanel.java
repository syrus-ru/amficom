/**
 * $Id: MapPenBarPanel.java,v 1.8 2005/02/28 16:16:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * @version $Revision: 1.8 $, $Date: 2005/02/28 16:16:43 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapPenBarPanel extends JPanel 
{
	static final int ELEMENT_DIMENSION = 30;
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
		this.penComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					penSelected(e);
				}
			});
		this.add(this.penComboBox, BorderLayout.CENTER);
	}

	public void setProtoElements()
	{
		if(this.logicalNetLayer == null)
			return;
		Collection els = LinkTypeController.getPens(this.logicalNetLayer.getContext());
		this.penComboBox.setContents(els, false);
		this.penComboBox.setSelected(this.logicalNetLayer.getPen());
	}

	void penSelected(ActionEvent e)
	{
		if(this.logicalNetLayer == null)
			return;
		PhysicalLinkType mpe = (PhysicalLinkType)this.penComboBox.getSelectedObjectResource();
		this.logicalNetLayer.setPen(mpe);
	}
}

