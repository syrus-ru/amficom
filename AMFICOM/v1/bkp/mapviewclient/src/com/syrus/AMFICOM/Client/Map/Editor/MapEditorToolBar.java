/**
 * $Id: MapEditorToolBar.java,v 1.13 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.awt.event.ActionListener;

/**
 * Панель инструментов модуля "Редактор топологических схем". 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/05/27 15:14:57 $
 * @module mapviewclient_v1
 */
public class MapEditorToolBar extends JToolBar implements ApplicationModelListener {
	private ApplicationModel aModel;

	/**
	 * <img src="images/open_session.gif">.
	 */
	JButton sessionOpen = new JButton();
	/**
	 * <img src="images/close_session.gif">.
	 */
	JButton buttonCloseSession = new JButton();
	/**
	 * <img src="images/domains.gif">.
	 */
	JButton menuSessionDomain = new JButton();
	/**
	 * <img src="images/new.gif">.
	 */
	JButton menuMapNew = new JButton();
	/**
	 * <img src="images/map_mini.gif">.
	 */
	JButton menuMapOpen = new JButton();
	/**
	 * <img src="images/save.gif">.
	 */
	JButton menuMapSave = new JButton();
	/**
	 * <img src="images/addmap.gif">.
	 */
	JButton menuMapAddMap = new JButton();
	/**
	 * <img src="images/removemap.gif">.
	 */
	JButton menuMapRemoveMap = new JButton();
	/**
	 * <img src="images/addExternal.gif">.
	 */
	JButton menuMapAddExternal = new JButton();
	/**
	 * <img src="images/newview.gif">.
	 */
	JButton menuMapViewNew = new JButton();
	/**
	 * <img src="images/openview.gif">.
	 */
	JButton menuMapViewOpen = new JButton();
	/**
	 * <img src="images/saveview.gif">.
	 */
	JButton menuMapViewSave = new JButton();
	/**
	 * <img src="images/addtoview.gif">.
	 */
	JButton menuMapViewAddScheme = new JButton();
	/**
	 * <img src="images/removefromview.gif">.
	 */
	JButton menuMapViewRemoveScheme = new JButton();

	/**
	 * <img src="images/mapsetup.gif">.
	 */
	JButton menuViewSetup = new JButton();

	public static final int IMG_SIZE = 16;
	public static final int BTN_SIZE = 24;

	public MapEditorToolBar() {
		super();

		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		ActionListener actionAdapter = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonActionPerformed(e);
			}
		};

		Dimension buttonSize = new Dimension(BTN_SIZE, BTN_SIZE);

		this.sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.sessionOpen.setMaximumSize(buttonSize);
		this.sessionOpen.setPreferredSize(buttonSize);
		this.sessionOpen.setToolTipText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_NEW));
		this.sessionOpen.setName(MapEditorApplicationModel.ITEM_SESSION_NEW);
		this.sessionOpen.addActionListener(actionAdapter);

		this.buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.buttonCloseSession.setMaximumSize(buttonSize);
		this.buttonCloseSession.setPreferredSize(buttonSize);
		this.buttonCloseSession.setToolTipText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_CLOSE));
		this.buttonCloseSession.setName(MapEditorApplicationModel.ITEM_SESSION_CLOSE);
//		this.buttonCloseSession.addActionListener(actionAdapter);

		this.menuSessionDomain.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/domains.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuSessionDomain.setMaximumSize(buttonSize);
		this.menuSessionDomain.setPreferredSize(buttonSize);
		this.menuSessionDomain.setToolTipText(LangModelGeneral.getString(MapEditorApplicationModel.ITEM_SESSION_DOMAIN));
		this.menuSessionDomain.setName(MapEditorApplicationModel.ITEM_SESSION_DOMAIN);
//		this.menuSessionDomain.addActionListener(actionAdapter);

		this.menuMapNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapNew.setMaximumSize(buttonSize);
		this.menuMapNew.setPreferredSize(buttonSize);
		this.menuMapNew.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_NEW));
		this.menuMapNew.setName(MapEditorApplicationModel.ITEM_MAP_NEW);
		this.menuMapNew.addActionListener(actionAdapter);

		this.menuMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapOpen.setMaximumSize(buttonSize);
		this.menuMapOpen.setPreferredSize(buttonSize);
		this.menuMapOpen.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_OPEN));
		this.menuMapOpen.setName(MapEditorApplicationModel.ITEM_MAP_OPEN);
		this.menuMapOpen.addActionListener(actionAdapter);

		this.menuMapSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapSave.setMaximumSize(buttonSize);
		this.menuMapSave.setPreferredSize(buttonSize);
		this.menuMapSave.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_SAVE));
		this.menuMapSave.setName(MapEditorApplicationModel.ITEM_MAP_SAVE);
		this.menuMapSave.addActionListener(actionAdapter);

		this.menuMapAddMap.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addmap.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapAddMap.setMaximumSize(buttonSize);
		this.menuMapAddMap.setPreferredSize(buttonSize);
		this.menuMapAddMap.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		this.menuMapAddMap.setName(MapEditorApplicationModel.ITEM_MAP_ADD_MAP);
		this.menuMapAddMap.addActionListener(actionAdapter);

		this.menuMapRemoveMap.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removemap.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapRemoveMap.setMaximumSize(buttonSize);
		this.menuMapRemoveMap.setPreferredSize(buttonSize);
		this.menuMapRemoveMap.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		this.menuMapRemoveMap.setName(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP);
		this.menuMapRemoveMap.addActionListener(actionAdapter);

		this.menuMapAddExternal.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addexternal.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapAddExternal.setMaximumSize(buttonSize);
		this.menuMapAddExternal.setPreferredSize(buttonSize);
		this.menuMapAddExternal.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		this.menuMapAddExternal.setName(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL);
		this.menuMapAddExternal.addActionListener(actionAdapter);

		this.menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewNew.setMaximumSize(buttonSize);
		this.menuMapViewNew.setPreferredSize(buttonSize);
		this.menuMapViewNew.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		this.menuMapViewNew.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW);
		this.menuMapViewNew.addActionListener(actionAdapter);

		this.menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewNew.setMaximumSize(buttonSize);
		this.menuMapViewNew.setPreferredSize(buttonSize);
		this.menuMapViewNew.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		this.menuMapViewNew.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN);
		this.menuMapViewNew.addActionListener(actionAdapter);

		this.menuMapViewSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/saveview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewSave.setMaximumSize(buttonSize);
		this.menuMapViewSave.setPreferredSize(buttonSize);
		this.menuMapViewSave.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		this.menuMapViewSave.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE);
		this.menuMapViewSave.addActionListener(actionAdapter);

		this.menuMapViewAddScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewAddScheme.setMaximumSize(buttonSize);
		this.menuMapViewAddScheme.setPreferredSize(buttonSize);
		this.menuMapViewAddScheme.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuMapViewAddScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		this.menuMapViewAddScheme.addActionListener(actionAdapter);

		this.menuMapViewRemoveScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewRemoveScheme.setMaximumSize(buttonSize);
		this.menuMapViewRemoveScheme.setPreferredSize(buttonSize);
		this.menuMapViewRemoveScheme.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		this.menuMapViewRemoveScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);
		this.menuMapViewRemoveScheme.addActionListener(actionAdapter);

		this.menuViewSetup.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/mapsetup.gif").
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuViewSetup.setMaximumSize(buttonSize);
		this.menuViewSetup.setPreferredSize(buttonSize);
		this.menuViewSetup.setToolTipText(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
		this.menuViewSetup.setName(MapEditorApplicationModel.ITEM_VIEW_CONTROLS);
		this.menuViewSetup.addActionListener(actionAdapter);

		add(this.sessionOpen);
//		add(this.buttonCloseSession);
//		addSeparator();
//		add(this.menuSessionDomain);
		addSeparator();
		add(this.menuMapNew);
		add(this.menuMapOpen);
		add(this.menuMapSave);
		addSeparator();
		add(this.menuMapAddMap);
		add(this.menuMapRemoveMap);
		addSeparator();
		add(this.menuMapAddExternal);
		addSeparator();
		add(this.menuMapViewNew);
		add(this.menuMapViewNew);
		add(this.menuMapViewSave);
		addSeparator();
		add(this.menuMapViewAddScheme);
		add(this.menuMapViewRemoveScheme);
		addSeparator();
		add(this.menuViewSetup);
		addSeparator();
	}

	public void setModel(ApplicationModel a) {
		this.aModel = a;
	}

	public ApplicationModel getModel() {
		return this.aModel;
	}

	public void modelChanged(String e) {
		modelChanged(new String[] { e
		});
	}

	public void modelChanged(String[] elementNames) {
		this.sessionOpen.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_NEW));
		this.sessionOpen.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW));
		this.buttonCloseSession.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_CLOSE));
		this.buttonCloseSession.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE));
		this.menuSessionDomain.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_SESSION_DOMAIN));
		this.menuSessionDomain.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN));

		this.menuMapNew.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_NEW));
		this.menuMapNew.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_NEW));
		this.menuMapOpen.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_OPEN));
		this.menuMapOpen.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN));
		this.menuMapSave.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE));
		this.menuMapSave.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE));
		this.menuMapAddMap.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		this.menuMapAddMap.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		this.menuMapRemoveMap.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		this.menuMapRemoveMap.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		this.menuMapAddExternal.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		this.menuMapAddExternal.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));

		this.menuMapViewNew.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		this.menuMapViewNew.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		this.menuMapViewNew.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		this.menuMapViewNew.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		this.menuMapViewSave.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		this.menuMapViewSave.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		this.menuMapViewAddScheme.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuMapViewAddScheme.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuMapViewRemoveScheme.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		this.menuMapViewRemoveScheme.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));

		this.menuViewSetup.setVisible(this.aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
		this.menuViewSetup.setEnabled(this.aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
	}

	public void buttonActionPerformed(ActionEvent e) {
		if(this.aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command.execute();
	}
}
