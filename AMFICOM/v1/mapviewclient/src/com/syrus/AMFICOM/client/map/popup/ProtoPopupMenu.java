package com.syrus.AMFICOM.Client.Map.Popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.Client.Map.UI.MapElementLabel;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.map.SiteNodeType;

public final class ProtoPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();

	private JMenuItem propertiesMenuItem = new JMenuItem();

	private SiteNodeType proto;

	private MapElementLabel lab;

	private static ProtoPopupMenu instance = new ProtoPopupMenu();

	private ProtoPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static ProtoPopupMenu getInstance() {
		return instance;
	}

	public void setElement(Object me) {
		this.proto = (SiteNodeType )me;
	}

	public void setElementLabel(MapElementLabel lab) {
		this.lab = lab;
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap.getString("Delete"));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeProto();
			}
		});
		this.propertiesMenuItem.setText(LangModelMap.getString("Properties"));
		this.propertiesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProperties();
			}
		});
		this.add(this.removeMenuItem);
		this.addSeparator();
		this.add(this.propertiesMenuItem);
	}

	void showProperties() {
		SiteNodeTypeEditor prop = new SiteNodeTypeEditor();
		if(prop == null)
			return;
		EditorDialog dialog = new EditorDialog(
				LangModelGeneral.getString("Properties"), 
				true, 
				this.proto,
				prop);

		dialog.setVisible(true);

		this.lab.updateIcon();
	}

	void removeProto() {
		// empty
	}
}
