package com.syrus.AMFICOM.Client.Resource;

final class DataSourceException extends Exception 
{
	public DataSourceException()
	{
	}

	public DataSourceException(String message)
	{
		super(message);
	}

	public DataSourceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DataSourceException(Throwable cause)
	{
		super(cause);
	}
}
