/**
 * $Id: MapNodeLinkSizeField.java,v 1.2 2004/09/15 08:21:49 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;

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
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:21:49 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkSizeField extends JTextField 
{
	LogicalNetLayer lnl;
	MapNodeLinkElement nodelink;
	MapNodeElement node;
	
	public MapNodeLinkSizeField(
			LogicalNetLayer lnl,
			MapNodeLinkElement nodelink,
			MapNodeElement node)
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

		addKeyListener(new MapNodeLinkSizeField.MapNodeLinkSizeField_KeyAdapter(this));
		addFocusListener(new MapNodeLinkSizeField.MapNodeLinkSizeField_FocusAdapter(this));
	}

	class MapNodeLinkSizeField_FocusAdapter extends FocusAdapter
	{
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeField_FocusAdapter(MapNodeLinkSizeField adaptee)
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
	
	class MapNodeLinkSizeField_KeyAdapter extends java.awt.event.KeyAdapter
	{
		MapNodeLinkSizeField adaptee;

		MapNodeLinkSizeField_KeyAdapter(MapNodeLinkSizeField adaptee)
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
						adaptee.lnl.repaint();
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
