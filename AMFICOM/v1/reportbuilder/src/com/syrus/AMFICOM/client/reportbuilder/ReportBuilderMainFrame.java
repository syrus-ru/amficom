/*
 * $Id: ReportBuilderMainFrame.java,v 1.5 2005/09/03 12:42:21 peskovsky Exp $
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
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

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
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.command.template.ReportTemplateNewCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.templatescheme.ReportSendEventCommand;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTemplateRenderer;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

public class ReportBuilderMainFrame extends AbstractMainFrame implements PropertyChangeListener {
	private static final long serialVersionUID = 8315696633544939499L;

	public static final String TREE_FRAME = "report.Tree.availableElements";
	public static final String TEMPLATE_SCHEME_FRAME = "report.UI.templateScheme";	

	UIDefaults frames;
	/**
	 * Тулбар, висящий на рендерере шаблона. Хранится как поле главного окна,
	 * поскольку рендереры могут менять, а тулбар - неизменен.
	 */
	protected TemplateRendererInnerToolbar innerToolbar = null;
	
	protected ReportTemplateRenderer reportTemplateRenderer = null;
	
	public ReportBuilderMainFrame(final ApplicationContext aContext) {
		super(
			aContext,
			LangModelReport.getString("report.UI.mainWindowTitle"), 
			new ReportBuilderMenuBar(aContext.getApplicationModel()), 
			new ReportBuilderToolBar());
		
		ApplicationModel aModel = aContext.getApplicationModel();
		this.innerToolbar = new TemplateRendererInnerToolbar();
		this.innerToolbar.setApplicationModel(aModel);
		this.innerToolbar.setContext(this.aContext);		
		
		this.reportTemplateRenderer = new ReportTemplateRenderer();
		this.reportTemplateRenderer.setContext(this.aContext);
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
				templateSchemeFrame.getContentPane().add(ReportBuilderMainFrame.this.reportTemplateRenderer, BorderLayout.CENTER);
				
				templateSchemeFrame.getContentPane().add(ReportBuilderMainFrame.this.innerToolbar, BorderLayout.NORTH);				

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
	public void setModel(ApplicationModel aModel) {
		super.setModel(aModel);
		if (this.innerToolbar != null)
			this.innerToolbar.setApplicationModel(aModel);
	}
	
	@Override
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ReportEvent.TYPE, this);
		}
		if (aContext != null) {
			super.setContext(aContext);
			this.aContext.getDispatcher().addPropertyChangeListener(ReportEvent.TYPE, this);
		}
		
		if (this.reportTemplateRenderer != null)
			this.reportTemplateRenderer.setContext(this.aContext);
		if (this.innerToolbar != null)
			this.innerToolbar.setContext(this.aContext);
	}
	
		
	@Override
	public void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW,
				new ReportTemplateNewCommand(this.aContext));
//		aModel.setCommand(
//			ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD,
//			new ReportTemplateOpenCommand(this.aContext));
//		aModel.setCommand("menuReportTemplateSave", new ReportTemplateSaveCommand(this.aContext));
//		aModel.setCommand("menuReportTemplateSaveAs", new ReportTemplateSaveAsCommand(this.aContext));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_WINDOW_TREE,
				this.getLazyCommand(TREE_FRAME));
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME,
				this.getLazyCommand(TEMPLATE_SCHEME_FRAME));

		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_LABEL
					+ ReportBuilderApplicationModel.START,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.LABEL_CREATION_STARTED));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_LABEL
					+ ReportBuilderApplicationModel.CANCEL,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.LABEL_CREATION_CANCELED));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_IMAGE
					+ ReportBuilderApplicationModel.START,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.IMAGE_CREATION_STARTED));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_IMAGE
					+ ReportBuilderApplicationModel.CANCEL,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.IMAGE_CREATION_CANCELED));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_DELETE_OBJECT,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.DELETE_OBJECT));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_CHANGE_VIEW,
				new ReportTemplateNewCommand(this.aContext));
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_SAVE_REPORT,
				new ReportTemplateNewCommand(this.aContext));
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_PRINT_REPORT,
				new ReportTemplateNewCommand(this.aContext));
		
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
		this.setApplicationModelForTemplateSchemeStandart(aModel);
		aModel.fireModelChanged("");
	}

	@Override
	public void setSessionClosed() {
		super.setSessionClosed();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setAllItemsEnabled(false);
		aModel.fireModelChanged("");
		
		setFramesVisible(false);
	}

	private void setApplicationModelForTemplateSchemeStandart(ApplicationModel aModel){
		//////Вообще-то эти пять строк из super
		aModel.setEnabled(ApplicationModel.MENU_SESSION_DOMAIN, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, true);
		//////////
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_EXPORT, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_IMPORT, true);

		aModel.setEnabled(ReportBuilderApplicationModel.MENU_INSERT_IMAGE, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_INSERT_LABEL, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_DELETE_OBJECT, false);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_CHANGE_VIEW, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_SAVE_REPORT, false);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_PRINT_REPORT, false);
		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TREE, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME, true);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		ApplicationModel aModel = this.aContext.getApplicationModel();		
		super.propertyChange(pce);
		if (pce instanceof ReportFlagEvent) {
			String eventType = ((ReportFlagEvent)pce).getEventType();
			if (eventType.equals(ReportFlagEvent.LABEL_CREATION_STARTED)) {
				aModel.setAllItemsEnabled(false);
				aModel.setEnabled(ReportBuilderApplicationModel.MENU_INSERT_LABEL, true);
				aModel.fireModelChanged("");
			}
			else if (eventType.equals(ReportFlagEvent.IMAGE_CREATION_STARTED)) {
				aModel.setAllItemsEnabled(false);
				aModel.setEnabled(ReportBuilderApplicationModel.MENU_INSERT_IMAGE, true);
				aModel.fireModelChanged("");
			}
	
			else if (	eventType.equals(ReportFlagEvent.LABEL_CREATION_CANCELED)
					||	eventType.equals(ReportFlagEvent.IMAGE_CREATION_CANCELED)) {
				this.setApplicationModelForTemplateSchemeStandart(aModel);
				aModel.fireModelChanged("");
			}
			else if (eventType.equals(ReportFlagEvent.DELETE_OBJECT)) {
				aModel.setEnabled(ReportBuilderApplicationModel.MENU_DELETE_OBJECT,false);
				aModel.fireModelChanged("");
			}
		}
		else if (pce instanceof ComponentSelectionChangeEvent) {
			RenderingComponent component = 
				((ComponentSelectionChangeEvent)pce).getRenderingComponent();
			//Делаем кнопку удаления объектов активной, если выбран объект
			aModel.setEnabled(ReportBuilderApplicationModel.MENU_DELETE_OBJECT, (component != null));
			aModel.fireModelChanged("");
		}
		else if (pce instanceof AttachLabelEvent) {
			aModel.setAllItemsEnabled(false);
			aModel.fireModelChanged("");
		}
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, ReportBuilderMainFrame.this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ReportBuilderMainFrame.this);
			ReportBuilderMainFrame.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
		}
		super.processWindowEvent(e);
	}
}
