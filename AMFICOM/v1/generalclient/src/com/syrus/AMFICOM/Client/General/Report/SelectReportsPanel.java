package com.syrus.AMFICOM.Client.General.Report;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.BorderLayout;

import java.awt.List;
import java.awt.Point;

import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.dnd.*;
import java.awt.datatransfer.Transferable;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

/*import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.ReflectogrammLoadDialog;

import com.syrus.AMFICOM.Client.General.Command.Analysis.FileCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Analysis.InitialAnalysisCommand;*/

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceElementLabel;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilterPane;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.Filter.SetRestrictionsWindow;
import com.syrus.AMFICOM.Client.General.Filter.LogicScheme;

import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
/*import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;*/

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
//import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
//import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
//import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
//import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
//import com.syrus.AMFICOM.Client.Resource.Network.Port;
//import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
//import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
//import com.syrus.AMFICOM.Client.Resource.Result.Result;
//import com.syrus.AMFICOM.Client.Resource.Result.Test;
//import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.AMFICOM.Client.General.Report.ObjectResourceReportModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
//import com.syrus.AMFICOM.Client.Optimize.Report.OptimizationReportModel;
//import com.syrus.AMFICOM.Client.Analysis.Report.EvaluationReportModel;
//import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;

/**
 * <p>Description: Панель для отображения дерева доступных отчётов</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class SelectReportsPanel extends JInternalFrame implements
	OperationListener

{
	public static String ev_additionalPaneCreated = "ev_additionalPaneCreated";
	public static String ev_closingAdditionalPanel = "ev_closingAdditionalPanel";

	public static String ev_onlyReportRealized = "ev_onlyReportRealized";

	public ApplicationContext aContext = null;

	public boolean objectsDraggable = true;

	private JDesktopPane desktop = null;

	private JFrame ownerWindow = null;

	private XYLayout xYLayout1 = new XYLayout();

	private XYLayout xYLayout2 = new XYLayout();

	private ObjectResourceTreeModel rtm = null;

	private UniTreePanel reportsTreePanel = null;

	private JInternalFrame additionalPanel = null;

	private ReportTemplate reportTemplate = null;

	private ObjectResourceFilter curFilterChanging = null;

	private boolean tdsEventHappened = true;
	private boolean firstEvent = true;

	public SelectReportsPanel(JDesktopPane desktop,
									  ApplicationContext aContext,
									  JFrame oW,
									  ReportTemplate rT,
									  ObjectResourceTreeModel ortm)
	{
		try
		{
			this.desktop = desktop;
			this.aContext = aContext;
			this.ownerWindow = oW;

			this.reportTemplate = rT;
			this.rtm = ortm;

			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setClosable(true);
		this.setResizable(true);
		this.setVisible(true);
		this.setTitle(LangModelReport.String("label_availableReports"));
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/general.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		this.setSize(new Dimension(280, -1));

		reportsTreePanel =
			new UniTreePanel(this.aContext.getDispatcher(), this.aContext, rtm);

		this.getContentPane().add(new JScrollPane(reportsTreePanel));

		reportsTreePanel.getTree().setCellRenderer(
			new AvailableReportsTreeRenderer(reportTemplate));

		reportsTreePanel.getTree().addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					TreePath treePath = reportsTreePanel.getTree().getSelectionPath();
					if (treePath == null)
						return;

					ObjectResourceTreeNode lastElem =
								(ObjectResourceTreeNode) treePath.getLastPathComponent();

					if (lastElem.getObject() instanceof ObjectsReport)
					{
						ObjectsReport report = (ObjectsReport)lastElem.getObject();
						aContext.getDispatcher().notify(new OperationEvent(report,0,ev_onlyReportRealized));
					}
				}
			}

			public void mouseEntered(MouseEvent e)
			{}

			public void mouseExited(MouseEvent e)
			{}

			public void mousePressed(MouseEvent e)
			{}

			public void mouseReleased(MouseEvent e)
			{
				if (tdsEventHappened != true)
					aContext.getDispatcher().notify(
						new OperationEvent(reportsTreePanel, 0,
												 TreeDataSelectionEvent.type));

				tdsEventHappened = false;
			}
		});

		this.aContext.getDispatcher().register(this, TreeDataSelectionEvent.type);
		this.aContext.getDispatcher().register(this,
															ObjectResourceFilterPane.
															state_filterClosed);
		this.aContext.getDispatcher().register(this,
															ObjectResourceFilterPane.
															state_filterChanged);

//		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter()
//		{
//			public void internalFrameClosing(InternalFrameEvent e)
//			{
//				aContext.getDispatcher().notify(
//					new OperationEvent("", 0, SelectReportsPanel.ev_closingWindow));
//			}
//		});
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type) &&
			 oe.getSource().equals(reportsTreePanel))
		{
			TreePath treePath = reportsTreePanel.getTree().getSelectionPath();
			if (treePath == null)
				return;

			ObjectResourceTreeNode lastElem =
						(ObjectResourceTreeNode) treePath.getLastPathComponent();

			if ((lastElem.getParent() != null) &&
				 ((ObjectResourceTreeNode) lastElem.getParent()).getObject()instanceof
				 ObjectsReport)
			{
				ObjectsReport curReport =
					(ObjectsReport) ((ObjectResourceTreeNode) lastElem.getParent()).
					getObject();

				if (curReport.view_type.equals(ObjectResourceReportModel.
														 rt_objectsReport))
				{
					ObjectResourceReportModel orrm =
						(ObjectResourceReportModel) curReport.model;

					Vector columns = (Vector) curReport.getReserve();
					if (columns.contains((String) lastElem.getObject()))
						columns.remove((String) lastElem.getObject());
					else
						columns.add((String) lastElem.getObject());
					tdsEventHappened = true;
					reportsTreePanel.repaint();
				}
			}

			if (!firstEvent)
			{
				firstEvent = true;
				return;
			}
			else
				firstEvent = false;

			if (additionalPanel != null)
			{
				if ((additionalPanel instanceof SetRestrictionsWindow)
					 && (curFilterChanging != null))
				{
//				((SetRestrictionsWindow)additionalPanel).closeSchemeWindow();
					aContext.getDispatcher().notify(
						new OperationEvent(curFilterChanging,
												 0,
												 com.syrus.AMFICOM.Client.General.
												 Filter.
												 ObjectResourceFilterPane.
												 state_filterClosed));
				}
				else
					aContext.getDispatcher().notify(
						new OperationEvent("", 0,
												 SelectReportsPanel.ev_closingAdditionalPanel));

				additionalPanel.dispose();
				additionalPanel = null;
			}

			//Для фильтруемых типов объектов активируем опцию фильтрования
			if (lastElem.getObject()instanceof ObjectResourceReportModel)
			{
				ObjectResourceReportModel orrm =
					(ObjectResourceReportModel) lastElem.getObject();
				if (curFilterChanging == null)
					setFilterPanel(orrm);
				else if (!curFilterChanging.equals(orrm.findORFilterforModel(
					reportTemplate,aContext.getDataSourceInterface())))
					setFilterPanel(orrm);
			}

//			if (rtm.toAskObjects(lastElem))
//			{
//
//				if (lastElem.getObject()instanceof OptimizationReportModel)
//				{
//					additionalPanel = new SelectSolutionFrame (aContext);
//
//					aContext.getDispatcher().notify(
//						new OperationEvent(additionalPanel, 0,
//												 SelectReportsPanel.ev_additionalPaneCreated));
//				}
//				else if (lastElem.getObject()instanceof EvaluationReportModel)
//				{
//					ReflectogrammLoadDialog dialog;
//					if (Pool.get("dialog", "TraceLoadDialog") == null)
//					{
//						dialog = new ReflectogrammLoadDialog(this.aContext);
//						Pool.put("dialog", "TraceLoadDialog", dialog);
//					}
//					else
//					{
//						dialog = (ReflectogrammLoadDialog) Pool.get("dialog",
//							"TraceLoadDialog");
//					}
//					dialog.show();
//
//					if (dialog.ret_code == 0)
//						return;
//					if (dialog.getResult() == null)
//						return;
//
//					Result res = dialog.getResult();
//
//					Enumeration childs = lastElem.children();
//					while (childs.hasMoreElements())
//					{
//						ObjectResourceTreeNode curChild = (ObjectResourceTreeNode)
//							childs.nextElement();
//						try
//						{
//							((ObjectsReport) curChild.getObject()).setReserve(res.getId());
//						}
//						catch (CreateReportException cre)
//						{}
//					}
//				}
//			}
		}

		if (oe.getActionCommand().equals(ObjectResourceFilterPane.
													state_filterChanged))
		{
			ObjectResourceFilter newFilter = (ObjectResourceFilter) oe.getSource();

			if (newFilter.logicScheme.checkScheme())
			{
				reportTemplate.objectResourceFilters.remove(curFilterChanging);
				reportTemplate.objectResourceFilters.add(newFilter);
				curFilterChanging = newFilter;
			}
			else if (newFilter.logicScheme.schemeIsEmpty())
			{
				reportTemplate.objectResourceFilters.remove(curFilterChanging);
				curFilterChanging = null;
			}
		}

		//Закрыто окно редактирования фильтра
		if (oe.getActionCommand().equals(com.syrus.AMFICOM.Client.General.
													Filter.
													ObjectResourceFilterPane.
													state_filterClosed))
		{
			curFilterChanging = null;
			aContext.getDispatcher().notify(
				new OperationEvent("", 0, SelectReportsPanel.ev_closingAdditionalPanel));
		}
	}

	private void setFilterPanel(ObjectResourceReportModel model)
	{
		curFilterChanging = model.findORFilterforModel(reportTemplate,aContext.getDataSourceInterface());

		if (curFilterChanging == null)
		{
			System.out.println("No filter for this type of objects!");
			return;
		}

		DataSet dataset = new DataSet();

		additionalPanel = new SetRestrictionsWindow(
			(ObjectResourceFilter) curFilterChanging.clone(),
			dataset,
			aContext,
			this.ownerWindow);

		aContext.getDispatcher().notify(
			new OperationEvent(additionalPanel, 0,
									 SelectReportsPanel.ev_additionalPaneCreated));
	}

	public void setEnabled(boolean en)
	{
		setEnabledTree((ObjectResourceTreeNode)
							this.reportsTreePanel.getTree().getModel().getRoot(), en);
		objectsDraggable = en;
	}

	private void setEnabledTree(ObjectResourceTreeNode node, boolean state)
	{
		Enumeration children = node.children();
		if (children == null)
			return;

		while (children.hasMoreElements())
		{
			ObjectResourceTreeNode child =
				(ObjectResourceTreeNode) children.nextElement();
			setEnabledTree(child, state);
		}

		node.setDragDropEnabled(state);
	}
}

class AvailableReportsTreeRenderer implements TreeCellRenderer
{
	ReportTemplate reportTemplate = null;

	public AvailableReportsTreeRenderer(ReportTemplate reportTemplate)
	{
		this.reportTemplate = reportTemplate;
	}

	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean selected,
		boolean expanded, boolean leaf,
		int row, boolean hasFocus)
	{
		ObjectResourceTreeNode node = (ObjectResourceTreeNode) value;

		node.setForeground(Color.black);
		node.setBackground(Color.white);

		if (node.getObject()instanceof ObjectResourceReportModel)
			node.setForeground(Color.MAGENTA);

		if (node.getObject()instanceof ObjectsReport)
		{
			ObjectsReport rep = (ObjectsReport) node.getObject();

			switch (rep.model.getReportKind(rep))
			{
				case 1:
					node.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
														getImage(
						"images/table_report.gif")));
					break;
				case 0:
					node.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
														getImage(
						"images/graph_report.gif")));
					break;
				case -1:
					node.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						"images/scheme.gif")));
			}

			node.setForeground(new Color(0, 150, 0));
		}

		if ((tree.getSelectionPath() != null) &&
			 node.equals(tree.getSelectionPath().getLastPathComponent()))
		{
			node.setForeground(Color.white);
			node.setBackground(Color.blue);
		}

		if ((node.getParent() != null) &&
			 ((ObjectResourceTreeNode) node.getParent()).getObject()instanceof
			 ObjectsReport)
		{
			ObjectsReport curReport = (ObjectsReport) ((ObjectResourceTreeNode)
				node.
				getParent()).getObject();

			if (curReport.view_type.equals(ObjectResourceReportModel.
													 rt_objectsReport))
			{
				Vector columns = (Vector) curReport.getReserve();
				if (!columns.contains((String) node.getObject()))
					node.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															 getImage(
						"images/unselect.gif").getScaledInstance(
						10,
						10,
						Image.SCALE_DEFAULT)));
				else
					node.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															 getImage(
						"images/selectall.gif").getScaledInstance(
						10,
						10,
						Image.SCALE_DEFAULT)));
			}
		}
		return node.getComponent();
	}
}