package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class GeneralStringFilterPanel extends FilterPanel
{
	JTextField textField = new JTextField();
	JLabel jLabel1 = new JLabel();
	XYLayout xYLayout1 = new XYLayout();

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
		this.setLayout(xYLayout1);
		jLabel1.setText(LangModel.String("labelStringForSearch"));
		this.add(jLabel1, new XYConstraints(10, 20, 200, 20));
		this.add(textField, new XYConstraints(10, 50, 200, 20));
//		this.setLayout(null);
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