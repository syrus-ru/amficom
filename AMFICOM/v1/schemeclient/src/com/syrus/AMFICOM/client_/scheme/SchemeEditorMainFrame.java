/*-
 * $Id: SchemeEditorMainFrame.java,v 1.41 2006/06/06 12:43:26 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

/**
 * @author $Author: stas $
 * @version $Revision: 1.41 $, $Date: 2006/06/06 12:43:26 $
 * @module schemeclient
 */

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_ADDITIONAL_PROPERIES;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_CHARACTERISTICS;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_GENERAL_PROPERTIES;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_TREE;

import java.awt.BorderLayout;
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
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathEditCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.PathSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ProtoElementsExportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ProtoElementsImportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeExportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeImportCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeImportCommitCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ValidateSchemeCommand;
import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
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
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.FullSchemeTreeModel;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeUI;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;

public class SchemeEditorMainFrame extends AbstractMainFrame {
	private static final long serialVersionUID = 8315696633544939499L;
		
	ArrayList graphs = new ArrayList();
	SchemeTabbedPane schemeTab;
	
	UIDefaults frames;

	public SchemeEditorMainFrame(final ApplicationContext aContext) {
		super(aContext, I18N.getString("Title.schemeEditor"), 
				new SchemeEditorMenuBar(aContext.getApplicationModel()), 
				new SchemeEditorToolBar());
	}
	
	protected void initFrames() {
		this.frames = new UIDefaults();
		this.schemeTab = new SchemeTabbedPane(this.aContext);
		this.schemeTab.setEditable(false);
		
		this.frames.put(FRAME_EDITOR_MAIN, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EDITOR_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(SchemeEditorMainFrame.this.aContext, SchemeEditorMainFrame.this.schemeTab);
				SchemeEditorMainFrame.this.desktopPane.add(editorFrame);
				return editorFrame;
			}
		});

		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame(I18N.getString(FRAME_GENERAL_PROPERTIES));
				SchemeEditorMainFrame.this.desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, SchemeEditorMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame(I18N.getString(FRAME_CHARACTERISTICS));
				SchemeEditorMainFrame.this.desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, SchemeEditorMainFrame.this.aContext);
				return characteristicFrame;
			}
		});

		this.frames.put(AdditionalPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Level.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame(I18N.getString(FRAME_ADDITIONAL_PROPERIES));
				SchemeEditorMainFrame.this.desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, SchemeEditorMainFrame.this.aContext);
				return additionalFrame;
			}
		});
		
		this.frames.put(FRAME_TREE, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TREE_FRAME", Level.FINEST);
				JInternalFrame treeFrame = new JInternalFrame();
				treeFrame.setName(FRAME_TREE);
				treeFrame.setIconifiable(true);
				treeFrame.setClosable(true);
				treeFrame.setResizable(true);
				treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				treeFrame.setTitle(I18N.getString(FRAME_TREE));
				
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
				SchemeEditorMainFrame f = (SchemeEditorMainFrame) this.mainframe;
				Rectangle r = SchemeEditorMainFrame.this.scrollPane.getViewportBorderBounds();
				int w = r.width + SchemeEditorMainFrame.this.scrollPane.getVerticalScrollBar().getVisibleRect().width;
				int h = r.height + SchemeEditorMainFrame.this.scrollPane.getHorizontalScrollBar().getVisibleRect().height;
				Dimension size = new Dimension(w, h);
				SchemeEditorMainFrame.this.desktopPane.setPreferredSize(size);
				SchemeEditorMainFrame.this.desktopPane.setSize(size);

				JInternalFrame editorFrame = (JInternalFrame) f.frames.get(FRAME_EDITOR_MAIN);
				JInternalFrame generalFrame = (JInternalFrame) f.frames.get(GeneralPropertiesFrame.NAME);
				JInternalFrame characteristicsFrame = (JInternalFrame) f.frames.get(CharacteristicPropertiesFrame.NAME);
				JInternalFrame additionalFrame = (JInternalFrame) f.frames.get(AdditionalPropertiesFrame.NAME);
				JInternalFrame treeFrame = (JInternalFrame) f.frames.get(FRAME_TREE);
				
				normalize(editorFrame);
				editorFrame.setSize(3 * w / 5, h);
				editorFrame.setLocation(w / 5, 0);

				normalize(generalFrame);
				generalFrame.setSize(w/5, h/2);
				generalFrame.setLocation(4*w/5, 0);

				normalize(characteristicsFrame);
				characteristicsFrame.setSize(w/5, h / 4);
				characteristicsFrame.setLocation(4*w/5, h/2);

				normalize(additionalFrame);
				additionalFrame.setSize(w / 5, h / 4);
				additionalFrame.setLocation(4 * w / 5, 3 * h / 4);

				normalize(treeFrame);
				treeFrame.setSize(w / 5, h);
				treeFrame.setLocation(0, 0);
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
		aModel.setCommand("menuSchemeSave", new SchemeSaveAllCommand(this.schemeTab));
		aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand(this.aContext, this.schemeTab));
		aModel.setCommand("menuSchemeValidate", new ValidateSchemeCommand(this.schemeTab));

		
		aModel.setCommand("Menu.import.protos", new ProtoElementsImportCommand(this.schemeTab));
		aModel.setCommand("Menu.import.config", new ConfigImportCommand(this.schemeTab));
		aModel.setCommand("Menu.import.scheme", new SchemeImportCommand(this.schemeTab));
		aModel.setCommand("Menu.import.commit", new SchemeImportCommitCommand(this.aContext));
		
		aModel.setCommand("Menu.export.protos", new ProtoElementsExportCommand(this.schemeTab));
		aModel.setCommand("Menu.export.config", new ConfigExportCommand(this.schemeTab));
		aModel.setCommand("Menu.export.scheme", new SchemeExportCommand(this.schemeTab));
		
		aModel.setCommand("menuPathNew", new PathNewCommand(this.schemeTab));
		aModel.setCommand("menuPathSave", new PathSaveCommand(this.schemeTab));
		aModel.setCommand("menuPathCancel", new PathCancelCommand(this.schemeTab));
		aModel.setCommand("menuPathEdit", new PathEditCommand(this.schemeTab));

//		CreateSchemeReportCommand rc = new CreateSchemeReportCommand(aContext);
//		for (Iterator it = graphs.iterator(); it.hasNext();)
//			rc.setParameter(CreateSchemeReportCommand.PANEL, it.next());
//		rc.setParameter(CreateSchemeReportCommand.TYPE, ReportTemplate.rtt_Scheme);
		aModel.setCommand("menuReportCreate", new CreateSchemeReportCommand(this.aContext, this.schemeTab));

		aModel.setCommand("menuWindowTree", this.getLazyCommand(FRAME_TREE));
		aModel.setCommand("menuWindowScheme", this.getLazyCommand(FRAME_EDITOR_MAIN));
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
		if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			ObjectSelectedEvent sne = (ObjectSelectedEvent) ae;
			if (sne.isSelected(ObjectEntities.SCHEMEPATH_CODE)) {
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

		SchemePermissionManager.setSchemeTranslation();
		boolean creationAllowed = SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.CREATE); 
		boolean savingAllowed = SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.SAVE);
		boolean editionAllowed = SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
		
		this.schemeTab.setEditable(editionAllowed);
		
		aModel.setEnabled("menuWindowTree", true);
		aModel.setEnabled("menuWindowScheme", true);
		aModel.setEnabled("menuWindowUgo", true);
		aModel.setEnabled("menuWindowProps", true);
		aModel.setEnabled("menuWindowList", true);
		
		aModel.setEnabled("Menu.export", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.export.scheme", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.export.config", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.export.protos", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.import", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.import.scheme", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.import.config", creationAllowed && savingAllowed);
		aModel.setEnabled("Menu.import.protos", creationAllowed && savingAllowed);
		
		aModel.setEnabled("menuSchemeNew", creationAllowed);
		aModel.setEnabled("menuSchemeLoad", true);
		aModel.setEnabled("menuSchemeSave", savingAllowed);
		aModel.setEnabled("menuSchemeSaveAs", savingAllowed);
		aModel.setEnabled("menuSchemeValidate", editionAllowed);
		aModel.setEnabled("menuPathNew", editionAllowed);
		aModel.setEnabled("menuReportCreate", true);
		
		aModel.fireModelChanged("");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				aModel.getCommand("menuWindowScheme").execute();
				aModel.getCommand("menuWindowTree").execute();
				aModel.getCommand("menuWindowUgo").execute();
				aModel.getCommand("menuWindowProps").execute();
				aModel.getCommand("menuWindowList").execute();
				
				aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
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
		aModel.setEnabled("menuSchemeValidate", false);
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
			AbstractMainFrame.getGlobalDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, SchemeEditorMainFrame.this);
			SchemeEditorMainFrame.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
		}
		super.processWindowEvent(e);
	}
}

