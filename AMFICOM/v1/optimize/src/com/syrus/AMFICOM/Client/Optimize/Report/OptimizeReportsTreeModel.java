package com.syrus.AMFICOM.Client.Optimize.Report;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Image;

import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;

import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.Optimize.Report.OptimizationReportModel;

import javax.swing.tree.TreePath;

/**
 * <p>Description: Модель дерева с доступными отчётами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class OptimizeReportsTreeModel extends ObjectResourceTreeModel
	implements OperationListener
{
	private ApplicationContext aContext = null;
	private SelectSolutionFrame ssFrame = null;

	public OptimizeReportsTreeModel(ApplicationContext aC)
	{
		super();
		aContext = aC;
	}

	public void operationPerformed (OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			UniTreePanel reportsTreePanel = (UniTreePanel) oe.getSource();
			TreePath treePath = reportsTreePanel.getTree().getSelectionPath();
			if (treePath == null)
				return;

			ObjectResourceTreeNode lastElem =
				(ObjectResourceTreeNode) treePath.getLastPathComponent();

			if (lastElem.getObject() instanceof OptimizationReportModel)
			{
				SelectSolutionFrame ssFrame = new SelectSolutionFrame (this.aContext);
			}
		}
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
			"root",
			LangModelReport.getString("label_availableReports"),
			true);
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		if (((String) node.getObject()).equals("root"))
			return Color.magenta;
		return Color.black;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();

		//для строки - общая часть для дерева отчётов + деревья топологоии и схемы
		if (node.getObject() instanceof String)
		{
			if (node.getObject().equals("root"))
			{
				OptimizationReportModel optModel = new OptimizationReportModel();
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					optModel,
					optModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);
			}
		}

		else if (node.getObject()instanceof APOReportModel)
		{
			APOReportModel rm =
				(APOReportModel) node.getObject();

			Vector fields = rm.getAvailableReports();

			for (int i = 0; i < fields.size(); i++)
			{
				String curName = (String) fields.get(i);
				String langName = rm.getLangForField(curName);

				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					new ObjectsReport((APOReportModel) node.getObject(),
					curName,
					APOReportModel.rt_tableReport,
					toAskObjects(node)),
					langName,
					true,
					new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/new.gif").
					getScaledInstance(16, 16, Image.SCALE_SMOOTH)));

				ortn.setDragDropEnabled(true);
				ortn.setFinal(true);

				vec.add(ortn);
			}
		}

		return vec;
	}

	public boolean toAskObjects(ObjectResourceTreeNode node)
	{
		return true;
	}
}