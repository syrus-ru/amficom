package com.syrus.AMFICOM.Client.Analysis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.*;

public class ReflectogrammLoadDialog extends JDialog implements OperationListener
{
		public int ret_code = 0;
		private ObjectResource resource;
		private ReflectogramEvent []etalon;
		private String testId = null;

		private Dispatcher dispatcher = new Dispatcher();
		private ApplicationContext aContext;
		private String domainID;

		private JButton okButton;
		private JButton cancelButton;
		private JButton updateButton1 = new JButton();

		DataSet data;
		JScrollPane scrollPane = new JScrollPane();

		private boolean AlwaysGetEtalons = true;


		public ReflectogrammLoadDialog(ApplicationContext aContext, boolean AlwaysGetEtalons)
		{
			this(aContext);
			this.AlwaysGetEtalons = AlwaysGetEtalons;
		}

		public ReflectogrammLoadDialog(ApplicationContext aContext)
		{
			super(Environment.getActiveWindow());
			this.aContext = aContext;
			domainID = new String(aContext.getSessionInterface().getDomainId());
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

			setTitle(LangModelAnalyse.getString("trace"));
			setDomainIntoTitle();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = new Dimension (350, 600);
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height - 34;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width - 10;
			}
			setSize(new Dimension(355, 613));
			setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height - 30) / 2);

			setResizable(true);

//      ReflectogrammTreeModel lrtm =
//      new ReflectogrammTreeModel(this.aContext.getDataSourceInterface());
//
//      UniTreePanel utp = new UniTreePanel(this.dispatcher, this.aContext, lrtm);


			JPanel ocPanel = new JPanel();
			ocPanel.setMinimumSize(new Dimension(293, 30));
			ocPanel.setPreferredSize(new Dimension(350, 35));
			ocPanel.setLayout(new FlowLayout());

			okButton = new JButton();
			okButton.setText(LangModelAnalyse.getString("okButton"));
			okButton.setEnabled(false);
			okButton.setMaximumSize(new Dimension(91, 27));
			okButton.setMinimumSize(new Dimension(91, 27));
			okButton.setPreferredSize(new Dimension(91, 27));
			okButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					okButton_actionPerformed(e);
				}
			});
			cancelButton = new JButton();
			cancelButton.setText(LangModelAnalyse.getString("cancelButton"));
			cancelButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					cancelButton_actionPerformed(e);
				}
			});


			cancelButton.setMaximumSize(new Dimension(91, 27));
			cancelButton.setMinimumSize(new Dimension(91, 27));
			cancelButton.setPreferredSize(new Dimension(91, 27));

			updateButton1.setMaximumSize(new Dimension(91, 27));
			updateButton1.setMinimumSize(new Dimension(91, 27));
			updateButton1.setPreferredSize(new Dimension(91, 27));

			updateButton1.setText(LangModelAnalyse.getString("refreshButton"));
			updateButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateButton1_actionPerformed(e);
				}
			});
			ocPanel.add(okButton);
		ocPanel.add(updateButton1, null);
			ocPanel.add(cancelButton);
			getContentPane().setLayout(new BorderLayout());


			this.getContentPane().add(scrollPane, BorderLayout.CENTER);

			setTree();

			this.getContentPane().add(ocPanel, BorderLayout.SOUTH);

		}


		public void setVisible(boolean key)
		{
			if(!domainID.equals(aContext.getSessionInterface().getDomainId()))
			{
				setTree();
			}

			super.setVisible(key);
		}



		public void show()
		{
			if(!domainID.equals(aContext.getSessionInterface().getDomainId()))
			{
				setTree();
			}
			super.show();
		}


		private void setTree()
		{
			this.domainID = new String(aContext.getSessionInterface().getDomainId());

			this.getContentPane().remove(scrollPane);
			scrollPane = new JScrollPane();

			ReflectogrammTreeModel lrtm = new ReflectogrammTreeModel(this.aContext.getDataSourceInterface());
			UniTreePanel utp = new UniTreePanel(this.dispatcher, this.aContext, lrtm);

			scrollPane.getViewport().add(utp, null);

			this.getContentPane().add(scrollPane, BorderLayout.CENTER);
			setDomainIntoTitle();
		}

		private void setDomainIntoTitle()
		{
			String name = Pool.getName("domain", domainID);
			if(name != null)
				setTitle("Выберите рефлектограмму" + " ("+name+")");
		}



		public void operationPerformed(OperationEvent oe)
		{
			if (oe instanceof TreeDataSelectionEvent)
			{
				TreeDataSelectionEvent ev = (TreeDataSelectionEvent)oe;
				if (ev.getDataClass() != null && ev.getDataClass().equals(Result.class)
						&& ev.getSelectionNumber() != -1)
				{
					okButton.setEnabled(true);
					data = ev.getDataSet();
					resource = data.get(ev.getSelectionNumber());

					if(AlwaysGetEtalons)
						setEtalon();
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
			ret_code = 0;
			dispose();
		}

		void updateButton1_actionPerformed(ActionEvent e)
		{
			setTree();
			okButton.setEnabled(false);
			super.show();
		}

		public Result getResult()
		{
			if(resource == null)
				return null;
			if(!(resource instanceof Result))
				return null;

			Result r = (Result)resource;
			String type = r.getResultType();

			String name = null;
			if(type != null && type.equals("modeling"))
			{
				name = Pool.getName(Modeling.typ, r.getModelingId());
			}

			if(name != null)
				r.setName(name);

			return r;
		}


		public BellcoreStructure getBellcoreStructure()
		{
			BellcoreStructure bs = null;


			Result res = getResult();
			if(res == null)
				return null;

			Iterator it = res.getParameterList().iterator();
			while (it.hasNext())
			{
				Parameter param = (Parameter)it.next();
				if (param.getGpt().getId().equals(AnalysisUtil.REFLECTOGRAMM))
					bs = new BellcoreReader().getData(param.getValue());
			}
			Test test = (Test)Pool.get(Test.typ, res.getTestId());
			if(test != null)
				bs.monitored_element_id = test.getMonitoredElementId();
			bs.title = res.getName();
				return bs;
		}


		private void setEtalon()
		{/*
			Result result = (Result)resource;
							 if(result == null) // checking of the correct result;
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }
							 else if(result.test_id == null)
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }
							 else if(result.test_id.equals(""))
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }

			if(testId != null && result.test_id.equals(testId) && this.etalon != null) // etalon is already set.
				return;

			DataSourceInterface dsi = aContext.getDataSourceInterface();

			Test test = (Test)Pool.get(Test.typ, result.test_id);
							 if(test == null) // checking of the correct test;
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }
							 else if(test.test_setup_id == null)
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }
							 else if(test.test_setup_id.equals(""))
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }
							 else if(test.monitored_element_id == null)
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }
							 else if( test.monitored_element_id.equals(""))
							 {
								 testId = null;
								 etalon = null;
								 return;
							 }

			if(Pool.get(TestSetup.typ, test.test_setup_id) == null) // if test setup is not loaded - to load it;
			{
				dsi.loadTestSetup(test.test_setup_id);
			}
			TestSetup testSetup = (TestSetup)Pool.get(TestSetup.typ, test.test_setup_id);

								if(testSetup == null) // checking of the correct test setup;
								{
									testId = null;
									etalon = null;
									return;
								}
								else if(testSetup.threshold_set_id == null)
								{
									testId = null;
									etalon = null;
									return;
								}
								else if(testSetup.threshold_set_id.equals(""))
								{
									testId = null;
									etalon = null;
									return;
								}
								else if(testSetup.monitored_element_ids == null)
								{
									testId = null;
									etalon = null;
									return;
								}
								else if(testSetup.monitored_element_ids.equals(""))
								{
									testId = null;
									etalon = null;
									return;
								}

			if(Pool.get(ThresholdSet.typ, testSetup.threshold_set_id) == null) //if threshold set is not loaded - to load it;
			{
				String []thresholdSetsIds = {testSetup.threshold_set_id};
				dsi.LoadThresholdSets(thresholdSetsIds);
			}

			ThresholdSet thresholdSet = (ThresholdSet)Pool.get(ThresholdSet.typ, testSetup.threshold_set_id);
								if(thresholdSet == null) //checking of the correct threshold set;
								{
									testId = null;
									etalon = null;
									return;
								}

			thresholdSet.updateLocalFromTransferable();
//------------------------------------
			Threshold []threshold = null;
			for (int i = 0; i < thresholdSet.thresholds.size(); i++)
			{
				Parameter p = (Parameter)thresholdSet.thresholds.get(i);
				if (p.codename.equals("dadara_thresholds"))
				{
					threshold = Threshold.fromByteArray(p.value);
				}
			}

								if(threshold == null) // checking of the correct threshold;
								{
									testId = null;
									etalon = null;
									return;
								}

			if(Pool.get(Etalon.typ, testSetup.etalon_id) == null)
			{
				dsi.getEtalonsByME(test.monitored_element_id);
			}
			Etalon et = (Etalon)Pool.get(Etalon.typ, testSetup.etalon_id);

								if(et == null) //checking of the correct etalon;
								{
									testId = null;
									etalon = null;
									return;
								}

			ReflectogramEvent []re = null;
			for(Enumeration e = et.etalon_parameters.elements(); e.hasMoreElements(); )
			{
				Parameter p = (Parameter)e.nextElement();
				if(p.codename.equals("dadara_etalon_event_array"))
				{
					re = ReflectogramEvent.fromByteArray(p.value);
				}
			}
								if(re == null) //checking of the correct etalon's ReflectogramEvents
								{
									testId = null;
									etalon = null;
									return;
								}

			for(int i=0; i<re.length && i<threshold.length; i++)
			{
				re[i].setThreshold(threshold[i]);
			}

			this.etalon = re;
			this.testId = result.test_id;*/
		}



		public ReflectogramEvent []getEtalon()
		{
			return etalon;
		}
}











//-------------------------------------------------------------------
//-------------------------------------------------------------------
//-------------------------------------------------------------------
//-------------------------------------------------------------------


class ReflectogrammTreeModel extends ObjectResourceTreeModel
{
	private String []modelingResultIds;
	private String []predictionResultIds;
	DataSourceInterface dsi;
	String domainID;

	public ReflectogrammTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
		domainID = dsi.getSession().getDomainId();
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", "Объекты", true);
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
		if(node.expanded)
				return;
		node.expanded = true;
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(MonitoredElement.typ))
			{
				new ConfigDataSourceImage(dsi).LoadISM();
			}
			else if(s.equals(Modeling.typ))
			{
				dsi.GetModelings();
			}
			else if(s.equals("predictionresult"))
			{
				Vector v = new Vector();
				Hashtable h = Pool.getHash(Modeling.typ);
				if(h != null)
				{
					for(Enumeration e = h.elements(); e.hasMoreElements();)
					{
						Modeling m = (Modeling)e.nextElement();
						if(m.getTypeId().equals("optprognosis"))// && m.getDomainId().equals(domainID))
						{
							String resultID = new SurveyDataSourceImage(dsi).GetModelingResult(m.getId());
							v.add(resultID);
						}
					}
				}
				this.predictionResultIds = (String [])v.toArray(new String[v.size()]);
			}
			else if(s.equals("modelingresult"))
			{
				Vector v = new Vector();
				Hashtable h = Pool.getHash(Modeling.typ);
				if(h != null)
				{
					for(Enumeration e = h.elements(); e.hasMoreElements();)
					{
						Modeling m = (Modeling)e.nextElement();
						if(m.getTypeId().equals(AnalysisUtil.DADARA))// && m.getDomainId().equals(domainID))
						{
							String resultID = new SurveyDataSourceImage(dsi).GetModelingResult(m.getId());
							v.add(resultID);
						}
					}
				}
				this.modelingResultIds = (String [])v.toArray(new String[v.size()]);
			}
		}
		else if(node.getObject() instanceof MonitoredElement)
		{
			Object o = node.getObject();
			MonitoredElement me = (MonitoredElement)o;
			new SurveyDataSourceImage(dsi).GetTestsForME(me.getId());
		}
		else if(node.getObject() instanceof Test)
		{
				Object o = node.getObject();
					Test t = (Test)o;
					//new SurveyDataSourceImage(dsi).GetTestResult(t.getId());
					new SurveyDataSourceImage(dsi).GetResults(t.getResultIds());
		}
	}





	private Class getNodeClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof ObjectResource)
		{
			ObjectResource or = (ObjectResource)node.getObject();
			if(or instanceof Test)
			{
				return Test.class;
			}
			else if(or instanceof Result)
			{
				return Result.class;
			}
			else if(or instanceof MonitoredElement)
			{
				return MonitoredElement.class;
			}
			else if(or instanceof Modeling)
			{
				return Modeling.class;
			}
			else return null;
		}

		return null;
	}


	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof ObjectResource)
		{
			ObjectResource or = (ObjectResource)node.getObject();
			if(or instanceof MonitoredElement)
				return Test.class;
			else if(or instanceof Test)
				return Result.class;
		}
		else if(node.getObject() instanceof String)
		{
			String s = (String)node.getObject();
			if(s.equals("elementaryresult")) //User;
			{
				return Result.class;
			}
			else if(s.equals(Test.typ)) //Profile;
			{
				return Test.class;
			}
			else if(s.equals(MonitoredElement.typ))
			{
				return MonitoredElement.class;
			}
			else if(s.equals("predictionresult"))
			{
				return Result.class;
			}
			else if(s.equals("modelingresult"))
			{
				return Result.class;
			}
		}
		return null;

	}



	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		ObjectResourceTreeNode ortn;

		if(node.getObject() instanceof String)
		{
			String s = (String)node.getObject();

			if(s.equals("root")) // Root of the tree
			{
				ortn = new ObjectResourceTreeNode(MonitoredElement.typ, "Пути тестирования", true);
				vec.add(ortn);
				ortn = new ObjectResourceTreeNode(Modeling.typ, "Модели рефлектограмм", true);
				vec.add(ortn);
			}
			else if(s.equals(Modeling.typ))
			{
				ortn = new ObjectResourceTreeNode("predictionresult", "Прогнозируемые рефлектограммы", true);
				vec.add(ortn);
				ortn = new ObjectResourceTreeNode("modelingresult", "Рассчетные рефлектограммы", true);
				vec.add(ortn);
			}
			else if(s.equals("predictionresult"))
			{
				for(int i=0; i<predictionResultIds.length; i++)
				{
					Result r = (Result)Pool.get(Result.typ, predictionResultIds[i]);
					if(r != null)
					{
						Modeling m = (Modeling)Pool.get(Modeling.typ, r.getModelingId());
						if(m != null)
						{
							ortn = new ObjectResourceTreeNode(r, m.getName(), true, getIcon("predictionresult"), true);
							vec.add(ortn);
						}
					}
				}
			}
			else if(s.equals("modelingresult"))
			{
				for(int i=0; i<modelingResultIds.length; i++)
				{
					Result r = (Result)Pool.get(Result.typ, modelingResultIds[i]);
					if(r != null)
					{
						Modeling m = (Modeling)Pool.get(Modeling.typ, r.getModelingId());
						if(m != null)
						{
							ortn = new ObjectResourceTreeNode(r, m.getName(), true, getIcon("modelingresult"), true);
							vec.add(ortn);
						}
					}
				}
			}
			else if(s.equals(MonitoredElement.typ))
			{
				DataSet dSet = new DataSet(Pool.getHash(MonitoredElement.typ));
				ObjectResourceSorter sorter = MonitoredElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )enum.nextElement();
					if(me.domain_id.equals(domainID))
					{
						ortn = new ObjectResourceTreeNode(me, me.getName(), true);
						vec.add(ortn);
					}
				}
			}
		}
		else if(node.getObject() instanceof ObjectResource)
		{
			ObjectResource or = (ObjectResource )node.getObject();
			if(or instanceof MonitoredElement)
			{
					Hashtable testsHt = new Hashtable();
					MonitoredElement me = (MonitoredElement)or;
					Hashtable ht = Pool.getHash(Test.typ);
					if(ht != null)
					{
						for(Enumeration e = ht.elements(); e.hasMoreElements(); )
						{
							Test t = (Test)e.nextElement();
							if(t.getMonitoredElementId().equals(me.id))// && t.getDomainId().equals(domainID))
							{
								testsHt.put(t.getId(), t);
							}
						}
					}

					DataSet dSet = new DataSet(testsHt);
					ObjectResourceSorter sorter = Test.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();
					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Test t = (Test)enum.nextElement();
						ortn = new ObjectResourceTreeNode(t, t.getName(), true, getIcon(Test.typ));
						vec.add(ortn);
					}
			}
			else if(or instanceof Test)
			{
				Test t = (Test)or;
				DataSet dSet = new DataSet(t.getResults());
				ObjectResourceSorter sorter = Result.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();
				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					Result r = (Result)enum.nextElement();
					ortn = new ObjectResourceTreeNode(r, r.getName(), true, getIcon(Result.typ), true);
					vec.add(ortn);
				}
			}

		}

		return vec;
	}








	private ImageIcon getIcon(String typ)
	{
		if(typ.equals(Modeling.typ))
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().
													 getImage("images/model_mini.gif").
													 getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		else if(typ.equals(Test.typ))
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().
													 getImage("images/testir1.gif").
													 getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		else if(typ.equals(Result.typ))
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().
													 getImage("images/result.gif").
													 getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		else if(typ.equals("predictionresult"))
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().
													 getImage("images/main/prognosis.gif").
													 getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		else if(typ.equals("modelingresult"))
		{
			return new ImageIcon(Toolkit.getDefaultToolkit().
													 getImage("images/main/model_mini.gif").
													 getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		else
		{
			return null;
		}
	}

}