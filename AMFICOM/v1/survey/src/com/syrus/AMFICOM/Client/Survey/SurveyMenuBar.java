package com.syrus.AMFICOM.Client.Survey;

// Copyright (c) Syrus Systems 2000 Syrus Systems
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class SurveyMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

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

	JMenu menuView = new JMenu();
	JMenuItem menuViewNavigator = new JMenuItem();
	JMenuItem menuViewMessages = new JMenuItem();
	JMenuItem menuViewToolbar = new JMenuItem();
	JMenuItem menuViewRefresh = new JMenuItem();
	JMenuItem menuViewCatalogue = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();

	JMenu menuEvaluate = new JMenu();
	JMenuItem menuEvaluateRequest = new JMenuItem();
	JMenuItem menuEvaluateScheduler = new JMenuItem();
	JMenuItem menuEvaluateArchive = new JMenuItem();
	JMenu menuEvaluateTrack = new JMenu();
	JMenuItem menuEvaluateTrackRequest = new JMenuItem();
	JMenuItem menuEvaluateTrackTask = new JMenuItem();
	JMenuItem menuEvaluateResult = new JMenuItem();
	JMenuItem menuEvaluateAnalize = new JMenuItem();
	JMenuItem menuEvaluateAnalizeExt = new JMenuItem();
	JMenuItem menuEvaluateNorms = new JMenuItem();
	JMenuItem menuEvaluateModeling = new JMenuItem();
	JMenuItem menuEvaluatePrognosis = new JMenuItem();
	JMenuItem menuEvaluateViewAll = new JMenuItem();

	JMenu menuVisualize = new JMenu();
	JMenuItem menuVisualizeNetGIS = new JMenuItem();
	JMenuItem menuVisualizeISMGIS = new JMenuItem();
	JMenuItem menuVisualizeMapEdit = new JMenuItem();
	JMenuItem menuVisualizeMapClose = new JMenuItem();
	JMenuItem menuVisualizeNet = new JMenuItem();
	JMenuItem menuVisualizeISM = new JMenuItem();
	JMenuItem menuVisualizeSchemeEdit = new JMenuItem();
	JMenuItem menuVisualizeSchemeClose = new JMenuItem();

	JMenuItem menuViewMapSetup = new JMenuItem();

	JMenu menuMaintain = new JMenu();
	JMenuItem menuMaintainAlarm = new JMenuItem();
	JMenuItem menuMaintainAlert = new JMenuItem();
	JMenuItem menuMaintainCall = new JMenuItem();
	JMenuItem menuMaintainEvent = new JMenuItem();

	JMenu menuReport = new JMenu();
	JMenuItem menuReportTable = new JMenuItem();
	JMenuItem menuReportHistogramm = new JMenuItem();
	JMenuItem menuReportGraph = new JMenuItem();
	JMenuItem menuReportComplex = new JMenuItem();
	JMenuItem menuReportReport = new JMenuItem();

	JMenu menuTools = new JMenu();
	JMenu menuToolsSort = new JMenu();
	JMenuItem menuToolsSortNew = new JMenuItem();
	JMenuItem menuToolsSortSave = new JMenuItem();
	JMenuItem menuToolsSortOpen = new JMenuItem();
	JMenuItem menuToolsSortList = new JMenuItem();
	JMenu menuToolsFilter = new JMenu();
	JMenuItem menuToolsFilterNew = new JMenuItem();
	JMenuItem menuToolsFilterSave = new JMenuItem();
	JMenuItem menuToolsFilterOpen = new JMenuItem();
	JMenuItem menuToolsFilterList = new JMenuItem();
	JMenu menuToolsFind = new JMenu();
	JMenuItem menuToolsFindFast = new JMenuItem();
	JMenuItem menuToolsFindWord = new JMenuItem();
	JMenuItem menuToolsFindField = new JMenuItem();
	JMenuItem menuToolsFindNext = new JMenuItem();
	JMenuItem menuToolsFindQuery = new JMenuItem();
	JMenu menuToolsBookmark = new JMenu();
	JMenuItem menuToolsBookmarkSet = new JMenuItem();
	JMenuItem menuToolsBookmarkGoto = new JMenuItem();
	JMenuItem menuToolsBookmarkList = new JMenuItem();
	JMenuItem menuToolsBookmarkRemove = new JMenuItem();
	JMenuItem menuToolsBookmarkEdit = new JMenuItem();
	JMenuItem menuToolsDictionary = new JMenuItem();
	JMenuItem menuToolsLanguage = new JMenuItem();
	JMenuItem menuToolsLock = new JMenuItem();
	JMenu menuToolsStyle = new JMenu();
	JMenuItem menuToolsStyleText = new JMenuItem();
	JMenuItem menuToolsStyleGraph = new JMenuItem();
	JMenuItem menuToolsStyleLine = new JMenuItem();
	JMenuItem menuToolsStyleTable = new JMenuItem();
	JMenuItem menuToolsStyleScheme = new JMenuItem();
	JMenuItem menuToolsStyleMap = new JMenuItem();
	JMenuItem menuToolsStyleSound = new JMenuItem();
	JMenuItem menuToolsStyleColor = new JMenuItem();
	JMenuItem menuToolsStyleLink = new JMenuItem();
	JMenuItem menuToolsOptions = new JMenuItem();

	JMenu menuWindow = new JMenu();
	JMenuItem menuWindowClose = new JMenuItem();
	JMenuItem menuWindowCloseAll = new JMenuItem();
	JMenuItem menuWindowTileHorizontal = new JMenuItem();
	JMenuItem menuWindowTileVertical = new JMenuItem();
	JMenuItem menuWindowCascade = new JMenuItem();
	JMenuItem menuWindowArrange = new JMenuItem();
	JMenuItem menuWindowArrangeIcons = new JMenuItem();
	JMenuItem menuWindowMinimizeAll = new JMenuItem();
	JMenuItem menuWindowRestoreAll = new JMenuItem();
	JMenuItem menuWindowList = new JMenuItem();

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

	public SurveyMenuBar()
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

	public SurveyMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		SurveyMenuBar_this_actionAdapter actionAdapter =
				new SurveyMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.Text("menuSession"));
		menuSession.setName("menuSession");
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
		menuSessionChangePassword.setText(LangModel.Text("menuSessionChangePassword"));
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

		menuView.setText(LangModelSurvey.getString("View"));
		menuView.setName("menuView");
		menuViewNavigator.setText(LangModelSurvey.getString("Object_navigator"));
		menuViewNavigator.setName("menuViewNavigator");
		menuViewNavigator.addActionListener(actionAdapter);
		menuViewMessages.setText(LangModelSurvey.getString("Diagnostic_message_window"));
		menuViewMessages.setName("menuViewMessages");
		menuViewMessages.addActionListener(actionAdapter);
		menuViewToolbar.setText(LangModelSurvey.getString("Toolbar"));
		menuViewToolbar.setName("menuViewToolbar");
		menuViewToolbar.addActionListener(actionAdapter);
		menuViewRefresh.setText(LangModelSurvey.getString("Refresh_info"));
		menuViewRefresh.setName("menuViewRefresh");
		menuViewRefresh.addActionListener(actionAdapter);
		menuViewCatalogue.setText(LangModelSurvey.getString("Operation_catalogue"));
		menuViewCatalogue.setName("menuViewCatalogue");
		menuViewCatalogue.addActionListener(actionAdapter);
		menuViewAll.setText(LangModelSurvey.getString("Allocate_windows"));
		menuViewAll.setName("menuViewAll");
		menuViewAll.addActionListener(actionAdapter);

		menuEvaluate.setText(LangModelSurvey.getString("Operation"));
		menuEvaluate.setName("menuEvaluate");
		menuEvaluateRequest.setText(LangModelSurvey.getString("Problem_statement"));
		menuEvaluateRequest.setName("menuEvaluateRequest");
		menuEvaluateRequest.addActionListener(actionAdapter);
		menuEvaluateScheduler.setText(LangModelSurvey.getString("Scheduling"));
		menuEvaluateScheduler.setName("menuEvaluateScheduler");
		menuEvaluateScheduler.addActionListener(actionAdapter);
		menuEvaluateArchive.setText(LangModelSurvey.getString("Measurement_archive"));
		menuEvaluateArchive.setName("menuEvaluateArchive");
		menuEvaluateArchive.addActionListener(actionAdapter);
		menuEvaluateTrack.setText(LangModelSurvey.getString("Execution_control"));
		menuEvaluateTrack.setName("menuEvaluateTrack");
		menuEvaluateTrackRequest.setText(LangModelSurvey.getString("Requests"));
		menuEvaluateTrackRequest.setName("menuEvaluateTrackRequest");
		menuEvaluateTrackRequest.addActionListener(actionAdapter);
		menuEvaluateTrackTask.setText(LangModelSurvey.getString("Tasks"));
		menuEvaluateTrackTask.setName("menuEvaluateTrackTask");
		menuEvaluateTrackTask.addActionListener(actionAdapter);
		menuEvaluateResult.setText(LangModelSurvey.getString("Results_overview"));
		menuEvaluateResult.setName("menuEvaluateResult");
		menuEvaluateResult.addActionListener(actionAdapter);
		menuEvaluateAnalize.setText(LangModelSurvey.getString("Evaluation_analysis"));
		menuEvaluateAnalize.setName("menuEvaluateAnalize");
		menuEvaluateAnalize.addActionListener(actionAdapter);
		menuEvaluateAnalizeExt.setText(LangModelSurvey.getString("Researching"));
		menuEvaluateAnalizeExt.setName("menuEvaluateAnalizeExt");
		menuEvaluateAnalizeExt.addActionListener(actionAdapter);
		menuEvaluateNorms.setText(LangModelSurvey.getString("Evaluation"));
		menuEvaluateNorms.setName("menuEvaluateNorms");
		menuEvaluateNorms.addActionListener(actionAdapter);
		menuEvaluateModeling.setText(LangModelSurvey.getString("Modeling"));
		menuEvaluateModeling.setName("menuEvaluateModeling");
		menuEvaluateModeling.addActionListener(actionAdapter);
		menuEvaluatePrognosis.setText(LangModelSurvey.getString("Forecasting"));
		menuEvaluatePrognosis.setName("menuEvaluatePrognosis");
		menuEvaluatePrognosis.addActionListener(actionAdapter);
		menuEvaluateViewAll.setText(LangModelSurvey.getString("View_all"));
		menuEvaluateViewAll.setName("menuEvaluateViewAll");
		menuEvaluateViewAll.addActionListener(actionAdapter);

		menuVisualize.setText(LangModelSurvey.getString("View"));
		menuVisualize.setName("menuVisualize");
		menuVisualizeNet.setText(LangModelSurvey.getString("Net_scheme"));
		menuVisualizeNet.setName("menuVisualizeNet");
		menuVisualizeNet.addActionListener(actionAdapter);
		menuVisualizeNetGIS.setText(LangModelSurvey.getString("Topological_scheme_of_net"));
		menuVisualizeNetGIS.setName("menuVisualizeNetGIS");
		menuVisualizeNetGIS.addActionListener(actionAdapter);
		menuVisualizeISM.setText(LangModelSurvey.getString("Monitoring_devices_scheme"));
		menuVisualizeISM.setName("menuVisualizeISM");
		menuVisualizeISM.addActionListener(actionAdapter);
		menuVisualizeISMGIS.setText(LangModelSurvey.getString("Topological_monitoring_devices_scheme"));
		menuVisualizeISMGIS.setName("menuVisualizeISMGIS");
		menuVisualizeISMGIS.addActionListener(actionAdapter);
		menuVisualizeMapEdit.setText(LangModelSurvey.getString("Topology_editor"));
		menuVisualizeMapEdit.setName("menuVisualizeMapEdit");
		menuVisualizeMapEdit.addActionListener(actionAdapter);
		menuVisualizeMapClose.setText(LangModelSurvey.getString("Close_topological_scheme"));
		menuVisualizeMapClose.setName("menuVisualizeMapClose");
		menuVisualizeMapClose.addActionListener(actionAdapter);
		menuVisualizeSchemeEdit.setText(LangModelSurvey.getString("Scheme_editor"));
		menuVisualizeSchemeEdit.setName("menuVisualizeSchemeEdit");
		menuVisualizeSchemeEdit.addActionListener(actionAdapter);
		menuVisualizeSchemeClose.setText(LangModelSurvey.getString("Close_scheme"));
		menuVisualizeSchemeClose.setName("menuVisualizeSchemeClose");
		menuVisualizeSchemeClose.addActionListener(actionAdapter);

		menuViewMapSetup.setText(LangModelSurvey.getString("Map_operations"));
		menuViewMapSetup.setName("menuViewMapSetup");
		menuViewMapSetup.addActionListener(actionAdapter);

		menuMaintain.setText(LangModelSurvey.getString("Maintain"));
		menuMaintain.setName("menuMaintain");
		menuMaintainAlarm.setText(LangModelSurvey.getString("Alarm_signals"));
		menuMaintainAlarm.setName("menuMaintainAlarm");
		menuMaintainAlarm.addActionListener(actionAdapter);
		menuMaintainAlert.setText(LangModelSurvey.getString("Alert_messages"));
		menuMaintainAlert.setName("menuMaintainAlert");
		menuMaintainAlert.addActionListener(actionAdapter);
		menuMaintainCall.setText(LangModelSurvey.getString("Claims"));
		menuMaintainCall.setName("menuMaintainCall");
		menuMaintainCall.addActionListener(actionAdapter);
		menuMaintainEvent.setText(LangModelSurvey.getString("Events"));
		menuMaintainEvent.setName("menuMaintainEvent");
		menuMaintainEvent.addActionListener(actionAdapter);

		menuReport.setText(LangModelSurvey.getString("Report"));
		menuReport.setName("menuReport");
		menuReportTable.setText(LangModelSurvey.getString("Tabulared_form"));
		menuReportTable.setName("menuReportTable");
		menuReportTable.addActionListener(actionAdapter);
		menuReportHistogramm.setText(LangModelSurvey.getString("Histogramm"));
		menuReportHistogramm.setName("menuReportHistogramm");
		menuReportHistogramm.addActionListener(actionAdapter);
		menuReportGraph.setText(LangModelSurvey.getString("Graph"));
		menuReportGraph.setName("menuReportGraph");
		menuReportGraph.addActionListener(actionAdapter);
		menuReportComplex.setText(LangModelSurvey.getString("Complex_report"));
		menuReportComplex.setName("menuReportComplex");
		menuReportComplex.addActionListener(actionAdapter);
		menuReportReport.setText(LangModelSurvey.getString("Reports"));
		menuReportReport.setName("menuReportReport");
		menuReportReport.addActionListener(actionAdapter);

		menuTools.setText(LangModelSurvey.getString("Tools"));
		menuTools.setName("menuTools");
		menuToolsSort.setText(LangModelSurvey.getString("Sorting"));
		menuToolsSort.setName("menuToolsSort");
		menuToolsSortNew.setText(LangModelSurvey.getString("New_sorting"));
		menuToolsSortNew.setName("menuToolsSortNew");
		menuToolsSortNew.addActionListener(actionAdapter);
		menuToolsSortSave.setText(LangModelSurvey.getString("Save_sorting"));
		menuToolsSortSave.setName("menuToolsSortSave");
		menuToolsSortSave.addActionListener(actionAdapter);
		menuToolsSortOpen.setText(LangModelSurvey.getString("Open_sorting"));
		menuToolsSortOpen.setName("menuToolsSortOpen");
		menuToolsSortOpen.addActionListener(actionAdapter);
		menuToolsSortList.setText(LangModelSurvey.getString("Sorting_list"));
		menuToolsSortList.setName("menuToolsSortList");
		menuToolsSortList.addActionListener(actionAdapter);
		menuToolsFilter.setText(LangModelSurvey.getString("Filter"));
		menuToolsFilter.setName("menuToolsFilter");
		menuToolsFilterNew.setText(LangModelSurvey.getString("New_filter"));
		menuToolsFilterNew.setName("menuToolsFilterNew");
		menuToolsFilterNew.addActionListener(actionAdapter);
		menuToolsFilterSave.setText(LangModelSurvey.getString("Save_filter"));
		menuToolsFilterSave.setName("menuToolsFilterSave");
		menuToolsFilterSave.addActionListener(actionAdapter);
		menuToolsFilterOpen.setText(LangModelSurvey.getString("Open_filter"));
		menuToolsFilterOpen.setName("menuToolsFilterOpen");
		menuToolsFilterOpen.addActionListener(actionAdapter);
		menuToolsFilterList.setText(LangModelSurvey.getString("Filter_list"));
		menuToolsFilterList.setName("menuToolsFilterList");
		menuToolsFilterList.addActionListener(actionAdapter);
		menuToolsFind.setText(LangModelSurvey.getString("Search"));
		menuToolsFind.setName("menuToolsFind");
		menuToolsFindFast.setText(LangModelSurvey.getString("Fast_search"));
		menuToolsFindFast.setName("menuToolsFindFast");
		menuToolsFindFast.addActionListener(actionAdapter);
		menuToolsFindWord.setText(LangModelSurvey.getString("Search_by_word"));
		menuToolsFindWord.setName("menuToolsFindWord");
		menuToolsFindWord.addActionListener(actionAdapter);
		menuToolsFindField.setText(LangModelSurvey.getString("Search_by_fields"));
		menuToolsFindField.setName("menuToolsFindField");
		menuToolsFindField.addActionListener(actionAdapter);
		menuToolsFindNext.setText(LangModelSurvey.getString("Find_next"));
		menuToolsFindNext.setName("menuToolsFindNext");
		menuToolsFindNext.addActionListener(actionAdapter);
		menuToolsFindQuery.setText(LangModelSurvey.getString("Complex_search"));
		menuToolsFindQuery.setName("menuToolsFindQuery");
		menuToolsFindQuery.addActionListener(actionAdapter);
		menuToolsBookmark.setText(LangModelSurvey.getString("Bookmarks"));
		menuToolsBookmark.setName("menuToolsBookmark");
		menuToolsBookmarkSet.setText(LangModelSurvey.getString("Set_bookmark"));
		menuToolsBookmarkSet.setName("menuToolsBookmarkSet");
		menuToolsBookmarkSet.addActionListener(actionAdapter);
		menuToolsBookmarkGoto.setText(LangModelSurvey.getString("Goto_bookmark"));
		menuToolsBookmarkGoto.setName("menuToolsBookmarkGoto");
		menuToolsBookmarkGoto.addActionListener(actionAdapter);
		menuToolsBookmarkList.setText(LangModelSurvey.getString("Bookmark_list"));
		menuToolsBookmarkList.setName("menuToolsBookmarkList");
		menuToolsBookmarkList.addActionListener(actionAdapter);
		menuToolsBookmarkRemove.setText(LangModelSurvey.getString("Delete_bookmark"));
		menuToolsBookmarkRemove.setName("menuToolsBookmarkRemove");
		menuToolsBookmarkRemove.addActionListener(actionAdapter);
		menuToolsBookmarkEdit.setText(LangModelSurvey.getString("Edit_bookmark"));
		menuToolsBookmarkEdit.setName("menuToolsBookmarkEdit");
		menuToolsBookmarkEdit.addActionListener(actionAdapter);
		menuToolsDictionary.setText(LangModelSurvey.getString("Dictionary"));
		menuToolsDictionary.setName("menuToolsDictionary");
		menuToolsDictionary.addActionListener(actionAdapter);
		menuToolsLanguage.setText(LangModelSurvey.getString("Language"));
		menuToolsLanguage.setName("menuToolsLanguage");
		menuToolsLanguage.addActionListener(actionAdapter);
		menuToolsLock.setText(LangModelSurvey.getString("Lock_application"));
		menuToolsLock.setName("menuToolsLock");
		menuToolsLock.addActionListener(actionAdapter);
		menuToolsStyle.setText(LangModelSurvey.getString("Style"));
		menuToolsStyle.setName("menuToolsStyle");
		menuToolsStyleText.setText(LangModelSurvey.getString("Style"));
		menuToolsStyleText.setName("menuToolsStyleText");
		menuToolsStyleText.addActionListener(actionAdapter);
		menuToolsStyleGraph.setText(LangModelSurvey.getString("Graphics"));
		menuToolsStyleGraph.setName("menuToolsStyleGraph");
		menuToolsStyleGraph.addActionListener(actionAdapter);
		menuToolsStyleLine.setText(LangModelSurvey.getString("Line"));
		menuToolsStyleLine.setName("menuToolsStyleLine");
		menuToolsStyleLine.addActionListener(actionAdapter);
		menuToolsStyleTable.setText(LangModelSurvey.getString("Table"));
		menuToolsStyleTable.setName("menuToolsStyleTable");
		menuToolsStyleTable.addActionListener(actionAdapter);
		menuToolsStyleScheme.setText(LangModelSurvey.getString("Scheme"));
		menuToolsStyleScheme.setName("menuToolsStyleScheme");
		menuToolsStyleScheme.addActionListener(actionAdapter);
		menuToolsStyleMap.setText(LangModelSurvey.getString("Map"));
		menuToolsStyleMap.setName("menuToolsStyleMap");
		menuToolsStyleMap.addActionListener(actionAdapter);
		menuToolsStyleSound.setText(LangModelSurvey.getString("Sound"));
		menuToolsStyleSound.setName("menuToolsStyleSound");
		menuToolsStyleSound.addActionListener(actionAdapter);
		menuToolsStyleColor.setText(LangModelSurvey.getString("Color"));
		menuToolsStyleColor.setName("menuToolsStyleColor");
		menuToolsStyleColor.addActionListener(actionAdapter);
		menuToolsStyleLink.setText(LangModelSurvey.getString("Link"));
		menuToolsStyleLink.setName("menuToolsStyleLink");
		menuToolsStyleLink.addActionListener(actionAdapter);
		menuToolsOptions.setText(LangModelSurvey.getString("Options"));
		menuToolsOptions.setName("menuToolsOptions");
		menuToolsOptions.addActionListener(actionAdapter);

		menuWindow.setText(LangModelSurvey.getString("Window"));
		menuWindow.setName("menuWindow");
		menuWindowClose.setText(LangModelSurvey.getString("Close"));
		menuWindowClose.setName("menuWindowClose");
		menuWindowClose.addActionListener(actionAdapter);
		menuWindowCloseAll.setText(LangModelSurvey.getString("CloseAll"));
		menuWindowCloseAll.setName("menuWindowCloseAll");
		menuWindowCloseAll.addActionListener(actionAdapter);
		menuWindowTileHorizontal.setText(LangModelSurvey.getString("TileHorizontal"));
		menuWindowTileHorizontal.setName("menuWindowTileHorizontal");
		menuWindowTileHorizontal.addActionListener(actionAdapter);
		menuWindowTileVertical.setText(LangModelSurvey.getString("TileVertical"));
		menuWindowTileVertical.setName("menuWindowTileVertical");
		menuWindowTileVertical.addActionListener(actionAdapter);
		menuWindowCascade.setText(LangModelSurvey.getString("Cascade"));
		menuWindowCascade.setName("menuWindowCascade");
		menuWindowCascade.addActionListener(actionAdapter);
		menuWindowArrange.setText(LangModelSurvey.getString("Arrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowArrangeIcons.setText(LangModelSurvey.getString("ArrangeIcons"));
		menuWindowArrangeIcons.setName("menuWindowArrangeIcons");
		menuWindowArrangeIcons.addActionListener(actionAdapter);
		menuWindowMinimizeAll.setText(LangModelSurvey.getString("MinimizeAll"));
		menuWindowMinimizeAll.setName("menuWindowMinimizeAll");
		menuWindowMinimizeAll.addActionListener(actionAdapter);
		menuWindowRestoreAll.setText(LangModelSurvey.getString("RestoreAll"));
		menuWindowRestoreAll.setName("menuWindowRestoreAll");
		menuWindowRestoreAll.addActionListener(actionAdapter);
		menuWindowList.setText(LangModelSurvey.getString("List"));
		menuWindowList.setName("menuWindowList");
		menuWindowList.addActionListener(actionAdapter);

		menuHelp.setText(LangModel.Text("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModel.Text("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModel.Text("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModel.Text("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModel.Text("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModel.Text("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModel.Text("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModel.Text("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModel.Text("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModel.Text("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.add(menuSessionSave);
		menuSession.add(menuSessionUndo);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewNavigator);
		menuView.add(menuViewMessages);
		menuView.add(menuViewToolbar);
		menuView.add(menuViewCatalogue);
		menuView.addSeparator();
		menuView.add(menuViewRefresh);

		menuEvaluate.add(menuEvaluateRequest);
		menuEvaluate.add(menuEvaluateScheduler);
		menuEvaluate.add(menuEvaluateTrack);
		menuEvaluateTrack.add(menuEvaluateTrackRequest);
		menuEvaluateTrack.add(menuEvaluateTrackTask);
		//		menuEvaluate.add(menuEvaluateModeling);
		menuEvaluate.add(menuEvaluateAnalize);
		menuEvaluate.add(menuEvaluateAnalizeExt);
		menuEvaluate.add(menuEvaluateNorms);
		menuEvaluate.add(menuEvaluatePrognosis);

		menuVisualize.add(menuVisualizeNetGIS);
		menuVisualize.add(menuVisualizeMapEdit);
		menuVisualize.add(menuVisualizeMapClose);
		menuVisualize.addSeparator();
		menuVisualize.add(menuVisualizeNet);
		menuVisualize.add(menuVisualizeSchemeEdit);
		menuVisualize.add(menuVisualizeSchemeClose);
		//menuVisualize.addSeparator();
		//menuVisualize.add(menuVisualizeISM);
		//menuVisualize.add(menuVisualizeISMGIS);
		//menuVisualize.add(menuEvaluateViewAll);
		menuVisualize.addSeparator();
		menuVisualize.add(menuEvaluateArchive);
		menuVisualize.add(menuEvaluateResult);
		menuVisualize.add(menuMaintainAlarm);
		menuVisualize.add(menuViewMapSetup);
		menuVisualize.addSeparator();
		menuVisualize.add(menuViewAll);

		menuMaintain.add(menuMaintainAlert);
		menuMaintain.add(menuMaintainCall);
		menuMaintain.add(menuMaintainEvent);

		menuReport.add(menuReportTable);
		menuReport.add(menuReportHistogramm);
		menuReport.add(menuReportGraph);
		menuReport.add(menuReportComplex);
		menuReport.add(menuReportReport);

		menuTools.add(menuToolsSort);
		menuTools.add(menuToolsFilter);
		menuTools.addSeparator();
		menuTools.add(menuToolsFind);
		menuTools.add(menuToolsBookmark);
		menuTools.addSeparator();
		menuTools.add(menuToolsDictionary);
		menuTools.add(menuToolsLanguage);
		menuTools.addSeparator();
		menuTools.add(menuToolsLock);
		menuTools.addSeparator();
		menuTools.add(menuToolsStyle);
		menuTools.add(menuToolsOptions);
		menuToolsSort.add(menuToolsSortNew);
		menuToolsSort.add(menuToolsSortSave);
		menuToolsSort.add(menuToolsSortOpen);
		menuToolsSort.add(menuToolsSortList);
		menuToolsSort.addSeparator();
		menuToolsFilter.add(menuToolsFilterNew);
		menuToolsFilter.add(menuToolsFilterSave);
		menuToolsFilter.add(menuToolsFilterOpen);
		menuToolsFilter.add(menuToolsFilterList);
		menuToolsFilter.addSeparator();
		menuToolsFind.add(menuToolsFindFast);
		menuToolsFind.add(menuToolsFindWord);
		menuToolsFind.add(menuToolsFindField);
		menuToolsFind.add(menuToolsFindNext);
		menuToolsFind.addSeparator();
		menuToolsFind.add(menuToolsFindQuery);
		menuToolsBookmark.add(menuToolsBookmarkSet);
		menuToolsBookmark.add(menuToolsBookmarkRemove);
		menuToolsBookmark.add(menuToolsBookmarkGoto);
		menuToolsBookmark.add(menuToolsBookmarkList);
		menuToolsBookmark.add(menuToolsBookmarkEdit);
		menuToolsBookmark.addSeparator();
		menuToolsStyle.add(menuToolsStyleText);
		menuToolsStyle.add(menuToolsStyleGraph);
		menuToolsStyle.add(menuToolsStyleLine);
		menuToolsStyle.add(menuToolsStyleTable);
		menuToolsStyle.add(menuToolsStyleScheme);
		menuToolsStyle.add(menuToolsStyleMap);
		menuToolsStyle.add(menuToolsStyleSound);
		menuToolsStyle.add(menuToolsStyleColor);
		menuToolsStyle.add(menuToolsStyleLink);

		menuWindow.add(menuWindowClose);
		menuWindow.add(menuWindowCloseAll);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowTileHorizontal);
		menuWindow.add(menuWindowTileVertical);
		menuWindow.add(menuWindowCascade);
		menuWindow.add(menuWindowArrange);
		menuWindow.add(menuWindowArrangeIcons);
		menuWindow.add(menuWindowMinimizeAll);
		menuWindow.add(menuWindowRestoreAll);
		menuWindow.addSeparator();
		menuWindow.addSeparator();
		menuWindow.add(menuWindowList);

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
		this.add(menuView);
		this.add(menuEvaluate);
		this.add(menuVisualize);
		this.add(menuMaintain);
		this.add(menuReport);
//		this.add(menuTools);
		this.add(menuWindow);
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
//		int count = e.length;
//		int i;

//		System.out.println("changed model in menu bar");

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

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));
		menuViewNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
		menuViewNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));
		menuViewMessages.setVisible(aModel.isVisible("menuViewMessages"));
		menuViewMessages.setEnabled(aModel.isEnabled("menuViewMessages"));
		menuViewToolbar.setVisible(aModel.isVisible("menuViewToolbar"));
		menuViewToolbar.setEnabled(aModel.isEnabled("menuViewToolbar"));
		menuViewRefresh.setVisible(aModel.isVisible("menuViewRefresh"));
		menuViewRefresh.setEnabled(aModel.isEnabled("menuViewRefresh"));
		menuViewCatalogue.setVisible(aModel.isVisible("menuViewCatalogue"));
		menuViewCatalogue.setEnabled(aModel.isEnabled("menuViewCatalogue"));
		menuViewAll.setVisible(aModel.isVisible("menuViewAll"));
		menuViewAll.setEnabled(aModel.isEnabled("menuViewAll"));

		menuEvaluate.setVisible(aModel.isVisible("menuEvaluate"));
		menuEvaluate.setEnabled(aModel.isEnabled("menuEvaluate"));
		menuEvaluateRequest.setVisible(aModel.isVisible("menuEvaluateRequest"));
		menuEvaluateRequest.setEnabled(aModel.isEnabled("menuEvaluateRequest"));
		menuEvaluateScheduler.setVisible(aModel.isVisible("menuEvaluateScheduler"));
		menuEvaluateScheduler.setEnabled(aModel.isEnabled("menuEvaluateScheduler"));
		menuEvaluateArchive.setVisible(aModel.isVisible("menuEvaluateArchive"));
		menuEvaluateArchive.setEnabled(aModel.isEnabled("menuEvaluateArchive"));
		menuEvaluateTrack.setVisible(aModel.isVisible("menuEvaluateTrack"));
		menuEvaluateTrack.setEnabled(aModel.isEnabled("menuEvaluateTrack"));
		menuEvaluateTrackRequest.setVisible(aModel.isVisible("menuEvaluateTrackRequest"));
		menuEvaluateTrackRequest.setEnabled(aModel.isEnabled("menuEvaluateTrackRequest"));
		menuEvaluateTrackTask.setVisible(aModel.isVisible("menuEvaluateTrackTask"));
		menuEvaluateTrackTask.setEnabled(aModel.isEnabled("menuEvaluateTrackTask"));
		menuEvaluateResult.setVisible(aModel.isVisible("menuEvaluateResult"));
		menuEvaluateResult.setEnabled(aModel.isEnabled("menuEvaluateResult"));
		menuEvaluateAnalize.setVisible(aModel.isVisible("menuEvaluateAnalize"));
		menuEvaluateAnalize.setEnabled(aModel.isEnabled("menuEvaluateAnalize"));
		menuEvaluateAnalizeExt.setVisible(aModel.isVisible("menuEvaluateAnalizeExt"));
		menuEvaluateAnalizeExt.setEnabled(aModel.isEnabled("menuEvaluateAnalizeExt"));
		menuEvaluateNorms.setVisible(aModel.isVisible("menuEvaluateNorms"));
		menuEvaluateNorms.setEnabled(aModel.isEnabled("menuEvaluateNorms"));
		menuEvaluateModeling.setVisible(aModel.isVisible("menuEvaluateModeling"));
		menuEvaluateModeling.setEnabled(aModel.isEnabled("menuEvaluateModeling"));
		menuEvaluatePrognosis.setVisible(aModel.isVisible("menuEvaluatePrognosis"));
		menuEvaluatePrognosis.setEnabled(aModel.isEnabled("menuEvaluatePrognosis"));
		menuEvaluateViewAll.setVisible(aModel.isVisible("menuEvaluateViewAll"));
		menuEvaluateViewAll.setEnabled(aModel.isEnabled("menuEvaluateViewAll"));

		menuVisualize.setVisible(aModel.isVisible("menuVisualize"));
		menuVisualize.setEnabled(aModel.isEnabled("menuVisualize"));
		menuVisualizeNet.setVisible(aModel.isVisible("menuVisualizeNet"));
		menuVisualizeNet.setEnabled(aModel.isEnabled("menuVisualizeNet"));
		menuVisualizeNetGIS.setVisible(aModel.isVisible("menuVisualizeNetGIS"));
		menuVisualizeNetGIS.setEnabled(aModel.isEnabled("menuVisualizeNetGIS"));
		menuVisualizeISM.setVisible(aModel.isVisible("menuVisualizeISM"));
		menuVisualizeISM.setEnabled(aModel.isEnabled("menuVisualizeISM"));
		menuVisualizeISMGIS.setVisible(aModel.isVisible("menuVisualizeISMGIS"));
		menuVisualizeISMGIS.setEnabled(aModel.isEnabled("menuVisualizeISMGIS"));
		menuVisualizeMapEdit.setVisible(aModel.isVisible("menuVisualizeMapEdit"));
		menuVisualizeMapEdit.setEnabled(aModel.isEnabled("menuVisualizeMapEdit"));
		menuVisualizeMapClose.setVisible(aModel.isVisible("menuVisualizeMapClose"));
		menuVisualizeMapClose.setEnabled(aModel.isEnabled("menuVisualizeMapClose"));
		menuVisualizeSchemeEdit.setVisible(aModel.isVisible("menuVisualizeSchemeEdit"));
		menuVisualizeSchemeEdit.setEnabled(aModel.isEnabled("menuVisualizeSchemeEdit"));
		menuVisualizeSchemeClose.setVisible(aModel.isVisible("menuVisualizeSchemeClose"));
		menuVisualizeSchemeClose.setEnabled(aModel.isEnabled("menuVisualizeSchemeClose"));

		menuViewMapSetup.setVisible(aModel.isVisible("menuViewMapSetup"));
		menuViewMapSetup.setEnabled(aModel.isEnabled("menuViewMapSetup"));

		menuMaintain.setVisible(aModel.isVisible("menuMaintain"));
		menuMaintain.setEnabled(aModel.isEnabled("menuMaintain"));
		menuMaintainAlarm.setVisible(aModel.isVisible("menuMaintainAlarm"));
		menuMaintainAlarm.setEnabled(aModel.isEnabled("menuMaintainAlarm"));
		menuMaintainAlert.setVisible(aModel.isVisible("menuMaintainAlert"));
		menuMaintainAlert.setEnabled(aModel.isEnabled("menuMaintainAlert"));
		menuMaintainCall.setVisible(aModel.isVisible("menuMaintainCall"));
		menuMaintainCall.setEnabled(aModel.isEnabled("menuMaintainCall"));
		menuMaintainEvent.setVisible(aModel.isVisible("menuMaintainEvent"));
		menuMaintainEvent.setEnabled(aModel.isEnabled("menuMaintainEvent"));

		menuReport.setVisible(aModel.isVisible("menuReport"));
		menuReport.setEnabled(aModel.isEnabled("menuReport"));
		menuReportTable.setVisible(aModel.isVisible("menuReportTable"));
		menuReportTable.setEnabled(aModel.isEnabled("menuReportTable"));
		menuReportHistogramm.setVisible(aModel.isVisible("menuReportHistogramm"));
		menuReportHistogramm.setEnabled(aModel.isEnabled("menuReportHistogramm"));
		menuReportGraph.setVisible(aModel.isVisible("menuReportGraph"));
		menuReportGraph.setEnabled(aModel.isEnabled("menuReportGraph"));
		menuReportComplex.setVisible(aModel.isVisible("menuReportComplex"));
		menuReportComplex.setEnabled(aModel.isEnabled("menuReportComplex"));
		menuReportReport.setVisible(aModel.isVisible("menuReportReport"));
		menuReportReport.setEnabled(aModel.isEnabled("menuReportReport"));

		menuTools.setVisible(aModel.isVisible("menuTools"));
		menuTools.setEnabled(aModel.isEnabled("menuTools"));

		menuToolsSort.setVisible(aModel.isVisible("menuToolsSort"));
		menuToolsSort.setEnabled(aModel.isEnabled("menuToolsSort"));
		menuToolsSortNew.setVisible(aModel.isVisible("menuToolsSortNew"));
		menuToolsSortNew.setEnabled(aModel.isEnabled("menuToolsSortNew"));
		menuToolsSortSave.setVisible(aModel.isVisible("menuToolsSortSave"));
		menuToolsSortSave.setEnabled(aModel.isEnabled("menuToolsSortSave"));
		menuToolsSortOpen.setVisible(aModel.isVisible("menuToolsSortOpen"));
		menuToolsSortOpen.setEnabled(aModel.isEnabled("menuToolsSortOpen"));
		menuToolsSortList.setVisible(aModel.isVisible("menuToolsSortList"));
		menuToolsSortList.setEnabled(aModel.isEnabled("menuToolsSortList"));

		menuToolsFilter.setVisible(aModel.isVisible("menuToolsFilter"));
		menuToolsFilter.setEnabled(aModel.isEnabled("menuToolsFilter"));
		menuToolsFilterNew.setVisible(aModel.isVisible("menuToolsFilterNew"));
		menuToolsFilterNew.setEnabled(aModel.isEnabled("menuToolsFilterNew"));
		menuToolsFilterSave.setVisible(aModel.isVisible("menuToolsFilterSave"));
		menuToolsFilterSave.setEnabled(aModel.isEnabled("menuToolsFilterSave"));
		menuToolsFilterOpen.setVisible(aModel.isVisible("menuToolsFilterOpen"));
		menuToolsFilterOpen.setEnabled(aModel.isEnabled("menuToolsFilterOpen"));
		menuToolsFilterList.setVisible(aModel.isVisible("menuToolsFilterList"));
		menuToolsFilterList.setEnabled(aModel.isEnabled("menuToolsFilterList"));

		menuToolsFind.setVisible(aModel.isVisible("menuToolsFind"));
		menuToolsFind.setEnabled(aModel.isEnabled("menuToolsFind"));
		menuToolsFindFast.setVisible(aModel.isVisible("menuToolsFindFast"));
		menuToolsFindFast.setEnabled(aModel.isEnabled("menuToolsFindFast"));
		menuToolsFindWord.setVisible(aModel.isVisible("menuToolsFindWord"));
		menuToolsFindWord.setEnabled(aModel.isEnabled("menuToolsFindWord"));
		menuToolsFindField.setVisible(aModel.isVisible("menuToolsFindField"));
		menuToolsFindField.setEnabled(aModel.isEnabled("menuToolsFindField"));
		menuToolsFindNext.setVisible(aModel.isVisible("menuToolsFindNext"));
		menuToolsFindNext.setEnabled(aModel.isEnabled("menuToolsFindNext"));
		menuToolsFindQuery.setVisible(aModel.isVisible("menuToolsFindQuery"));
		menuToolsFindQuery.setEnabled(aModel.isEnabled("menuToolsFindQuery"));

		menuToolsBookmark.setVisible(aModel.isVisible("menuToolsBookmark"));
		menuToolsBookmark.setEnabled(aModel.isEnabled("menuToolsBookmark"));
		menuToolsBookmarkSet.setVisible(aModel.isVisible("menuToolsBookmarkSet"));
		menuToolsBookmarkSet.setEnabled(aModel.isEnabled("menuToolsBookmarkSet"));
		menuToolsBookmarkGoto.setVisible(aModel.isVisible("menuToolsBookmarkGoto"));
		menuToolsBookmarkGoto.setEnabled(aModel.isEnabled("menuToolsBookmarkGoto"));
		menuToolsBookmarkList.setVisible(aModel.isVisible("menuToolsBookmarkList"));
		menuToolsBookmarkList.setEnabled(aModel.isEnabled("menuToolsBookmarkList"));
		menuToolsBookmarkRemove.setVisible(aModel.isVisible("menuToolsBookmarkRemove"));
		menuToolsBookmarkRemove.setEnabled(aModel.isEnabled("menuToolsBookmarkRemove"));
		menuToolsBookmarkEdit.setVisible(aModel.isVisible("menuToolsBookmarkEdit"));
		menuToolsBookmarkEdit.setEnabled(aModel.isEnabled("menuToolsBookmarkEdit"));

		menuToolsDictionary.setVisible(aModel.isVisible("menuToolsDictionary"));
		menuToolsDictionary.setEnabled(aModel.isEnabled("menuToolsDictionary"));
		menuToolsLanguage.setVisible(aModel.isVisible("menuToolsLanguage"));
		menuToolsLanguage.setEnabled(aModel.isEnabled("menuToolsLanguage"));
		menuToolsLock.setVisible(aModel.isVisible("menuToolsLock"));
		menuToolsLock.setEnabled(aModel.isEnabled("menuToolsLock"));

		menuToolsStyle.setVisible(aModel.isVisible("menuToolsStyle"));
		menuToolsStyle.setEnabled(aModel.isEnabled("menuToolsStyle"));
		menuToolsStyleText.setVisible(aModel.isVisible("menuToolsStyleText"));
		menuToolsStyleText.setEnabled(aModel.isEnabled("menuToolsStyleText"));
		menuToolsStyleGraph.setVisible(aModel.isVisible("menuToolsStyleGraph"));
		menuToolsStyleGraph.setEnabled(aModel.isEnabled("menuToolsStyleGraph"));
		menuToolsStyleLine.setVisible(aModel.isVisible("menuToolsStyleLine"));
		menuToolsStyleLine.setEnabled(aModel.isEnabled("menuToolsStyleLine"));
		menuToolsStyleTable.setVisible(aModel.isVisible("menuToolsStyleTable"));
		menuToolsStyleTable.setEnabled(aModel.isEnabled("menuToolsStyleTable"));
		menuToolsStyleScheme.setVisible(aModel.isVisible("menuToolsStyleScheme"));
		menuToolsStyleScheme.setEnabled(aModel.isEnabled("menuToolsStyleScheme"));
		menuToolsStyleMap.setVisible(aModel.isVisible("menuToolsStyleMap"));
		menuToolsStyleMap.setEnabled(aModel.isEnabled("menuToolsStyleMap"));
		menuToolsStyleSound.setVisible(aModel.isVisible("menuToolsStyleSound"));
		menuToolsStyleSound.setEnabled(aModel.isEnabled("menuToolsStyleSound"));
		menuToolsStyleColor.setVisible(aModel.isVisible("menuToolsStyleColor"));
		menuToolsStyleColor.setEnabled(aModel.isEnabled("menuToolsStyleColor"));
		menuToolsStyleLink.setVisible(aModel.isVisible("menuToolsStyleLink"));
		menuToolsStyleLink.setEnabled(aModel.isEnabled("menuToolsStyleLink"));

		menuToolsOptions.setVisible(aModel.isVisible("menuToolsOptions"));
		menuToolsOptions.setEnabled(aModel.isEnabled("menuToolsOptions"));

		menuWindow.setVisible(aModel.isVisible("menuWindow"));
		menuWindow.setEnabled(aModel.isEnabled("menuWindow"));
		menuWindowClose.setVisible(aModel.isVisible("menuWindowClose"));
		menuWindowClose.setEnabled(aModel.isEnabled("menuWindowClose"));
		menuWindowCloseAll.setVisible(aModel.isVisible("menuWindowCloseAll"));
		menuWindowCloseAll.setEnabled(aModel.isEnabled("menuWindowCloseAll"));
		menuWindowTileHorizontal.setVisible(aModel.isVisible("menuWindowTileHorizontal"));
		menuWindowTileHorizontal.setEnabled(aModel.isEnabled("menuWindowTileHorizontal"));
		menuWindowTileVertical.setVisible(aModel.isVisible("menuWindowTileVertical"));
		menuWindowTileVertical.setEnabled(aModel.isEnabled("menuWindowTileVertical"));
		menuWindowCascade.setVisible(aModel.isVisible("menuWindowCascade"));
		menuWindowCascade.setEnabled(aModel.isEnabled("menuWindowCascade"));
		menuWindowArrange.setVisible(aModel.isVisible("menuWindowArrange"));
		menuWindowArrange.setEnabled(aModel.isEnabled("menuWindowArrange"));
		menuWindowArrangeIcons.setVisible(aModel.isVisible("menuWindowArrangeIcons"));
		menuWindowArrangeIcons.setEnabled(aModel.isEnabled("menuWindowArrangeIcons"));
		menuWindowMinimizeAll.setVisible(aModel.isVisible("menuWindowMinimizeAll"));
		menuWindowMinimizeAll.setEnabled(aModel.isEnabled("menuWindowMinimizeAll"));
		menuWindowRestoreAll.setVisible(aModel.isVisible("menuWindowRestoreAll"));
		menuWindowRestoreAll.setEnabled(aModel.isEnabled("menuWindowRestoreAll"));
		menuWindowList.setVisible(aModel.isVisible("menuWindowList"));
		menuWindowList.setEnabled(aModel.isEnabled("menuWindowList"));

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

class SurveyMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	SurveyMenuBar adaptee;

	SurveyMenuBar_this_actionAdapter(SurveyMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

