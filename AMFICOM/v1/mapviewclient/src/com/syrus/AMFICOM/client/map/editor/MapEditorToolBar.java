/*-
 * $$Id: MapEditorToolBar.java,v 1.26 2006/05/17 10:13:46 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.editor;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

/**
 * Панель инструментов модуля "Редактор топологических схем".
 *  
 * @version $Revision: 1.26 $, $Date: 2006/05/17 10:13:46 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
*/
public class MapEditorToolBar extends AbstractMainToolBar {

	private static final long serialVersionUID = -5786094840618759071L;
	/**
	 * <img src="images/open_session.gif">.
	 */
	JButton sessionOpen = new JButton();
	/**
	 * <img src="images/close_session.gif">.
	 */
	JButton sessionClose = new JButton();
	/**
	 * <img src="images/domains.gif">.
	 */
	JButton sessionDomain = new JButton();
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
	 * <img src="images/newmapview.gif">.
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
	 * <img src="images/map_layers.gif">.
	 */
	JButton menuViewMapLayers = new JButton();

	public static final int IMG_SIZE = 16;
	public static final int BTN_SIZE = 24;

	public MapEditorToolBar() {
		super();

		createButtons();
	}

	private void createButtons() {

		Dimension buttonSize = new Dimension(BTN_SIZE, BTN_SIZE);

		this.menuMapNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newmap.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapNew.setMaximumSize(buttonSize);
		this.menuMapNew.setPreferredSize(buttonSize);
		this.menuMapNew.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_NEW));
		this.menuMapNew.setName(MapEditorApplicationModel.ITEM_MAP_NEW);
		this.menuMapNew.addActionListener(super.actionListener);

		this.menuMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openmap.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapOpen.setMaximumSize(buttonSize);
		this.menuMapOpen.setPreferredSize(buttonSize);
		this.menuMapOpen.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_OPEN));
		this.menuMapOpen.setName(MapEditorApplicationModel.ITEM_MAP_OPEN);
		this.menuMapOpen.addActionListener(super.actionListener);

		this.menuMapSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/savemap.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapSave.setMaximumSize(buttonSize);
		this.menuMapSave.setPreferredSize(buttonSize);
		this.menuMapSave.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_SAVE));
		this.menuMapSave.setName(MapEditorApplicationModel.ITEM_MAP_SAVE);
		this.menuMapSave.addActionListener(super.actionListener);

		this.menuMapAddMap.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addmap.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapAddMap.setMaximumSize(buttonSize);
		this.menuMapAddMap.setPreferredSize(buttonSize);
		this.menuMapAddMap.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
		this.menuMapAddMap.setName(MapEditorApplicationModel.ITEM_MAP_ADD_MAP);
		this.menuMapAddMap.addActionListener(super.actionListener);

		this.menuMapRemoveMap.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removemap.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapRemoveMap.setMaximumSize(buttonSize);
		this.menuMapRemoveMap.setPreferredSize(buttonSize);
		this.menuMapRemoveMap.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
		this.menuMapRemoveMap.setName(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP);
		this.menuMapRemoveMap.addActionListener(super.actionListener);

		this.menuMapAddExternal.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addexternal.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapAddExternal.setMaximumSize(buttonSize);
		this.menuMapAddExternal.setPreferredSize(buttonSize);
		this.menuMapAddExternal.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		this.menuMapAddExternal.setName(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL);
		this.menuMapAddExternal.addActionListener(super.actionListener);

		this.menuMapViewNew.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/newmapview.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewNew.setMaximumSize(buttonSize);
		this.menuMapViewNew.setPreferredSize(buttonSize);
		this.menuMapViewNew.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
		this.menuMapViewNew.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW);
		this.menuMapViewNew.addActionListener(super.actionListener);

		this.menuMapViewOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openmapview.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewOpen.setMaximumSize(buttonSize);
		this.menuMapViewOpen.setPreferredSize(buttonSize);
		this.menuMapViewOpen.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
		this.menuMapViewOpen.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN);
		this.menuMapViewOpen.addActionListener(super.actionListener);

		this.menuMapViewSave.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/savemapview.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewSave.setMaximumSize(buttonSize);
		this.menuMapViewSave.setPreferredSize(buttonSize);
		this.menuMapViewSave.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
		this.menuMapViewSave.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE);
		this.menuMapViewSave.addActionListener(super.actionListener);

		this.menuMapViewAddScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addtoview.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewAddScheme.setMaximumSize(buttonSize);
		this.menuMapViewAddScheme.setPreferredSize(buttonSize);
		this.menuMapViewAddScheme.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
		this.menuMapViewAddScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME);
		this.menuMapViewAddScheme.addActionListener(super.actionListener);

		this.menuMapViewRemoveScheme.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefromview.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuMapViewRemoveScheme.setMaximumSize(buttonSize);
		this.menuMapViewRemoveScheme.setPreferredSize(buttonSize);
		this.menuMapViewRemoveScheme.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		this.menuMapViewRemoveScheme.setName(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME);
		this.menuMapViewRemoveScheme.addActionListener(super.actionListener);

		this.menuViewMapLayers.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/map_layers.gif"). //$NON-NLS-1$
				getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_DEFAULT)));
		this.menuViewMapLayers.setMaximumSize(buttonSize);
		this.menuViewMapLayers.setPreferredSize(buttonSize);
		this.menuViewMapLayers.setToolTipText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS));
		this.menuViewMapLayers.setName(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS);
		this.menuViewMapLayers.addActionListener(super.actionListener);

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
		add(this.menuMapViewOpen);
		add(this.menuMapViewSave);
		addSeparator();
		add(this.menuMapViewAddScheme);
		add(this.menuMapViewRemoveScheme);
		addSeparator();
		add(this.menuViewMapLayers);
		addSeparator();

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String e[]) {
				this.modelChanged(""); //$NON-NLS-1$
			}

			public void modelChanged(String elementName) {
				MapEditorToolBar.this.menuMapNew.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_NEW));
				MapEditorToolBar.this.menuMapNew.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_NEW));
				MapEditorToolBar.this.menuMapOpen.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_OPEN));
				MapEditorToolBar.this.menuMapOpen.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN));
				MapEditorToolBar.this.menuMapSave.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_SAVE));
				MapEditorToolBar.this.menuMapSave.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE));

				MapEditorToolBar.this.menuMapAddMap.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
				MapEditorToolBar.this.menuMapAddMap.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_MAP));
				MapEditorToolBar.this.menuMapRemoveMap.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));
				MapEditorToolBar.this.menuMapRemoveMap.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP));

				MapEditorToolBar.this.menuMapAddExternal.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
				MapEditorToolBar.this.menuMapAddExternal.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL));
		
				MapEditorToolBar.this.menuMapViewNew.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
				MapEditorToolBar.this.menuMapViewNew.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW));
				MapEditorToolBar.this.menuMapViewOpen.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
				MapEditorToolBar.this.menuMapViewOpen.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN));
				MapEditorToolBar.this.menuMapViewSave.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));
				MapEditorToolBar.this.menuMapViewSave.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE));

				MapEditorToolBar.this.menuMapViewAddScheme.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
				MapEditorToolBar.this.menuMapViewAddScheme.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME));
				MapEditorToolBar.this.menuMapViewRemoveScheme.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
				MapEditorToolBar.this.menuMapViewRemoveScheme.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME));
		
				MapEditorToolBar.this.menuViewMapLayers.setVisible(MapEditorToolBar.this.getApplicationModel().isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS));
				MapEditorToolBar.this.menuViewMapLayers.setEnabled(MapEditorToolBar.this.getApplicationModel().isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP_LAYERS));
			}
		});
	}
}
