/*-
 * $$Id: TreePopupMenu.java,v 1.5 2005/10/11 08:56:13 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * @version $Revision: 1.5 $, $Date: 2005/10/11 08:56:13 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class TreePopupMenu extends JPopupMenu {

	private JMenuItem copyMenuItem = new JMenuItem();

	final Object object;

	final ApplicationContext aContext;

	public TreePopupMenu(Object object, ApplicationContext context) {
		this.object = object;
		this.aContext = context;

		this.copyMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_COPY));
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
