/*
* Менеджер внешнего вида, выстраивающий элементы вдоль наибольшей из сторон
* родительской панели. В случае, если стороны равны, выстраивает элементы
* по горизонтали.
*/
package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class LineLayout extends Object implements LayoutManager
{

	public LineLayout()
	{
	}

	public void addLayoutComponent(String name, Component comp)
	{
	}

	public Dimension preferredLayoutSize(Container parent)
	{
//	System.out.println("Preferred parent width: "+(int)parent.getSize().getWidth());
//	System.out.println("Preferred parent Height: "+(int)parent.getSize().getHeight());
		return new Dimension(20, 20);
//		return parent.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container parent)
	{
//		return parent.getMinimumSize();
		return new Dimension(20, 20);
	}

	public void layoutContainer(Container parent)
	{
		int width = (int)parent.getSize().getWidth();
		int height = (int)parent.getSize().getHeight();
		int parentHeight;
		int parentWidth;
		if (height<20)
		{
			parentHeight=20;
		}
		else
		{
			parentHeight=height;
		}

		if (width<20)
		{
			parentWidth=20;
		}
		else
		{
			parentWidth=width;
		}

//	System.out.println("Height: "+height+"   Width: "+width);
		Component currentComponent;
		boolean row = false;
		if (width<height)
		{
			height=width;
		}
		else
		{
			width=height;
			row = true;
		}
		int x = 0;
		int y = 1;
		int numberOfComponents = parent.getComponentCount();
		int i;
		for (i=0; i < numberOfComponents; i++)
		{
			currentComponent = parent.getComponent(i);
			currentComponent.setBounds(x,y,currentComponent.getWidth(), currentComponent.getHeight());
//			System.out.println("Set bounds for " + currentComponent.getName() +
//					" at X: " + x + "  Y: " + y +
//					" Height: " + currentComponent.getHeight() +
//					"   Width: " + currentComponent.getWidth());
			//width-4, height-4);
			if (row)
			{
				x += currentComponent.getWidth() + 1;
			}
			else
			{
				y += currentComponent.getWidth() + 1;
			}
		}
		parent.setSize(parentWidth, parentHeight);
	}

	public void removeLayoutComponent(Component comp)
	{
	}

}

