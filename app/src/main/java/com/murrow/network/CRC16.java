package com.murrow.network;

/**
 * Created by Corbin Murrow on 1/27/2016.
 */
public class CRC16
{
    private int crc;
    private int polynomial;

    public CRC16()
    {
        polynomial = 0x11021; //x^16 + x^12 + x^5 + 1 == 10001000000100001 == 0x11021
        crc = 0;
    }

    public Integer getCRC()
    {
        return crc;
    }

    public String getCRCHexString()
    {
        return Integer.toHexString(crc);
    }

    public void resetCRC()
    {
        crc = 0;
    }

    public void setCRC(Integer crc)
    {
        this.crc = crc;
    }

    public void update(byte newByte)
    {
        int tempInput = newByte << 8;
        int tempCRC = crc^tempInput;
        int highBit = 0x10000; //bit 16 is set

        for (int i = 0; i < 8; i++)
        {
            tempCRC = (tempCRC << 1);
            if ((tempCRC & highBit) > 0)
                tempCRC = tempCRC ^ polynomial;
        }

        crc = tempCRC;
    }

    public void update(byte[] newBytes)
    {
        for (byte b : newBytes)
            update(b);
    }

    public String toString()
    {
        return getCRC().toString();
    }

}
