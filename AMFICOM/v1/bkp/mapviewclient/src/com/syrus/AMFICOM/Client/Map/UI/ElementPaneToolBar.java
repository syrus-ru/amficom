/**
 * $Id: ElementPaneToolBar.java,v 1.2 2004/09/16 12:00:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;

import java.awt.FlowLayout;
import java.awt.Image;

import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * На этой панельке располагаются элементы которые будут наноситься на карту
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/16 12:00:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ElementPaneToolBar extends JPanel 
{
	private static final int ELEMENT_DIMENSION = 30;

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

	public ElementPaneToolBar(java.util.List elements)
	{
		this();
		setProtoElements(elements);
	}

	private void jbInit()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void setProtoElements(java.util.List elements)
	{
		this.removeAll();
		MapNodeProtoElement mpe;
		ImageIcon icon;
		MapElementLabel label;

		//Добавляем элементы
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			mpe = (MapNodeProtoElement )it.next();
			icon = new ImageIcon(
				mpe.getImage().getScaledInstance(
					ELEMENT_DIMENSION, 
					ELEMENT_DIMENSION, 
					Image.SCALE_SMOOTH));
			label = new MapElementLabel(mpe);
			label.setToolTipText(mpe.getName());
			this.add(label);
		}
	}

	//Включить выключить панель
	public void setEnableDisablePanel(boolean b)
	{
		for (int i = 0; i < this.getComponentCount(); i++)
			((MapElementLabel)this.getComponent(i)).setEnabled(b);
	}

}

