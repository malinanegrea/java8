package Activites;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonitoredData implements Comparable<MonitoredData>{

	private Date startTime;
	private Date endTime;
	private String activity;

	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getActivity() {
		return activity;
	}
	

	public String getStartDate() {
		 DateFormat df =new SimpleDateFormat("yyyy-MMM-dd");
		return df.format(startTime);
	}

	public String getEndDate() {
		 DateFormat df =new SimpleDateFormat("yyyy-MMM-dd");
		 return df.format(endTime);
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	
	@Override
	public int compareTo(MonitoredData d) {
		if(d instanceof MonitoredData) {
			MonitoredData m = (MonitoredData)d;
			return activity.compareTo(m.getActivity());
		}
		return -1;
	}
	
	public long duration() {
		long difference =(endTime.getTime() - startTime.getTime());
		return difference;
	}
	public int date() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
}
