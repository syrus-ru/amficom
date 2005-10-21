/*-
 * $Id: ElementsEditorMainFrame.java,v 1.20 2005/10/21 11:55:14 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.General.Command.Scheme.ComponentNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ComponentSaveCommand;
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
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.FullSchemeTreeModel;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeUI;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.20 $, $Date: 2005/10/21 11:55:14 $
 * @module schemeclient
 */

public class ElementsEditorMainFrame extends AbstractMainFrame {
	private static final long serialVersionUID = -7638134017425749912L;

	public static final String	TREE_FRAME = "treeFrame";
	
	UIDefaults					frames;
	
	ElementsTabbedPane elementsTab;

	public ElementsEditorMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelSchematics.getString("ElementsEditorTitle"), //$NON-NLS-1$
				new ElementsEditorMenuBar(aContext.getApplicationModel()),
				new ElementsEditorToolBar());
	}

	public ElementsEditorMainFrame() {
		this(new ApplicationContext());
	}

	protected void initFrames() {
		this.frames = new UIDefaults();
		
		this.elementsTab = new ElementsTabbedPane(this.aContext);
		this.elementsTab.setEditable(true);
		
		this.frames.put(SchemeViewerFrame.NAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EDITOR_FRAME", Level.FINEST);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(ElementsEditorMainFrame.this.aContext, ElementsEditorMainFrame.this.elementsTab);
				editorFrame.setTitle(LangModelSchematics.getString("elementsMainTitle"));
				ElementsEditorMainFrame.this.desktopPane.add(editorFrame);
				return editorFrame;
			}
		});
		
		this.frames.put(GeneralPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Level.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame(LangModelScheme.getString("Title.properties"));
				ElementsEditorMainFrame.this.desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, ElementsEditorMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(CharacteristicPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Level.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame(LangModelScheme.getString("Title.characteristics"));
				ElementsEditorMainFrame.this.desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, ElementsEditorMainFrame.this.aContext);
				return characteristicFrame;
			}
		});

		this.frames.put(AdditionalPropertiesFrame.NAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Level.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame(LangModelScheme.getString("Title.additional"));
				ElementsEditorMainFrame.this.desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, ElementsEditorMainFrame.this.aContext);
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
				
				FullSchemeTreeModel model = new FullSchemeTreeModel(ElementsEditorMainFrame.this.aContext);
				TreeFilterUI tfUI = new TreeFilterUI(new SchemeTreeUI(model.getRoot(), ElementsEditorMainFrame.this.aContext), new FilterPanel());

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				ElementsEditorMainFrame.this.desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		super.setWindowArranger(new WindowArranger(ElementsEditorMainFrame.this) {

			@Override
			public void arrange() {
				Rectangle r = ElementsEditorMainFrame.this.scrollPane.getViewportBorderBounds();
				int w = r.width + ElementsEditorMainFrame.this.scrollPane.getVerticalScrollBar().getWidth();
				int h = r.height + ElementsEditorMainFrame.this.scrollPane.getHorizontalScrollBar().getHeight();
				Dimension size = new Dimension(w, h);
				ElementsEditorMainFrame.this.desktopPane.setPreferredSize(size);
				ElementsEditorMainFrame.this.desktopPane.setSize(size);

				JInternalFrame editorFrame = null;
				JInternalFrame generalFrame = null;
				JInternalFrame characteristicsFrame = null;
				JInternalFrame additionalFrame = null;
				JInternalFrame treeFrame = null;
				
				for (Component component : ElementsEditorMainFrame.this.desktopPane.getComponents()) {
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
	
	@Override
	protected void initModule() {
		super.initModule();
		
		initFrames();
		
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.setCommand("menuComponentNew", new ComponentNewCommand(this.aContext, this.elementsTab));
		aModel.setCommand("menuComponentSave", new ComponentSaveCommand(this.elementsTab));

		aModel.setCommand("menuWindowTree", this.getLazyCommand(TREE_FRAME));
		aModel.setCommand("menuWindowScheme", this.getLazyCommand(SchemeViewerFrame.NAME));
		aModel.setCommand("menuWindowUgo", this.getLazyCommand(AdditionalPropertiesFrame.NAME));
		aModel.setCommand("menuWindowProps", this.getLazyCommand(CharacteristicPropertiesFrame.NAME));
		aModel.setCommand("menuWindowList", this.getLazyCommand(GeneralPropertiesFrame.NAME));

		setDefaultModel(aModel);
		aModel.fireModelChanged("");
	}

	private AbstractCommand getLazyCommand(final Object key) {
		return new AbstractCommand() {

			private Command	command;

			private Command getLazyCommand() {
				if (this.command == null) {
					Object object = ElementsEditorMainFrame.this.frames.get(key);
					if (object instanceof JInternalFrame) {
						Log.debugMessage("init getLazyCommand for " + key, Level.FINEST);
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
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);

		aModel.setEnabled("menuComponent", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setVisible("menuSessionDomain", false);
		aModel.setVisible("Menu.export", false);
		aModel.setVisible("Menu.import", false);
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

		aModel.setEnabled("menuComponentNew", true);
		aModel.setEnabled("menuComponentSave", true);
		
		aModel.fireModelChanged("");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				aModel.getCommand("menuComponentNew").execute();
			}
		});
	}

	@Override
	public void loggedOut() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
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
			if (!this.elementsTab.confirmUnsavedChanges()) {
				return;
			}
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, ElementsEditorMainFrame.this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, ElementsEditorMainFrame.this);
			ElementsEditorMainFrame.this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
		}
		super.processWindowEvent(e);
	}
}