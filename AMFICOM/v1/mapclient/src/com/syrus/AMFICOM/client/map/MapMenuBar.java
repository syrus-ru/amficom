package com.syrus.AMFICOM.Client.Map;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class MapMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuMap = new JMenu();
	JMenuItem menuMapNew = new JMenuItem();
	JMenuItem menuMapOpen = new JMenuItem();
	JMenuItem menuMapClose = new JMenuItem();
	JMenuItem menuMapSave = new JMenuItem();
	JMenuItem menuMapOptions = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();
	JMenu menuEdit = new JMenu();
	JMenuItem menuEditUndo = new JMenuItem();
	JMenuItem menuEditRedo = new JMenuItem();
	JMenuItem menuEditCut = new JMenuItem();
	JMenuItem menuEditCopy = new JMenuItem();
	JMenuItem menuEditPaste = new JMenuItem();
	JMenuItem menuEditDelete = new JMenuItem();
	JMenuItem menuEditSelectAll = new JMenuItem();
	JMenuItem menuEditSelect = new JMenuItem();
	JMenu menuView = new JMenu();
	JMenuItem menuViewNavigator = new JMenuItem();
	JMenuItem menuViewToolbar = new JMenuItem();
	JMenuItem menuViewRefresh = new JMenuItem();
	JMenuItem menuViewPoints = new JMenuItem();
	JMenuItem menuViewMetrics = new JMenuItem();
	JMenuItem menuViewPanel = new JMenuItem();
	JMenu menuNavigate = new JMenu();
	JMenuItem menuNavigateLaft = new JMenuItem();
	JMenuItem menuNavigateRight = new JMenuItem();
	JMenuItem menuNavigateUp = new JMenuItem();
	JMenuItem menuNavigateDown = new JMenuItem();
	JMenuItem menuNavigateZoomIn = new JMenuItem();
	JMenuItem menuNavigateZoomOut = new JMenuItem();
	JMenuItem menuNavigateZoomBox = new JMenuItem();
	JMenuItem menuNavigateZoomSelection = new JMenuItem();
	JMenuItem menuNavigateZoomMap = new JMenuItem();
	JMenuItem menuNavigateCenterPoint = new JMenuItem();
	JMenuItem menuNavigateCenterSelection = new JMenuItem();
	JMenuItem menuNavigateCenterMap = new JMenuItem();
	JMenu menuElement = new JMenu();
	JMenuItem menuElementCatalogue = new JMenuItem();
	JMenuItem menuElementGroup = new JMenuItem();
	JMenuItem menuElementUngroup = new JMenuItem();
	JMenuItem menuElementAlign = new JMenuItem();
	JMenuItem menuElementProperties = new JMenuItem();
	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	public MapMenuBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public MapMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		MapMenuBar_this_actionAdapter actionAdapter =
				new MapMenuBar_this_actionAdapter(this);

		menuMap.setText(LangModelMap.Text("menuMap"));
		menuMap.setName("menuMap");
		menuMapNew.setText(LangModelMap.Text("menuMapNew"));
		menuMapNew.setName("menuMapNew");
		menuMapNew.addActionListener(actionAdapter);
		menuMapOpen.setText(LangModelMap.Text("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);
		menuMapClose.setText(LangModelMap.Text("menuMapClose"));
		menuMapClose.setName("menuMapClose");
		menuMapClose.addActionListener(actionAdapter);
		menuMapSave.setText(LangModelMap.Text("menuMapSave"));
		menuMapSave.setName("menuMapSave");
		menuMapSave.addActionListener(actionAdapter);
		menuMapOptions.setText(LangModelMap.Text("menuMapOptions"));
		menuMapOptions.setName("menuMapOptions");
		menuMapOptions.addActionListener(actionAdapter);
		menuExit.setText(LangModelMap.Text("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuEdit.setText(LangModelMap.Text("menuEdit"));
		menuEdit.setName("menuEdit");
		menuEditUndo.setText(LangModelMap.Text("menuEditUndo"));
		menuEditUndo.setName("menuEditUndo");
		menuEditUndo.addActionListener(actionAdapter);
		menuEditRedo.setText(LangModelMap.Text("menuEditRedo"));
		menuEditRedo.setName("menuEditRedo");
		menuEditRedo.addActionListener(actionAdapter);
		menuEditCut.setText(LangModelMap.Text("menuEditCut"));
		menuEditCut.setName("menuEditCut");
		menuEditCut.addActionListener(actionAdapter);
		menuEditCopy.setText(LangModelMap.Text("menuEditCopy"));
		menuEditCopy.setName("menuEditCopy");
		menuEditCopy.addActionListener(actionAdapter);
		menuEditPaste.setText(LangModelMap.Text("menuEditPaste"));
		menuEditPaste.setName("menuEditPaste");
		menuEditPaste.addActionListener(actionAdapter);
		menuEditDelete.setText(LangModelMap.Text("menuEditDelete"));
		menuEditDelete.setName("menuEditDelete");
		menuEditDelete.addActionListener(actionAdapter);
		menuEditSelectAll.setText(LangModelMap.Text("menuEditSelectAll"));
		menuEditSelectAll.setName("menuEditSelectAll");
		menuEditSelectAll.addActionListener(actionAdapter);
		menuEditSelect.setText(LangModelMap.Text("menuEditSelect"));
		menuEditSelect.setName("menuEditSelect");
		menuEditSelect.addActionListener(actionAdapter);

		menuView.setText(LangModelMap.Text("menuView"));
		menuView.setName("menuView");
		menuViewNavigator.setText(LangModelMap.Text("menuViewNavigator"));
		menuViewNavigator.setName("menuViewNavigator");
		menuViewNavigator.addActionListener(actionAdapter);
		menuViewToolbar.setText(LangModelMap.Text("menuViewToolbar"));
		menuViewToolbar.setName("menuViewToolbar");
		menuViewToolbar.addActionListener(actionAdapter);
		menuViewRefresh.setText(LangModelMap.Text("menuViewRefresh"));
		menuViewRefresh.setName("menuViewRefresh");
		menuViewRefresh.addActionListener(actionAdapter);
		menuViewPoints.setText(LangModelMap.Text("menuViewPoints"));
		menuViewPoints.setName("menuViewPoints");
		menuViewPoints.addActionListener(actionAdapter);
		menuViewMetrics.setText(LangModelMap.Text("menuViewMetrics"));
		menuViewMetrics.setName("menuViewMetrics");
		menuViewMetrics.addActionListener(actionAdapter);
		menuViewPanel.setText(LangModelMap.Text("menuViewPanel"));
		menuViewPanel.setName("menuViewPanel");
		menuViewPanel.addActionListener(actionAdapter);

		menuNavigate.setText(LangModelMap.Text("menuNavigate"));
		menuNavigate.setName("menuNavigate");
		menuNavigateLaft.setText(LangModelMap.Text("menuNavigateLeft"));
		menuNavigateLaft.setName("menuNavigateLeft");
		menuNavigateLaft.addActionListener(actionAdapter);
		menuNavigateRight.setText(LangModelMap.Text("menuNavigateRight"));
		menuNavigateRight.setName("menuNavigateRight");
		menuNavigateRight.addActionListener(actionAdapter);
		menuNavigateUp.setText(LangModelMap.Text("menuNavigateUp"));
		menuNavigateUp.setName("menuNavigateUp");
		menuNavigateUp.addActionListener(actionAdapter);
		menuNavigateDown.setText(LangModelMap.Text("menuNavigateDown"));
		menuNavigateDown.setName("menuNavigateDown");
		menuNavigateDown.addActionListener(actionAdapter);
		menuNavigateZoomIn.setText(LangModelMap.Text("menuNavigateZoomIn"));
		menuNavigateZoomIn.setName("menuNavigateZoomIn");
		menuNavigateZoomIn.addActionListener(actionAdapter);
		menuNavigateZoomOut.setText(LangModelMap.Text("menuNavigateZoomOut"));
		menuNavigateZoomOut.setName("menuNavigateZoomOut");
		menuNavigateZoomOut.addActionListener(actionAdapter);
		menuNavigateZoomBox.setText(LangModelMap.Text("menuNavigateZoomBox"));
		menuNavigateZoomBox.setName("menuNavigateZoomBox");
		menuNavigateZoomBox.addActionListener(actionAdapter);
		menuNavigateZoomSelection.setText(LangModelMap.Text("menuNavigateZoomSelection"));
		menuNavigateZoomSelection.setName("menuNavigateZoomSelection");
		menuNavigateZoomSelection.addActionListener(actionAdapter);
		menuNavigateZoomMap.setText(LangModelMap.Text("menuNavigateZoomMap"));
		menuNavigateZoomMap.setName("menuNavigateZoomMap");
		menuNavigateZoomMap.addActionListener(actionAdapter);
		menuNavigateCenterPoint.setText(LangModelMap.Text("menuNavigateCenterPoint"));
		menuNavigateCenterPoint.setName("menuNavigateCenterPoint");
		menuNavigateCenterPoint.addActionListener(actionAdapter);
		menuNavigateCenterSelection.setText(LangModelMap.Text("menuNavigateCenterSelection"));
		menuNavigateCenterSelection.setName("menuNavigateCenterSelection");
		menuNavigateCenterSelection.addActionListener(actionAdapter);
		menuNavigateCenterMap.setText(LangModelMap.Text("menuNavigateCenterMap"));
		menuNavigateCenterMap.setName("menuNavigateCenterMap");
		menuNavigateCenterMap.addActionListener(actionAdapter);

		menuElement.setText(LangModelMap.Text("menuElement"));
		menuElement.setName("menuElement");
		menuElementCatalogue.setText(LangModelMap.Text("menuElementCatalogue"));
		menuElementCatalogue.setName("menuElementCatalogue");
		menuElementCatalogue.addActionListener(actionAdapter);
		menuElementGroup.setText(LangModelMap.Text("menuElementGroup"));
		menuElementGroup.setName("menuElementGroup");
		menuElementGroup.addActionListener(actionAdapter);
		menuElementUngroup.setText(LangModelMap.Text("menuElementUngroup"));
		menuElementUngroup.setName("menuElementUngroup");
		menuElementUngroup.addActionListener(actionAdapter);
		menuElementAlign.setText(LangModelMap.Text("menuElementAlign"));
		menuElementAlign.setName("menuElementAlign");
		menuElementAlign.addActionListener(actionAdapter);
		menuElementProperties.setText(LangModelMap.Text("menuElementProperties"));
		menuElementProperties.setName("menuElementProperties");
		menuElementProperties.addActionListener(actionAdapter);

		menuHelp.setText(LangModelMap.Text("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModelMap.Text("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModelMap.Text("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModelMap.Text("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModelMap.Text("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModelMap.Text("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModelMap.Text("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuMap.add(menuMapNew);
		menuMap.add(menuMapOpen);
		menuMap.add(menuMapClose);
		menuMap.add(menuMapSave);
		menuMap.addSeparator();
		menuMap.add(menuMapOptions);
		menuMap.addSeparator();
		menuMap.add(menuExit);
		this.add(menuMap);
		menuEdit.add(menuEditUndo);
		menuEdit.add(menuEditRedo);
		menuEdit.addSeparator();
		menuEdit.add(menuEditCut);
		menuEdit.add(menuEditCopy);
		menuEdit.add(menuEditPaste);
		menuEdit.add(menuEditDelete);
		menuEdit.addSeparator();
		menuEdit.add(menuEditSelectAll);
		menuEdit.add(menuEditSelect);
		this.add(menuEdit);
		menuView.add(menuViewNavigator);
		menuView.add(menuViewToolbar);
		menuView.add(menuViewRefresh);
		menuView.add(menuViewPoints);
		menuView.add(menuViewMetrics);
		menuView.add(menuViewPanel);
		this.add(menuView);
		menuNavigate.add(menuNavigateLaft);
		menuNavigate.add(menuNavigateRight);
		menuNavigate.add(menuNavigateUp);
		menuNavigate.add(menuNavigateDown);
		menuNavigate.addSeparator();
		menuNavigate.add(menuNavigateZoomIn);
		menuNavigate.add(menuNavigateZoomOut);
		menuNavigate.add(menuNavigateZoomBox);
		menuNavigate.add(menuNavigateZoomSelection);
		menuNavigate.add(menuNavigateZoomMap);
		menuNavigate.addSeparator();
		menuNavigate.add(menuNavigateCenterPoint);
		menuNavigate.add(menuNavigateCenterSelection);
		menuNavigate.add(menuNavigateCenterMap);
		this.add(menuNavigate);
		menuElement.add(menuElementCatalogue);
		menuElement.add(menuElementGroup);
		menuElement.add(menuElementUngroup);
		menuElement.add(menuElementAlign);
		menuElement.add(menuElementProperties);
		this.add(menuElement);
		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpCourse);
		menuHelp.add(menuHelpHelp);
		menuHelp.add(menuHelpAbout);
		this.add(menuHelp);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		int count = e.length;
		int i;

		menuMap.setVisible(aModel.isVisible("menuMap"));
		menuMap.setEnabled(aModel.isEnabled("menuMap"));

		menuMapNew.setVisible(aModel.isVisible("menuMapNew"));
		menuMapNew.setEnabled(aModel.isEnabled("menuMapNew"));

		menuMapOpen.setVisible(aModel.isVisible("menuMapOpen"));
		menuMapOpen.setEnabled(aModel.isEnabled("menuMapOpen"));

		menuMapClose.setVisible(aModel.isVisible("menuMapClose"));
		menuMapClose.setEnabled(aModel.isEnabled("menuMapClose"));

		menuMapSave.setVisible(aModel.isVisible("menuMapSave"));
		menuMapSave.setEnabled(aModel.isEnabled("menuMapSave"));

		menuMapOptions.setVisible(aModel.isVisible("menuMapOptions"));
		menuMapOptions.setEnabled(aModel.isEnabled("menuMapOptions"));

		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuEdit.setVisible(aModel.isVisible("menuEdit"));
		menuEdit.setEnabled(aModel.isEnabled("menuEdit"));

		menuEditUndo.setVisible(aModel.isVisible("menuEditUndo"));
		menuEditUndo.setEnabled(aModel.isEnabled("menuEditUndo"));

		menuEditRedo.setVisible(aModel.isVisible("menuEditRedo"));
		menuEditRedo.setEnabled(aModel.isEnabled("menuEditRedo"));

		menuEditCut.setVisible(aModel.isVisible("menuEditCut"));
		menuEditCut.setEnabled(aModel.isEnabled("menuEditCut"));

		menuEditCopy.setVisible(aModel.isVisible("menuEditCopy"));
		menuEditCopy.setEnabled(aModel.isEnabled("menuEditCopy"));

		menuEditPaste.setVisible(aModel.isVisible("menuEditPaste"));
		menuEditPaste.setEnabled(aModel.isEnabled("menuEditPaste"));

		menuEditDelete.setVisible(aModel.isVisible("menuEditDelete"));
		menuEditDelete.setEnabled(aModel.isEnabled("menuEditDelete"));

		menuEditSelectAll.setVisible(aModel.isVisible("menuEditSelectAll"));
		menuEditSelectAll.setEnabled(aModel.isEnabled("menuEditSelectAll"));

		menuEditSelect.setVisible(aModel.isVisible("menuEditSelect"));
		menuEditSelect.setEnabled(aModel.isEnabled("menuEditSelect"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));

		menuViewNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
		menuViewNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));

		menuViewToolbar.setVisible(aModel.isVisible("menuViewToolbar"));
		menuViewToolbar.setEnabled(aModel.isEnabled("menuViewToolbar"));

		menuViewRefresh.setVisible(aModel.isVisible("menuViewRefresh"));
		menuViewRefresh.setEnabled(aModel.isEnabled("menuViewRefresh"));

		menuViewPoints.setVisible(aModel.isVisible("menuViewPoints"));
		menuViewPoints.setEnabled(aModel.isEnabled("menuViewPoints"));

		menuViewMetrics.setVisible(aModel.isVisible("menuViewMetrics"));
		menuViewMetrics.setEnabled(aModel.isEnabled("menuViewMetrics"));

		menuViewPanel.setVisible(aModel.isVisible("menuViewPanel"));
		menuViewPanel.setEnabled(aModel.isEnabled("menuViewPanel"));

		menuNavigate.setVisible(aModel.isVisible("menuNavigate"));
		menuNavigate.setEnabled(aModel.isEnabled("menuNavigate"));

		menuNavigateLaft.setVisible(aModel.isVisible("menuNavigateLaft"));
		menuNavigateLaft.setEnabled(aModel.isEnabled("menuNavigateLaft"));

		menuNavigateRight.setVisible(aModel.isVisible("menuNavigateRight"));
		menuNavigateRight.setEnabled(aModel.isEnabled("menuNavigateRight"));

		menuNavigateUp.setVisible(aModel.isVisible("menuNavigateUp"));
		menuNavigateUp.setEnabled(aModel.isEnabled("menuNavigateUp"));

		menuNavigateDown.setVisible(aModel.isVisible("menuNavigateDown"));
		menuNavigateDown.setEnabled(aModel.isEnabled("menuNavigateDown"));

		menuNavigateZoomIn.setVisible(aModel.isVisible("menuNavigateZoomIn"));
		menuNavigateZoomIn.setEnabled(aModel.isEnabled("menuNavigateZoomIn"));

		menuNavigateZoomOut.setVisible(aModel.isVisible("menuNavigateZoomOut"));
		menuNavigateZoomOut.setEnabled(aModel.isEnabled("menuNavigateZoomOut"));

		menuNavigateZoomBox.setVisible(aModel.isVisible("menuNavigateZoomBox"));
		menuNavigateZoomBox.setEnabled(aModel.isEnabled("menuNavigateZoomBox"));

		menuNavigateZoomSelection.setVisible(aModel.isVisible("menuNavigateZoomSelection"));
		menuNavigateZoomSelection.setEnabled(aModel.isEnabled("menuNavigateZoomSelection"));

		menuNavigateZoomMap.setVisible(aModel.isVisible("menuNavigateZoomMap"));
		menuNavigateZoomMap.setEnabled(aModel.isEnabled("menuNavigateZoomMap"));

		menuNavigateCenterPoint.setVisible(aModel.isVisible("menuNavigateCenterPoint"));
		menuNavigateCenterPoint.setEnabled(aModel.isEnabled("menuNavigateCenterPoint"));

		menuNavigateCenterSelection.setVisible(aModel.isVisible("menuNavigateCenterSelection"));
		menuNavigateCenterSelection.setEnabled(aModel.isEnabled("menuNavigateCenterSelection"));

		menuNavigateCenterMap.setVisible(aModel.isVisible("menuNavigateCenterMap"));
		menuNavigateCenterMap.setEnabled(aModel.isEnabled("menuNavigateCenterMap"));

		menuElement.setVisible(aModel.isVisible("menuElement"));
		menuElement.setEnabled(aModel.isEnabled("menuElement"));

		menuElementCatalogue.setVisible(aModel.isVisible("menuElementCatalogue"));
		menuElementCatalogue.setEnabled(aModel.isEnabled("menuElementCatalogue"));

		menuElementGroup.setVisible(aModel.isVisible("menuElementGroup"));
		menuElementGroup.setEnabled(aModel.isEnabled("menuElementGroup"));

		menuElementUngroup.setVisible(aModel.isVisible("menuElementUngroup"));
		menuElementUngroup.setEnabled(aModel.isEnabled("menuElementUngroup"));

		menuElementAlign.setVisible(aModel.isVisible("menuElementAlign"));
		menuElementAlign.setEnabled(aModel.isEnabled("menuElementAlign"));

		menuElementProperties.setVisible(aModel.isVisible("menuElementProperties"));
		menuElementProperties.setEnabled(aModel.isEnabled("menuElementProperties"));

		menuHelp.setVisible(aModel.isVisible("menuHelp"));
		menuHelp.setEnabled(aModel.isEnabled("menuHelp"));

		menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
		menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));

		menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
		menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));

		menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
		menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));

		menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
		menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));

		menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
		menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));

		menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
		menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));

	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}
}

class MapMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	MapMenuBar adaptee;

	MapMenuBar_this_actionAdapter(MapMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

