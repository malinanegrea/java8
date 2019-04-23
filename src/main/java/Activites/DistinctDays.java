package Activites;

import java.util.List;

public interface DistinctDays {

	public void countDistinctDays(List<MonitoredData> m);
	
	 // A non-abstract (or default) function
    default void normalFun()
    {
       System.out.println("Hello");
    }
}
