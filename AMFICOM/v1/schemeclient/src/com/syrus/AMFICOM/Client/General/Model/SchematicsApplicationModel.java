package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

public class SchematicsApplicationModel extends ApplicationModel
{
	public SchematicsApplicationModel()
	{
    add(ApplicationModel.MENU_SESSION);
    add(ApplicationModel.MENU_SESSION_NEW);
    add(ApplicationModel.MENU_SESSION_CLOSE);
//  add(ApplicationModel.MENU_SESSION_CONNECTION);
    add(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
    add(ApplicationModel.MENU_EXIT);

    add(ApplicationModel.MENU_HELP);
    add(ApplicationModel.MENU_HELP_ABOUT);

		add("menuScheme");
		add("menuSchemeNew");
		add("menuSchemeLoad");
		add("menuSchemeSave");
		add("menuSchemeSaveAs");
		
		add("Menu.import");
		add("Menu.import.scheme");
		add("Menu.import.config");
		add("Menu.import.protos");
		add("Menu.import.commit");
		
		add("Menu.export");
		add("Menu.export.scheme");
		add("Menu.export.config");
		add("Menu.export.protos");

		add("menuComponent");
		add("menuComponentSave");
		add("menuComponentNew");

		add("menuPath");
		add("menuPathNew");
		add("menuPathSave");
		add("menuPathEdit");
		add("menuPathAutoCreate");
		add("menuPathDelete");
		add("menuPathCancel");

		add("menuReportCreate");
		add("menuReport");

		add("menuWindow");
		add("menuWindowTree");
		add("menuWindowScheme");
		add("menuWindowUgo");
		add("menuWindowProps");
		add("menuWindowList");
		
		add(ApplicationModel.MENU_VIEW_ARRANGE);
		
		this.initUIConstats();
		I18N.addResourceBundle("com.syrus.AMFICOM.client.report.report");
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
		
		UIManager.put(SchemeResourceKeys.COLOR_PORT_COMMON, new Color(200, 255, 200));
		UIManager.put(SchemeResourceKeys.COLOR_PORT_NO_LINK, new Color(255, 255, 200));
		UIManager.put(SchemeResourceKeys.COLOR_PORT_NO_TYPE, new Color(255, 200, 200));
		UIManager.put(SchemeResourceKeys.COLOR_PORT_TERMAL, new Color(200, 200, 255));
	}
}
