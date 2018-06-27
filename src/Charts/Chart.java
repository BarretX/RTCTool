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
	public List<String> ticks=null;
	
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
		
		String Ticks="";
		String format="";
		String dateformat="M/d/yy";
		if(ticks!=null&&ticks.size()!=0)
		{
			Ticks+=",\"ticks\": [";
			for(int i=0;i<ticks.size();++i)
			{			
				Ticks+="\""+ticks.get(i)+"\"";
				if(i!=ticks.size()-1)
					Ticks+=",";
			}
			Ticks+="]";
			format+=",\"format\":\""+dateformat+"\"";  //if you set ticket, is possible a line trend, date is the x_Axis
		}
		
		String AxisFormat=",\"hAxis\":{\"title\":\""+xTitle+"\"" +format+Ticks+ "},\"vAxis\":{\"title\":\""+yTitle+"\",\"format\":\""+yAxisFormat+"\"}";
		String Legend=",\"legend\":{\"position\":\"bottom\",\"alignment\":\"center\",\"maxLines\":\"2\",\"textStyle\":{\"color\":\"#555555\"}}";
		//String ChartArea=",\"chartArea\":{\"height\":\"175%\"}";
		String head="\"description\": \""+description
				+"\",\"options\":{\"title\":\""+title+"\""+colors+AxisFormat+Legend+"},\"chartType\":\""+chartType+"\",\"datatable\":";
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
