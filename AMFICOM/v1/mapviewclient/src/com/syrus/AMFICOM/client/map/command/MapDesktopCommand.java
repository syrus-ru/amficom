package com.syrus.AMFICOM.Client.Map.Command;

import java.awt.Component;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;

public abstract class MapDesktopCommand
{
	public static MapGeneralPropertiesFrame findMapGeneralPropertiesFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapGeneralPropertiesFrame)
					return (MapGeneralPropertiesFrame )comp;
		}
		return null;
	}
	
	public static MapAdditionalPropertiesFrame findMapAdditionalPropertiesFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapAdditionalPropertiesFrame)
					return (MapAdditionalPropertiesFrame )comp;
		}
		return null;
	}
	
	public static MapCharacteristicPropertiesFrame findMapCharacteristicsFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapCharacteristicPropertiesFrame)
					return (MapCharacteristicPropertiesFrame )comp;
		}
		return null;
	}
	
	public static MapViewTreeFrame findMapViewTreeFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapViewTreeFrame)
					return (MapViewTreeFrame )comp;
		}
		return null;
	}
	
	public static ControlsFrame findControlsFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof ControlsFrame)
					return (ControlsFrame )comp;
		}
		return null;
	}
	
	public static MapFrame findMapFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapFrame)
					return (MapFrame )comp;
		}
		return null;
	}
	
}
