package com.syrus.util;

public interface Logger {

	public void debugMessage(String mesg, int debugLevel);

	public void debugException(Exception exception, int debugLevel);

	public void errorMessage(String mesg);

	public void errorException(Exception exception);
}