/*
 * $Id: MapEditorWindowArranger.java,v 1.10 2005/05/27 15:14:57 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map.Editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/05/27 15:14:57 $
 * @module mapviewclient_v1
 */

public class MapEditorWindowArranger extends WindowArranger
		implements PropertyChangeListener
{
	public static final String EVENT_ARRANGE = "ev_me_arrange";
	
	private JDesktopPane desktop = null;

	public MapEditorWindowArranger(
			JDesktopPane desktop)
	{
		super(null);
		this.desktop = desktop;
	}

	public void arrange()
	{
		if (this.desktop == null)
			return;
		
		int w = this.desktop.getSize().width;
		int h = this.desktop.getSize().height;

/*----- Центр ------*/
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if (mapFrame != null)
		{
			normalize(mapFrame);		
			mapFrame.setSize(3 * w / 5, h);
			mapFrame.setLocation(w / 5, 0);
		}

/*----- Левая сторона ------*/
		MapViewTreeFrame treeFrame = MapDesktopCommand.findMapViewTreeFrame(this.desktop);
		if (treeFrame != null)
		{
			normalize(treeFrame);
			treeFrame.setSize(w / 5, h / 2);
			treeFrame.setLocation(0, 0);
		}

		ControlsFrame controlsFrame = MapDesktopCommand.findControlsFrame(this.desktop);
		if (controlsFrame != null)		
		{
			normalize(controlsFrame);		
			controlsFrame.setSize(w / 5, h / 2);
			controlsFrame.setLocation(0, h / 2);
		}

/*----- Правая сторона ------*/
		MapGeneralPropertiesFrame propFrame = MapDesktopCommand.findMapGeneralPropertiesFrame(this.desktop);
		if (propFrame != null)
		{
			normalize(propFrame);		
			propFrame.setSize(w / 5, h * 2 / 5);
			propFrame.setLocation(4 * w / 5, 0);
		}

		MapAdditionalPropertiesFrame addFrame = MapDesktopCommand.findMapAdditionalPropertiesFrame(this.desktop);
		if (addFrame != null)
		{
			normalize(addFrame);		
			addFrame.setSize(w / 5, h * 2 / 5);
			addFrame.setLocation(4 * w / 5, h * 2 / 5);
		}

		MapCharacteristicPropertiesFrame charFrame = MapDesktopCommand.findMapCharacteristicsFrame(this.desktop);
		if (charFrame != null)
		{
			normalize(charFrame);
			charFrame.setSize(w / 5, h / 5);
			charFrame.setLocation(4 * w / 5, h * 4 / 5);
		}
	}
	
	public void propertyChange(PropertyChangeEvent pce)
	{
		if (		(pce.getPropertyName().equals(MapEditorWindowArranger.EVENT_ARRANGE))
				&& 	(pce.getSource() instanceof JDesktopPane)
				&&	(pce.getSource().equals(this.desktop)))
		{
			this.arrange();
		}
	}
}