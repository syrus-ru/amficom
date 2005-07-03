package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import com.syrus.AMFICOM.filter.FilterExpressionInterface;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GeneralStringFilterPanel extends FilterPanel
{
	JTextField textField = new JTextField();
	JLabel jLabel1 = new JLabel();

	public GeneralStringFilterPanel()
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
		jLabel1.setText(LangModel.getString("labelStringForSearch"));
		this.add(jLabel1,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
		this.add(textField,  new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
	}

	public FilterExpressionInterface getExpression(String col_id, String col_name, boolean conditionsRequested)
	{
		List vec = new ArrayList();
		vec.add("string");
		vec.add(textField.getText());
		FilterExpression fexp = new FilterExpression();
		fexp.setVec(vec);

		String expName = LangModel.getString("labelFiltration") + " \'" + col_name + "\' "+LangModel.getString("labelPoPodstroke");
		if (conditionsRequested)
			expName += (" " + textField.getText());

		fexp.setName(expName);
		fexp.setColumnName(col_name);
		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		List vec = expr.getVec();
		textField.setText((String) vec.get(1));
	}
}
