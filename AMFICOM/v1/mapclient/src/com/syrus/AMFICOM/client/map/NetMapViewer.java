package com.syrus.AMFICOM.Client.Map;


import com.ofx.mapViewer.*;
import com.ofx.*;
import com.ofx.component.swing.*;
import com.ofx.base.*;

import java.util.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

//Данный класс выводит карту на экран

public class NetMapViewer extends JMapViewer
{
	public LogicalNetLayer lnl = null;
	public MyToolTippedPanel mttp = null;
	public DropTarget dropTarget = null;
	public ToolTipManager ttm = null;

	protected static NetMapViewer mapViewer = null;
	protected static boolean dbset = false;
	

	public static NetMapViewer createNetMapViewer(MapMainFrame myMapMainFrame)
	{
		if(mapViewer == null)
		{
			mapViewer = new NetMapViewer();
		}
		mapViewer.setMapMainFrame(myMapMainFrame);
		return mapViewer;
	}

	protected void setMapMainFrame(MapMainFrame myMapMainFrame)
	{
		if(lnl != null)
		{
			this.removeMouseListener(lnl);
			this.removeMouseMotionListener(lnl);
			this.removeMouseListener(mttp.ls);
			this.removeMouseMotionListener(mttp.ls);
			dropTarget.setActive(false);
			ttm.unregisterComponent(mttp);
		}
		try
		{
			lnl = new LogicalNetLayer(myMapMainFrame, this, myMapMainFrame);

			lnl.setMode(LogicalNetLayer.DRAW_ACTION_MODE);
			this.addMouseListener(lnl);
			dropTarget = new DropTarget( this.getMapCanvas(), lnl);
			dropTarget.setActive(true);
			this.addMouseMotionListener(lnl);

			mttp = new MyToolTippedPanel(this);
			this.addMouseListener(mttp.ls);
			this.addMouseMotionListener(mttp.ls);

			ttm = ToolTipManager.sharedInstance();
			ttm.registerComponent(mttp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected NetMapViewer()
	{
		super();
	}

	public NetMapViewer( MapMainFrame myMapMainFrame)
	{
		super();

		try
		{

			lnl = new LogicalNetLayer(myMapMainFrame, this, myMapMainFrame);

			lnl.setMode(LogicalNetLayer.DRAW_ACTION_MODE);
			this.addMouseListener(lnl);
			DropTarget dropTarget = new DropTarget( this.getMapCanvas(), lnl);
			dropTarget.setActive(true);
			this.addMouseMotionListener(lnl);

			MyToolTippedPanel mttp = new MyToolTippedPanel(this);
			this.addMouseListener(mttp.ls);
			this.addMouseMotionListener(mttp.ls);

			ToolTipManager ttm = ToolTipManager.sharedInstance();
			ttm.registerComponent(mttp);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//Установить карту
	public void setMap( String myDataBasePath , String myBataBaseName )
	{
		try
		{
			SxMapViewer anSxMapViewer = getSxMapViewer();

			if(!dbset)
				setDBName( myDataBasePath);
			setMapName( myBataBaseName);

			anSxMapViewer.addLayer( "Network layer", lnl);
			try 
			{
				SxMarkerLayer markerLayer = (SxMarkerLayer) anSxMapViewer.getLayer(SxMapLayerInterface.MARKER);
				markerLayer.listenForMapEvents( false );
				markerLayer.setEnabled(false);
			} 
			catch (Exception ex) 
			{
			} 
			
			anSxMapViewer.removeNamedLayer("OFX LOGO");
			anSxMapViewer.removeNamedLayer("OFX COPYRIGHT");
			anSxMapViewer.postPaintEvent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void closeMap()
	{
		SxEnvironment.singleton().getQuery().close();
		super.closeSession();
		com.ofx.service.SxServiceFactory.shutdown();
	}

}


