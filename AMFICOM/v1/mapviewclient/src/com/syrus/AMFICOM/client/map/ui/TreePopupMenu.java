/**
 * $Id: TreePopupMenu.java,v 1.3 2005/09/25 16:08:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

public class TreePopupMenu extends JPopupMenu {

	private JMenuItem copyMenuItem = new JMenuItem();

	final Object object;

	final ApplicationContext aContext;

	public TreePopupMenu(Object object, ApplicationContext context) {
		this.object = object;
		this.aContext = context;

		this.copyMenuItem.setText(LangModelMap.getString(MapEditorResourceKeys.POPUP_COPY));
		this.copyMenuItem.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					TreePopupMenu.this.aContext.getDispatcher().firePropertyChange(
							new MapEvent(this, MapEvent.COPY_TYPE, TreePopupMenu.this.object));
				}
			});
		
		this.add(this.copyMenuItem);
	}

}
