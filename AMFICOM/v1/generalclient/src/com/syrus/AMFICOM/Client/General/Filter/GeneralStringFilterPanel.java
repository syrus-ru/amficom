package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;

import java.util.Vector;

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
		jLabel1.setText(LangModel.String("labelStringForSearch"));
		this.add(jLabel1,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
		this.add(textField,  new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
	}

	public FilterExpression getExpression(String col_id, String col_name)
	{
		Vector vec = new Vector();
		vec.add("string");
		vec.add(textField.getText());
		FilterExpression fexp = new FilterExpression();
		fexp.setVec(vec);
		fexp.setName(LangModel.String("labelFiltration")+" \'"+col_name+"\' "+LangModel.String("labelPoPodstroke")+" "+textField.getText());
		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		Vector vec = expr.getVec();
		textField.setText((String) vec.elementAt(1));
	}
}
