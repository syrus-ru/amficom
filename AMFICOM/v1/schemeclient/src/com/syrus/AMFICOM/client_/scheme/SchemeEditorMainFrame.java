/*-
 * $Id: SchemeEditorMainFrame.java,v 1.9 2005/06/22 10:16:05 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/06/22 10:16:05 $
 * @module schemeclient_v1
 */

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.Schematics.UI.ElementsNavigatorPanel;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.ui.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

public class SchemeEditorMainFrame extends AbstractMainFrame {
	SchemeGraph scheme_graph;

	static int scheme_count = 0;

	public static final String	EDITOR_FRAME	= "editorFrame";
	public static final String	GENERAL_PROPERIES_FRAME	= "generalFrame";
	public static final String	CHARACTERISTIC_PROPERIES_FRAME = "characteristicFrame";
	public static final String	ADDITIONAL_PROPERIES_FRAME = "additionalFrame";
	public static final String	TREE_FRAME = "treeFrame";
		
	ArrayList graphs = new ArrayList();
	SchemeTabbedPane schemeTab;
	
	UIDefaults frames;

	public SchemeEditorMainFrame(ApplicationContext aContext) {
		super(aContext, LangModelSchematics.getString("SchemeEditorTitle"), new SchemeEditorMenuBar(aContext.getApplicationModel()), new SchemeEditorToolBar());

		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				initModule();
				desktopPane.setPreferredSize(desktopPane.getSize());
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						SchemeEditorMainFrame.this.windowArranger.arrange();
					}
				});
			}
		});
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				UgoPanel p = schemeTab.getCurrentPanel();
				while (p != null) {
					if (!schemeTab.removePanel(p))
						return;
					p = schemeTab.getCurrentPanel();
				}
				dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, SchemeEditorMainFrame.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, SchemeEditorMainFrame.this);
				SchemeEditorMainFrame.this.aContext.getApplicationModel().getCommand("menuExit").execute();
			}
		});

		this.frames = new UIDefaults();
		this.frames.put(EDITOR_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EDITOR_FRAME", Log.FINEST);
				schemeTab = new SchemeTabbedPane(SchemeEditorMainFrame.this.aContext);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(SchemeEditorMainFrame.this.aContext, schemeTab);
				editorFrame.setTitle(LangModelSchematics.getString("schemeMainTitle"));
				desktopPane.add(editorFrame);
				return editorFrame;
			}
		});

		this.frames.put(GENERAL_PROPERIES_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Log.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame("Title");
				desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, SchemeEditorMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(CHARACTERISTIC_PROPERIES_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Log.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame("Title");
				desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, SchemeEditorMainFrame.this.aContext);
				return characteristicFrame;
			}
		});

		this.frames.put(ADDITIONAL_PROPERIES_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Log.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame("Title");
				desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, SchemeEditorMainFrame.this.aContext);
				return additionalFrame;
			}
		});
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | TREE_FRAME", Log.FINEST);
				JInternalFrame treeFrame = new JInternalFrame();
				treeFrame.setIconifiable(true);
				treeFrame.setClosable(true);
				treeFrame.setResizable(true);
				treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				treeFrame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						"images/general.gif")));
				treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
				
				SchemeTreeModel model = new SchemeTreeModel(SchemeEditorMainFrame.this.aContext);
				ElementsNavigatorPanel utp = new ElementsNavigatorPanel(SchemeEditorMainFrame.this.aContext,
						dispatcher, model);
				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(utp, BorderLayout.CENTER);
				
				desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		this.setWindowArranger(new WindowArranger(SchemeEditorMainFrame.this) {
					public void arrange() {
						SchemeEditorMainFrame f = (SchemeEditorMainFrame) this.mainframe;

						int w = f.desktopPane.getSize().width;
						int h = f.desktopPane.getSize().height;
						
						JInternalFrame editorFrame = (JInternalFrame) f.frames.get(SchemeEditorMainFrame.EDITOR_FRAME);
						JInternalFrame generalFrame = (JInternalFrame) f.frames.get(SchemeEditorMainFrame.GENERAL_PROPERIES_FRAME);
						JInternalFrame characteristicsFrame = (JInternalFrame) f.frames.get(SchemeEditorMainFrame.CHARACTERISTIC_PROPERIES_FRAME);
						JInternalFrame additionalFrame = (JInternalFrame) f.frames.get(SchemeEditorMainFrame.ADDITIONAL_PROPERIES_FRAME);
						JInternalFrame treeFrame = (JInternalFrame) f.frames.get(SchemeEditorMainFrame.TREE_FRAME);
						
						normalize(editorFrame);
						normalize(generalFrame);
						normalize(characteristicsFrame);
						normalize(additionalFrame);
						normalize(treeFrame);

						editorFrame.setSize(3 * w / 5, h);
						additionalFrame.setSize(w / 5, 2 * h / 5);
						generalFrame.setSize(w/5, 3 * h / 10);
						characteristicsFrame.setSize(w/5, 3 * h / 10);
						treeFrame.setSize(w / 5, 2 * h / 5);

						editorFrame.setLocation(2 * w / 5, 0);
						additionalFrame.setLocation(w / 5, 0);
						generalFrame.setLocation(4*w/5, 0);
						characteristicsFrame.setLocation(4*w/5, 3*h/10);
						treeFrame.setLocation(0, 0);
					}
				});
			}
	
	
	public SchemeEditorMainFrame() {
		this(new ApplicationContext());
	}

		
	public void initModule() {
		super.initModule();
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setCommand("menuSchemeNew", new SchemeNewCommand(aContext));
		aModel.setCommand("menuSchemeLoad", new SchemeOpenCommand(aContext));
		aModel.setCommand("menuSchemeSave", new SchemeSaveCommand(aContext, schemeTab));
		aModel.setCommand("menuSchemeSaveAs", new SchemeSaveAsCommand(aContext, schemeTab));

		
//		aModel.setCommand("menuSchemeExport", new SchemeToFileCommand(Environment
//				.getDispatcher(), aContext));
		// aModel.setCommand("menuSchemeImport", new
		// SchemeFromFileCommand(Environment.getDispatcher(), aContext));

		// TODO разобраться с созданием пути
		/*
		aModel.setCommand("menuPathNew", new PathNewCommand(aContext, schemeTab));
		aModel.setCommand("menuPathEdit", new PathEditCommand(aContext, schemeTab));
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

		aModel.setCommand("menuPathSave", new PathSaveCommand(aContext, schemeTab));
		aModel.setCommand("menuPathCancel", new PathCancelCommand(aContext,
				schemeTab));
		aModel.setCommand("menuPathDelete", new PathDeleteCommand(aContext,
				schemeTab));*/

//		CreateSchemeReportCommand rc = new CreateSchemeReportCommand(aContext);
//		for (Iterator it = graphs.iterator(); it.hasNext();)
//			rc.setParameter(CreateSchemeReportCommand.PANEL, it.next());
//		rc.setParameter(CreateSchemeReportCommand.TYPE, ReportTemplate.rtt_Scheme);
//		aModel.setCommand("menuReportCreate", rc);

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(new WindowArranger(this) {
			public void arrange() {
				SchemeEditorMainFrame.this.windowArranger.arrange();
			}
		}));
		
		aModel.setCommand("menuWindowTree", this.getLazyCommand(TREE_FRAME));
		aModel.setCommand("menuWindowScheme", this.getLazyCommand(EDITOR_FRAME));
		aModel.setCommand("menuWindowUgo", this.getLazyCommand(ADDITIONAL_PROPERIES_FRAME));
		aModel.setCommand("menuWindowProps", this.getLazyCommand(GENERAL_PROPERIES_FRAME));
		aModel.setCommand("menuWindowList", this.getLazyCommand(CHARACTERISTIC_PROPERIES_FRAME));

		aModel.fireModelChanged("");

//		if (ClientSessionEnvironment.getInstance().sessionEstablished()) {
//			internal_dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_OPENED_EVENT), true);
//		}
	}

	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);
		aModel.setEnabled("menuScheme", true);
		aModel.setEnabled("menuPath", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);
	}

	public void propertyChange(PropertyChangeEvent ae) {
		if (ae.getPropertyName().equals(CreatePathEvent.TYPE)) {
			CreatePathEvent cpe = (CreatePathEvent) ae;
			if (cpe.CREATE_PATH || cpe.EDIT_PATH) {
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathSave", true);
				aModel.setEnabled("menuPathAddStart", true);
				aModel.setEnabled("menuPathAddEnd", true);
				aModel.setEnabled("menuPathAddLink", true);
				aModel.setEnabled("menuPathCancel", true);
				aModel.setEnabled("menuPathAutoCreate", true);
				aModel.getCommand("menuPathAutoCreate").setParameter("panel",
						schemeTab.getCurrentPanel());
				aModel.fireModelChanged("");
			}
			if (cpe.CANCEL_PATH_CREATION || cpe.SAVE_PATH) {
				ApplicationModel aModel = aContext.getApplicationModel();
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
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel")) {
					aModel.setEnabled("menuPathRemoveLink", true);
					aModel.fireModelChanged("");
				}
			} else {
				ApplicationModel aModel = aContext.getApplicationModel();
				if (aModel.isEnabled("menuPathCancel")) {
					aModel.setEnabled("menuPathRemoveLink", false);
					aModel.fireModelChanged("");
				}
			}
		} else if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			ObjectSelectedEvent sne = (ObjectSelectedEvent) ae;
			if (sne.isSelected(ObjectSelectedEvent.SCHEME_PATH)) {
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuPathEdit", true);
				aModel.setEnabled("menuPathDelete", true);
				aModel.fireModelChanged("");
			} else {
				ApplicationModel aModel = aContext.getApplicationModel();
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
		else if (ae.getPropertyName().equals(SchemeEvent.TYPE)) {
			SchemeEvent se = (SchemeEvent)ae;
			if (se.isType(SchemeEvent.OPEN_SCHEME)) {
				Scheme scheme = (Scheme)se.getObject();
				schemeTab.openScheme(scheme);
			} 
			else if (se.isType(SchemeEvent.OPEN_SCHEMEELEMENT)) {
				Identifier se_id = (Identifier) ae.getSource();
				SchemeElement sel = (SchemeElement)se.getObject();
				schemeTab.openSchemeElement(sel);
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

			public void execute() {
				this.getLazyCommand().execute();
			}
		};
	}
	
	public void setSessionOpened() {
		super.setSessionOpened();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSchemeExport", true);
		aModel.setEnabled("menuSchemeImport", true);

		aModel.setEnabled("menuWindowArrange", true);
		aModel.setEnabled("menuWindowTree", true);
		aModel.setEnabled("menuWindowScheme", true);
		aModel.setEnabled("menuWindowCatalog", true);
		aModel.setEnabled("menuWindowUgo", true);
		aModel.setEnabled("menuWindowProps", true);
		aModel.setEnabled("menuWindowList", true);
		aModel.fireModelChanged("");
	}

	public void setDomainSelected() {
		super.setDomainSelected();
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSchemeNew", true);
		aModel.setEnabled("menuSchemeLoad", true);
		aModel.setEnabled("menuSchemeSave", true);
		aModel.setEnabled("menuSchemeSaveAs", true);
		aModel.setEnabled("menuInsertToCatalog", true);
		aModel.setEnabled("menuPathNew", true);
		aModel.setEnabled("menuReportCreate", true);

		aModel.fireModelChanged();
	}

	public void setSessionClosed() {
		super.setSessionClosed();
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSchemeNew", false);
		aModel.setEnabled("menuSchemeLoad", false);
		aModel.setEnabled("menuSchemeSave", false);
		aModel.setEnabled("menuSchemeSaveAs", false);
		aModel.setEnabled("menuInsertToCatalog", false);
		aModel.setEnabled("menuSchemeExport", false);
		aModel.setEnabled("menuSchemeImport", false);
		aModel.setEnabled("menuPathNew", false);
		aModel.setEnabled("menuReportCreate", false);

		aModel.setEnabled("menuWindowArrange", false);
		aModel.setEnabled("menuWindowTree", false);
		aModel.setEnabled("menuWindowScheme", false);
		aModel.setEnabled("menuWindowCatalog", false);
		aModel.setEnabled("menuWindowUgo", false);
		aModel.setEnabled("menuWindowProps", false);
		aModel.setEnabled("menuWindowList", false);

		aModel.fireModelChanged("");
	}

	void this_windowClosing(WindowEvent e) {
		UgoPanel p = schemeTab.getCurrentPanel();
		while (p != null) {
			/*
			 * if (p[i].getGraph().getSchemeElement() != null &&
			 * p[i].getGraph().isGraphChanged()) { schemeTab.selectPanel(p[i]); int
			 * res = JOptionPane.showConfirmDialog( Environment.getActiveWindow(),
			 * "Элемент \"" + p[i].getGraph().getSchemeElement().getName() + "\" был
			 * изменен. Сохранить схему перед закрытием?", "Подтверждение",
			 * JOptionPane.YES_NO_CANCEL_OPTION); if (res == JOptionPane.OK_OPTION) {
			 * SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, schemeTab,
			 * ugoPane); ssc.execute(); if (ssc.ret_code == SchemeSaveCommand.CANCEL)
			 * return; } else if (res == JOptionPane.CANCEL_OPTION) return; }
			 */
			if (!schemeTab.removePanel(p))
				return;
			p = schemeTab.getCurrentPanel();
		}

		/*
		 * p = schemeTab.getAllPanels(); for (int i = 0; i < p.length; i++) {
		 * schemeTab.selectPanel(p[i]);
		 * 
		 * if (p[i].getGraph().getScheme() != null &&
		 * p[i].getGraph().isGraphChanged()) { schemeTab.selectPanel(p[i]); int res =
		 * JOptionPane.showConfirmDialog( Environment.getActiveWindow(), "Схема \"" +
		 * p[i].getGraph().getScheme().getName() + "\" была изменена. Сохранить
		 * схему перед закрытием?", "Подтверждение",
		 * JOptionPane.YES_NO_CANCEL_OPTION); if (res == JOptionPane.OK_OPTION) {
		 * SchemeSaveCommand ssc = new SchemeSaveCommand(aContext, schemeTab,
		 * ugoPane); ssc.execute(); if (ssc.ret_code == SchemeSaveCommand.CANCEL)
		 * return; } else if (res == JOptionPane.CANCEL_OPTION) return; } if
		 * (!schemeTab.removePanel(p[i])) return; }
		 */
	}
	
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this_windowClosing(e);
		}
		super.processWindowEvent(e);
	}
}

