package Activites;

import java.util.List;

public interface DistinctAction {
	
	public void countDistinctActions(List<MonitoredData> m);

	 // A non-abstract (or default) function
    default void normalFun()
    {
       System.out.println("Hello");
    }
}
