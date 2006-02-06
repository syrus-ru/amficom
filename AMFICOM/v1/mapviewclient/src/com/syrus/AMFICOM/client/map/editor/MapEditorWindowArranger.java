/*-
 * $$Id: MapEditorWindowArranger.java,v 1.23 2005/10/19 11:56:52 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeFrame;

/**
 * @version $Revision: 1.23 $, $Date: 2005/10/19 11:56:52 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */

public class MapEditorWindowArranger extends WindowArranger
		implements PropertyChangeListener
{
	public static final String EVENT_ARRANGE = "ev_me_arrange"; //$NON-NLS-1$
	
	private JDesktopPane desktop = null;

	public MapEditorWindowArranger(
			JDesktopPane desktop)
	{
		super(null);
		this.desktop = desktop;
	}

	@Override
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
			treeFrame.setSize(w / 5, h);
			treeFrame.setLocation(0, 0);
		}

//		ControlsFrame controlsFrame = MapDesktopCommand.findControlsFrame(this.desktop);
//		if (controlsFrame != null)		
//		{
//			normalize(controlsFrame);		
//			controlsFrame.setSize(w / 5, h / 5);
//			controlsFrame.setLocation(0, h * 4 / 5);
//		}

/*----- Правая сторона ------*/
		GeneralPropertiesFrame propFrame = MapDesktopCommand.findGeneralPropertiesFrame(this.desktop);
		if (propFrame != null)
		{
			normalize(propFrame);		
			propFrame.setSize(w / 5, h * 2 / 5);
			propFrame.setLocation(4 * w / 5, 0);
		}

		AdditionalPropertiesFrame addFrame = MapDesktopCommand.findAdditionalPropertiesFrame(this.desktop);
		if (addFrame != null)
		{
			normalize(addFrame);		
			addFrame.setSize(w / 5, h * 2 / 5);
			addFrame.setLocation(4 * w / 5, h * 2 / 5);
		}

		CharacteristicPropertiesFrame charFrame = MapDesktopCommand.findCharacteristicsFrame(this.desktop);
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
				&&	(pce.getSource().equals(this.desktop)))
		{
			this.arrange();
		}
	}
}