package Activites;

import java.util.List;

public interface ActivitesPerDays {

	public void countActivites(List<MonitoredData> m);

	 // A non-abstract (or default) function
   default void normalFun()
   {
      System.out.println("Hello");
   }
}
