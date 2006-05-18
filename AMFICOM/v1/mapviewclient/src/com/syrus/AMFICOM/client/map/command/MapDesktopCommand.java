/*-
 * $$Id: MapDesktopCommand.java,v 1.13 2005/10/19 11:56:52 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command;

import java.awt.Component;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeFrame;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/19 11:56:52 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class MapDesktopCommand {
	public static GeneralPropertiesFrame findGeneralPropertiesFrame(
			JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof GeneralPropertiesFrame)
				return (GeneralPropertiesFrame )comp;
		}
		return null;
	}

	public static AdditionalPropertiesFrame findAdditionalPropertiesFrame(
			JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof AdditionalPropertiesFrame)
				return (AdditionalPropertiesFrame )comp;
		}
		return null;
	}

	public static CharacteristicPropertiesFrame findCharacteristicsFrame(
			JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof CharacteristicPropertiesFrame)
				return (CharacteristicPropertiesFrame )comp;
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
