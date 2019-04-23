package Activites;

import java.util.List;

public interface ActitityFilter {

	public void filterActivites(List<MonitoredData> m);

	 // A non-abstract (or default) function
   default void normalFun()
   {
      System.out.println("Hello");
   }
}
