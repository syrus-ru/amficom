package com.syrus.AMFICOM.Client.Optimize;

// Copyright (c) Syrus Systems 2003 Syrus Systems
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

public class OptimizeMenuBar extends JMenuBar	implements ApplicationModelListener
{	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionSave = new JMenuItem();
	JMenuItem menuSessionUndo = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuScheme = new JMenu();
	JMenuItem menuMapOpen = new JMenuItem();
	JMenuItem menuSchemeOpen = new JMenuItem();
	JMenuItem menuSchemeSave = new JMenuItem();
  JMenuItem menuSchemeSaveAs = new JMenuItem();
  JMenuItem menuLoadSm = new JMenuItem();
  JMenuItem menuClearScheme = new JMenuItem();
	JMenuItem menuSchemeClose = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewMap = new JMenuItem();
	JMenuItem menuViewScheme = new JMenuItem();
	JMenuItem menuViewGraph = new JMenuItem();
	JMenuItem menuViewMapElProperties = new JMenuItem();
  JMenuItem menuViewSchElProperties = new JMenuItem();
	JMenuItem menuViewSolution = new JMenuItem();
	JMenuItem menuViewKIS = new JMenuItem();
	JMenuItem menuViewMode = new JMenuItem();
	JMenuItem menuViewParams = new JMenuItem();
	//JMenuItem menuViewOptions = new JMenuItem();
	JMenuItem menuViewShowall = new JMenuItem();

	JMenu menuOptimize = new JMenu();
  JMenu menuOptimizeCriteria = new JMenu();
  JMenu menuOptimizeMode = new JMenu();
  JCheckBoxMenuItem menuOptimizeModeUnidirect = new JCheckBoxMenuItem();// одностороннее тестирование
  JCheckBoxMenuItem menuOptimizeModeBidirect = new JCheckBoxMenuItem(); // двустороннее тестирование
  JMenu menuOptimizeCriteriaPrice = new JMenu();
  JMenuItem menuOptimizeCriteriaPriceLoad = new JMenuItem();
  JMenuItem menuOptimizeCriteriaPriceSave = new JMenuItem();
  JMenuItem menuOptimizeCriteriaPriceSaveas = new JMenuItem();
  JMenuItem menuOptimizeCriteriaPriceClose = new JMenuItem();
	JMenuItem menuOptimizeStart = new JMenuItem();
	JMenuItem menuOptimizeStop = new JMenuItem();
	//JMenuItem menuOptimizeAuto = new JMenuItem();
	//JMenuItem menuOptimizeNext = new JMenuItem();

	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpStart = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpSupport = new JMenuItem();
	JMenuItem menuHelpLicense = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();


	//-------------------------------------------------------------------------------------------------------------
	public OptimizeMenuBar()
	{	super();
		try
		{	jbInit();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	public OptimizeMenuBar(ApplicationModel aModel)
	{	this();
		this.aModel = aModel;
	}
	//-------------------------------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{	OptimizeMenuBar_this_actionAdapter actionAdapter = new OptimizeMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.Text("menuSession"));
		menuSession.setName("menuSession");
//		System.out.println(LangModelOptimize.Text("menuSession"));
		menuSessionNew.setText(LangModel.Text("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.Text("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModel.Text("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.Text("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(

		LangModel.Text("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionSave.setText(LangModel.Text("menuSessionSave"));
		menuSessionSave.setName("menuSessionSave");
		menuSessionSave.addActionListener(actionAdapter);
		menuSessionUndo.setText(LangModel.Text("menuSessionUndo"));
		menuSessionUndo.setName("menuSessionUndo");
		menuSessionUndo.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.Text("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModel.Text("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

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

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
//		menuSession.addSeparator();
//		menuSession.add(menuSessionSave);
//		menuSession.add(menuSessionUndo);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

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

		this.add(menuSession);
		this.add(menuScheme);
		this.add(menuView);
		this.add(menuOptimize);
		this.add(menuHelp);
	}
	//--------------------------------------------------------------------------------------------------------------
	public void setModel(ApplicationModel a)
	{	aModel = a;
	}
	//--------------------------------------------------------------------------------------------------------------
	public ApplicationModel getModel()
	{	return aModel;
	}
	//--------------------------------------------------------------------------------------------------------------
	public void modelChanged(String e[])
	{	int count = e.length;
		int i;
		System.out.println("changed model in menu bar");

    menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));
		menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
		menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));
		menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
		menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));
		menuSessionOptions.setVisible(aModel.isVisible("menuSessionOptions"));
		menuSessionOptions.setEnabled(aModel.isEnabled("menuSessionOptions"));
		menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
		menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));
		menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
		menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));
		menuSessionSave.setVisible(aModel.isVisible("menuSessionSave"));
		menuSessionSave.setEnabled(aModel.isEnabled("menuSessionSave"));
		menuSessionUndo.setVisible(aModel.isVisible("menuSessionUndo"));
		menuSessionUndo.setEnabled(aModel.isEnabled("menuSessionUndo"));
		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));
		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuScheme.setVisible(aModel.isVisible("menuScheme"));
		menuScheme.setEnabled(aModel.isEnabled("menuScheme"));
		menuMapOpen.setVisible(aModel.isVisible("menuMapOpen"));
		menuMapOpen.setEnabled(aModel.isEnabled("menuMapOpen"));
		menuSchemeOpen.setVisible(aModel.isVisible("menuSchemeOpen"));
		menuSchemeOpen.setEnabled(aModel.isEnabled("menuSchemeOpen"));
		menuSchemeSave.setVisible(aModel.isVisible("menuSchemeSave"));
		menuSchemeSave.setEnabled(aModel.isEnabled("menuSchemeSave"));
    menuSchemeSaveAs.setVisible(aModel.isVisible("menuSchemeSave"));
    menuSchemeSaveAs.setEnabled(aModel.isEnabled("menuSchemeSave"));
    menuLoadSm.setVisible(aModel.isVisible("menuLoadSm"));
    menuLoadSm.setEnabled(aModel.isEnabled("menuLoadSm"));
    menuClearScheme.setVisible(aModel.isVisible("menuClearScheme"));
    menuClearScheme.setEnabled(aModel.isEnabled("menuClearScheme"));
		menuSchemeClose.setVisible(aModel.isVisible("menuSchemeClose"));
		menuSchemeClose.setEnabled(aModel.isEnabled("menuSchemeClose"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));
		menuViewMap.setVisible(aModel.isVisible("menuViewMap"));
		menuViewMap.setEnabled(aModel.isEnabled("menuViewMap"));
		menuViewScheme.setVisible(aModel.isVisible("menuViewScheme"));
		menuViewScheme.setEnabled(aModel.isEnabled("menuViewScheme"));
		menuViewMapElProperties.setVisible(aModel.isVisible("menuViewMapElProperties"));
		menuViewMapElProperties.setEnabled(aModel.isEnabled("menuViewMapElProperties"));
    menuViewSchElProperties.setVisible(aModel.isVisible("menuViewSchElProperties"));
		menuViewSchElProperties.setEnabled(aModel.isEnabled("menuViewSchElProperties"));
		menuViewGraph.setVisible(aModel.isVisible("menuViewGraph"));
		menuViewGraph.setEnabled(aModel.isEnabled("menuViewGraph"));
		menuViewSolution.setVisible(aModel.isVisible("menuViewSolution"));
		menuViewSolution.setEnabled(aModel.isEnabled("menuViewSolution"));
		menuViewKIS.setVisible(aModel.isVisible("menuViewKIS"));
		menuViewKIS.setEnabled(aModel.isEnabled("menuViewKIS"));
		menuViewMode.setVisible(aModel.isVisible("menuViewMode"));
		menuViewMode.setEnabled(aModel.isEnabled("menuViewMode"));
		menuViewParams.setVisible(aModel.isVisible("menuViewParams"));
		menuViewParams.setEnabled(aModel.isEnabled("menuViewParams"));
		menuViewShowall.setVisible(aModel.isVisible("menuViewShowall"));
		menuViewShowall.setEnabled(aModel.isEnabled("menuViewShowall"));

		menuOptimize.setVisible(aModel.isVisible("menuOptimize"));
		menuOptimize.setEnabled(aModel.isEnabled("menuOptimize"));
    menuOptimizeCriteria.setVisible(aModel.isVisible("menuOptimizeCriteria"));
		menuOptimizeCriteria.setEnabled(aModel.isEnabled("menuOptimizeCriteria"));

    menuOptimizeMode.setVisible(aModel.isVisible("menuOptimizeMode"));
		menuOptimizeMode.setEnabled(aModel.isEnabled("menuOptimizeMode"));
    menuOptimizeModeUnidirect.setVisible(aModel.isVisible("menuOptimizeModeUnidirect"));
    menuOptimizeModeUnidirect.setEnabled(aModel.isEnabled("menuOptimizeModeUnidirect"));
    menuOptimizeModeBidirect.setVisible(aModel.isVisible("menuOptimizeModeBidirect"));
		menuOptimizeModeBidirect.setEnabled(aModel.isEnabled("menuOptimizeModeBidirect"));
    menuOptimizeModeUnidirect.setSelected(aModel.isSelected("menuOptimizeModeUnidirect"));
    menuOptimizeModeBidirect.setSelected(aModel.isSelected("menuOptimizeModeBidirect"));

    menuOptimizeCriteriaPrice.setVisible(aModel.isVisible("menuOptimizeCriteriaPrice"));
		menuOptimizeCriteriaPrice.setEnabled(aModel.isEnabled("menuOptimizeCriteriaPrice"));
		menuOptimizeStart.setVisible(aModel.isVisible("menuOptimizeStart"));
		menuOptimizeStart.setEnabled(aModel.isEnabled("menuOptimizeStart"));
    menuOptimizeCriteriaPriceLoad.setVisible(aModel.isVisible("menuOptimizeCriteriaPriceLoad"));
    menuOptimizeCriteriaPriceLoad.setEnabled(aModel.isEnabled("menuOptimizeCriteriaPriceLoad"));
    menuOptimizeCriteriaPriceSave.setVisible(aModel.isVisible("menuOptimizeCriteriaPriceSave"));
    menuOptimizeCriteriaPriceSave.setEnabled(aModel.isEnabled("menuOptimizeCriteriaPriceSave"));
    menuOptimizeCriteriaPriceSaveas.setVisible(aModel.isVisible("menuOptimizeCriteriaPriceSaveas"));
    menuOptimizeCriteriaPriceSaveas.setEnabled(aModel.isEnabled("menuOptimizeCriteriaPriceSaveas"));
    menuOptimizeCriteriaPriceClose.setVisible(aModel.isVisible("menuOptimizeCriteriaPriceClose"));
    menuOptimizeCriteriaPriceClose.setEnabled(aModel.isEnabled("menuOptimizeCriteriaPriceClose"));
		menuOptimizeStop.setVisible(aModel.isVisible("menuOptimizeStop"));
		menuOptimizeStop.setEnabled(aModel.isEnabled("menuOptimizeStop"));

		menuHelp.setVisible(aModel.isVisible("menuHelp"));
		menuHelp.setEnabled(aModel.isEnabled("menuHelp"));
		menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
		menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));
		menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
		menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));
		menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
		menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));
		menuHelpStart.setVisible(aModel.isVisible("menuHelpStart"));
		menuHelpStart.setEnabled(aModel.isEnabled("menuHelpStart"));
		menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
		menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));
		menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
		menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));
		menuHelpSupport.setVisible(aModel.isVisible("menuHelpSupport"));
		menuHelpSupport.setEnabled(aModel.isEnabled("menuHelpSupport"));
		menuHelpLicense.setVisible(aModel.isVisible("menuHelpLicense"));
		menuHelpLicense.setEnabled(aModel.isEnabled("menuHelpLicense"));
		menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
		menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));
	}
	//-------------------------------------------------------------------------------------------------------------
	public void this_actionPerformed(ActionEvent e)
	{	if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}
}

//==================================================================================================================
class OptimizeMenuBar_this_actionAdapter	implements java.awt.event.ActionListener
{
	OptimizeMenuBar adaptee;
	//-------------------------------------------------------------------------------------------------------------
	OptimizeMenuBar_this_actionAdapter(OptimizeMenuBar adaptee)
	{	this.adaptee = adaptee;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{	adaptee.this_actionPerformed(e);
	}
}

