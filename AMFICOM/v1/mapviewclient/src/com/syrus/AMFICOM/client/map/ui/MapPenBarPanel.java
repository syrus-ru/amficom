/*-
 * $$Id: MapPenBarPanel.java,v 1.19 2006/02/15 11:13:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.util.Log;

/**
 *  Ќа этой панельке располагаютс€ элементы которые будут наноситьс€ на карту
 *  
 * @version $Revision: 1.19 $, $Date: 2006/02/15 11:13:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
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
		  Log.errorMessage(e);
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
		Collection els = LinkTypeController.getTopologicalLinkTypes(this.logicalNetLayer.getMapView().getMap());
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

