package com.syrus.AMFICOM.Client.Map.Report;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;


import javax.swing.JPanel;
import java.awt.BorderLayout;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.MapReportApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapOpenCommand;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;

import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;

import com.syrus.AMFICOM.Client.Resource.Map.MapContext;

public class MapRenderPanel extends JPanel
{
	RenderingObject reportsRO = null;
	MapContext mapContext = null;
	NetMapViewer nmv = null;

	public MapRenderPanel(RenderingObject ro)
			throws CreateReportException
	{
    reportsRO = ro;
		Object reserve = reportsRO.getReportToRender().getReserve();

		this.reportsRO = ro;
		String reserve_string = (String) reportsRO.getReportToRender().
			getReserve();
		int separatPosit = reserve_string.indexOf(":");
		if (separatPosit == -1)
			throw new CreateReportException(reportsRO.getReportToRender().
													  getName(),
													  CreateReportException.cantImplement);

		String map_id = reserve_string.substring(separatPosit + 1);
		mapContext = (MapContext) Pool.get(MapContext.typ, map_id);

		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(new MapReportApplicationModelFactory().create());
		aC.setDispatcher(new Dispatcher());
		Vector panelElements = new Vector();

		aC.setSessionInterface(SessionInterface.getActiveSession());
		aC.setConnectionInterface(ConnectionInterface.getActiveConnection());
		DataSourceInterface dataSource = aC.getApplicationModel().
			getDataSource(SessionInterface.getActiveSession());
		aC.setDataSourceInterface(dataSource);

		try
		{
			MapMainFrame mmf = new MapMainFrame(panelElements, aC);
			nmv = mmf.myMapViewer;

			mmf.setMapContext(mapContext);

			JPanel toolBarPanel = new JPanel();
			toolBarPanel.setLayout(new BorderLayout());
			toolBarPanel.add(mmf.mapToolBar, BorderLayout.WEST);
		}
		catch (Exception e)
		{
			throw new CreateReportException(reportsRO.getReportToRender().getName(),
													  CreateReportException.cantImplement);
		}

		this.setPreferredSize(new Dimension(reportsRO.width, reportsRO.height));
		nmv.setSize(reportsRO.width, reportsRO.height);
    
		this.setLayout(new BorderLayout());
//		this.add(nmv, BorderLayout.CENTER);
//      this.add(toolBarPanel, BorderLayout.NORTH);
		nmv.setVisible(true);
	}

	public MapRenderPanel(MapMainFrame mmf)
			throws CreateReportException
	{
		if (mmf == null)
			throw new CreateReportException("",CreateReportException.generalError);

		this.nmv = mmf.myMapViewer;
		this.setLayout(new BorderLayout());
//		this.add(nmv, BorderLayout.CENTER);
	}

	public void fitToRenderingObject (RenderingObject ro)
	{
    this.reportsRO = ro;  
		this.setPreferredSize(new Dimension(reportsRO.width, reportsRO.height));
		nmv.setVisible(true);
		nmv.setSize(reportsRO.width, reportsRO.height);    
    nmv.setLocation(reportsRO.x,reportsRO.y);
	}
  
  public void paint (Graphics g)
  {
    super.paint(g);
    nmv.paint(g);
  }
}