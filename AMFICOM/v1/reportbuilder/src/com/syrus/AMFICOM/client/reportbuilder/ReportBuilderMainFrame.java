/*
 * $Id: ReportBuilderMainFrame.java,v 1.3 2005/08/31 10:29:03 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.reportbuilder.command.ReportTemplateNewCommand;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

public class ReportBuilderMainFrame extends AbstractMainFrame {
	private static final long serialVersionUID = 8315696633544939499L;

	public static final String TREE_FRAME = "report.Tree.availableElements";
	public static final String TEMPLATE_SCHEME_FRAME = "report.UI.templateScheme";	

	UIDefaults frames;
	
	public ReportBuilderMainFrame(final ApplicationContext aContext) {
		super(
			aContext,
			LangModelReport.getString("report.UI.mainWindowTitle"), 
			new ReportBuilderMenuBar(aContext.getApplicationModel()), 
			new ReportBuilderToolBar());
	}
	
	protected void initFrames() {
		this.frames = new UIDefaults();
		
		this.frames.put(TEMPLATE_SCHEME_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				JInternalFrame templateSchemeFrame = new JInternalFrame();
				templateSchemeFrame.setName(TEMPLATE_SCHEME_FRAME);
				templateSchemeFrame.setIconifiable(true);
				templateSchemeFrame.setClosable(true);
				templateSchemeFrame.setResizable(true);
				templateSchemeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				templateSchemeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				templateSchemeFrame.setTitle(LangModelReport.getString(TEMPLATE_SCHEME_FRAME));
				
				templateSchemeFrame.getContentPane().setLayout(new BorderLayout());
				templateSchemeFrame.getContentPane().add(new ReportTemplateRenderer(), BorderLayout.CENTER);

				ReportBuilderMainFrame.this.desktopPane.add(templateSchemeFrame);
				return templateSchemeFrame;
			}
		});
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TREE_FRAME", Level.FINEST);
				JInternalFrame treeFrame = new JInternalFrame();
				treeFrame.setName(TREE_FRAME);
				treeFrame.setIconifiable(true);
				treeFrame.setClosable(true);
				treeFrame.setResizable(true);
				treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				treeFrame.setTitle(LangModelReport.getString(TREE_FRAME));
				
				ReportTemplateElementsTreeModel model = new ReportTemplateElementsTreeModel(ReportBuilderMainFrame.this.aContext);
				IconedTreeUI tfUI = new IconedTreeUI(model.getRoot());

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				ReportBuilderMainFrame.this.desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		this.setWindowArranger(new WindowArranger(ReportBuilderMainFrame.this) {
			@Override
			public void arrange() {
				Rectangle r = ReportBuilderMainFrame.this.scrollPane.getViewportBorderBounds();
				int w = r.width + ReportBuilderMainFrame.this.scrollPane.getVerticalScrollBar().getVisibleRect().width;
				int h = r.height + ReportBuilderMainFrame.this.scrollPane.getHorizontalScrollBar().getVisibleRect().height;
				Dimension size = new Dimension(w, h);
				ReportBuilderMainFrame.this.desktopPane.setPreferredSize(size);
				ReportBuilderMainFrame.this.desktopPane.setSize(size);

				JInternalFrame treeFrame = null;
				JInternalFrame templateSchemeFrame = null;
				
				for (Component component : ReportBuilderMainFrame.this.desktopPane.getComponents()) {
					if (component.getName().equals(TREE_FRAME))
						treeFrame = (JInternalFrame)component;
					else if (component.getName().equals(TEMPLATE_SCHEME_FRAME))
						templateSchemeFrame = (JInternalFrame)component;
				}
				
				if (treeFrame != null) {
					normalize(treeFrame);
					treeFrame.setSize(w / 5, h);
					treeFrame.setLocation(0, 0);
				}
				if (templateSchemeFrame != null) {
					normalize(templateSchemeFrame);
					templateSchemeFrame.setSize(4 * w / 5, h);
					templateSchemeFrame.setLocation(w / 5, 0);
				}
			}
		});
	}
	
	public ReportBuilderMainFrame() {
		this(new ApplicationContext());
	}

		
	@Override
	public void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand("menuReportTemplateNew", new ReportTemplateNewCommand(this.aContext));
//		aModel.setCommand("menuReportTemplateLoad", new ReportTemplateOpenCommand(this.aContext));
//		aModel.setCommand("menuReportTemplateSave", new ReportTemplateSaveCommand(this.aContext));
//		aModel.setCommand("menuReportTemplateSaveAs", new ReportTemplateSaveAsCommand(this.aContext));

		aModel.setCommand("menuWindowTree", this.getLazyCommand(TREE_FRAME));
		aModel.setCommand("menuWindowTemplateScheme", this.getLazyCommand(TEMPLATE_SCHEME_FRAME));

		setDefaultModel(aModel);
		aModel.fireModelChanged("");
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = ReportBuilderMainFrame.this.frames.get(key);
					if (object instanceof JInternalFrame) {
						System.out.println("init getLazyCommand for " + key);
						this.command = new ShowWindowCommand((JInternalFrame)object);
					}
				}
				return this.command;
			}

			@Override
			public void execute() {
				this.getLazyCommand().execute();
			}
		};
	}
	
	@Override
	public void setSessionOpened() {
		super.setSessionOpened();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.setEnabled("menuReportTemplateNew", true);
		aModel.setEnabled("menuReportTemplateSave", true);
		aModel.setEnabled("menuReportTemplateLoad", true);
		aModel.setEnabled("menuReportTemplateExport", true);
		aModel.setEnabled("menuReportTemplateImport", true);

		aModel.setEnabled("menuWindowTree", true);
		aModel.setEnabled("menuWindowTemplateScheme", true);
		aModel.fireModelChanged("");
	}

	@Override
	public void setSessionClosed() {
		super.setSessionClosed();
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuReportTemplateNew", false);
		aModel.setEnabled("menuReportTemplateSave", false);
		aModel.setEnabled("menuReportTemplateLoad", false);
		aModel.setEnabled("menuReportTemplateExport", false);
		aModel.setEnabled("menuReportTemplateImport", false);

		aModel.setEnabled("menuWindowTree", false);
		aModel.setEnabled("menuWindowTemplateScheme", false);

		aModel.fireModelChanged("");
		
		setFramesVisible(false);
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, ReportBuilderMainFrame.this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ReportBuilderMainFrame.this);
			ReportBuilderMainFrame.this.aContext.getApplicationModel().getCommand("menuExit").execute();
		}
		super.processWindowEvent(e);
	}
}
