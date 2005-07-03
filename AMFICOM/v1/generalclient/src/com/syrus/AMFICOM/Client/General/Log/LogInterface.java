package com.syrus.AMFICOM.Client.General.Log;

import java.util.Vector;

public interface LogInterface
{

	public abstract void println(Object obj);

	public abstract void println(String s);

	public abstract void println(Exception exception);

	public abstract void println(Vector vector);

	public abstract void printStack(String s);

	public abstract void debug(Exception exception);

	public abstract void debug(String s);

	public abstract void printStackTrace(Exception exception);

	public abstract void printStackTrace(LinkageError linkageerror);

	public abstract void close();
}
