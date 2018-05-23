package Helper;

import java.util.List;
import java.util.ArrayList;

public class EpicItem implements Comparable<EpicItem>
{
	public String EpicID="";
	public String EpicName="";
	public int FinishPoint=0;
	public int RemainPoint=0;
	public int TotalPoint=0;
	public int RiskEstimate=0;
	
	public int m_nlevel=0;
	public boolean m_isCompleted=false; 
	
	public List<String> ChildID = new ArrayList<>();	
	
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



