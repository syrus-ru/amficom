package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import com.syrus.AMFICOM.Client.General.Filter.TreeModelClone;
import com.syrus.AMFICOM.Client.General.Filter.FilterTree;
import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;
import com.syrus.AMFICOM.Client.General.Filter.FilterTreeNode;

import com.syrus.AMFICOM.filter.FilterExpressionInterface;

public class GeneralTreeFilterPanel extends FilterPanel
{
	FilterTree mt;
	JTree tree;
	private BorderLayout borderLayout1 = new BorderLayout();
	public int selRow;
	FilterTreeRenderer renderer = new FilterTreeRenderer();

	public GeneralTreeFilterPanel(FilterTree mt)
	{
		super();
		this.mt = mt;
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		tree = mt.getTree(aContext);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		tree.setBorder(BorderFactory.createEtchedBorder());
		tree.setCellRenderer(renderer);
		this.add(tree, BorderLayout.CENTER);
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					if(selPath != null)
					{
						FilterTreeNode mte = (FilterTreeNode)selPath.getLastPathComponent();
						Choise(mte);
						tree.repaint();
					}
				}
			}
		};
		tree.addMouseListener(ml);
	}

	void Choise(FilterTreeNode mte)
	{
		mte.setChildrenState(mte.state);
		if (mte.state == 0)
		{
			mte.state = 2;
		}
		else if (mte.state == 1 || mte.state == 2)
		{
			mte.state = 0;
		}
		if (!mte.equals(mte.getRoot()))
		{
			mte.MyGetParent(mte);
		}
	}

	public FilterExpressionInterface getExpression(String col_id, String col_name, boolean conditionsRequested)
	{
		List vec = new ArrayList();
		vec.add("list");
		TreeModelClone tm = (TreeModelClone)tree.getModel();
		TreeModelClone new_tm = tm.myclone();
		vec.add(new_tm);
		FilterExpression fexp = new FilterExpression();
		fexp.setVec(vec);

		String expName = LangModel.getString("labelFiltration") + " \'" + col_name + "\' " + LangModel.getString("labelPoSpisku");
		fexp.setName(expName);
		fexp.setColumnName(col_name);
		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		List vec = expr.getVec();
		TreeModelClone tm = (TreeModelClone)vec.get(1);
		TreeModelClone new_tm = tm.myclone();
		tree.setModel(new_tm);
	}
}
