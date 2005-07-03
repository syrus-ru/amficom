/**
 * $Id: MapPenBarPanel.java,v 1.15 2005/07/01 16:22:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.UI.dialogs.NamedObjectWrapper;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * @version $Revision: 1.15 $, $Date: 2005/07/01 16:22:36 $
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
			this.logicalNetLayer = logicalNetLayer;
			setProtoElements();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
	}

	private void jbInit()
	{
		this.setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 27));
		setMinimumSize(new Dimension(150, 27));
		setMaximumSize(new Dimension(150, 27));
		
		NamedObjectWrapper controller = NamedObjectWrapper.getInstance(); 
		this.penComboBox = new WrapperedComboBox(
				controller, 
				NamedObjectWrapper.KEY_NAME, 
				NamedObjectWrapper.KEY_NAME);
		
		this.penComboBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					penSelected(e);
				}
			});
		this.add(this.penComboBox, BorderLayout.CENTER);
	}

	public void setProtoElements() throws ApplicationException
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

