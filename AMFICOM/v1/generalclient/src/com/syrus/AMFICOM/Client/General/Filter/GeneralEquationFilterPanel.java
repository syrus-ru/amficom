package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterExpression;

import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class GeneralEquationFilterPanel extends FilterPanel
{
	XYLayout xYLayout1 = new XYLayout();

	JTextField textField = new JTextField();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JRadioButton eqButton = new JRadioButton();
	JRadioButton lessButton = new JRadioButton();
	JRadioButton moreButton = new JRadioButton();
	ButtonGroup radio;

	public GeneralEquationFilterPanel()
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
		jLabel1.setText(LangModel.String("labelUslovie"));
		jLabel2.setText(LangModel.String("labelZnachenie"));
		eqButton.setText("=");
		lessButton.setText("<");
		moreButton.setText(">");
		radio = new ButtonGroup();
		radio.add(lessButton);
		radio.add(eqButton);
		radio.add(moreButton);

		this.add(jLabel1, new XYConstraints(10, 20, 75, 20));
		this.add(jLabel2, new XYConstraints(130, 20, 75, 20));

		this.add(lessButton, new XYConstraints(5, 50, 40, 20));
		this.add(eqButton, new XYConstraints(50, 50, 40, 20));
		this.add(moreButton, new XYConstraints(95, 50, 40, 20));

		this.add(textField, new XYConstraints(140, 50, 90, 20));
//		this.setLayout(null);
	}

	public FilterExpression getExpression(String col_id, String col_name)
	{
		Vector vec = new Vector();
		String temp = "";
		vec.add("numeric");
		if(eqButton.isSelected())
			temp = "=";
		else if(lessButton.isSelected())
			temp = "<";
		else if(moreButton.isSelected())
			temp = ">";
		vec.add(temp);
		vec.add(textField.getText());
		FilterExpression fexp = new FilterExpression();
		fexp.setName(LangModel.String("labelFiltration")+" \'"+col_name+"\' "+LangModel.String("labelPoZnach")+" "+temp+" "+textField.getText());
		fexp.setVec(vec);
		fexp.setId(col_id);
		return fexp;
	}

	public void setExpression(FilterExpression expr)
	{
		Vector vec = expr.getVec();
		if(((String )vec.elementAt(1)).equals("="))
			eqButton.setSelected(true);
		else if(((String )vec.elementAt(1)).equals("<"))
			lessButton.setSelected(true);
		else if(((String )vec.elementAt(1)).equals(">"))
			moreButton.setSelected(true);
		textField.setText((String) vec.elementAt(2));
	}
}