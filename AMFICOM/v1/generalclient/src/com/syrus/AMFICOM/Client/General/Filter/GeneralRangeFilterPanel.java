package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;

import java.util.Vector;

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

		jLabel1.setText(LangModel.String("labelFrom"));
		jLabel2.setText(LangModel.String("labelTo"));

		this.add(jLabel1,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		this.add(loField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));

		this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
		this.add(hiField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
	}

	public FilterExpression getExpression(String col_id, String col_name)
	{
		Vector vec = new Vector();
		vec.add("range");
		vec.add(loField.getText());
		vec.add(hiField.getText());
		FilterExpression fexp = new FilterExpression();
		fexp.setName(LangModel.String("labelFiltration")+" \'"+col_name+"\' "+LangModel.String("labelPoDiapOt")+" "+loField.getText()+" "+LangModel.String("labelPoDiapDo")+" "+hiField.getText());
		fexp.setVec(vec);
		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		Vector vec = expr.getVec();
		loField.setText((String) vec.elementAt(1));
		hiField.setText((String) vec.elementAt(2));
	}
}