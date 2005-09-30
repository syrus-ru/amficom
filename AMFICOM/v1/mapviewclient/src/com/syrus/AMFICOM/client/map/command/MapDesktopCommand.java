/*-
 * $$Id: MapDesktopCommand.java,v 1.12 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command;

import java.awt.Component;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.ui.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeFrame;

/**
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class MapDesktopCommand {
	public static MapGeneralPropertiesFrame findMapGeneralPropertiesFrame(
			JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof MapGeneralPropertiesFrame)
				return (MapGeneralPropertiesFrame )comp;
		}
		return null;
	}

	public static MapAdditionalPropertiesFrame findMapAdditionalPropertiesFrame(
			JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof MapAdditionalPropertiesFrame)
				return (MapAdditionalPropertiesFrame )comp;
		}
		return null;
	}

	public static MapCharacteristicPropertiesFrame findMapCharacteristicsFrame(
			JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof MapCharacteristicPropertiesFrame)
				return (MapCharacteristicPropertiesFrame )comp;
		}
		return null;
	}

	public static MapViewTreeFrame findMapViewTreeFrame(JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof MapViewTreeFrame)
				return (MapViewTreeFrame )comp;
		}
		return null;
	}

	public static MapFrame findMapFrame(JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof MapFrame)
				return (MapFrame )comp;
		}
		return null;
	}

}
