package Activites;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		String fileName = "Activities.txt";
		List<MonitoredData> listMonitor = new ArrayList<MonitoredData>();
		try {
			Stream<String> stream = Files.lines(Paths.get(fileName));
			stream.forEach(line -> {
				String delims = "[		]+";
				String[] tokens = line.split(delims);
				MonitoredData m = new MonitoredData();
				DateFormat formatter1;
				formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					m.setStartTime(formatter1.parse(tokens[0]));
					m.setEndTime(formatter1.parse(tokens[1]));
					m.setActivity(tokens[2]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listMonitor.add(m);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		DistinctDays days = (List<MonitoredData> m) -> {

			long l = m.stream().filter(distinctByKey(MonitoredData::getStartDate))
					.filter(distinctByKey(MonitoredData::getEndDate)).count();
			PrintWriter writer;
			try {
				writer = new PrintWriter("distictdays.txt");
				writer.println(l);
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};

		days.countDistinctDays(listMonitor);

		DistinctAction actions = (List<MonitoredData> m) -> {
			Map<String, Integer> activityMap = new HashMap<String, Integer>();
			List<MonitoredData> copy = new ArrayList<MonitoredData>();
			copy.addAll(m);
			Stream<MonitoredData> act = copy.stream().filter(distinctByKey(MonitoredData::getActivity));
			act.sorted().forEach(activity -> {
				copy.addAll(m);
				long l = copy.stream().filter(a -> a.getActivity().equals(activity.getActivity())).count();
				Integer i = (int) l;
				activityMap.put(activity.getActivity(), i);
			}

			);

			PrintWriter writer = null;
			try {
				writer = new PrintWriter("distictActivites.txt");
				for (String s : activityMap.keySet()) {
					writer.println(s + " " + activityMap.get(s));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();
		};

		actions.countDistinctActions(listMonitor);

		ComputeDuration comp = (List<MonitoredData> m) -> {
			Map<String, Long> durationMap = new HashMap<String, Long>();
			List<MonitoredData> copy = new ArrayList<MonitoredData>();
			copy.addAll(m);
			Stream<MonitoredData> act = copy.stream().filter(distinctByKey(MonitoredData::getActivity));
			act.sorted().forEach(activity -> {
				copy.addAll(m);
				long sum = copy.stream().filter(a -> a.getActivity().equals(activity.getActivity()))
						.mapToLong(i -> i.duration()).sum();

				if (sum < 36000000) {
					durationMap.put(activity.getActivity(), sum/60000);
				}

			}

			);

			PrintWriter writer = null;
			try {
				writer = new PrintWriter("computeDuration.txt");
				for (String s : durationMap.keySet()) {
					writer.println(s + " " + durationMap.get(s));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();

		};

		comp.computeDuration(listMonitor);

		ActivitesPerDays activites = (List<MonitoredData> m) -> {
			Map<Integer, Map<String, Integer>> map = new HashMap<Integer, Map<String, Integer>>();
			Map<String, Integer> activityMap = new HashMap<String, Integer>();
			List<MonitoredData> copy1 = new ArrayList<MonitoredData>();
			List<MonitoredData> copy2 = new ArrayList<MonitoredData>();
			copy1.addAll(m);
			Stream<MonitoredData> date = copy1.stream().filter(distinctByKey(MonitoredData::getStartDate))
					.filter(distinctByKey(MonitoredData::getEndDate)).sorted();
			date.forEach(d -> {
				copy2.clear();
				copy2.addAll(m);
				activityMap.clear();
				Stream<MonitoredData> act = copy2.stream().filter(da -> da.getStartDate().equals(d.getStartDate()))
						.filter(distinctByKey(MonitoredData::getActivity));
				act.sorted().forEach(a -> {
					copy2.clear();
					copy2.addAll(m);
					Long c = copy2.stream().filter(activity -> activity.getActivity().equals(a.getActivity()))
							.filter(da -> da.getStartDate().equals(d.getStartDate())).count();
					Integer i = c.intValue();
					activityMap.put(a.getActivity(), i);
				});

				map.put(d.date(), activityMap);

			});
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("activitesPerDay.txt");
				for (Integer i : map.keySet()) {
					for (String key : map.get(i).keySet()) {
						writer.println(i + " " + key + " " + map.get(i).get(key));
					}

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();
		};

		activites.countActivites(listMonitor);

		ActitityFilter filter = (List<MonitoredData> m) -> {
			List<MonitoredData> copy = new ArrayList<MonitoredData>();
			List<MonitoredData> list = new ArrayList<MonitoredData>();
			copy.addAll(m);
			Stream<MonitoredData> activity = copy.stream().filter(distinctByKey(MonitoredData::getActivity));
			activity.sorted().forEach(a -> {
				copy.clear();
				copy.addAll(m);
				long a5 = copy.stream().filter(act -> act.getActivity().equals(a.getActivity()))
						.filter(d -> d.duration() < 5).count();
				copy.clear();
				copy.addAll(m);
				long at = copy.stream().filter(act -> act.getActivity().equals(a.getActivity())).count();
				
				if ((a5 * 100) / at > 90) {
					list.add(a);
				}
			});

			copy.clear();
			copy.addAll(m);
			List<String> str = copy.stream().filter(distinctByKey(MonitoredData::getActivity)).filter(a -> !list.contains(a)).map(mo -> mo.getActivity())
					.collect(Collectors.toList());
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("activityFilter.txt");
				for (String key :str) {
					writer.println(key );

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.close();
		};
		filter.filterActivites(listMonitor);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

}
