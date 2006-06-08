/*-
 * $$Id: MarkPopupMenu.java,v 1.21 2006/06/08 12:32:53 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.21 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MarkPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 5259178384513977950L;

	private JMenuItem removeMenuItem = new JMenuItem();

	private Mark mark;

	private static MarkPopupMenu instance = new MarkPopupMenu();

	private MarkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static MarkPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.mark = (Mark )me;
		
		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeMark();
			}
		});
		this.add(this.removeMenuItem);
	}

	void removeMark() {
		if (confirmDelete()) {
			super.removeMapElement(this.mark);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
