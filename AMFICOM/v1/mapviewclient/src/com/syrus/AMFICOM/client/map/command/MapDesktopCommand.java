package com.syrus.AMFICOM.Client.Map.Command;

import java.awt.Component;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementCharacteristicsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;

public abstract class MapDesktopCommand
{
	public static MapElementPropertiesFrame findMapPropertyFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapElementPropertiesFrame)
					return (MapElementPropertiesFrame )comp;
		}
		return null;
	}
	
	public static MapElementsFrame findMapElementsFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapElementsFrame)
					return (MapElementsFrame )comp;
		}
		return null;
	}
	
	public static MapElementCharacteristicsFrame findMapSchemeTreeFrame(JDesktopPane desktop)
	{
		for(int i = 0; i < desktop.getComponents().length; i++)
		{
			Component comp = desktop.getComponent(i);
			if(comp != null
				&& comp instanceof MapElementCharacteristicsFrame)
					return (MapElementCharacteristicsFrame )comp;
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
