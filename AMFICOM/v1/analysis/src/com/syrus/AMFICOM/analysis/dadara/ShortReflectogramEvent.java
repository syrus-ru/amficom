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
		this.begin = re.begin;
		this.end   = re.end;
		this.type = (short)re.getType();
			 if(re.getType() == ReflectogramEvent.CONNECTOR)
			 {
				 this.A1 = (float)re.a1_connector;
				 this.aLet = (float)re.aLet_connector;
				 this.attenuation = (float)(re.a1_connector - re.a2_connector);
			 }
			 else if(re.getType() == ReflectogramEvent.WELD)
			 {
				 this.A1 = (float)re.a_weld;
				 this.aLet = 0f;
				 //this.attenuation = (float)re.boost_weld;// + (float)((re.end - re.begin)*re.b_weld);
				 // changed by Stas
				 this.attenuation = (float)(re.refAmpl(re.begin)[0] - re.refAmpl(re.end)[0]);
			 }
			 else
			 {
				 this.A1 = (float)re.a_linear;
				 this.aLet = 0f;
				 this.attenuation = (float)re.b_linear;
			 }
	}
}