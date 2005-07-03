package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import sun.java2d.loops.ScaledBlit;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

public class SchematicsApplicationModel extends ApplicationModel
{
	public SchematicsApplicationModel()
	{
		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuSessionDomain");
		add("menuSessionSave");
		add("menuSessionUndo");
		add("menuExit");

		add("menuBar");
		add("toolBar");
		add("statusBar");

		add("menuScheme");
		add("menuSchemeNew");
		add("menuSchemeLoad");
		add("menuSchemeSave");
		add("menuSchemeSaveAs");
		add("menuInsertToCatalog");
		add("menuSchemeImport");
		add("menuSchemeExport");

		add("menuComponent");
		add("menuComponentSave");
		add("menuComponentNew");

		add("menuPath");
		add("menuPathNew");
		add("menuPathSave");
		add("menuPathEdit");
		add("menuPathAddStart");
		add("menuPathAddEnd");
		add("menuPathAddLink");
		add("menuPathRemoveLink");
		add("menuPathAutoCreate");
		add("menuPathDelete");
		add("menuPathCancel");

		add("menuReportCreate");
		add("menuReport");

		add("menuWindow");
		add("menuWindowArrange");
		add("menuWindowTree");
		add("menuWindowScheme");
		add("menuWindowCatalog");
		add("menuWindowUgo");
		add("menuWindowProps");
		add("menuWindowList");
		
		this.initUIConstats();
	}
	
	private void initUIConstats() {
		UIManager.put(SchemeResourceKeys.ICON_SCHEMATICS, Toolkit
				.getDefaultToolkit().getImage("images/main/schematics_mini.gif"));
		UIManager.put(SchemeResourceKeys.ICON_COMPONENTS, Toolkit
				.getDefaultToolkit().getImage("images/main/components_mini.gif"));
		
		UIManager.put(SchemeResourceKeys.ICON_CATALOG, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/folder.gif").getScaledInstance(
						16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(SchemeResourceKeys.ICON_SCHEME, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/scheme.gif").getScaledInstance(
						16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(SchemeResourceKeys.ICON_NEW, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/new.gif").getScaledInstance(16,
						16, Image.SCALE_SMOOTH)));
		UIManager.put(SchemeResourceKeys.ICON_SAVE, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16,
						16, Image.SCALE_SMOOTH)));
		UIManager.put(SchemeResourceKeys.ICON_SYNCHRONIZE, new ImageIcon(Toolkit
				.getDefaultToolkit().getImage("images/synchronize.gif").getScaledInstance(16,
						16, Image.SCALE_SMOOTH)));
	}
}
