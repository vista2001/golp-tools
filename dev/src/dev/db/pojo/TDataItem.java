package dev.db.pojo;

public class TDataItem
{
    private int dataItemId;
    private String dataName;
    private String dataDesc;
    private String dataLvL;
    private String dataType;
    private int dataLen;
    private String dataAop;
    private long fmlId;
    private String isPublished;//0-δ����,1-�ѷ���

    public int getDataItemId()
    {
        return dataItemId;
    }

    public void setDataItemId(int dataItemId)
    {
        this.dataItemId = dataItemId;
    }

    public String getDataName()
    {
        return dataName;
    }

    public void setDataName(String dataName)
    {
        this.dataName = dataName;
    }

    public String getDataDesc()
    {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc)
    {
        this.dataDesc = dataDesc;
    }

    /**
     * ������dataLvL��ֵ��
     * @return ��dataLvL��ֵ
     */
    public String getDataLvL()
    {
        return dataLvL;
    }

    /**
     * ����dataLvL��ֵΪ��0�����򷵻ء�0-APP����
     * ����dataLvL��ֵΪ��1�����򷵻ء�1-GOLP����
     * @return
     */
    public String getDataLvL2()
    {
        if (dataLvL.equals("0"))
        {
            return "0-APP";
        }
        else
        {
            return "1-GOLP";
        }
    }

    /**
     * ������dataLvL��ֵ������dataLvL��
     * @param dataLvL
     */
    public void setDataLvL(String dataLvL)
    {
        this.dataLvL = dataLvL;
    }

    /**
     * ������dataLvL�ĵ�һ���ַ�������dataLvL��
     * @param dataLvL
     */
    public void setDataLvL2(String dataLvL)
    {
        this.dataLvL = dataLvL.substring(0, 1);
    }

    /**
     * ������dataType��ֵ��
     * @return ��dataType��ֵ
     */
    public String getDataType()
    {
        return dataType;
    }

    /**
     * <br>��dataType��ֵΪ��0�������ء�0-in����
     * <br>��dataType��ֵΪ��1�������ء�1-long����
     * <br>��dataType��ֵΪ��2�������ء�2-double����
     * <br>��dataType��ֵΪ��3�������ء�3-char����
     * <br>��dataType��ֵΪ��4�������ء�4-char[]����
     * <br>��dataType��ֵΪ��5�������ء�5-String����
     * @return
     */
    public String getDataType2()
    {
        String dataType = null;
        switch (this.dataType)
        {
        case "0":
            dataType = "0-int";
            break;
        case "1":
            dataType = "1-long";
            break;
        case "2":
            dataType = "2-double";
            break;
        case "3":
            dataType = "3-char";
            break;
        case "4":
            dataType = "4-char[]";
            break;
        case "5":
            dataType = "5-String";
            break;
        }
        return dataType;
    }

    /**
     * ������dataType��ֵ������dataType��
     * @param dataType
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    /**
     * ������dataType�ĵ�һ���ַ�������dataType��
     * @param dataType
     */
    public void setDataType2(String dataType)
    {
        this.dataType = dataType.substring(0, 1);
    }

    public int getDataLen()
    {
        return dataLen;
    }

    public void setDataLen(int dataLen)
    {
        this.dataLen = dataLen;
    }

    public String getDataAop()
    {
        return dataAop;
    }

    public void setDataAop(String dataAop)
    {
        this.dataAop = dataAop;
    }

    public long getFmlId()
    {
        return fmlId;
    }

    public void setFmlId(long fmlId)
    {
        this.fmlId = fmlId;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("�������ʶ��" + dataItemId
                + System.getProperty("line.separator"));
        stringBuilder.append("���������ƣ�" + dataName
                + System.getProperty("line.separator"));
        stringBuilder.append("�������" + getDataLvL2()
                + System.getProperty("line.separator"));
        stringBuilder.append("���������ͣ�" + getDataType2()
                + System.getProperty("line.separator"));
        stringBuilder.append("������ȣ�" + dataLen
                + System.getProperty("line.separator"));
        stringBuilder.append("FML��ʶ��" + fmlId
                + System.getProperty("line.separator"));
        stringBuilder.append("�������麯����" + dataAop
                + System.getProperty("line.separator"));
        stringBuilder.append("������" + dataDesc);
        return stringBuilder.toString();
    }

	public String getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}

}
