/**
 * $Id: MapElementsBarPanel.java,v 1.7 2005/01/21 16:19:58 krupenn Exp $
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

import java.awt.FlowLayout;

import java.util.Iterator;

import javax.swing.JPanel;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/21 16:19:58 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapElementsBarPanel extends JPanel 
{
	static final int ELEMENT_DIMENSION = 30;
	
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
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		
		java.util.List elements = getMapProtoElements();
		
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

	private java.util.List getMapProtoElements()
	{
		return NodeTypeController.getTopologicalProtos(aContext);
	}
}

