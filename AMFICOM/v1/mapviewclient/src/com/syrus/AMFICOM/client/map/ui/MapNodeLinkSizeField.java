/**
 * $Id: MapNodeLinkSizeField.java,v 1.8 2005/06/06 12:20:35 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
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
 * @version $Revision: 1.8 $, $Date: 2005/06/06 12:20:35 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapNodeLinkSizeField extends JTextField 
{
	LogicalNetLayer logicalNetLayer;
	NodeLink nodeLink;
	AbstractNode node;
	
	public MapNodeLinkSizeField(
			LogicalNetLayer logicalNetLayer,
			NodeLink nodeLink,
			AbstractNode node)
	{
		super();
		this.logicalNetLayer = logicalNetLayer;
		this.nodeLink = nodeLink;
		this.node = node;
		jbInit();
		grabFocus();
	}

	public void jbInit()
	{
		this.setFont(MapPropertiesManager.getFont());
		this.setBackground(MapPropertiesManager.getTextBackground());
		this.setForeground(MapPropertiesManager.getTextColor());

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
		{//empty
		}
		
		public void focusLost(FocusEvent e)
		{
			this.adaptee.setVisible(false);
			if(this.adaptee.getParent() != null)
				this.adaptee.getParent().remove(this.adaptee);
			this.adaptee.removeFocusListener(this);
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
				this.adaptee.setVisible(false);
				if(this.adaptee.getParent() != null)
					this.adaptee.getParent().remove(this.adaptee);
				this.adaptee.removeKeyListener(this);
			}

			if (code == KeyEvent.VK_ENTER)
			{
				try
				{
					double dist = Double.parseDouble(this.adaptee.getText());
					this.adaptee.logicalNetLayer.setNodeLinkSizeFrom(this.adaptee.nodeLink, this.adaptee.node, dist);
					this.adaptee.setVisible(false);
					if(this.adaptee.getParent() != null)
						this.adaptee.getParent().remove(this.adaptee);
					this.adaptee.removeKeyListener(this);

					if(this.adaptee.logicalNetLayer != null)
					{
						this.adaptee.logicalNetLayer.repaint(false);
					}
				}
				catch(Exception ex)
				{
					System.out.println("Illegal distance");
				}
			}
		}
		public void keyReleased(KeyEvent e) {/*empty*/}
		public void keyTyped(KeyEvent e) {/*empty*/}
	}
	
}
