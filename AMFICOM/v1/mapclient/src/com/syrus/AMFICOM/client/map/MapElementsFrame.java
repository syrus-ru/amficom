package com.syrus.AMFICOM.Client.Map;

import java.awt.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

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
		Dispatcher disp;
		if(this.aContext != null)
		{
			disp = this.aContext.getDispatcher();
			if(disp != null)
			{
				disp.unregister(this, "mapselectevent");
				disp.unregister(this, "mapdeselectevent");
				disp.unregister(this, "mapchangeevent");
				disp.unregister(this, "mapselectionchangeevent");
			}
		}
		this.aContext = aContext;
		panel.setContext(aContext);
		if(aContext == null)
			return;
		
		disp = this.aContext.getDispatcher();
		if(disp != null)
		{
			disp.register(this, "mapselectevent");
			disp.register(this, "mapdeselectevent");
			disp.register(this, "mapchangeevent");
			disp.register(this, "mapselectionchangeevent");
		}
	}
	
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		if(!isVisible)
			return;
		
		MapMainFrame mmf = (MapMainFrame )Pool.get("environment", "mapmainframe");
		if(mmf != null)
			if(mmf.isVisible())
				if(mmf.getParent().equals(this.getParent()))
					setMapContext(mmf.getMapContext());
	}

	private void jbInit() throws Exception
	{
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(false);
		this.setResizable(true);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle(LangModelMap.getString("elementsTitle"));

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);
//		this.setPreferredSize(new Dimension(200, 300));
	}

	public void setMapContext(MapContext mc)
	{
		panel.setMapContext(mc);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("mapdeselectevent"))
			if(panel.perform_processing)
				setMapContext(null);
		if(	ae.getActionCommand().equals("mapselectevent"))
			if(panel.perform_processing)
				setMapContext((MapContext )ae.getSource());
		if(	ae.getActionCommand().equals("mapchangeevent"))
			panel.updateTable();
		if(	ae.getActionCommand().equals("mapselectionchangeevent"))
			panel.setSelectedObjects();
	}
}