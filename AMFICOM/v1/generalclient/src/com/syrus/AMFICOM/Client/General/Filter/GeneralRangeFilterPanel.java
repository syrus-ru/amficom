package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.filter.FilterExpressionInterface;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GeneralRangeFilterPanel extends FilterPanel
{
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JTextField loField = new JTextField();
	JTextField hiField = new JTextField();

	public GeneralRangeFilterPanel()
	{
		super();
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
		this.setLayout(new GridBagLayout());

		jLabel1.setText(LangModel.getString("labelFrom"));
		jLabel2.setText(LangModel.getString("labelTo"));

		this.add(jLabel1,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		this.add(loField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));

		this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		this.add(hiField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
	}

	public FilterExpressionInterface getExpression(String col_id, String col_name, boolean conditionsRequested)
	{
		List vec = new ArrayList();
		vec.add("range");
		vec.add(loField.getText());
		vec.add(hiField.getText());
		FilterExpression fexp = new FilterExpression();

		String expName = LangModel.getString("labelFiltration")+" \'"+col_name+"\' ";
		if (conditionsRequested)
			expName += (LangModel.getString("labelPoDiapOt")+" "+loField.getText()+" "+LangModel.getString("labelPoDiapDo")+" "+hiField.getText());

		fexp.setName(expName);
		fexp.setColumnName(col_name);
		fexp.setVec(vec);
		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		List vec = expr.getVec();
		loField.setText((String) vec.get(1));
		hiField.setText((String) vec.get(2));
	}
}
