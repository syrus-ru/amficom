package com.syrus.AMFICOM.Client.Model;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class ModelMainToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = -1939493412625726895L;

	public ModelMainToolBar() {
		initItems();
	}

	private void initItems() {
		final JButton menuViewMapViewOpen = new JButton();
		menuViewMapViewOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif")
				.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewMapViewOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewMapViewOpen.setToolTipText(LangModelModel.getString("menuViewMapViewOpen"));
		menuViewMapViewOpen.setName("menuViewMapViewOpen");
		menuViewMapViewOpen.addActionListener(super.actionListener);

		final JButton menuViewModelLoad = new JButton();
		menuViewModelLoad.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_model.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewModelLoad.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewModelLoad.setToolTipText(LangModelModel.getString("menuViewModelLoad"));
		menuViewModelLoad.setName("menuViewModelLoad");
		menuViewModelLoad.addActionListener(super.actionListener);

		final JButton menuViewSchemeOpen = new JButton();
		menuViewSchemeOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewSchemeOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		menuViewSchemeOpen.setToolTipText(LangModelModel.getString("menuViewSchemeOpen"));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(super.actionListener);

		final JButton traceAddCompare = new JButton();
		traceAddCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_add.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		traceAddCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		traceAddCompare.setName("menuTraceAddCompare");
		traceAddCompare.addActionListener(super.actionListener);

		final JButton traceRemoveCompare = new JButton();
		traceRemoveCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_remove.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		traceRemoveCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		traceRemoveCompare.setName("menuTraceRemoveCompare");
		traceRemoveCompare.addActionListener(super.actionListener);

		final JButton buttonFileOpen = new JButton();
		buttonFileOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonFileOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		buttonFileOpen.setToolTipText(LangModelAnalyse.getString("menuFileOpen"));
		buttonFileOpen.setName("menuFileOpen");
		buttonFileOpen.addActionListener(super.actionListener);

		final JButton fileAdd = new JButton();
		fileAdd.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		fileAdd.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		fileAdd.setToolTipText(LangModelAnalyse.getString("menuFileAddCompare"));
		fileAdd.setName("menuFileAddCompare");
		fileAdd.addActionListener(super.actionListener);

		final JButton fileRemove = new JButton();
		fileRemove.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		fileRemove.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		fileRemove.setToolTipText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		fileRemove.setName("menuFileRemoveCompare");
		fileRemove.addActionListener(super.actionListener);

		final JButton saveModel = new JButton();
		saveModel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		saveModel.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		saveModel.setToolTipText(LangModelModel.getString("menuViewModelSave"));
		saveModel.setName("menuViewModelSave");
		saveModel.addActionListener(super.actionListener);

		addSeparator();
		add(menuViewMapViewOpen);
		add(menuViewSchemeOpen);
		addSeparator();
		add(buttonFileOpen);
		add(fileAdd);
		add(fileRemove);
		addSeparator();
		add(menuViewModelLoad);
		add(traceAddCompare);
		add(traceRemoveCompare);
		addSeparator();
		add(saveModel);

		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e });
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = ModelMainToolBar.this.getApplicationModel();
				
				menuViewMapViewOpen.setVisible(aModel.isVisible("menuViewMapViewOpen"));
				menuViewMapViewOpen.setEnabled(aModel.isEnabled("menuViewMapViewOpen"));
				menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
				menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
				menuViewModelLoad.setVisible(aModel.isVisible("menuViewModelLoad"));
				menuViewModelLoad.setEnabled(aModel.isEnabled("menuViewModelLoad"));
				traceAddCompare.setVisible(aModel.isVisible("menuTraceAddCompare"));
				traceAddCompare.setEnabled(aModel.isEnabled("menuTraceAddCompare"));
				traceRemoveCompare.setVisible(aModel.isVisible("menuTraceRemoveCompare"));
				traceRemoveCompare.setEnabled(aModel.isEnabled("menuTraceRemoveCompare"));
				buttonFileOpen.setVisible(aModel.isVisible("menuFileOpen"));
				buttonFileOpen.setEnabled(aModel.isEnabled("menuFileOpen"));
				fileAdd.setEnabled(aModel.isEnabled("menuFileAddCompare"));
				fileAdd.setVisible(aModel.isVisible("menuFileAddCompare"));
				fileRemove.setEnabled(aModel.isEnabled("menuFileRemoveCompare"));
				fileRemove.setVisible(aModel.isVisible("menuFileRemoveCompare"));
				saveModel.setVisible(aModel.isVisible("menuViewModelSave"));
				saveModel.setEnabled(aModel.isEnabled("menuViewModelSave"));
			}
		});
	}
}
