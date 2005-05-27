package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.map.Mark;

public final class MarkPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();

	private Mark mark;

	private static MarkPopupMenu instance = new MarkPopupMenu();

	private MarkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static MarkPopupMenu getInstance() {
		return instance;
	}

	public void setElement(Object me) {
		this.mark = (Mark )me;
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeMark();
			}
		});
		this.add(this.removeMenuItem);
	}

	void removeMark() {
		super.removeMapElement(this.mark);

		try {
			getLogicalNetLayer().repaint(false);
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
