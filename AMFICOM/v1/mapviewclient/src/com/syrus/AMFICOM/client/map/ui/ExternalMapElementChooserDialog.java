/*-
 * $$Id: ExternalMapElementChooserDialog.java,v 1.15 2006/02/15 11:13:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2006/02/15 11:13:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ExternalMapElementChooserDialog extends JDialog 
		implements TreeSelectionListener {

	static public final int RET_OK = 1;
	static public final int RET_CANCEL = 2;

	protected JPanel topPanel = new JPanel();
	protected JButton buttonCancel = new JButton();
	protected JButton buttonOpen = new JButton();

	protected JScrollPane scrollPane = new JScrollPane();

	protected SiteNode retObject;
	protected int retCode = 2;
	
	protected JPanel eastPanel = new JPanel();
	protected JPanel westPanel = new JPanel();
	protected JPanel bottomPanel = new JPanel();
	protected BorderLayout borderLayout1 = new BorderLayout();
	protected BorderLayout borderLayout2 = new BorderLayout();
	protected FlowLayout flowLayout2 = new FlowLayout();
	protected FlowLayout flowLayout3 = new FlowLayout();
	protected BorderLayout borderLayout3 = new BorderLayout();

	LogicalTreeUI treeUI;
	JTree tree;
	ItemTreeModel treeModel;
	MapTreeModel model;
	TreeCellRenderer treeRenderer;
	Item root;

	Map map;

	public ExternalMapElementChooserDialog(Map map, String title) throws HeadlessException {
		super(Environment.getActiveWindow(), title, true);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			Log.errorMessage(e);
		}
		this.map = map;
		Item item = this.model.createSingleMapRoot(map);
		this.model.populate(item);
		this.root.addChild(item);
	}

	protected void jbInit() throws Exception
	{
		this.setResizable(false);
		this.setSize(new Dimension(550, 320));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);

		this.getContentPane().setLayout(this.borderLayout2);
		this.topPanel.setLayout(this.borderLayout3);
		this.topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.buttonCancel.setText(I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL));
		this.buttonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonCancel_actionPerformed(e);
				}
			});
		this.buttonOpen.setText(I18N.getString(MapEditorResourceKeys.BUTTON_CHOOSE));
		this.buttonOpen.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonOpen_actionPerformed(e);
				}
			});
		this.eastPanel.setLayout(this.flowLayout3);
		this.westPanel.setLayout(this.flowLayout2);
		this.bottomPanel.setLayout(this.borderLayout1);
		this.flowLayout3.setAlignment(2);
		this.eastPanel.add(this.buttonOpen, null);
		this.eastPanel.add(this.buttonCancel, null);
		this.bottomPanel.add(this.westPanel, BorderLayout.WEST);
		this.bottomPanel.add(this.eastPanel, BorderLayout.CENTER);
		this.getContentPane().add(this.bottomPanel, BorderLayout.SOUTH);
		this.getContentPane().add(this.topPanel, BorderLayout.CENTER);

		this.model = new MapTreeModel();

		this.root = new IconedNode("root", I18N.getString(MapEditorResourceKeys.TREE_MAP)); //$NON-NLS-1$
		this.treeUI = new LogicalTreeUI(this.root, false);
		this.tree = this.treeUI.getTree();
		this.treeModel = (ItemTreeModel )this.tree.getModel();
		this.treeModel.setAllwaysSort(false);

		this.scrollPane.getViewport().add(this.tree);
		this.tree.addTreeSelectionListener(this);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
//		this.treeRenderer = new MapViewTreeCellRenderer(this.model);
//		this.tree.setCellRenderer(this.treeRenderer);

		this.topPanel.add(this.scrollPane, BorderLayout.CENTER);
		this.buttonOpen.setEnabled(false);
	}

	public int getReturnCode()
	{
		return this.retCode;
	}
	
	public SiteNode getReturnObject()
	{
		return this.retObject;
	}
	
	protected void buttonOpen_actionPerformed(ActionEvent e)
	{
		if(this.retObject == null)
			return;

		this.retCode = RET_OK;
		this.dispose();
	}

	protected void buttonCancel_actionPerformed(ActionEvent e)
	{
		this.retCode = RET_CANCEL;
		this.dispose();
	}

	public void valueChanged(TreeSelectionEvent e) {
		this.buttonOpen.setEnabled(false);
		this.retObject = null;
		TreePath paths[] = this.tree.getSelectionPaths();
		for (int i = 0; i < paths.length; i++) 
		{
			Item node = (Item )paths[i].getLastPathComponent();
			if(node.getObject() instanceof SiteNode) {
				this.buttonOpen.setEnabled(true);
				this.retObject = (SiteNode)node.getObject();
				break;
			}
		}
	}

}
