package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class GraphFrame extends JInternalFrame implements OperationListener
{
	private Dispatcher dispatcher;
	private GeneralTableModel tModel;
	private JTable jTable;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();

	//--------------------------------------------------------------------------------------------------------------
	public GraphFrame()
	{	this(new Dispatcher());
	}
	//--------------------------------------------------------------------------------------------------------------
	public GraphFrame(Dispatcher dispatcher)
	{	super();
		try
		{	jbInit();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}

		init_module(dispatcher);
	}
	//--------------------------------------------------------------------------------------------------------------
	void init_module(Dispatcher dispatcher)
	{	this.dispatcher = dispatcher;
		dispatcher.register(this, "myevent");
	}
	//--------------------------------------------------------------------------------------------------------------
	public void operationPerformed(OperationEvent ae)
	{	if(ae.getActionCommand().equals("myevent"))
		{	String id = (String)(ae.getSource());
			setTableModel();
			updTableModel(id);
			setVisible(true);
		}
	}
	//--------------------------------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{	tModel = new GeneralTableModel(
					new String[] {LangModelOptimize.getString("overallKey"),
												LangModelOptimize.getString("overallValue")},
					new String[] {"1", "2"},
					0);
		jTable = new JTable(tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(130);

		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelOptimize.getString("overallTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMaximumSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}
	//--------------------------------------------------------------------------------------------------------------
	void updTableModel(String id){}
	//--------------------------------------------------------------------------------------------------------------
	void setTableModel()
	{	tModel.clearTable();

		Vector length = new Vector(2);
		length.add(LangModelOptimize.getString("totalLength"));
		length.add("");
		tModel.insertRow(length);

		Vector loss = new Vector(2);
		loss.add(LangModelOptimize.getString("totalLoss"));
		loss.add("");
		tModel.insertRow(loss);

		Vector attenuation = new Vector(2);
		attenuation.add(LangModelOptimize.getString("totalAttenuation"));
		attenuation.add("");
		tModel.insertRow(attenuation);

		Vector orl = new Vector(2);
		orl.add(LangModelOptimize.getString("totalReturnLoss"));
		orl.add("");
		tModel.insertRow(orl);

		Vector evNum = new Vector(2);
		evNum.add(LangModelOptimize.getString("totalEvents"));
		evNum.add("");
		tModel.insertRow(evNum);
	}
	//--------------------------------------------------------------------------------------------------------------
}