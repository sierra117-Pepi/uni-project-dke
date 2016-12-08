package at.jku.dke.servlets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frequentis.semnotam.pr.AlternateAerodromePropertyType;
import com.frequentis.semnotam.pr.SegmentType;

import at.jku.dke.filter.FilterData;
import at.jku.dke.marshalling.InputFileCreator;

@SuppressWarnings("serial")
@WebServlet("/defineFlightServlet")
public class DefineFlightServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			String apType = request.getParameter("aptype");
			String designator = request.getParameter("apdesignator");
			String wingspam = request.getParameter("apwingspam");
			String maxWeight = request.getParameter("apmaxweight");
			String minWeight = request.getParameter("apminweight");
			String departTime = createDateString(request.getParameter("departDate"), request.getParameter("departHour"),
					request.getParameter("departMin"), "00");
			String arriveTime = createDateString(request.getParameter("arriveDate"), request.getParameter("arriveHour"),
					request.getParameter("arriveMin"), "00");
			String route = request.getParameter("route");
			String departAP = request.getParameter("takeoff");
			String rulesDep = request.getParameter("rulesDep");
			String conditionsDep = request.getParameter("conditionsDep");
			String arriveAP = request.getParameter("land");
			String rulesDes = request.getParameter("rulesDes");
			String conditionsDes = request.getParameter("conditionsDes");
			List<SegmentType> segmentDB = DefineSegmentServlet.getSegments();
			List<AlternateAerodromePropertyType> alternativeAirportDB = DefineAlternativeAirportServlet.getAeroports();
			if (correctFlightData(apType, designator, wingspam, maxWeight, minWeight, departTime, arriveTime, route,
					departAP, arriveAP)) {
				InputFileCreator.createInputFile(apType, designator, wingspam, maxWeight, minWeight, departTime,
						arriveTime, route, departAP, rulesDep, conditionsDep, arriveAP, rulesDes, conditionsDes,
						segmentDB, alternativeAirportDB);
				FilterData.filter();
				response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/correct_flightdata.jsp");
			} else {
				response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/flight_input.jsp");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static boolean correctFlightData(String apType, String designator, String wingspam, String maxWeight,
			String minWeight, String departTime, String arriveTime, String route, String departAP, String arriveAP) {
		if (apType == null || apType.equals("")) {
			return false;
		}
		if (designator == null || designator.equals("")) {
			return false;
		}
		if (wingspam == null || wingspam.equals("")) {
			return false;
		}
		if (maxWeight == null || maxWeight.equals("")) {
			return false;
		}
		if (minWeight == null || minWeight.equals("")) {
			return false;
		}
		if (departAP == null || departAP.equals("")) {
			return false;
		}
		if (arriveAP == null || arriveAP.equals("")) {
			return false;
		}

		return checkDate(departTime, arriveTime);
	}

	private static boolean checkDate(String deaprtTime, String arriveTime) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		TimeZone timeZoneLocalTime = Calendar.getInstance().getTimeZone();
		format.setTimeZone(timeZoneLocalTime);
		Date depart = null;
		Date arrive = null;
		try {
			depart = format.parse(deaprtTime);
			arrive = format.parse(arriveTime);

			String current = correctTimeFormat(new Date().toString());
			Date currentDate = format.parse(current);
			if (currentDate.after(depart) || currentDate.after(arrive)) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return depart.before(arrive);
	}

	private static String correctTimeFormat(String date) {
		String[] times = date.split(" ");
		return times[2] + "-" + getMonth(times[1]) + "-" + times[5] + " " + times[3];
	}

	private static String getMonth(String month) {
		switch (month) {
			case "Jan":
				return "1";
			case "Feb":
				return "2";
			case "Mar":
				return "3";
			case "Apr":
				return "4";
			case "May":
				return "5";
			case "Jun":
				return "6";
			case "Jul":
				return "7";
			case "Aug":
				return "8";
			case "Sep":
				return "9";
			case "Oct":
				return "10";
			case "Nov":
				return "11";
			case "Dec":
				return "12";
			default:
				return "-1";
		}
	}

	private static String createDateString(String date, String hour, String min, String sec) {
		if ((date != null && hour != null && min != null)
				&& (!date.equals("") && !hour.equals("0") && !min.equals(""))) {
			String[] dates = date.split("/");
			String fullDate = dates[0] + "-" + dates[1] + "-" + dates[2] + " " + hour + ":" + min + ":" + sec;
			return fullDate;
		}
		return null;
	}
}
