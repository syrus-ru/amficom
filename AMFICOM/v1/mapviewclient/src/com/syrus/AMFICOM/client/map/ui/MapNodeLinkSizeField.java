/**
 * $Id: MapNodeLinkSizeField.java,v 1.5 2005/01/21 16:19:58 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * Поле редактирования длины фрагмента линии. При отображении компонент
 * помещается на родительский компонент. При удачном редактировании 
 * (пользователь нажал ENTER) меняет фрагмент линии в карте.
 * При завершении редактирования  сам убирает себя из родительского компонента
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/21 16:19:58 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkSizeField extends JTextField 
{
	LogicalNetLayer lnl;
	NodeLink nodelink;
	AbstractNode node;
	
	public MapNodeLinkSizeField(
			LogicalNetLayer lnl,
			NodeLink nodelink,
			AbstractNode node)
	{
		super();
		this.lnl = lnl;
		this.nodelink = nodelink;
		this.node = node;
		jbInit();
		grabFocus();
	}

	public void jbInit()
	{
		this.setFont(MapPropertiesManager.getFont());

		addKeyListener(new MapNodeLinkSizeField.MapNodeLinkSizeFieldKeyAdapter(this));
		addFocusListener(new MapNodeLinkSizeField.MapNodeLinkSizeFieldFocusAdapter(this));
	}

	class MapNodeLinkSizeFieldFocusAdapter extends FocusAdapter
	{
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeFieldFocusAdapter(MapNodeLinkSizeField adaptee)
		{
			this.adaptee = adaptee;
		}

		public void focusGained(FocusEvent e)
		{
		}
		
		public void focusLost(FocusEvent e)
		{
			adaptee.setVisible(false);
			if(adaptee.getParent() != null)
				adaptee.getParent().remove(adaptee);
			adaptee.removeFocusListener(this);
		}
	}
	
	class MapNodeLinkSizeFieldKeyAdapter extends java.awt.event.KeyAdapter
	{
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeFieldKeyAdapter(MapNodeLinkSizeField adaptee)
		{
			this.adaptee = adaptee;
		}

		public void keyPressed(KeyEvent e) 
		{
			int code = e.getKeyCode();

			if (code == KeyEvent.VK_ESCAPE)
			{
				adaptee.setVisible(false);
				if(adaptee.getParent() != null)
					adaptee.getParent().remove(adaptee);
				adaptee.removeKeyListener(this);
			}

			if (code == KeyEvent.VK_ENTER)
			{
				try
				{
					double dist = Double.parseDouble(adaptee.getText());
					adaptee.lnl.setNodeLinkSizeFrom(adaptee.nodelink, adaptee.node, dist);
					adaptee.setVisible(false);
					if(adaptee.getParent() != null)
						adaptee.getParent().remove(adaptee);
					adaptee.removeKeyListener(this);

					if(adaptee.lnl != null)
					{
						adaptee.lnl.repaint(false);
					}
				}
				catch(Exception ex)
				{
					System.out.println("Illegal distance");
				}
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
	
}
