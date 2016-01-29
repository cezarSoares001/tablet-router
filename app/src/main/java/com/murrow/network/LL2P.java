package com.murrow.network;

import android.util.Base64;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

import java.util.Arrays;

/**
 * Created by Corbin Murrow on 1/27/2016.
 */
public class LL2P
{
    private Integer dstAddr;
    private Integer srcAddr;
    private Integer type;
    private byte[] payload;
    private CRC16 crc;

    /* ---- CONSTRUCTORS ---- */

    public LL2P(String dstAddr, String srcAddr, String type, String payload)
    {
        setDstAddr(dstAddr);
        setSrcAddr(srcAddr);
        setType(type);
        setPayload(Base64.encode(payload.getBytes(), Base64.DEFAULT));

        this.crc = new CRC16();
        calculateCRC();
    }

    public LL2P(byte[] data)
    {
        String tmpData = "";

        for (byte b : data)
            tmpData += (char)b;

        setDstAddr(tmpData.substring(0, 5));
        setSrcAddr(tmpData.substring(6, 11));
        setType(tmpData.substring(12, 15));
    }

    public LL2P()
    {
        setDstAddr("000000");
        setSrcAddr("000000");
        setType("0000");
        setPayload(Base64.encode("0".getBytes(), Base64.DEFAULT));

        this.crc = new CRC16();
        calculateCRC();
    }

    /* ---- SETTERS ---- */

    void setDstAddr(String val)
    {
        dstAddr = Integer.valueOf(val, 16);
    }

    public void setSrcAddr(String val)
    {
        srcAddr = Integer.valueOf(val, 16);
    }

    public void setType(String val)
    {
        type = Integer.valueOf(val, 16);
    }

    public void setDstAddr(int val)
    {
        dstAddr = val;
    }

    public void setSrcAddr(int val)
    {
        srcAddr = val;
    }

    public void setType(int val)
    {
        type = val;
    }

    public void setPayload(byte[] arr)
    {
        payload = arr;
    }

    //Todo: implement setCRC
    public void setCRC(String val)
    {

    }

    /* ---- GETTERS ---- */
    public String getDstAddrHexString()
    {
        return Utilities.padHex(Integer.toHexString(dstAddr), NetworkConstants.LL2P_ADDR_LENGTH * 2);
    }

    public String getSrcAddrHexString()
    {
        return Utilities.padHex(Integer.toHexString(srcAddr), NetworkConstants.LL2P_ADDR_LENGTH * 2);
    }

    public String getTypeHexString()
    {
        return Utilities.padHex(Integer.toHexString(type), NetworkConstants.LL2P_TYPE_LENGTH * 2);
    }

    public String getCRCHexString()
    {
        return Utilities.padHex(crc.getCRCHexString(), NetworkConstants.CRC_LENGTH * 2);
    }

    public int getDstAddr()
    {
        return dstAddr;
    }

    public int getSrcAddr()
    {
        return srcAddr;
    }

    public int getType()
    {
        return type;
    }

    public int getCRC()
    {
        return crc.getCRC();
    }

    public byte[] getPayloadBytes()
    {
        return payload;
    }

    public String getPayloadString()
    {
        return new String(Base64.decode(payload, Base64.DEFAULT));
    }

    /* ---- PRIVATE METHODS ---- */
    private void calculateCRC()
    {
        crc.update(this.toString().getBytes());
    }

    //Todo: implement getFrameBytes
    private byte[] getFrameBytes()
    {
        return null;
    }

    /* ---- OVERRIDES ---- */

    @Override
    public String toString()
    {
        return getDstAddrHexString() + getSrcAddrHexString() + getTypeHexString() + getPayloadString();
    }
}
