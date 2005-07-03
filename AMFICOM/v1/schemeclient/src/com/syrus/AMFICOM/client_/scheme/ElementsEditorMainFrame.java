/*-
 * $Id: ElementsEditorMainFrame.java,v 1.6 2005/06/22 10:16:05 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.General.Command.Scheme.ComponentNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.ComponentSaveCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.UI.GeneralPropertiesFrame;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
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
import com.syrus.AMFICOM.client_.scheme.ui.SchemeEventHandler;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeUI;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.filterclient.MeasurementConditionWrapper;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/06/22 10:16:05 $
 * @module schemeclient_v1
 */

public class ElementsEditorMainFrame extends AbstractMainFrame {

	public static final String	EDITOR_FRAME	= "editorFrame";
	public static final String	GENERAL_PROPERIES_FRAME	= "generalFrame";
	public static final String	CHARACTERISTIC_PROPERIES_FRAME = "characteristicFrame";
	public static final String	ADDITIONAL_PROPERIES_FRAME = "additionalFrame";
	public static final String	TREE_FRAME = "treeFrame";
	
	UIDefaults					frames;
	
	ElementsTabbedPane elementsTab;

	public ElementsEditorMainFrame(final ApplicationContext aContext) {
		super(aContext, LangModelSchematics.getString("ElementsEditorTitle"),
				new ElementsEditorMenuBar(aContext.getApplicationModel()),
				new ElementsEditorToolBar());
		
		this.addComponentListener(new ComponentAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
			 */
			public void componentShown(ComponentEvent e) {
				initModule();
				ElementsEditorMainFrame.this.desktopPane.setPreferredSize(ElementsEditorMainFrame.this.desktopPane.getSize());
				ElementsEditorMainFrame.this.windowArranger.arrange();
			}
		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ElementsEditorMainFrame.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
						ElementsEditorMainFrame.this);
				Environment.getDispatcher()
						.removePropertyChangeListener(ContextChangeEvent.TYPE, ElementsEditorMainFrame.this);
				aContext.getApplicationModel().getCommand("menuExit").execute();
			}
		});

		this.frames = new UIDefaults();
		
		this.frames.put(EDITOR_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | EDITOR_FRAME", Log.FINEST);
				elementsTab = new ElementsTabbedPane(aContext);
				SchemeViewerFrame editorFrame = new SchemeViewerFrame(aContext, elementsTab);
				editorFrame.setClosable(false);
				editorFrame.setTitle(LangModelSchematics.getString("elementsMainTitle"));
				desktopPane.add(editorFrame);
				return editorFrame;
			}
		});
		
		this.frames.put(GENERAL_PROPERIES_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | GENERAL_PROPERIES_FRAME", Log.FINEST);
				GeneralPropertiesFrame generalFrame = new GeneralPropertiesFrame("Title");
				desktopPane.add(generalFrame);
				new SchemeEventHandler(generalFrame, ElementsEditorMainFrame.this.aContext);
				return generalFrame;
			}
		});

		this.frames.put(CHARACTERISTIC_PROPERIES_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | CHARACTERISTIC_PROPERIES_FRAME", Log.FINEST);
				CharacteristicPropertiesFrame characteristicFrame = new CharacteristicPropertiesFrame("Title");
				desktopPane.add(characteristicFrame);
				new SchemeEventHandler(characteristicFrame, ElementsEditorMainFrame.this.aContext);
				return characteristicFrame;
			}
		});

		this.frames.put(ADDITIONAL_PROPERIES_FRAME, new UIDefaults.LazyValue() {
			public Object createValue(UIDefaults table) {
				Log.debugMessage(".createValue | ADDITIONAL_PROPERIES_FRAME", Log.FINEST);
				AdditionalPropertiesFrame additionalFrame = new AdditionalPropertiesFrame("Title");
				desktopPane.add(additionalFrame);
				new SchemeEventHandler(additionalFrame, ElementsEditorMainFrame.this.aContext);
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
				treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
				treeFrame.setTitle(LangModelSchematics.getString("treeFrameTitle"));
				treeFrame.setSize(new Dimension(100,200));
				treeFrame.setMinimumSize(treeFrame.getSize());
				treeFrame.setPreferredSize(treeFrame.getSize());

				
				SchemeTreeModel model = new SchemeTreeModel(ElementsEditorMainFrame.this.aContext);
				PopulatableIconedNode root = new PopulatableIconedNode(model, "root", "Сеть");
				TreeFilterUI tfUI = new TreeFilterUI(new SchemeTreeUI(root, aContext), new FilterPanel());

				treeFrame.getContentPane().setLayout(new BorderLayout());
				treeFrame.getContentPane().add(tfUI.getPanel(), BorderLayout.CENTER);

				desktopPane.add(treeFrame);
				return treeFrame;
			}
		});
		
		super.setWindowArranger(new WindowArranger(ElementsEditorMainFrame.this) {

			public void arrange() {
				ElementsEditorMainFrame f = (ElementsEditorMainFrame) mainframe;

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
				additionalFrame.setSize(w / 5, h / 3);
				generalFrame.setSize(w/5, h / 3);
				characteristicsFrame.setSize(w/5, h / 3);
				treeFrame.setSize(w / 5, h);

				editorFrame.setLocation(w / 5, 0);
				additionalFrame.setLocation(4 * w / 5, 2 * h / 3);
				generalFrame.setLocation(4*w/5, 0);
				characteristicsFrame.setLocation(4*w/5, h/3);
				treeFrame.setLocation(0, 0);
			}
		});
	}

	public ElementsEditorMainFrame() {
		this(new ApplicationContext());
	}

	public void initModule() {
		super.initModule();
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.setCommand("menuComponentNew", new ComponentNewCommand(aContext,
				elementsTab, null));
		aModel.setCommand("menuComponentSave", new ComponentSaveCommand(aContext,
				elementsTab, null));

		aModel.setCommand("menuWindowArrange", new ArrangeWindowCommand(this.windowArranger));
		aModel.setCommand("menuWindowTree", this.getLazyCommand(TREE_FRAME));
		aModel.setCommand("menuWindowScheme", this.getLazyCommand(EDITOR_FRAME));
		aModel.setCommand("menuWindowUgo", this.getLazyCommand(ADDITIONAL_PROPERIES_FRAME));
		aModel.setCommand("menuWindowProps", this.getLazyCommand(CHARACTERISTIC_PROPERIES_FRAME));
		aModel.setCommand("menuWindowList", this.getLazyCommand(GENERAL_PROPERIES_FRAME));
		
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

	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);

		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuComponent", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setVisible("menuSessionDomain", false);
		aModel.setVisible("menuSchemeExport", false);
		aModel.setVisible("menuSchemeImport", false);
	}

	public void setSessionOpened() {
		super.setSessionOpened();
		
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuWindowArrange", true);
		aModel.setEnabled("menuWindowTree", true);
		aModel.setEnabled("menuWindowScheme", true);
		aModel.setEnabled("menuWindowUgo", true);
		aModel.setEnabled("menuWindowProps", true);
		aModel.setEnabled("menuWindowList", true);

		aModel.fireModelChanged("");

		JInternalFrame editorFrame = (JInternalFrame)this.frames.get(EDITOR_FRAME);
		editorFrame.setVisible(true);
		JInternalFrame addFrame = (JInternalFrame)this.frames.get(ADDITIONAL_PROPERIES_FRAME);
		addFrame.setVisible(true);
		JInternalFrame generalFrame = (JInternalFrame)this.frames.get(GENERAL_PROPERIES_FRAME);
		generalFrame.setVisible(true);
		JInternalFrame charFrame = (JInternalFrame)this.frames.get(CHARACTERISTIC_PROPERIES_FRAME);
		charFrame.setVisible(true);
		JInternalFrame treeFrame = (JInternalFrame)this.frames.get(TREE_FRAME);
		treeFrame.setVisible(true);
	}

	public void setSessionClosed() {
		super.setSessionClosed();
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuWindowTree", false);
		aModel.setEnabled("menuWindowScheme", false);
		aModel.setEnabled("menuWindowCatalog", false);
		aModel.setEnabled("menuWindowUgo", false);
		aModel.setEnabled("menuWindowProps", false);
		aModel.setEnabled("menuWindowList", false);

		aModel.fireModelChanged("");

		JInternalFrame editorFrame = (JInternalFrame)this.frames.get(EDITOR_FRAME);
		editorFrame.setVisible(false);
		JInternalFrame addFrame = (JInternalFrame)this.frames.get(ADDITIONAL_PROPERIES_FRAME);
		addFrame.setVisible(false);
		JInternalFrame generalFrame = (JInternalFrame)this.frames.get(GENERAL_PROPERIES_FRAME);
		generalFrame.setVisible(false);
		JInternalFrame charFrame = (JInternalFrame)this.frames.get(CHARACTERISTIC_PROPERIES_FRAME);
		charFrame.setVisible(false);
		JInternalFrame treeFrame = (JInternalFrame)this.frames.get(TREE_FRAME);
		treeFrame.setVisible(false);
	}

}