package com.syrus.AMFICOM.Client.Analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import com.syrus.io.BellcoreStructure;

public class TestSetupLoadDialog extends JDialog implements OperationListener
{
	public int ret_code = 0;
	public ObjectResource resource;

	private Dispatcher dispatcher = new Dispatcher();
	private ApplicationContext aContext;

	JButton okButton;
	JButton cancelButton;
	DataSet data;
	JScrollPane scrollPane = new JScrollPane();

	public TestSetupLoadDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		dispatcher.register(this, "treedataselectionevent");
	}

	private void jbInit() throws Exception
	{
		setModal(true);
		setTitle("Выбор шаблона");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (350, 600);
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height - 34;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width - 10;
		}
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height - 30) / 2);

		setResizable(true);

		TestSetupTreeModel lrtm = new TestSetupTreeModel(aContext.getDataSourceInterface());

		UniTreePanel utp = new UniTreePanel(dispatcher, aContext, lrtm);

		JPanel ocPanel = new JPanel();
		ocPanel.setPreferredSize(new Dimension(350, 30));
		ocPanel.setLayout(new FlowLayout());

		okButton = new JButton();
		okButton.setText("OK");
		okButton.setEnabled(false);
		okButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});
		cancelButton = new JButton();
		cancelButton.setText("Отмена");
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		ocPanel.add(okButton);
		ocPanel.add(cancelButton);
		getContentPane().setLayout(new BorderLayout());

		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(utp, null);
		utp.setVisible(true);
		scrollPane.setVisible(true);

		this.getContentPane().add(ocPanel, BorderLayout.SOUTH);
	}

	public void operationPerformed(OperationEvent oe)
	{
		if (oe instanceof TreeDataSelectionEvent)
		{
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent)oe;
			if (ev.getDataClass() != null && ev.getDataClass().equals(TestSetup.class)
					&& ev.getSelectionNumber() != -1)
			{
				okButton.setEnabled(true);
				data = ev.getDataSet();
				resource = data.get(ev.getSelectionNumber());
			}
			else
			{
				okButton.setEnabled(false);
			}
		}
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		ret_code = 1;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}
}

class TestSetupTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public TestSetupTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		if (bs != null && !bs.monitored_element_id.equals(""))
		{
			MonitoredElement me = (MonitoredElement)Pool.get(MonitoredElement.typ, bs.monitored_element_id);
			return new ObjectResourceTreeNode ("root", "Шаблоны на \"" +
					(me.getName().equals("") ? me.getId() : me.getName()) + "\"" , true);
		}
		return new ObjectResourceTreeNode("root", "Шаблоны", true);
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	private Class getNodeClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof TestSetup)
		{
			return TestSetup.class;
		}
		else
			return null;
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		return TestSetup.class;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		ObjectResourceTreeNode ortn;

		if(node.getObject() instanceof String)
		{
			String s = (String)node.getObject();

			if(s.equals("root"))
			{
				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
				if (bs != null && !bs.monitored_element_id.equals(""))
				{
					String me_id = bs.monitored_element_id;
					String ids[] = dsi.getTestSetupsByME(me_id);
					for (int i = 0; i < ids.length; i++)
						dsi.loadTestSetup(ids[i]);

					Hashtable testsHt = new Hashtable();

					Hashtable ht = Pool.getHash(TestSetup.typ);
					if(ht != null)
					{
						for(Enumeration e = ht.elements(); e.hasMoreElements(); )
						{
							TestSetup t = (TestSetup)e.nextElement();
							for (int i = 0; i < t.monitored_element_ids.length; i++)
								if(t.monitored_element_ids[i].equals(me_id))
								{
								testsHt.put(t.id, t);
								break;
							}
						}
					}

					DataSet dSet = new DataSet(testsHt);
					ObjectResourceSorter sorter = TestSetup.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();
					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						TestSetup t = (TestSetup)enum.nextElement();
						ortn = new ObjectResourceTreeNode(t, t.getName(), true, true);
						vec.add(ortn);
					}
				}
			}
		}
		return vec;
	}
}

