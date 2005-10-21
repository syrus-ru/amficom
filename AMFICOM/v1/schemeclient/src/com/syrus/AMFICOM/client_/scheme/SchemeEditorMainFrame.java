/*-
 * $Id: SchemeEditorMainFrame.java,v 1.30 2005/10/21 11:55:14 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

/**
 * @author $Author: stas $
 * @version $Revision: 1.30 $, $Date: 2005/10/21 11:55:14 $
 * @module schemeclient
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.General.Command.Scheme.ConfigExportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ConfigImportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.CreateSchemeReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathCancelCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ProtoElementsExportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ProtoElementsImportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeExportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeImportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeImportCommitCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.FullSchemeTreeModel;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeUI;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.util.Log;

public class SchemeEditorMainFrame extends AbstractMainFrame {
	private static final long serialVersionUID = 8315696633544939499L;

	public static final String	TREE_FRAME = "treeFrame";
		
	ArrayList graphs = new ArrayList();
	SchemeTabbedPane schemeTab;
	
	UIDefaults frames;

	public SchemeEditorMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelSchematics.getString("SchemeEditorTitle"), 
				new SchemeEditorMenuBar(aContext.getApplicationModel()), 
				new SchemeEditorToolBar());
	}
	
	protected void initFrames() {
		this.frames = new UIDefaults();
		this.schemeTab = new SchemeTabbedPane(this.aContext);
		this.schemeTab.setEditable(true);
		
		this.frames.put(SchemeViewerFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EDITOR_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(SchemeEditorMainFrame.this.aContext, SchemeEditorMainFrame.this.schemeTab);
				editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
				SchemeEditorMainFrame.this.desktopPane.add(editorFrame);
				return editorFrame;
			}
		});

		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame(LangModelScheme.getString("Title.properties"));
				SchemeEditorMainFrame.this.desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, SchemeEditorMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame(LangModelScheme.getString("Title.characteristics"));
				SchemeEditorMainFrame.this.desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, SchemeEditorMainFrame.this.aContext);
				return characteristicFrame;
			}
		});

		this.frames.put(AdditionalPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Level.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame(LangModelScheme.getString("Title.additional"));
				SchemeEditorMainFrame.this.desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, SchemeEditorMainFrame.this.aContext);
				return additionalFrame;
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
				treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
				
				FullSchemeTreeModel model = new FullSchemeTreeModel(SchemeEditorMainFrame.this.aContext);
				TreeFilterUI tfUI = new TreeFilterUI(new SchemeTreeUI(model.getRoot(), SchemeEditorMainFrame.this.aContext), new FilterPanel());

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				SchemeEditorMainFrame.this.desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		this.setWindowArranger(new WindowArranger(SchemeEditorMainFrame.this) {
			@Override
			public void arrange() {
				Rectangle r = SchemeEditorMainFrame.this.scrollPane.getViewportBorderBounds();
				int w = r.width + SchemeEditorMainFrame.this.scrollPane.getVerticalScrollBar().getVisibleRect().width;
				int h = r.height + SchemeEditorMainFrame.this.scrollPane.getHorizontalScrollBar().getVisibleRect().height;
				Dimension size = new Dimension(w, h);
				SchemeEditorMainFrame.this.desktopPane.setPreferredSize(size);
				SchemeEditorMainFrame.this.desktopPane.setSize(size);

				JInternalFrame editorFrame = null;
				JInternalFrame generalFrame = null;
				JInternalFrame characteristicsFrame = null;
				JInternalFrame additionalFrame = null;
				JInternalFrame treeFrame = null;
				
				for (Component component : SchemeEditorMainFrame.this.desktopPane.getComponents()) {
					if (TREE_FRAME.equals(component.getName()))
						treeFrame = (JInternalFrame)component;
					else if (SchemeViewerFrame.NAME.equals(component.getName()))
						editorFrame = (JInternalFrame)component;
					else if (GeneralPropertiesFrame.NAME.equals(component.getName()))
						generalFrame = (JInternalFrame)component;
					else if (CharacteristicPropertiesFrame.NAME.equals(component.getName()))
						characteristicsFrame = (JInternalFrame)component;
					else if (AdditionalPropertiesFrame.NAME.equals(component.getName()))
						additionalFrame = (JInternalFrame)component;
				}
				
				if (editorFrame != null) {
					normalize(editorFrame);
					editorFrame.setSize(3 * w / 5, h);
					editorFrame.setLocation(w / 5, 0);
				}
				if (generalFrame != null) {
					normalize(generalFrame);
					generalFrame.setSize(w/5, h/2);
					generalFrame.setLocation(4*w/5, 0);
				}
				if (characteristicsFrame != null) {
					normalize(characteristicsFrame);
					characteristicsFrame.setSize(w/5, h / 4);
					characteristicsFrame.setLocation(4*w/5, h/2);
				}
				if (additionalFrame != null) {
					normalize(additionalFrame);
					additionalFrame.setSize(w / 5, h / 4);
					additionalFrame.setLocation(4 * w / 5, 3 * h / 4);
				}
				if (treeFrame != null) {
					normalize(treeFrame);
					treeFrame.setSize(w / 5, h);
					treeFrame.setLocation(0, 0);
				}
			}
		});
	}
	
	public SchemeEditorMainFrame() {
		this(new ApplicationContext());
	}
		
	@Override
	public void initModule() {
		super.initModule();
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand("menuSchemeNew", new SchemeNewCommand(this.aContext));
		aModel.setCommand("menuSchemeLoad", new SchemeOpenCommand(this.aContext));
		aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(this.schemeTab));
		aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand(this.aContext, this.schemeTab));

		
		aModel.setCommand("Menu.import.protos", new ProtoElementsImportCommand(this.schemeTab));
		aModel.setCommand("Menu.import.config", new ConfigImportCommand(this.schemeTab));
		aModel.setCommand("Menu.import.scheme", new SchemeImportCommand(this.schemeTab));
		aModel.setCommand("Menu.import.commit", new SchemeImportCommitCommand(this.aContext));
		
		aModel.setCommand("Menu.export.protos", new ProtoElementsExportCommand(this.schemeTab));
		aModel.setCommand("Menu.export.config", new ConfigExportCommand(this.schemeTab));
		aModel.setCommand("Menu.export.scheme", new SchemeExportCommand(this.schemeTab));

		// TODO разобраться с созданием пути
		
		aModel.setCommand("menuPathNew", new PathNewCommand(this.schemeTab));
		aModel.setCommand("menuPathSave", new PathSaveCommand(this.schemeTab));
		aModel.setCommand("menuPathCancel", new PathCancelCommand(this.schemeTab));
		
		/*aModel.setCommand("menuPathEdit", new PathEditCommand(aContext, schemeTab));
		aModel.setCommand("menuPathAddStart", new PathSetStartCommand(aContext,
				schemeTab));
		aModel.setCommand("menuPathAddEnd", new PathSetEndCommand(aContext,
				schemeTab));
		aModel.setCommand("menuPathAddLink", new PathAddLinkCommand(aContext,
				schemeTab));

		aModel.setCommand("menuPathRemoveLink", new PathRemoveLinkCommand(aContext,
				scheme_graph));
		aModel.setCommand("menuPathAutoCreate", new PathAutoCreateCommand(aContext,
				schemeTab.getPanel()));

		
		aModel.setCommand("menuPathDelete", new PathDeleteCommand(aContext,
				schemeTab));*/

//		CreateSchemeReportCommand rc = new CreateSchemeReportCommand(aContext);
//		for (Iterator it = graphs.iterator(); it.hasNext();)
//			rc.setParameter(CreateSchemeReportCommand.PANEL, it.next());
//		rc.setParameter(CreateSchemeReportCommand.TYPE, ReportTemplate.rtt_Scheme);
		aModel.setCommand("menuReportCreate", new CreateSchemeReportCommand(this.aContext, this.schemeTab));

		aModel.setCommand("menuWindowTree", this.getLazyCommand(TREE_FRAME));
		aModel.setCommand("menuWindowScheme", this.getLazyCommand(SchemeViewerFrame.NAME));
		aModel.setCommand("menuWindowUgo", this.getLazyCommand(AdditionalPropertiesFrame.NAME));
		aModel.setCommand("menuWindowProps", this.getLazyCommand(CharacteristicPropertiesFrame.NAME));
		aModel.setCommand("menuWindowList", this.getLazyCommand(GeneralPropertiesFrame.NAME));

		setDefaultModel(aModel);
		aModel.fireModelChanged("");
	}

	@Override
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		aModel.setEnabled("menuScheme", true);
		aModel.setEnabled("menuPath", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(CreatePathEvent.TYPE)) {
			CreatePathEvent cpe = (CreatePathEvent) ae;
			if (cpe.CREATE_PATH || cpe.EDIT_PATH) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled("menuPathSave", true);
				aModel.setEnabled("menuPathAddStart", true);
				aModel.setEnabled("menuPathAddEnd", true);
				aModel.setEnabled("menuPathAddLink", true);
				aModel.setEnabled("menuPathCancel", true);
				aModel.setEnabled("menuPathAutoCreate", true);
				aModel.getCommand("menuPathAutoCreate").setParameter("panel",
						this.schemeTab.getCurrentPanel());
				aModel.fireModelChanged("");
			}
			if (cpe.CANCEL_PATH_CREATION || cpe.SAVE_PATH) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled("menuPathSave", false);
				aModel.setEnabled("menuPathAddStart", false);
				aModel.setEnabled("menuPathAddEnd", false);
				aModel.setEnabled("menuPathAddLink", false);
				aModel.setEnabled("menuPathRemoveLink", false);
				aModel.setEnabled("menuPathAutoCreate", false);
				aModel.setEnabled("menuPathCancel", false);
				aModel.fireModelChanged("");
			}
			if (cpe.PE_SELECTED) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel")) {
					aModel.setEnabled("menuPathRemoveLink", true);
					aModel.fireModelChanged("");
				}
			} else {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel")) {
					aModel.setEnabled("menuPathRemoveLink", false);
					aModel.fireModelChanged("");
				}
			}
		} else if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			ObjectSelectedEvent sne = (ObjectSelectedEvent) ae;
			if (sne.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", true);
				aModel.setEnabled("menuPathDelete", true);
				aModel.fireModelChanged("");
			} else {
				ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathDelete", false);
				aModel.fireModelChanged("");
			}
		}
		/*
		 * else if (ae.getActionCommand().equals(CatalogNavigateEvent.type)) {
		 * CatalogNavigateEvent cne = (CatalogNavigateEvent)ae; if
		 * (cne.CATALOG_PATH_SELECTED) { ApplicationModel aModel =
		 * aContext.getApplicationModel(); aModel.setEnabled("menuPathEdit", true);
		 * aModel.setEnabled("menuPathDelete", true); aModel.fireModelChanged(""); }
		 * if (cne.CATALOG_PATH_DESELECTED) { ApplicationModel aModel =
		 * aContext.getApplicationModel(); aModel.setEnabled("menuPathEdit", false);
		 * aModel.setEnabled("menuPathDelete", false); aModel.fireModelChanged(""); } }
		 */
		super.propertyChange(ae);
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = SchemeEditorMainFrame.this.frames.get(key);
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
	public void loggedIn() {
		SchemeObjectsFactory.init(this.aContext);
		final ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuWindowTree", true);
		aModel.setEnabled("menuWindowScheme", true);
		aModel.setEnabled("menuWindowUgo", true);
		aModel.setEnabled("menuWindowProps", true);
		aModel.setEnabled("menuWindowList", true);
		
		aModel.setEnabled("Menu.export", true);
		aModel.setEnabled("Menu.export.scheme", true);
		aModel.setEnabled("Menu.export.config", true);
		aModel.setEnabled("Menu.export.protos", true);
		aModel.setEnabled("Menu.import", true);
		aModel.setEnabled("Menu.import.scheme", true);
		aModel.setEnabled("Menu.import.config", true);
		aModel.setEnabled("Menu.import.protos", true);
		
		aModel.setEnabled("menuSchemeNew", true);
		aModel.setEnabled("menuSchemeLoad", true);
		aModel.setEnabled("menuSchemeSave", true);
		aModel.setEnabled("menuSchemeSaveAs", true);
		aModel.setEnabled("menuPathNew", true);
		aModel.setEnabled("menuReportCreate", true);
		
		aModel.fireModelChanged("");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				aModel.getCommand("menuSchemeNew").execute();
			}
		});
	}

	@Override
	public void loggedOut() {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuSchemeNew", false);
		aModel.setEnabled("menuSchemeLoad", false);
		aModel.setEnabled("menuSchemeSave", false);
		aModel.setEnabled("menuSchemeSaveAs", false);
		aModel.setEnabled("Menu.export", false);
		aModel.setEnabled("Menu.import", false);
		aModel.setEnabled("menuPathNew", false);
		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuWindowTree", false);
		aModel.setEnabled("menuWindowScheme", false);
		aModel.setEnabled("menuWindowUgo", false);
		aModel.setEnabled("menuWindowProps", false);
		aModel.setEnabled("menuWindowList", false);

		aModel.fireModelChanged("");
		
		setFramesVisible(false);
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			if (!this.schemeTab.confirmUnsavedChanges()) {
				return;
			}
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, SchemeEditorMainFrame.this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, SchemeEditorMainFrame.this);
			SchemeEditorMainFrame.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
		}
		super.processWindowEvent(e);
	}
}

