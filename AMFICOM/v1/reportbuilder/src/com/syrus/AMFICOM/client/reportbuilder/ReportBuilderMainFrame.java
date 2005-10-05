/*
 * $Id: ReportBuilderMainFrame.java,v 1.15 2005/10/05 09:39:37 peskovsky Exp $
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
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
import com.syrus.AMFICOM.client.report.ReportException;
import com.syrus.AMFICOM.client.report.ReportRenderer;
import com.syrus.AMFICOM.client.reportbuilder.ModuleMode.MODULE_MODE;
import com.syrus.AMFICOM.client.reportbuilder.command.template.NewTemplateCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.template.OpenTemplateCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.template.PrintReportCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.template.SaveAsTemplateCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.template.SaveReportCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.template.SaveTemplateCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.template.TemplateParametersCommand;
import com.syrus.AMFICOM.client.reportbuilder.command.templatescheme.ReportSendEventCommand;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTemplateRenderer;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;
/**
 * 
 * @author $Author: peskovsky $
 * @version $Revision: 1.15 $, $Date: 2005/10/05 09:39:37 $
 * @author Peskovsky Peter
 * @module reportbuilder_v1
 */
public class ReportBuilderMainFrame extends AbstractMainFrame implements PropertyChangeListener {
	private static final long serialVersionUID = 8315696633544939499L;

	public static final String TREE_FRAME = "report.Tree.rootTemplates";
	public static final String TEMPLATE_SCHEME_FRAME = "report.UI.templateScheme";	

	UIDefaults frames;
	/**
	 * Тулбар, висящий на рендерере шаблона. Хранится как поле главного окна,
	 * поскольку рендереры могут менять, а тулбар - неизменен.
	 */
	protected TemplateRendererInnerToolbar innerToolbar = null;

	protected JScrollPane rendererScrollPane = null;
	
	protected ReportTemplateRenderer reportTemplateRenderer = null;
	protected ReportRenderer reportPreviewRenderer = null;
	
	protected ReportTemplateElementsTreeMouseListener reportBuilderTreeMouseListener = null;
	
	public static final Map<Object, Object> EMPTY_REPORT_DATA =
		new HashMap<Object,Object>();
	
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
		
		this.frames.put(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				JInternalFrame templateSchemeFrame = new JInternalFrame();
				templateSchemeFrame.setName(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME);
				templateSchemeFrame.setIconifiable(true);
				templateSchemeFrame.setClosable(true);
				templateSchemeFrame.setResizable(true);
				templateSchemeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				templateSchemeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				templateSchemeFrame.setTitle(LangModelReport.getString(TEMPLATE_SCHEME_FRAME));
				
				ReportBuilderMainFrame.this.rendererScrollPane = new JScrollPane();
				ReportBuilderMainFrame.this.rendererScrollPane.getViewport().add(ReportBuilderMainFrame.this.reportTemplateRenderer);
				ReportBuilderMainFrame.this.rendererScrollPane.setAutoscrolls(true);

				templateSchemeFrame.getContentPane().setLayout(new BorderLayout());				
				templateSchemeFrame.getContentPane().add(
						ReportBuilderMainFrame.this.rendererScrollPane,
						BorderLayout.CENTER);
				
				templateSchemeFrame.getContentPane().add(
						ReportBuilderMainFrame.this.innerToolbar,
						BorderLayout.NORTH);				

				ReportBuilderMainFrame.this.desktopPane.add(templateSchemeFrame);
				return templateSchemeFrame;
			}
		});
		
		this.frames.put(ReportBuilderApplicationModel.MENU_WINDOW_TREE, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TREE_FRAME", Level.FINEST);
				JInternalFrame treeFrame = new JInternalFrame();
				treeFrame.setName(ReportBuilderApplicationModel.MENU_WINDOW_TREE);
				treeFrame.setIconifiable(true);
				treeFrame.setClosable(true);
				treeFrame.setResizable(true);
				treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				treeFrame.setTitle(LangModelReport.getString(TREE_FRAME));
				
				ReportTemplateElementsTreeModel model = new ReportTemplateElementsTreeModel(
						ReportBuilderMainFrame.this.aContext);
				IconedTreeUI iconedTreeUI = new IconedTreeUI(model.getRoot());
				TreeFilterUI treeFilterUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());				

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(treeFilterUI.getPanel(), BorderLayout.CENTER);
				JTree tree = iconedTreeUI.getTree();
				ReportBuilderMainFrame.this.reportBuilderTreeMouseListener =
					new ReportTemplateElementsTreeMouseListener(tree);
				ReportBuilderMainFrame.this.reportBuilderTreeMouseListener.setContext(
						ReportBuilderMainFrame.this.aContext);
				tree.addMouseListener(ReportBuilderMainFrame.this.reportBuilderTreeMouseListener);

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
					String name = component.getName();
					if (name == null)
						continue;
					
					if (name.equals(ReportBuilderApplicationModel.MENU_WINDOW_TREE))
						treeFrame = (JInternalFrame)component;
					else if (name.equals(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME))
						templateSchemeFrame = (JInternalFrame)component;
				}
				
				if (treeFrame != null) {
					normalize(treeFrame);
					treeFrame.setSize(2 * w / 7, h);
					treeFrame.setLocation(0, 0);
				}
				if (templateSchemeFrame != null) {
					normalize(templateSchemeFrame);
					templateSchemeFrame.setSize(5 * w / 7, h);
					templateSchemeFrame.setLocation(2 * w / 7, 0);
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
		if (this.reportBuilderTreeMouseListener != null)
			this.reportBuilderTreeMouseListener.setContext(this.aContext);
	}
	
		
	@Override
	public void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW,
				new NewTemplateCommand(this.aContext,this));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD,
				new OpenTemplateCommand(this.aContext,this));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE,
				new SaveTemplateCommand(this.aContext,this));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE_AS,
				new SaveAsTemplateCommand(this.aContext,this));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_TEMPLATE_PARAMETERS,
				new TemplateParametersCommand(this.aContext,this));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_WINDOW_TREE,
				this.getLazyCommand(ReportBuilderApplicationModel.MENU_WINDOW_TREE));
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME,
				this.getLazyCommand(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_LABEL
					+ ReportBuilderApplicationModel.START,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.LABEL_CREATION_STARTED));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_LABEL
					+ ReportBuilderApplicationModel.CANCEL,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.SPECIAL_MODE_CANCELED));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_IMAGE
					+ ReportBuilderApplicationModel.START,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.IMAGE_CREATION_STARTED));

		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_INSERT_IMAGE
					+ ReportBuilderApplicationModel.CANCEL,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.SPECIAL_MODE_CANCELED));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_DELETE_OBJECT,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.DELETE_OBJECT));
		
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_CHANGE_VIEW,
				new ReportSendEventCommand(this.aContext,ReportFlagEvent.CHANGE_VIEW));
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_SAVE_REPORT,
				new SaveReportCommand(this));
		aModel.setCommand(
				ReportBuilderApplicationModel.MENU_PRINT_REPORT,
				new PrintReportCommand(this));
		
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

		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE, false);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE_AS, false);
		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW, false);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TREE, false);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME, false);

		aModel.setEnabled(ReportBuilderApplicationModel.MENU_TEMPLATE, false);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_TEMPLATE_PARAMETERS, false);
		
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
		aModel.setEnabled(ApplicationModel.MENU_SESSION,true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_DOMAIN, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, true);
		//////////
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_NEW, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE_AS, true);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_LOAD, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_EXPORT, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_IMPORT, true);

		aModel.setEnabled(ReportBuilderApplicationModel.MENU_INSERT_IMAGE, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_INSERT_LABEL, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_DELETE_OBJECT, false);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_CHANGE_VIEW, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_SAVE_REPORT, false);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_PRINT_REPORT, false);

		aModel.setEnabled(ReportBuilderApplicationModel.MENU_TEMPLATE, true);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_TEMPLATE_PARAMETERS, true);
		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW, true);		
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TREE, true);
		aModel.setEnabled(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);		
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
	
			else if (eventType.equals(ReportFlagEvent.SPECIAL_MODE_CANCELED)) {
				this.setApplicationModelForTemplateSchemeStandart(aModel);
				aModel.fireModelChanged("");
			}
			else if (eventType.equals(ReportFlagEvent.DELETE_OBJECT)) {
				aModel.setEnabled(ReportBuilderApplicationModel.MENU_DELETE_OBJECT,false);
				aModel.fireModelChanged("");
			}
			else if (eventType.equals(ReportFlagEvent.CHANGE_VIEW)) {
				if (ModuleMode.getMode().equals(MODULE_MODE.TEMPLATE_SCHEME)) {
					ReportTemplate reportTemplate = this.reportTemplateRenderer.getTemplate();
				
					this.reportPreviewRenderer = new ReportRenderer(this.aContext);
					this.reportPreviewRenderer.setReportTemplate(reportTemplate);
					try {
						this.reportPreviewRenderer.setData(EMPTY_REPORT_DATA);
					} catch (Exception e) {
						Log.errorMessage("ReportBuilderMainFrame.propertyChange | "
								+ e.getMessage());
						Log.errorException(e);
						JOptionPane.showMessageDialog(
								Environment.getActiveWindow(),
								e.getMessage(),
								LangModelReport.getString("report.Exception.error"),
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					this.rendererScrollPane.getViewport().remove(this.reportTemplateRenderer);
					this.rendererScrollPane.getViewport().add(this.reportPreviewRenderer);
					
					ModuleMode.setMode(MODULE_MODE.REPORT_PREVIEW);
					
					aModel.setAllItemsEnabled(false);
					aModel.setEnabled(ReportBuilderApplicationModel.MENU_CHANGE_VIEW,true);					
					aModel.setEnabled(ReportBuilderApplicationModel.MENU_SAVE_REPORT,true);
					aModel.setEnabled(ReportBuilderApplicationModel.MENU_PRINT_REPORT,true);					
				}
				else {
					this.rendererScrollPane.getViewport().remove(this.reportPreviewRenderer);
					this.rendererScrollPane.getViewport().add(this.reportTemplateRenderer);					
					
					ModuleMode.setMode(MODULE_MODE.TEMPLATE_SCHEME);
					this.setApplicationModelForTemplateSchemeStandart(aModel);
				}
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
		else if (pce instanceof UseTemplateEvent) {
			this.setApplicationModelForTemplateSchemeStandart(aModel);			
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

	public ReportTemplateRenderer getTemplateRenderer() {
		return this.reportTemplateRenderer;
	}

	public ReportRenderer getReportRenderer() {
		return this.reportPreviewRenderer;
	}
}
