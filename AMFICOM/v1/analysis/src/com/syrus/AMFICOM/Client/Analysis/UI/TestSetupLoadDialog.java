package com.syrus.AMFICOM.Client.Analysis.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.io.BellcoreStructure;

public class TestSetupLoadDialog extends JDialog implements OperationListener
{
	public int ret_code = 0;
	public Object resource;

	private Dispatcher dispatcher = new Dispatcher();
	private ApplicationContext aContext;

	JButton okButton;
	JButton cancelButton;
	List data;
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

		TestSetupTreeModel lrtm = new TestSetupTreeModel(aContext);

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
			if (ev.getDataClass() != null && ev.getDataClass().equals(MeasurementSetup.class)
					&& ev.getSelectionNumber() != -1)
			{
				okButton.setEnabled(true);
				data = ev.getList();
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
	ApplicationContext aContext;

	public TestSetupTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public ObjectResourceTreeNode getRoot()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		try
		{
			MonitoredElement me = (MonitoredElement)MeasurementStorableObjectPool.getStorableObject(
						 new Identifier(bs.monitoredElementId), true);
			return new ObjectResourceTreeNode("root", "Шаблоны на \"" +
					(me.getName().equals("") ? me.getId().getIdentifierString() : me.getName()) + "\"", true);
		}
		catch(ApplicationException ex)
		{
			return new ObjectResourceTreeNode("root", "Шаблоны", true);
		}
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
		if(node.getObject() instanceof MeasurementSetup)
		{
			return MeasurementSetup.class;
		}
		else
			return null;
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		return MeasurementSetup.class;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		ObjectResourceTreeNode ortn;

		if(node.getObject() instanceof String)
		{
			String s = (String)node.getObject();

			if(s.equals("root"))
			{
				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
				if (bs != null && !bs.monitoredElementId.equals(""))
				{
					Identifier me_id = new Identifier(bs.monitoredElementId);
					Identifier domain_id = new Identifier(aContext.getSessionInterface().getDomainId());
					try
					{
						Domain domain = (Domain)MeasurementStorableObjectPool.getStorableObject(domain_id, true);

						StorableObjectCondition mSetupCondition = new DomainCondition(domain, ObjectEntities.MS_ENTITY_CODE);
						List mSetups = MeasurementStorableObjectPool.getStorableObjectsByCondition(mSetupCondition, true);

						java.util.Set testsHt = new HashSet();
						for(Iterator it = mSetups.iterator(); it.hasNext(); )
						{
							MeasurementSetup t = (MeasurementSetup)it.next();
							List me_ids = t.getMonitoredElementIds();
							for(Iterator it2 = me_ids.iterator(); it2.hasNext(); )
							{
								Identifier meId = (Identifier)it2.next();
								if(meId.equals(me_id))
								{
									testsHt.add(t);
									break;
								}
							}
						}

//						ObjectResourceSorter sorter = StubResource.getDefaultSorter();
//						sorter.setDataSet(testsHt);
						for(Iterator it = testsHt.iterator(); it.hasNext(); )
						{
							MeasurementSetup t = (MeasurementSetup)it.next();
							ortn = new ObjectResourceTreeNode(t, t.getDescription(), true, true);
							vec.add(ortn);
						}
					}
					catch(ApplicationException ex1)
					{
					}
				}
			}
		}
		return vec;
	}
}

