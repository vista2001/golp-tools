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
    private String isPublished;//0-未发布,1-已发布

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
     * 返回域dataLvL的值。
     * @return 域dataLvL的值
     */
    public String getDataLvL()
    {
        return dataLvL;
    }

    /**
     * 若域dataLvL的值为“0”，则返回“0-APP”，
     * 若域dataLvL的值为“1”，则返回“1-GOLP”。
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
     * 将参数dataLvL的值赋给域dataLvL。
     * @param dataLvL
     */
    public void setDataLvL(String dataLvL)
    {
        this.dataLvL = dataLvL;
    }

    /**
     * 将参数dataLvL的第一个字符赋给域dataLvL。
     * @param dataLvL
     */
    public void setDataLvL2(String dataLvL)
    {
        this.dataLvL = dataLvL.substring(0, 1);
    }

    /**
     * 返回域dataType的值。
     * @return 域dataType的值
     */
    public String getDataType()
    {
        return dataType;
    }

    /**
     * <br>若dataType的值为“0”，返回“0-in”，
     * <br>若dataType的值为“1”，返回“1-long”，
     * <br>若dataType的值为“2”，返回“2-double”，
     * <br>若dataType的值为“3”，返回“3-char”，
     * <br>若dataType的值为“4”，返回“4-char[]”，
     * <br>若dataType的值为“5”，返回“5-String”。
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
     * 将参数dataType的值赋给域dataType。
     * @param dataType
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    /**
     * 将参数dataType的第一个字符赋给域dataType。
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
        stringBuilder.append("数据项标识：" + dataItemId
                + System.getProperty("line.separator"));
        stringBuilder.append("数据项名称：" + dataName
                + System.getProperty("line.separator"));
        stringBuilder.append("数据项级别：" + getDataLvL2()
                + System.getProperty("line.separator"));
        stringBuilder.append("数据项类型：" + getDataType2()
                + System.getProperty("line.separator"));
        stringBuilder.append("数据项长度：" + dataLen
                + System.getProperty("line.separator"));
        stringBuilder.append("FML标识：" + fmlId
                + System.getProperty("line.separator"));
        stringBuilder.append("数据项检查函数：" + dataAop
                + System.getProperty("line.separator"));
        stringBuilder.append("描述：" + dataDesc);
        return stringBuilder.toString();
    }

	public String getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}

}
