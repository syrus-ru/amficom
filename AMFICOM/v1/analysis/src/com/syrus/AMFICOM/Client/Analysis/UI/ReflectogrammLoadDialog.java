package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeIconLabelCellRenderer;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.PopulatableItem;
import com.syrus.AMFICOM.logic.SelectionListener;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class ReflectogrammLoadDialog extends JDialog 
{
	public int ret_code = 0;
	Object resource;

	private ApplicationContext aContext;
	private Identifier domainId;

	JButton okButton;
	private JButton cancelButton;
	private JButton updateButton1 = new JButton();

	JScrollPane scrollPane = new JScrollPane();


	public ReflectogrammLoadDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
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
		setModal(true);

		setTitle(LangModelAnalyse.getString("trace"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(350, 600);
		if(frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height - 34;
		}
		if(frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width - 10;
		}
		setSize(new Dimension(355, 613));
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height - 30) / 2);

		setResizable(true);

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
		updateButton1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
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
		if(!domainId.equals(new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id)))
			setTree();
		super.setVisible(key);
	}

	public void show()
	{
		if(!domainId.equals(new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id)))
			setTree();
		super.show();
	}

	private void setTree()
	{
		domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);

		getContentPane().remove(scrollPane);
		scrollPane = new JScrollPane();

		try
		{
			Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domainId, true);		
			
			ArchiveChildrenFactory childrenFactory = ArchiveChildrenFactory.getInstance();
			childrenFactory.setDomainId(domainId);
			PopulatableItem item = new PopulatableItem();
			item.setObject(ArchiveChildrenFactory.ROOT);
			item.setName("Архив");
			item.setChildrenFactory(childrenFactory);
			item.populate();			
			LogicalTreeUI treeUI = new LogicalTreeUI(item, false);
			treeUI.setRenderer(IconPopulatableItem.class, new ItemTreeIconLabelCellRenderer());
			treeUI.getTreeModel().setAllwaysSort(false);
			this.scrollPane.getViewport().add(treeUI.getTree(), null);
			treeUI.addSelectionListener(new SelectionListener() {
				
				public void selectedItems(Collection items) {
					if (!items.isEmpty()) {
						for (Iterator it = items.iterator(); it.hasNext();) {
							Item item = (Item) it.next();
							Object object = item.getObject();
							if (object instanceof Result) {
								okButton.setEnabled(true);
								resource = object;
							} else {
								okButton.setEnabled(false);
							}
						}
					}
					
				}
			});
			
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			setTitle("Выберите рефлектограмму" + " (" + domain.getName() + ")");
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
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
		/*			String type = r.getResultType();
			 String name = null;
			 if(type != null && type.equals("modeling"))
			 {
				name = ((ObjectResource)Pool.get(Modeling.TYPE, r.getModelingId())).getName();
			 }
			 if(name != null)
				r.setName(name);*/

		return r;
	}

	public BellcoreStructure getBellcoreStructure()
	{
		BellcoreStructure bs = null;

		Result res = getResult();
		if(res == null)
			return null;

		SetParameter[] parameters = res.getParameters();
		for(int i = 0; i < parameters.length; i++)
		{
			SetParameter param = parameters[i];
			ParameterType type = (ParameterType)param.getType();
			if(type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}

		
		try {
			if (res.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
				Measurement measurement = (Measurement) res.getAction();
				Test test = (Test) MeasurementStorableObjectPool.getStorableObject(
						measurement.getTestId(), true);
				bs.monitoredElementId = test.getMonitoredElement().getId()
						.getIdentifierString();
				bs.title = test.getDescription();
			}
		}
		catch(ApplicationException ex)
		{
		}
		return bs;
	}
}
