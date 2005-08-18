package com.syrus.AMFICOM.Client.Optimize;

// Copyright (c) Syrus Systems 2003 Syrus Systems
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;

public class OptimizeMenuBar extends AbstractMainMenuBar{	

	//-------------------------------------------------------------------------------------------------------------
	public OptimizeMenuBar(ApplicationModel aModel)	{
		super(aModel);
	}

	//-------------------------------------------------------------------------------------------------------------
	@Override
	protected void addMenuItems() {
		final JMenu menuScheme = new JMenu();
		final JMenuItem menuMapOpen = new JMenuItem();
		final JMenuItem menuSchemeOpen = new JMenuItem();
		final JMenuItem menuSchemeSave = new JMenuItem();
	  final JMenuItem menuSchemeSaveAs = new JMenuItem();
	  final JMenuItem menuLoadSm = new JMenuItem();
	  final JMenuItem menuClearScheme = new JMenuItem();
		final JMenuItem menuSchemeClose = new JMenuItem();

		final JMenu menuView = new JMenu();
		final JMenuItem menuViewMap = new JMenuItem();
		final JMenuItem menuViewScheme = new JMenuItem();
		final JMenuItem menuViewGraph = new JMenuItem();
		final JMenuItem menuViewMapElProperties = new JMenuItem();
	  final JMenuItem menuViewSchElProperties = new JMenuItem();
		final JMenuItem menuViewSolution = new JMenuItem();
		final JMenuItem menuViewKIS = new JMenuItem();
		final JMenuItem menuViewMode = new JMenuItem();
		final JMenuItem menuViewParams = new JMenuItem();
		//JMenuItem menuViewOptions = new JMenuItem();
		final JMenuItem menuViewShowall = new JMenuItem();

		final JMenu menuOptimize = new JMenu();
	  final JMenu menuOptimizeCriteria = new JMenu();
	  final JMenu menuOptimizeMode = new JMenu();
	  final JCheckBoxMenuItem menuOptimizeModeUnidirect = new JCheckBoxMenuItem();// одностороннее тестирование
	  final JCheckBoxMenuItem menuOptimizeModeBidirect = new JCheckBoxMenuItem(); // двустороннее тестирование
	  final JMenu menuOptimizeCriteriaPrice = new JMenu();
	  final JMenuItem menuOptimizeCriteriaPriceLoad = new JMenuItem();
	  final JMenuItem menuOptimizeCriteriaPriceSave = new JMenuItem();
	  final JMenuItem menuOptimizeCriteriaPriceSaveas = new JMenuItem();
	  final JMenuItem menuOptimizeCriteriaPriceClose = new JMenuItem();
		final JMenuItem menuOptimizeStart = new JMenuItem();
		final JMenuItem menuOptimizeStop = new JMenuItem();
		//JMenuItem menuOptimizeAuto = new JMenuItem();
		//JMenuItem menuOptimizeNext = new JMenuItem();

		final JMenu menuHelp = new JMenu();
		final JMenuItem menuHelpContents = new JMenuItem();
		final JMenuItem menuHelpFind = new JMenuItem();
		final JMenuItem menuHelpTips = new JMenuItem();
		final JMenuItem menuHelpStart = new JMenuItem();
		final JMenuItem menuHelpCourse = new JMenuItem();
		final JMenuItem menuHelpHelp = new JMenuItem();
		final JMenuItem menuHelpSupport = new JMenuItem();
		final JMenuItem menuHelpLicense = new JMenuItem();
		final JMenuItem menuHelpAbout = new JMenuItem();
		
		menuScheme.setText(LangModelOptimize.Text("menuScheme"));
		menuScheme.setName("menuScheme");
		menuMapOpen.setText(LangModelOptimize.Text("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);
		menuSchemeOpen.setText(LangModelOptimize.Text("menuSchemeOpen"));
		menuSchemeOpen.setName("menuSchemeOpen");
		menuSchemeOpen.addActionListener(actionAdapter);
		menuSchemeSave.setText(LangModelOptimize.Text("menuSchemeSave"));
		menuSchemeSave.setName("menuSchemeSave");
    menuSchemeSave.addActionListener(actionAdapter);
    menuSchemeSaveAs.setText(LangModelOptimize.Text("menuSchemeSaveAs"));
		menuSchemeSaveAs.setName("menuSchemeSaveAs");
		menuSchemeSaveAs.addActionListener(actionAdapter);
    menuLoadSm.setText(LangModelOptimize.Text("menuLoadSm"));
    menuLoadSm.setName("menuLoadSm");
    menuLoadSm.addActionListener(actionAdapter);
    menuClearScheme.setText(LangModelOptimize.Text("menuClearScheme"));
    menuClearScheme.setName("menuClearScheme");
    menuClearScheme.addActionListener(actionAdapter);
		menuSchemeClose.setText(LangModelOptimize.Text("menuSchemeClose"));
		menuSchemeClose.setName("menuSchemeClose");
		menuSchemeClose.addActionListener(actionAdapter);

		menuView.setText(LangModelOptimize.Text("menuView"));
		menuView.setName("menuView");

		menuViewMap.setText(LangModelOptimize.Text("menuViewMap"));
		menuViewMap.setName("menuViewMap");
		menuViewMap.addActionListener(actionAdapter);

		menuViewScheme.setText(LangModelOptimize.Text("menuViewScheme"));
		menuViewScheme.setName("menuViewScheme");
		menuViewScheme.addActionListener(actionAdapter);

		menuViewGraph.setText(LangModelOptimize.Text("menuViewGraph"));
		menuViewGraph.setName("menuViewGraph");
		menuViewGraph.addActionListener(actionAdapter);

		menuViewMapElProperties.setText(LangModelOptimize.Text("menuViewMapElProperties"));
		menuViewMapElProperties.setName("menuViewMapElProperties");
		menuViewMapElProperties.addActionListener(actionAdapter);

    menuViewSchElProperties.setText(LangModelOptimize.Text("menuViewSchElProperties"));
    menuViewSchElProperties.setName("menuViewSchElProperties");
    menuViewSchElProperties.addActionListener(actionAdapter);

		menuViewSolution.setText(LangModelOptimize.Text("menuViewSolution"));
		menuViewSolution.setName("menuViewSolution");
		menuViewSolution.addActionListener(actionAdapter);

		menuViewKIS.setText(LangModelOptimize.Text("menuViewKIS"));
		menuViewKIS.setName("menuViewKIS");
		menuViewKIS.addActionListener(actionAdapter);

		menuViewMode.setText(LangModelOptimize.Text("menuViewMode"));
		menuViewMode.setName("menuViewMode");
		menuViewMode.addActionListener(actionAdapter);

		menuViewParams.setText(LangModelOptimize.Text("menuViewParams"));
		menuViewParams.setName("menuViewParams");
		menuViewParams.addActionListener(actionAdapter);

		//menuViewOptions.setText(LangModelOptimize.Text("menuViewOptions"));
		//menuViewOptions.setName("menuViewOptions");
		//menuViewOptions.addActionListener(actionAdapter);

		menuViewShowall.setText(LangModelOptimize.Text("menuViewShowall"));
		menuViewShowall.setName("menuViewShowall");
		menuViewShowall.addActionListener(actionAdapter);


		menuOptimize.setText(LangModelOptimize.Text("menuOptimize"));
		menuOptimize.setName("menuOptimize");
    menuOptimizeCriteria.setText(LangModelOptimize.Text("menuOptimizeCriteria"));
    menuOptimizeCriteria.setName("menuOptimizeCriteria");
    menuOptimizeCriteria.addActionListener(actionAdapter);
    menuOptimizeCriteriaPrice.setText(LangModelOptimize.Text("menuOptimizeCriteriaPrice"));
    menuOptimizeCriteriaPrice.setName("menuOptimizeCriteriaPrice");
    menuOptimizeCriteriaPrice.addActionListener(actionAdapter);
		menuOptimizeCriteriaPriceLoad.setText(LangModelOptimize.Text("menuOptimizeCriteriaPriceLoad"));
    menuOptimizeCriteriaPriceLoad.setName("menuOptimizeCriteriaPriceLoad");
    menuOptimizeCriteriaPriceLoad.addActionListener(actionAdapter);
    menuOptimizeCriteriaPriceSave.setText(LangModelOptimize.Text("menuOptimizeCriteriaPriceSave"));
    menuOptimizeCriteriaPriceSave.setName("menuOptimizeCriteriaPriceSave");
    menuOptimizeCriteriaPriceSave.addActionListener(actionAdapter);
    menuOptimizeCriteriaPriceSaveas.setText(LangModelOptimize.Text("menuOptimizeCriteriaPriceSaveas"));
    menuOptimizeCriteriaPriceSaveas.setName("menuOptimizeCriteriaPriceSaveas");
    menuOptimizeCriteriaPriceSaveas.addActionListener(actionAdapter);
    menuOptimizeCriteriaPriceClose.setText(LangModelOptimize.Text("menuOptimizeCriteriaPriceClose"));
    menuOptimizeCriteriaPriceClose.setName("menuOptimizeCriteriaPriceClose");
    menuOptimizeCriteriaPriceClose.addActionListener(actionAdapter);

    menuOptimizeMode.setText(LangModelOptimize.Text("menuOptimizeMode"));
    menuOptimizeMode.setName("menuOptimizeMode");
    menuOptimizeMode.addActionListener(actionAdapter);
    menuOptimizeModeUnidirect.setText(LangModelOptimize.Text("menuOptimizeModeUnidirect"));
    menuOptimizeModeUnidirect.setName("menuOptimizeModeUnidirect");
    menuOptimizeModeUnidirect.addActionListener(actionAdapter);
    menuOptimizeModeBidirect.setText(LangModelOptimize.Text("menuOptimizeModeBidirect"));
    menuOptimizeModeBidirect.setName("menuOptimizeModeBidirect");
    menuOptimizeModeBidirect.addActionListener(actionAdapter);

    menuOptimizeStart.setText(LangModelOptimize.Text("menuOptimizeStart"));
    menuOptimizeStart.setName("menuOptimizeStart");
		menuOptimizeStart.addActionListener(actionAdapter);
		menuOptimizeStop.setText(LangModelOptimize.Text("menuOptimizeStop"));
		menuOptimizeStop.setName("menuOptimizeStop");
		menuOptimizeStop.addActionListener(actionAdapter);

		//menuOptimizeAuto.setText(LangModelOptimize.Text("menuOptimizeAuto"));
		//menuOptimizeAuto.setName("menuOptimizeAuto");
		//menuOptimizeAuto.addActionListener(actionAdapter);
		//menuOptimizeNext.setText(LangModelOptimize.Text("menuOptimizeNext"));
		//menuOptimizeNext.setName("menuOptimizeNext");
		//menuOptimizeNext.addActionListener(actionAdapter);

		menuHelp.setText(LangModelOptimize.Text("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModelOptimize.Text("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModelOptimize.Text("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModelOptimize.Text("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModelOptimize.Text("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModelOptimize.Text("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModelOptimize.Text("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModelOptimize.Text("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModelOptimize.Text("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModelOptimize.Text("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);


		menuScheme.add(menuMapOpen);
		menuScheme.add(menuSchemeOpen);
    menuScheme.addSeparator();
    menuScheme.add(menuClearScheme);
    menuScheme.add(menuLoadSm);
    menuScheme.addSeparator();
    menuScheme.add(menuSchemeSave);
    menuScheme.add(menuSchemeSaveAs);

		menuScheme.add(menuSchemeClose);

		menuView.add(menuViewMap);
		menuView.add(menuViewScheme);
    menuView.addSeparator();
    menuView.add(menuViewMapElProperties);
    menuView.add(menuViewSchElProperties);
    menuView.add(menuViewKIS);
		menuView.add(menuViewParams);
		menuView.add(menuViewMode);
		menuView.add(menuViewSolution);
		menuView.add(menuViewGraph);
    menuView.addSeparator();
    //menuView.add(menuViewOptions);
		menuView.add(menuViewShowall);

    menuOptimize.add(menuOptimizeStart);
		menuOptimize.add(menuOptimizeStop);
    menuOptimize.addSeparator();
    menuOptimize.add(menuOptimizeMode);
    menuOptimizeMode.add(menuOptimizeModeUnidirect);
    menuOptimizeMode.add(menuOptimizeModeBidirect);
    menuOptimize.addSeparator();
    menuOptimize.add(menuOptimizeCriteria);
    menuOptimizeCriteria.add(menuOptimizeCriteriaPrice);
    menuOptimizeCriteriaPrice.add(menuOptimizeCriteriaPriceLoad);
    menuOptimizeCriteriaPrice.add(menuOptimizeCriteriaPriceSave);
    menuOptimizeCriteriaPrice.add(menuOptimizeCriteriaPriceSaveas);
    menuOptimizeCriteriaPrice.addSeparator();
    menuOptimizeCriteriaPrice.add(menuOptimizeCriteriaPriceClose);
		//menuOptimize.add(menuOptimizeAuto);
		//menuOptimize.add(menuOptimizeNext);

		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpStart);
		menuHelp.add(menuHelpCourse);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuScheme);
		this.add(menuView);
		this.add(menuOptimize);
		this.add(menuHelp);
		
		this.addApplicationModelListener(new ApplicationModelListener() {
			
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}

			public void modelChanged(String e[]) {
					ApplicationModel model = OptimizeMenuBar.this.getApplicationModel();

					menuScheme.setVisible(model.isVisible("menuScheme"));
					menuScheme.setEnabled(model.isEnabled("menuScheme"));
					menuMapOpen.setVisible(model.isVisible("menuMapOpen"));
					menuMapOpen.setEnabled(model.isEnabled("menuMapOpen"));
					menuSchemeOpen.setVisible(model.isVisible("menuSchemeOpen"));
					menuSchemeOpen.setEnabled(model.isEnabled("menuSchemeOpen"));
					menuSchemeSave.setVisible(model.isVisible("menuSchemeSave"));
					menuSchemeSave.setEnabled(model.isEnabled("menuSchemeSave"));
			    menuSchemeSaveAs.setVisible(model.isVisible("menuSchemeSave"));
			    menuSchemeSaveAs.setEnabled(model.isEnabled("menuSchemeSave"));
			    menuLoadSm.setVisible(model.isVisible("menuLoadSm"));
			    menuLoadSm.setEnabled(model.isEnabled("menuLoadSm"));
			    menuClearScheme.setVisible(model.isVisible("menuClearScheme"));
			    menuClearScheme.setEnabled(model.isEnabled("menuClearScheme"));
					menuSchemeClose.setVisible(model.isVisible("menuSchemeClose"));
					menuSchemeClose.setEnabled(model.isEnabled("menuSchemeClose"));

					menuView.setVisible(model.isVisible("menuView"));
					menuView.setEnabled(model.isEnabled("menuView"));
					menuViewMap.setVisible(model.isVisible("menuViewMap"));
					menuViewMap.setEnabled(model.isEnabled("menuViewMap"));
					menuViewScheme.setVisible(model.isVisible("menuViewScheme"));
					menuViewScheme.setEnabled(model.isEnabled("menuViewScheme"));
					menuViewMapElProperties.setVisible(model.isVisible("menuViewMapElProperties"));
					menuViewMapElProperties.setEnabled(model.isEnabled("menuViewMapElProperties"));
			    menuViewSchElProperties.setVisible(model.isVisible("menuViewSchElProperties"));
					menuViewSchElProperties.setEnabled(model.isEnabled("menuViewSchElProperties"));
					menuViewGraph.setVisible(model.isVisible("menuViewGraph"));
					menuViewGraph.setEnabled(model.isEnabled("menuViewGraph"));
					menuViewSolution.setVisible(model.isVisible("menuViewSolution"));
					menuViewSolution.setEnabled(model.isEnabled("menuViewSolution"));
					menuViewKIS.setVisible(model.isVisible("menuViewKIS"));
					menuViewKIS.setEnabled(model.isEnabled("menuViewKIS"));
					menuViewMode.setVisible(model.isVisible("menuViewMode"));
					menuViewMode.setEnabled(model.isEnabled("menuViewMode"));
					menuViewParams.setVisible(model.isVisible("menuViewParams"));
					menuViewParams.setEnabled(model.isEnabled("menuViewParams"));
					menuViewShowall.setVisible(model.isVisible("menuViewShowall"));
					menuViewShowall.setEnabled(model.isEnabled("menuViewShowall"));

					menuOptimize.setVisible(model.isVisible("menuOptimize"));
					menuOptimize.setEnabled(model.isEnabled("menuOptimize"));
			    menuOptimizeCriteria.setVisible(model.isVisible("menuOptimizeCriteria"));
					menuOptimizeCriteria.setEnabled(model.isEnabled("menuOptimizeCriteria"));

			    menuOptimizeMode.setVisible(model.isVisible("menuOptimizeMode"));
					menuOptimizeMode.setEnabled(model.isEnabled("menuOptimizeMode"));
			    menuOptimizeModeUnidirect.setVisible(model.isVisible("menuOptimizeModeUnidirect"));
			    menuOptimizeModeUnidirect.setEnabled(model.isEnabled("menuOptimizeModeUnidirect"));
			    menuOptimizeModeBidirect.setVisible(model.isVisible("menuOptimizeModeBidirect"));
					menuOptimizeModeBidirect.setEnabled(model.isEnabled("menuOptimizeModeBidirect"));
			    menuOptimizeModeUnidirect.setSelected(model.isSelected("menuOptimizeModeUnidirect"));
			    menuOptimizeModeBidirect.setSelected(model.isSelected("menuOptimizeModeBidirect"));

			    menuOptimizeCriteriaPrice.setVisible(model.isVisible("menuOptimizeCriteriaPrice"));
					menuOptimizeCriteriaPrice.setEnabled(model.isEnabled("menuOptimizeCriteriaPrice"));
					menuOptimizeStart.setVisible(model.isVisible("menuOptimizeStart"));
					menuOptimizeStart.setEnabled(model.isEnabled("menuOptimizeStart"));
			    menuOptimizeCriteriaPriceLoad.setVisible(model.isVisible("menuOptimizeCriteriaPriceLoad"));
			    menuOptimizeCriteriaPriceLoad.setEnabled(model.isEnabled("menuOptimizeCriteriaPriceLoad"));
			    menuOptimizeCriteriaPriceSave.setVisible(model.isVisible("menuOptimizeCriteriaPriceSave"));
			    menuOptimizeCriteriaPriceSave.setEnabled(model.isEnabled("menuOptimizeCriteriaPriceSave"));
			    menuOptimizeCriteriaPriceSaveas.setVisible(model.isVisible("menuOptimizeCriteriaPriceSaveas"));
			    menuOptimizeCriteriaPriceSaveas.setEnabled(model.isEnabled("menuOptimizeCriteriaPriceSaveas"));
			    menuOptimizeCriteriaPriceClose.setVisible(model.isVisible("menuOptimizeCriteriaPriceClose"));
			    menuOptimizeCriteriaPriceClose.setEnabled(model.isEnabled("menuOptimizeCriteriaPriceClose"));
					menuOptimizeStop.setVisible(model.isVisible("menuOptimizeStop"));
					menuOptimizeStop.setEnabled(model.isEnabled("menuOptimizeStop"));

					menuHelp.setVisible(model.isVisible("menuHelp"));
					menuHelp.setEnabled(model.isEnabled("menuHelp"));
					menuHelpContents.setVisible(model.isVisible("menuHelpContents"));
					menuHelpContents.setEnabled(model.isEnabled("menuHelpContents"));
					menuHelpFind.setVisible(model.isVisible("menuHelpFind"));
					menuHelpFind.setEnabled(model.isEnabled("menuHelpFind"));
					menuHelpTips.setVisible(model.isVisible("menuHelpTips"));
					menuHelpTips.setEnabled(model.isEnabled("menuHelpTips"));
					menuHelpStart.setVisible(model.isVisible("menuHelpStart"));
					menuHelpStart.setEnabled(model.isEnabled("menuHelpStart"));
					menuHelpCourse.setVisible(model.isVisible("menuHelpCourse"));
					menuHelpCourse.setEnabled(model.isEnabled("menuHelpCourse"));
					menuHelpHelp.setVisible(model.isVisible("menuHelpHelp"));
					menuHelpHelp.setEnabled(model.isEnabled("menuHelpHelp"));
					menuHelpSupport.setVisible(model.isVisible("menuHelpSupport"));
					menuHelpSupport.setEnabled(model.isEnabled("menuHelpSupport"));
					menuHelpLicense.setVisible(model.isVisible("menuHelpLicense"));
					menuHelpLicense.setEnabled(model.isEnabled("menuHelpLicense"));
					menuHelpAbout.setVisible(model.isVisible("menuHelpAbout"));
					menuHelpAbout.setEnabled(model.isEnabled("menuHelpAbout"));	
			}
		});
		
	}	
}

