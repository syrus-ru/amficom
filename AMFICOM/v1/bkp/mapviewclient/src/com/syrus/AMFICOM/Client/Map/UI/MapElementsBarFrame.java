/**
 * $Id: MapElementsBarFrame.java,v 1.5 2005/02/10 11:48:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

/**
 * Внутреннее окно, содержит панель элементов, перетаскиваемых на карту 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/02/10 11:48:39 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapElementsBarFrame extends JInternalFrame implements OperationListener
{
	ApplicationContext aContext;
	MapElementsBarPanel panel = new MapElementsBarPanel();

	public MapElementsBarFrame()
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

	public MapElementsBarFrame(ApplicationContext aContext)
	{
		this();
		setContext(aContext);
		this.panel.setEnableDisablePanel(true);
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
//				Dispatcher disp = this.aContext.getDispatcher();
				// unregister event listener
			}
		this.aContext = aContext;
		this.panel.setContext(aContext);
		if(aContext == null)
			return;
//		Dispatcher disp = aContext.getDispatcher();
//		if(disp == null)
//			return;
		// register event listener
	}

	private void jbInit()
	{
		this.setClosable(true);
		this.setIconifiable(false);
		this.setMaximizable(false);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle(LangModelMap.getString("elementsBarTitle"));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.panel, BorderLayout.CENTER);
	}
	
	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals(MapEvent.MAP_VIEW_DESELECTED))
			this.panel.setEnableDisablePanel(false);
		if(oe.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
			this.panel.setEnableDisablePanel(true);
	}
}
