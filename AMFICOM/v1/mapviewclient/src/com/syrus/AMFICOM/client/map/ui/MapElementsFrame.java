/**
 * $Id: MapElementsFrame.java,v 1.10 2005/02/10 11:48:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 * ���� �� ������� ���������. 
 * @version $Revision: 1.10 $, $Date: 2005/02/10 11:48:39 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapElementsFrame extends JInternalFrame 
	implements OperationListener
{
	ApplicationContext aContext;
	MapElementsPanel panel = new MapElementsPanel(null);

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
				disp.unregister(this, MapEvent.MAP_VIEW_SELECTED);
				disp.unregister(this, MapEvent.MAP_VIEW_DESELECTED);
				disp.unregister(this, MapEvent.MAP_CHANGED);
				disp.unregister(this, MapEvent.SELECTION_CHANGED);
				disp.unregister(this, MapEvent.MAP_FRAME_SHOWN);
			}
		this.aContext = aContext;
		this.panel.setContext(aContext);
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_VIEW_SELECTED);
		disp.register(this, MapEvent.MAP_VIEW_DESELECTED);
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
		getContentPane().add(this.panel, BorderLayout.CENTER);
	}

	public void setMap(Map mc)
	{
		this.panel.setMap(mc);
	}
	public void operationPerformed(OperationEvent ae)
	{

		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED) && this.panel.performProcessing)
			setMap(null);
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED) && this.panel.performProcessing)
			setMap(((MapView)ae.getSource()).getMap());
		if(ae.getActionCommand().equals(MapEvent.MAP_CHANGED) && this.panel.performProcessing)
		{
			this.panel.doNotify = false;
			this.panel.updateTable();
			this.panel.doNotify = true;
		}
		if(ae.getActionCommand().equals(MapEvent.SELECTION_CHANGED) && this.panel.performProcessing)
		{
			this.panel.doNotify = false;
			this.panel.setSelectedObjects();
			this.panel.doNotify = true;
		}
		if(	ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			MapFrame mapFrame = (MapFrame)ae.getSource();
			
			this.panel.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
		}

	}
}
