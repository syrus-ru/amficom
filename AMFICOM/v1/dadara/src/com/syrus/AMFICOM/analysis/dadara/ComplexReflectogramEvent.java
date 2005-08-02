/*
 * $Id: ComplexReflectogramEvent.java,v 1.18 2005/08/02 19:36:33 arseniy Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.18 $, $Date: 2005/08/02 19:36:33 $
 * @module dadara
 * 
 * ����� ������������ ��� �������� ����������� ���������� �
 * ������������������� ������� -
 * ����� ����������� {������, �����, ���} �������� �����
 * ������������������� ��������� - ������, ��������� � ��.,
 * ����������� ��� ����������� ���������� � ������� ������������.
 */
public class ComplexReflectogramEvent implements SimpleReflectogramEvent
{
	private int begin;
	private int end;
	private int type;

	private double aLet;
	private double mLoss;

	private double asympY0;
	private double asympY1;

	public int getBegin() { return this.begin; }
	public int getEnd() { return this.end; }
	public int getEventType() { return this.type; }

	public double getLength() { return this.end - this.begin; }

	/**
	 * @return ��������� ��������, �.�. yMax - asympY0
	 */
	public double getALet() { return this.aLet; }

	/**
	 * @return ������ �� �������.
	 * ��� ���. ������� ��� (asympY0 - asympY1);
	 * ��� �.�. � ����� ������� - �� ���������,
	 * ��� ��������� - �� ��, �� ����������������� � ������ ��������
	 * �������� �������. 
	 */
	public double getMLoss() { return hasMLoss() ? this.mLoss : 0; }

	/**
	 * @return ��������� �� �������� mLoss
	 */
	public boolean hasMLoss() {
		return getEventType() != SimpleReflectogramEvent.DEADZONE
			&& getEventType() != SimpleReflectogramEvent.ENDOFTRACE;
	}

	/**
	 * @return
	 * ��� ���. ������� - �� �������� ������������� � ������ �������;
	 * ��� ������� ���� - �������� ������������� ���������� ��������� �������
	 *   �� ������ ������� ����;
	 * ��� ������ ������� - �������� �.�. � ������ �������.
	 */
	public double getAsympY0() { return this.asympY0; }

	/**
	 * @return
	 * ��� ���. ������� - �� �������� ������������� � ����� �������;
	 * ��� ����� ������� - � ��������, �� ����������;
	 * ��� ������ ������� - �������� �.�. � ����� �������.
	 */
	public double getAsympY1() { return this.asympY1; }
	
	/**
	 * @return type == LOSS || type == GAIN
	 */
	protected boolean isSplice()
	{
		return this.type == LOSS || this.type == GAIN;
	}

	private void setALet(ModelTrace mt) {
		if (this.type == SimpleReflectogramEvent.LINEAR)
			this.aLet = 0;
		else {
			final int N = this.end - this.begin + 1;
			double []yArr = mt.getYArray(this.begin, N);
			this.aLet = ReflectogramMath.getArrayMax(yArr) - this.asympY0;
		}
	}

	// ����� ������ private, �.�. �� ������� ������ �� � ������������� ����.
	// ��������, ��� ������ ����� ���� ����� �������� mloss, � aLet - ������
	// �� ����������������
	private ComplexReflectogramEvent(SimpleReflectogramEvent se, ModelTrace mt)
	{
		this.begin = se.getBegin();
		this.end = se.getEnd();
		this.type = se.getEventType();
		if (this.type == SimpleReflectogramEvent.LINEAR)
		{
			final int N = this.end - this.begin + 1;
			double []yArr = mt.getYArray(this.begin, N);
			ModelFunction lin = ModelFunction.createLinearFrom0(yArr);
			this.asympY0 = lin.fun(this.begin);
			this.asympY1 = lin.fun(this.end);
			this.mLoss = this.asympY0 - this.asympY1;
		}
		else
		{
			this.asympY0 = mt.getY(this.begin);
			this.asympY1 = mt.getY(this.end);
			this.mLoss = this.asympY0 - this.asympY1; // ��� ������ ����� ��������
		}
	}

	public static ComplexReflectogramEvent[] createEvents(SimpleReflectogramEvent[] se, ModelTrace mt)
	{
		ComplexReflectogramEvent[] ret = new ComplexReflectogramEvent[se.length];

		// ������� �������
		for (int i = 0; i < ret.length; i++)
			ret[i] = new ComplexReflectogramEvent(se[i], mt);

		// ������������ mloss ������ � ����������� (�������� ������� ������ ������)
		for (int i = 0; i < ret.length; i++)
		{
			if (ret[i].isSplice()
					|| ret[i].getEventType()
					== SimpleReflectogramEvent.CONNECTOR)
			{
				// ����� ������� (�� ������ ��� ����) ������ ������� ��������
				// �������, ������� �� ����� ������ ���� ������
				int linCount = 0;
				double linAtt = 0;
				if (i > 0 && ret[i - 1].getLength() > ret[i].getLength())
				{
					linCount++;
					linAtt += ret[i - 1].mLoss / ret[i - 1].getLength();
				}
				if (i < ret.length - 1 && ret[i + 1].getLength() > ret[i].getLength())
				{
					linCount++;
					linAtt += ret[i + 1].mLoss / ret[i + 1].getLength();
				}
				if (linCount > 0)
					linAtt /= linCount;
				ret[i].mLoss -= linAtt * ret[i].getLength();
			}
		}

		// ������������ asympY0 � mLoss ��� �.�. � ����� �������
		if (se[0].getEventType() == SimpleReflectogramEvent.DEADZONE)
		{
			for (int j = 1; j < se.length; j++) {
				if (se[j].getEventType() == SimpleReflectogramEvent.LINEAR) {
					double po;
					int x1 = se[j].getBegin() + 1;
					int x2 = se[j].getEnd() - 1;
					if (x1 >= x2)
					{
						po = mt.getY(x1);
					}
					else
					{
						double y1 = mt.getY(x1);
						double y2 = mt.getY(x2);
						po = (x1 * y2 - x2 * y1) / (x1 - x2);
					}
					ret[0].asympY0 = po;
					break;
				}
			}
			for (int i = 0; i < ret.length; i++)
				if (!ret[i].hasMLoss())
					ret[i].mLoss = 0;
		}

		// ������������� aLet
		for (int i = 0; i < ret.length; i++)
			ret[i].setALet(mt);

		return ret;
	}
}
