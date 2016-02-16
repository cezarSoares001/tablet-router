package com.murrow.network;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;
import com.murrow.support.Utilities;

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

    private UIManager uiManager;

    /* ---- CONSTRUCTORS ---- */

    public LL2P(String dstAddr, String srcAddr, String type, String payload)
    {
        setDstAddr(dstAddr);
        setSrcAddr(srcAddr);
        setType(type);
        setPayload(Utilities.stringToBytes(payload));

        this.crc = new CRC16();
        calculateCRC();
    }

    public LL2P(byte[] data)
    {
        String tmpData = Utilities.bytesToString(data);

        setDstAddr(tmpData.substring(0, 6));
        setSrcAddr(tmpData.substring(6, 12));
        setType(tmpData.substring(12, 16));

        crc = new CRC16();
        setCRC(tmpData.substring(tmpData.length()-4, tmpData.length()));

        setPayload(Utilities.stringToBytes(tmpData.substring(16, tmpData.length()-4)));
    }

    public LL2P()
    {
        setDstAddr("000000");
        setSrcAddr("000000");
        setType("0000");
        setPayload(Utilities.stringToBytes("0"));

        this.crc = new CRC16();
        calculateCRC();
    }

    public void updateLL2PFrame(byte[] frame)
    {
        String tmpData = Utilities.bytesToString(frame);

        setDstAddr(tmpData.substring(0, 6));
        setSrcAddr(tmpData.substring(6, 12));
        setType(tmpData.substring(12, 16));
        setPayload(Utilities.stringToBytes(tmpData.substring((20))));

        calculateCRC();
    }

    public void getObjectReferences(Factory factory)
    {
        uiManager = factory.getUIManager();
        uiManager.updateLL2PDisplay(this);
    }

    public byte[] getFrameBytes()
    {
        return (this.toString() + getCRCHexString()).getBytes(); //this does not encode the frame in Base64 but it shows correctly on the mirror.
        //return Utilities.stringToBytes(this.toString() + getCRCHexString()); - I think this is how it should be done, but this is gibberish...
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

    public void setCRC(String val)
    {
        crc.setCRC(Integer.valueOf(val,16));
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
        return Utilities.bytesToString(payload);
    }

    /* ---- PRIVATE METHODS ---- */
    private void calculateCRC()
    {
        crc.update(Utilities.stringToBytes(this.toString()));
    }

    /* ---- OVERRIDES ---- */

    @Override
    public String toString()
    {
        return getDstAddrHexString() + getSrcAddrHexString() + getTypeHexString() + getPayloadString();
    }
}
