package Charts;



import java.util.List;

import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.render.JsonRenderer;
import EagleEyeAPI.EagleEyeAPI;

public class Chart implements  IToJson,IToEagleEye {
	public String description="";
	public String chartType="";
	public String title="";
	public String xTitle="";
	public String yTitle="";
	public String yAxisFormat="";
	public List<String> colorList=null;
	public DataTable tableData;
	public String isStacked= "true";
	public int chartLeft=200;
	public int baseLineIndex=2;
	
	@Override
	public String ToJson() {
		// TODO Auto-generated method stub
		//String head="\"description\": \""+description+"\",\"options\":{\"title\":\""+title+"\",\"hAxis\":{\"title\":\"\",\"format\":\"\"},\"vAxis\":{\"title\":\"\",\"format\":\"\"},\"combolines\":\"\",\"isStacked\":false,\"chartArea\":{}},\"chartType\":\""+chartType+"\",\"datatable\":";
		String colors="";
		if(colorList!=null&&colorList.size()!=0)
		{
			colors+=",\"colors\": [";
			for(int i=0;i<colorList.size();++i)
			{			
				colors+="\""+colorList.get(i)+"\"";
				if(i!=colorList.size()-1)
					colors+=",";
			}
			colors+="]";
		}
		
		String AxisFormat=",\"hAxis\":{\"title\":\""+xTitle+"\"},\"vAxis\":{\"title\":\""+yTitle+"\",\"format\":\""+yAxisFormat+"\"}";
		
		String head="\"description\": \""+description
				+"\",\"options\":{\"title\":\""+title+"\""+colors+AxisFormat+"},\"chartType\":\""+chartType+"\",\"datatable\":";
		String body=JsonRenderer.renderDataTable(tableData, true, true, true).toString();
		return "{"+head+body+"}";
	}
	
	@Override
	public String ToEagleEye() {
		// TODO Auto-generated method stub
		String json=ToJson();
		String output=EagleEyeAPI.createChart(json);
		return output.substring(output.length()-26, output.length()-2);
	}
	
	
}
