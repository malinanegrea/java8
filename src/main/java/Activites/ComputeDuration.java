package Activites;

import java.util.List;

public interface ComputeDuration {

	public void computeDuration(List<MonitoredData> m);

	 // A non-abstract (or default) function
   default void normalFun()
   {
      System.out.println("Hello");
   }
}
