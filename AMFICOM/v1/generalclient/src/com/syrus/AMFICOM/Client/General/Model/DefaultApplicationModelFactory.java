package com.syrus.AMFICOM.Client.General.Model;

public class DefaultApplicationModelFactory implements ApplicaionModelFactory 
{
	public DefaultApplicationModelFactory()
	{
	}

	
	public abstract ApplicationModel createMainApplicationModel();

	public abstract ApplicationModel createConfigureApplicationModel();

	public abstract ApplicationModel createAdministrateApplicationModel();

	public abstract ApplicationModel createTraceApplicationModel();

	public abstract ApplicationModel createSurveyApplicationModel();

	public abstract ApplicationModel createAnalyseApplicationModel();

	public abstract ApplicationModel createNormsApplicationModel();
}