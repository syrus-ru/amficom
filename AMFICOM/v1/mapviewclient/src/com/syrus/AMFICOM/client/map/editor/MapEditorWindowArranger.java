/*
 * $Id: MapEditorWindowArranger.java,v 1.1 2005/03/05 16:00:06 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.WindowArranger;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsBarFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeFrame;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/03/05 16:00:06 $
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
			mapFrame.setSize(3 * w/5, 15 * h/12);
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
			propFrame.setSize(w/5, 3 * h/5);
			propFrame.setLocation(4 * w/5, 0);
		}
	
		//Левая сторона
		ControlsFrame controlsFrame = MapDesktopCommand.findControlsFrame(this.desktop);
		if (controlsFrame != null)		
		{
			normalize(controlsFrame);		
			controlsFrame.setSize(w/5, 2 * h/5);
			controlsFrame.setLocation(0, 3 * h/5);
		}
		
		MapSchemeTreeFrame schemeTreeFrame = MapDesktopCommand.findMapSchemeTreeFrame(this.desktop);
		if (schemeTreeFrame != null)
		{
			normalize(schemeTreeFrame);
			
			if (controlsFrame != null)
				schemeTreeFrame.setSize(w/5, 3 * h/10);
			else
				schemeTreeFrame.setSize(w/5, h/2);
			
			schemeTreeFrame.setLocation(0, 0);
		}

		MapElementsFrame mapElemsFrame = MapDesktopCommand.findMapElementsFrame(this.desktop);
		if (mapElemsFrame != null)
		{
			normalize(mapElemsFrame);
			
			if (controlsFrame != null)
			{
				mapElemsFrame.setSize(w/5, 3 * h/10);
				mapElemsFrame.setLocation(0, 3 * h/10);				
			}
			else
			{
				mapElemsFrame.setSize(w/5, h/2);
				mapElemsFrame.setLocation(0, h/2);				
			}
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