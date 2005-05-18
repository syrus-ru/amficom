/*
 * $Id: ByteArrayConverter.java,v 1.5 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
 */
public class ByteArrayConverter
{
  private byte[] b;

  public ByteArrayConverter(byte[] array)
  {
    this.b = array;
  }

  public final short readIShort(int i)
  {
    return (short)(this.b[i+1] << 8 | this.b[i] & 0xff);
  }

  public final int readIUnsignedShort(int i)
  {
    return (this.b[i+1] & 0xff) << 8 | this.b[i] & 0xff;
  }

  public final int readIInt(int i)
  {
    return (this.b[i+3] & 0xff) << 24 | (this.b[i+2] & 0xff) << 16 |
            (this.b[i+1] & 0xff) << 8 | this.b[i] & 0xff;
  }

  public final long readIUnsignedInt(int i)
  {
    return (long)(this.b[i+3] & 0xff) << 24 | (long)(this.b[i+2] & 0xff) << 16 |
            (long)(this.b[i+1] & 0xff) << 8 | this.b[i] & 0xff;

  }

  public final double readIDouble(int i)
  {
    return Double.longBitsToDouble ((long)(this.b[i+7] & 0xff) << 56 | (long)(this.b[i+6] & 0xff) << 48 |
    (long)(this.b[i+5] & 0xff) << 40 | (long)(this.b[i+4] & 0xff) << 32 | (long)(this.b[i+3] & 0xff) << 24 |
    (long)(this.b[i+2] & 0xff) << 16 | (long)(this.b[i+1] & 0xff) <<  8 | this.b[i] & 0xff);
  }

  public final char readIChar(int i)
  {
    byte nul = 0x00;
    return (char)(nul << 8 | this.b[i] & 0xff);
  }

  public final String readIString(int i)
  {
    byte nul = 0x00;
    String s = "";

    while (true)
    {
      if (this.b[i] == 0x00)
        break;
      s += (char)(nul << 8 | this.b[i] & 0xff);
      i++;
    }
    return  s;
  }
}
