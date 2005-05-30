/**
 * $Id: MapPenBarPanel.java,v 1.11 2005/05/30 12:19:02 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.dialogs.NamedObjectController;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * @version $Revision: 1.11 $, $Date: 2005/05/30 12:19:02 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapPenBarPanel extends JPanel 
{
	static final int ELEMENT_DIMENSION = 30;
	private WrapperedComboBox penComboBox;

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
		
		NamedObjectController controller = NamedObjectController.getInstance(); 
		this.penComboBox = new WrapperedComboBox(
				controller, 
				NamedObjectController.KEY_NAME, 
				NamedObjectController.KEY_NAME);
		
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
		Collection els = LinkTypeController.getTopologicalLinkTypes();
		this.penComboBox.removeAllItems();
		this.penComboBox.addElements(els);
		this.penComboBox.setSelectedItem(this.logicalNetLayer.getCurrentPhysicalLinkType());
	}

	void penSelected(ActionEvent e)
	{
		if(this.logicalNetLayer == null)
			return;
		PhysicalLinkType mpe = (PhysicalLinkType )this.penComboBox.getSelectedItem();
		this.logicalNetLayer.setCurrentPhysicalLinkType(mpe);
	}
}

