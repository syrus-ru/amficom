package com.syrus.AMFICOM.Client.Map;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.*;

//На этой панельке располагаются элементы которые будут наноситься на карту

public class ElementPaneToolBar extends JPanel 
{
	int elementDimansion = 30;

	public ElementPaneToolBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
	}

	public ElementPaneToolBar(Vector elements)
	{
		this();
		setProtoElements(elements);
	}

	private void jbInit()
//			throws Exception
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void setProtoElements(Vector elements)
	{
		this.removeAll();
		Enumeration schemeElementsEnum = elements.elements();
		MapProtoElement currentSchemeElement;
		ImageIcon currentImageIcon;
		MapElementLabel currentSchemeElementLabel;

//Добавляем элементы
		while (schemeElementsEnum.hasMoreElements())
		{
			currentSchemeElement = (MapProtoElement)schemeElementsEnum.nextElement();
			currentImageIcon = new ImageIcon(currentSchemeElement.get_image().getScaledInstance(elementDimansion, elementDimansion, Image.SCALE_SMOOTH));
			currentSchemeElementLabel = new MapElementLabel(currentImageIcon, currentSchemeElement);
			currentSchemeElementLabel.setToolTipText( currentSchemeElement.getName());
			this.add(currentSchemeElementLabel);
		}
	}

//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		for (int i = 0; i < this.getComponentCount(); i++)
			((MapElementLabel)this.getComponent(i)).setEnableMapElemenetLabel(b);
	}

}

