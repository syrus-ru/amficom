package com.syrus.AMFICOM.analysis.dadara;

public class ShortReflectogramEvent
{
	public int begin;
	public int end;
	private short type;

	public float attenuation; // defined for all three types
	public float aLet;        // defined for connector only
	public float A1;          // defined for all three types

	public ShortReflectogramEvent(ReflectogramEvent re)
	{
		setData(re);
	}

	public void setType(int type)
	{
		this.type = (short)type;
	}

	public int getType()
	{
		return type;
	}

	public void setData(ReflectogramEvent re)
	{
		this.begin = re.getBegin();
		this.end   = re.getEnd();
		this.type = (short)re.getEventType();
			 if(re.getEventType() == ReflectogramEvent.CONNECTOR)
			 {
				 this.A1 = (float)re.getAsympY0();
				 this.aLet = (float)re.getALet();
				 this.attenuation = (float)(re.getAsympY0() - re.getAsympY1());
			 }
			 else if(re.getEventType() == ReflectogramEvent.WELD)
			 {
				 this.A1 = (float)re.getAsympY0();
				 this.aLet = 0f;
				 //this.attenuation = (float)re.boost_weld;// + (float)((re.end - re.begin)*re.b_weld);
				 // changed by Stas
				 this.attenuation = (float)re.getMLoss();
			 }
			 else
			 {
				 this.A1 = (float)re.getAsympY0();
				 this.aLet = 0f;
				 this.attenuation = (float)re.getMLoss();
			 }
	}
}