package EagleEyeAPI;

public class EagleEyeAPI {
	
	private static String back="http://apcndaekbhost:3000";

	public static String createChart(String json) {
		// TODO Auto-generated method stub
		String ret=	HttpRequest.sendPost(back+"/api/v1/charts",json);
		return ret;
	}
	public static String createChartSet(String json) {
		// TODO Auto-generated method stub
		String ret=	HttpRequest.sendPost(back+"/api/v1/chart-sets",json);
		return ret;
	}
	public static void deleteChart(String json) {
		// TODO Auto-generated method stub

	}
	public static void deleteChartSet(String json) {
		// TODO Auto-generated method stub

	}
	//
	public static void updateChart(String id,String json) {
		// TODO Auto-generated method stub
		HttpRequest.sendPost("http://apcndaekbhost:3000/api/v1/charts/"+id, json);
	}
	public static void updateChartSet(int id,String json) {
		// TODO Auto-generated method stub
		HttpRequest.sendPost("http://apcndaekbhost:3000/api/v1/chart-sets/"+id, json);
	}

}
