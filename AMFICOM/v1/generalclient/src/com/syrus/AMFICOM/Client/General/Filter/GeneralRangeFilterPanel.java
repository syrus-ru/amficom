package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;
import com.syrus.AMFICOM.Client.General.Filter.FilterPanel;

public class GeneralRangeFilterPanel extends FilterPanel
{
	XYLayout xYLayout1 = new XYLayout();

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
		this.setLayout(xYLayout1);
		jLabel1.setText(LangModel.String("labelFrom"));
		jLabel2.setText(LangModel.String("labelTo"));

		this.add(jLabel1, new XYConstraints(10, 20, 100, 20));
		this.add(loField, new XYConstraints(115, 20, 100, 20));

		this.add(jLabel2, new XYConstraints(10, 50, 100, 20));
		this.add(hiField, new XYConstraints(115, 50, 100, 20));
//		this.setLayout(null);
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