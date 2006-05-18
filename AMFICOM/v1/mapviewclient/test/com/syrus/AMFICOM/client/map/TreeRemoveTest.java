/**
 * $Id: TreeRemoveTest.java,v 1.2 2005/09/25 15:51:21 krupenn Exp $
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;

public class TreeRemoveTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		ResourceBundle resourceBundle = 
			ResourceBundle.getBundle("com.syrus.AMFICOM.client.map.treetest");
		String item1 = resourceBundle.getString("item1");
		String item2 = resourceBundle.getString("item2");
		String item3 = resourceBundle.getString("item3");
		String item4 = resourceBundle.getString("item4");
		
		Item root = new IconedNode("root", "root");
		final Item child1 = new IconedNode(item1, item1);
		final Item child2 = new IconedNode(item2, item2);
		final Item child3 = new IconedNode(item3, item3);
		final Item child4 = new IconedNode(item4, item4);
		
		root.addChild(child1);
		root.addChild(child2);
		root.addChild(child3);
		root.addChild(child4);
		
		for(int i = 0; i < 10; i++) {
			child4.addChild(new IconedNode("child " + i, "child " + i));
		}

		IconedTreeUI iconedTreeUI = new IconedTreeUI(root);

		final JTree tree = iconedTreeUI.getTree();

		ItemTreeModel treeModel = (ItemTreeModel)tree.getModel();
		treeModel.setAllwaysSort(false);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		JButton button1 = new JButton("remove " + item1);
		JButton button2 = new JButton("remove " + item2);
		JButton button3 = new JButton("remove " + item3);
		JButton button4 = new JButton("remove " + item4);
		JButton button5 = new JButton("remove " + item4 + " children");
		JButton button6 = new JButton("add " + item4 + " children");
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(button1);
		buttonsPanel.add(button2);
		buttonsPanel.add(button3);
		buttonsPanel.add(button4);
		buttonsPanel.add(button5);
		buttonsPanel.add(button6);
		
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				child1.setParent(null);
			}
		});
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				child2.setParent(null);
			}
		});
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				child3.setParent(null);
			}
		});
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				child4.setParent(null);
			}
		});
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Item> children = new LinkedList<Item>(child4.getChildren());
				for(Item item : children) {
					item.setParent(null);
				}
			}
		});
		button6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < 10; i++) {
					child4.addChild(new IconedNode("child " + i, "child " + i));
				}
			}
		});

		panel.setLayout(new BorderLayout());
		panel.add(iconedTreeUI.getPanel(), BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);
		
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
