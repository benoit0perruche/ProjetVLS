package visualizationTools;

public class test {

	public static void main(String[] arguments) throws Exception {

		long startTime = System.currentTimeMillis();
		long startTimeInter = System.currentTimeMillis();

		dataRecovery.FormatCsv.formaterFichierCsv("2014-1st-quarter.csv", "trips.csv");

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		visualizationTools.CsvWeekDay.getWeek("trips.csv", "week.csv");
		visualizationTools.CsvWeekDay.getWeekEnd("trips.csv", "weekend.csv");

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.getDistIntervalsNoDist("week.csv","static.json",24);
		visualizationTools.Intervals.getDistIntervalsNoDist("weekend.csv","static.json",24);

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.changeDistIntervals("week.csv",visualizationTools.Intervals.getDistIntervals());
		visualizationTools.Intervals.changeDistIntervals("weekend.csv",visualizationTools.Intervals.getDistIntervals());

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.getTimeIntervalsStart("week.csv",24);
		visualizationTools.Intervals.getTimeIntervalsStart("weekend.csv",24);

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.changeTimeIntervalsStart("week.csv",visualizationTools.Intervals.getStartTimeIntervals());
		visualizationTools.Intervals.changeTimeIntervalsStart("weekend.csv",visualizationTools.Intervals.getStartTimeIntervals());

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.getTimeIntervalsEnd("week.csv",24);
		visualizationTools.Intervals.getTimeIntervalsEnd("weekend.csv",24);

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);
		
		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.changeTimeIntervalsEnd("week.csv",visualizationTools.Intervals.getEndTimeIntervals());
		visualizationTools.Intervals.changeTimeIntervalsEnd("weekend.csv",visualizationTools.Intervals.getEndTimeIntervals());

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		System.out.println(elapsedTime/1000.);

		startTimeInter = System.currentTimeMillis();

		visualizationTools.Intervals.putIndex("week.csv","static.json");		
		visualizationTools.Intervals.putIndex("weekend.csv","static.json");

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTimeInter;
		long elapsedTimeT = stopTime - startTime;
		System.out.println(elapsedTime/1000.);
		System.out.println(elapsedTimeT/1000.);

	}

}
