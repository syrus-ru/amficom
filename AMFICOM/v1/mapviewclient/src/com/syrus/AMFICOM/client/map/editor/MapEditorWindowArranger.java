/*
 * $Id: MapEditorWindowArranger.java,v 1.3 2005/03/16 13:48:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;
import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.WindowArranger;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsBarFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeFrame;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/16 13:48:18 $
 * @module mapviewclient_v1
 */

public class MapEditorWindowArranger extends WindowArranger
		implements OperationListener
{
	public static final String EVENT_ARRANGE = "ev_me_arrange";
	
	private JDesktopPane desktop = null;

	public MapEditorWindowArranger(
			JDesktopPane desktop,
			ApplicationContext aContext)
	{
		super(null);
		this.desktop = desktop;
		aContext.getDispatcher().register(
				this,MapEditorWindowArranger.EVENT_ARRANGE);
	}

	public void arrange()
	{
		if (this.desktop == null)
			return;
		
		int w = this.desktop.getSize().width;
		int h = this.desktop.getSize().height;

		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if (mapFrame != null)
		{
			normalize(mapFrame);		
			mapFrame.setSize(3 * w/5, 11 * h/12);
			mapFrame.setLocation(w/5, 0);
		}

		MapElementsBarFrame elemBarFrame = MapDesktopCommand.findMapElementsBarFrame(this.desktop);
		if (elemBarFrame != null)
		{
			normalize(elemBarFrame);		
			elemBarFrame.setSize(3 * w/5, h/12);
			elemBarFrame.setLocation(w/5, 11 * h/12);
		}
		
		//Правая сторона
		MapPropertyFrame propFrame = MapDesktopCommand.findMapPropertyFrame(this.desktop);
		if (propFrame != null)
		{
			normalize(propFrame);		
			propFrame.setSize(w/5, h/2);
			propFrame.setLocation(4 * w/5, 0);
		}

		MapSchemeTreeFrame schemeTreeFrame = MapDesktopCommand.findMapSchemeTreeFrame(this.desktop);
		if (schemeTreeFrame != null)
		{
			normalize(schemeTreeFrame);
			schemeTreeFrame.setSize(w/5, h/2);
			schemeTreeFrame.setLocation(4 * w/5, h/2);
		}
	
		//Левая сторона
		MapViewTreeFrame mapViewTreeFrame = MapDesktopCommand.findMapViewTreeFrame(this.desktop);
		if (mapViewTreeFrame != null)
		{
			normalize(mapViewTreeFrame);
			mapViewTreeFrame.setSize(w/5, h/2);
			mapViewTreeFrame.setLocation(0, 0);
		}

		ControlsFrame controlsFrame = MapDesktopCommand.findControlsFrame(this.desktop);
		if (controlsFrame != null)		
		{
			normalize(controlsFrame);		
			controlsFrame.setSize(w/5, h/2);
			controlsFrame.setLocation(0, h/2);
		}
	}
	
	public void operationPerformed(OperationEvent oe)
	{
		if (		(oe.getActionCommand().equals(MapEditorWindowArranger.EVENT_ARRANGE))
				&& 	(oe.getSource() instanceof JDesktopPane)
				&&	(oe.getSource().equals(this.desktop)))
		{
			this.arrange();
		}
	}
}