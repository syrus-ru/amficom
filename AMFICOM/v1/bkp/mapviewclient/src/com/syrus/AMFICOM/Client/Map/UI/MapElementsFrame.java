/**
 * $Id: MapElementsFrame.java,v 1.3 2004/09/29 15:11:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 * Окно со списком элементов 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/09/29 15:11:26 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapElementsFrame extends JInternalFrame 
	implements OperationListener
{
	ApplicationContext aContext;
	MapElementPanel panel = new MapElementPanel(null);

	public MapElementsFrame()
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

	public MapElementsFrame(ApplicationContext aContext)
	{
		this();
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				Dispatcher disp = this.aContext.getDispatcher();
				disp.unregister(this, MapEvent.MAP_SELECTED);
				disp.unregister(this, MapEvent.MAP_DESELECTED);
				disp.unregister(this, MapEvent.MAP_CHANGED);
				disp.unregister(this, MapEvent.SELECTION_CHANGED);
				disp.unregister(this, MapEvent.MAP_FRAME_SHOWN);
			}
		this.aContext = aContext;
		panel.setContext(aContext);
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_SELECTED);
		disp.register(this, MapEvent.MAP_DESELECTED);
		disp.register(this, MapEvent.MAP_CHANGED);
		disp.register(this, MapEvent.SELECTION_CHANGED);
		disp.register(this, MapEvent.MAP_FRAME_SHOWN);
	}

	private void jbInit()
	{
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(false);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle(LangModelMap.getString("Elements"));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public void setMap(Map mc)
	{
		panel.setMap(mc);
	}
	public void operationPerformed(OperationEvent ae)
	{

		if(ae.getActionCommand().equals(MapEvent.MAP_CLOSED))
			if(panel.performProcessing)
				setMap(null);
		if(	ae.getActionCommand().equals(MapEvent.MAP_SELECTED))
			if(panel.performProcessing)
				setMap(((MapView)ae.getSource()).getMap());
		if(	ae.getActionCommand().equals(MapEvent.MAP_CHANGED))
			if(panel.performProcessing)
				panel.updateTable();
		if(	ae.getActionCommand().equals(MapEvent.SELECTION_CHANGED))
			if(panel.performProcessing)
				panel.setSelectedObjects();
		if(	ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			MapFrame mapFrame = (MapFrame)ae.getSource();
			
			panel.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
		}

	}
}
