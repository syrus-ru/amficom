/**
 * $Id: MapElementsBarPanel.java,v 1.5 2004/12/22 16:38:42 krupenn Exp $
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
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.FlowLayout;

import java.util.Iterator;

import javax.swing.JPanel;

/**
 *  На этой панельке располагаются элементы которые будут наноситься на карту
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapElementsBarPanel extends JPanel 
{
	final static int ELEMENT_DIMENSION = 30;

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
		setProtoElements();
	}

	private void jbInit()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void setContext(ApplicationContext aContext)
	{
	}
	
	public void setProtoElements()
	{
		MapElementLabel mel;
		SiteNodeType mpe;

		this.removeAll();
		
		java.util.List elements = getMapProtoElements();
		
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			mpe = (SiteNodeType)it.next();
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
		MapFrame mf = MapFrame.getMapMainFrame();
		return mf.getMapViewer().getLogicalNetLayer().getTopologicalProtos();
	}
}

