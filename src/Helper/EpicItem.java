package Helper;

public class EpicItem implements Comparable<EpicItem>
{
	public String EpicName="";
	public int FinishPoint=0;
	public int RemainPoint=0;
	public int TotalPoint=0;
	
	
	//override
	public int compareTo(EpicItem s)
	{
		if(this.TotalPoint<s.TotalPoint)
		{
			return 1;
		}
		else if(this.TotalPoint==s.TotalPoint)
		{
			return 0;
		}
		return -1;
	}
}



