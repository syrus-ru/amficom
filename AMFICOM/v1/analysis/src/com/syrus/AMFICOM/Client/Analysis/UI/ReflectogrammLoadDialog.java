package com.syrus.AMFICOM.Client.Analysis.UI;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client_.general.ui_.tree.UniTreePanel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.*;

public class ReflectogrammLoadDialog extends JDialog implements OperationListener
{
	public int ret_code = 0;
	private Object resource;
	private String testId = null;

	private Dispatcher dispatcher = new Dispatcher();
	private ApplicationContext aContext;
	private Identifier domainId;

	private JButton okButton;
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

		dispatcher.register(this, "treedataselectionevent");
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
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);

			ArchiveTreeModel lrtm = new ArchiveTreeModel(domain);
			UniTreePanel utp = new UniTreePanel(dispatcher, aContext, lrtm);

			scrollPane.getViewport().add(utp, null);

			getContentPane().add(scrollPane, BorderLayout.CENTER);
			setTitle("ֲûבונטעו נופכוךעמדנאללף" + " (" + domain.getName() + ")");
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}
	}

	public void operationPerformed(OperationEvent oe)
	{
		if(oe instanceof TreeDataSelectionEvent)
		{
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent)oe;
			if(ev.getDataClass() != null && ev.getDataClass().equals(Result.class)
					&& ev.getSelectionNumber() != -1)
			{
				okButton.setEnabled(true);
				List data = ev.getList();
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

		Measurement measurement = res.getMeasurement();
		try
		{
			Test test = (Test)MeasurementStorableObjectPool.getStorableObject(measurement.getTestId(), true);
			bs.monitoredElementId = test.getMonitoredElement().getId().getIdentifierString();
			bs.title = test.getDescription();
		}
		catch(ApplicationException ex)
		{
		}
		return bs;
	}
}
