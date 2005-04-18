/**
 * $Id: MapElementsBarPanel.java,v 1.12 2005/04/18 11:23:45 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.Dimension;
import java.awt.FlowLayout;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/04/18 11:23:45 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapElementsBarPanel extends JPanel 
{
	ApplicationContext aContext;

	public MapElementsBarPanel()
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
	}

	private void jbInit()
	{
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setPreferredSize(new Dimension(MapElementLabel.ELEMENT_DIMENSION + 10, -1));
		this.setBorder(BorderFactory.createEtchedBorder());
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		setProtoElements();
	}
	
	public void setProtoElements()
	{
		MapElementLabel mel;
		SiteNodeType mpe;

		this.removeAll();
		
		Collection elements = getMapProtoElements();
		
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			mpe = (SiteNodeType )it.next();
			mel = new MapElementLabel(mpe);
			mel.setToolTipText( mpe.getName());
			this.add(mel);
		}
	}

//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		for (int i = 0; i < this.getComponentCount(); i++)
			((MapElementLabel)this.getComponent(i)).setEnabled(b);
	}

	private Collection getMapProtoElements()
	{
		return NodeTypeController.getTopologicalNodeTypes();
	}
}

