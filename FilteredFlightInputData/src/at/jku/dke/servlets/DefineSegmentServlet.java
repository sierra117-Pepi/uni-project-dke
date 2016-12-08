package at.jku.dke.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.frequentis.semnotam.pr.SegmentType;
import com.frequentis.semnotam.pr.TimePeriodPropertyType;
import com.frequentis.semnotam.pr.TimePeriodType;

import aero.aixm.CodeFlightRuleType;
import at.jku.dke.databse.SegmentDB;
import net.opengis.gml.DirectPositionType;

@SuppressWarnings("serial")
@WebServlet("/defineSegmentServlet")
public class DefineSegmentServlet extends HttpServlet {

	private static List<SegmentType> segmentDB;
	private static int points = 1;

	public static List<SegmentType> getSegments() {
		return segmentDB;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		segmentDB = SegmentDB.get();
		Integer id = segmentDB.size() + 1;
		if (checkCorrectInput(request)) {
			segmentDB = createSegment(request, segmentDB, id);
			SegmentDB.saveDB(segmentDB);
			response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/correct_segmentdata.jsp");
		} else {
			response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/flight_segment.jsp");
		}
	}

	private static boolean checkCorrectInput(HttpServletRequest request) {
		if (request.getParameter("designator") == null || request.getParameter("designator").equals("")) {
			return false;
		}
		if (request.getParameter("departDate") == null) {
			return false;
		}
		if (request.getParameter("departHour") == null) {
			return false;
		}
		if (request.getParameter("departMin") == null) {
			return false;
		}
		if (request.getParameter("arriveDate") == null) {
			return false;
		}
		if (request.getParameter("arriveHour") == null) {
			return false;
		}
		if (request.getParameter("arriveMin") == null) {
			return false;
		}

		if (request.getParameter("startposition") == null) {
			return false;
		}

		if (request.getParameter("endposition") == null) {
			return false;
		}

		return checkCorrectTime(request.getParameter("departDate"), request.getParameter("departHour"),
				request.getParameter("departMin"), request.getParameter("arriveDate"),
				request.getParameter("arriveHour"), request.getParameter("arriveMin"));
	}

	private static boolean checkCorrectTime(String departDate, String departHour, String departMin, String arriveDate,
			String arriveHour, String arriveMin) {
		Date depart = convertDate(createDateString(departDate, departHour, departMin, "00"));
		Date arrive = convertDate(createDateString(arriveDate, arriveHour, arriveMin, "00"));

		String current = correctTimeFormat(new Date().toString());
		Date currentDate = convertDate(current);
		if (currentDate.after(depart) || currentDate.after(arrive)) {
			return false;
		}

		return depart.before(arrive);
	}

	private static List<SegmentType> createSegment(HttpServletRequest request, List<SegmentType> segmentDB,
			Integer segmentID) {
		SegmentType segmentType = new SegmentType();
		segmentType.setId("seg" + segmentID.toString());
		segmentType = setDesignator(request, segmentType);
		segmentType = setTimePeriod(request, segmentType);
		String rules = request.getParameter("rules");
		if (rules != null && (rules.equals("IFR") || rules.equals("VFR"))) {
			segmentType.setFlightRule(setFlightRules(rules));
		}
		String conditions = request.getParameter("conditions");
		if (conditions != null && (conditions.equals("IMC") || conditions.equals("VMC"))) {
			segmentType.setWeather(conditions);
		}
		segmentType = setStartPoint(request, segmentType);
		segmentType = setEndPoint(request, segmentType);
		segmentDB.add(segmentType);

		return segmentDB;
	}

	private static SegmentType setTimePeriod(HttpServletRequest request, SegmentType segmentType) {
		String departDate = request.getParameter("departDate");
		String departHour = request.getParameter("departHour");
		String departMin = request.getParameter("departMin");
		String arriveDate = request.getParameter("arriveDate");
		String arriveHour = request.getParameter("arriveHour");
		String arriveMin = request.getParameter("arriveMin");

		if (noTimeEntry(departDate, departHour, departMin) || noTimeEntry(arriveDate, arriveHour, arriveMin)) {
			return segmentType;
		} else {
			segmentType = createTimeEntry(segmentType, departDate, departHour, departMin, arriveDate, arriveHour,
					arriveMin);
			return segmentType;
		}
	}

	private static boolean noTimeEntry(String date, String hour, String min) {
		if (date == null || hour == null || min == null) {
			return true;
		}

		if (hour != null && hour.equals("hour")) {
			return true;
		}
		if (min != null && min.equals("min")) {
			return true;
		}

		return false;
	}

	private static SegmentType setEndPoint(HttpServletRequest request, SegmentType segmentType) {
		DirectPositionType directPositionType = new DirectPositionType();
		net.opengis.gml.PointPropertyType endPointType = new net.opengis.gml.PointPropertyType();
		net.opengis.gml.PointType end = new net.opengis.gml.PointType();
		end.setId("p" + points);
		end.setSrsDimension(new BigInteger("2"));
		end.setSrsName("urn:ogc:def:crs:EPSG::4326");
		directPositionType = setPositionCoordniates(request.getParameter("endposition"), directPositionType);
		end.setPos(directPositionType);
		JAXBElement<net.opengis.gml.PointType> endElement = new JAXBElement<>(new QName("Point"),
				net.opengis.gml.PointType.class, end);
		endPointType.setPoint(endElement);
		segmentType.setEndPoint(endPointType);
		points++;
		return segmentType;
	}

	private static SegmentType setStartPoint(HttpServletRequest request, SegmentType segmentType) {
		DirectPositionType directPositionType = new DirectPositionType();
		net.opengis.gml.PointPropertyType startPointType = new net.opengis.gml.PointPropertyType();
		net.opengis.gml.PointType start = new net.opengis.gml.PointType();
		start.setId("p" + points);
		start.setSrsDimension(new BigInteger("2"));
		start.setSrsName("urn:ogc:def:crs:EPSG::4326");
		directPositionType = setPositionCoordniates(request.getParameter("startposition"), directPositionType);
		start.setPos(directPositionType);
		JAXBElement<net.opengis.gml.PointType> startElement = new JAXBElement<>(new QName("Point"),
				net.opengis.gml.PointType.class, start);
		startPointType.setPoint(startElement);
		segmentType.setStartPoint(startPointType);
		points++;
		return segmentType;
	}

	private static DirectPositionType setPositionCoordniates(String sCoordinate,
			DirectPositionType directPositionType) {
		String[] coordinates = sCoordinate.split(" ");

		for (String coordinate : coordinates) {
			Double dCo = new Double(coordinate);
			directPositionType.getValue().add(dCo);
		}

		return directPositionType;
	}

	private static SegmentType setDesignator(HttpServletRequest request, SegmentType segmentType) {
		segmentType.setDesignator(request.getParameter("designator"));
		return segmentType;
	}

	private static CodeFlightRuleType setFlightRules(String flightRule) {
		CodeFlightRuleType codeFlightRuleType = new CodeFlightRuleType();
		codeFlightRuleType.setValue(flightRule);
		return codeFlightRuleType;
	}

	private static SegmentType createTimeEntry(SegmentType segmentType, String departDate, String departHour,
			String departMin, String arriveDate, String arriveHour, String arriveMin) {
		String departTime = createDateString(departDate, departHour, departMin, "00");
		String arriveTime = createDateString(arriveDate, arriveHour, arriveMin, "00");
		XMLGregorianCalendar xmlGregorianCalendarStart = null;
		XMLGregorianCalendar xmlGregorianCalendarEnd = null;
		try {
			Date depart = convertDate(departTime);
			Date arrive = convertDate(arriveTime);

			xmlGregorianCalendarStart = DatatypeFactory.newInstance().newXMLGregorianCalendar(createCalender(depart));
			xmlGregorianCalendarEnd = DatatypeFactory.newInstance().newXMLGregorianCalendar(createCalender(arrive));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		TimePeriodPropertyType timePeriodPropertyType = new TimePeriodPropertyType();
		TimePeriodType timePeriodType = new TimePeriodType();
		timePeriodType.setBeginPosition(xmlGregorianCalendarStart);
		timePeriodType.setEndPosition(xmlGregorianCalendarEnd);
		timePeriodPropertyType.setTimePeriod(timePeriodType);
		segmentType.setTime(timePeriodPropertyType);
		return segmentType;
	}

	private static GregorianCalendar createCalender(Date date) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		gregorianCalendar.setTime(date);
		return gregorianCalendar;
	}

	private static Date convertDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			TimeZone timeZone = Calendar.getInstance().getTimeZone();
			formatter.setTimeZone(timeZone);
			Date parsedDate = formatter.parse(date);
			return parsedDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String createDateString(String date, String hour, String min, String sec) {
		String[] dates = date.split("/");
		String fullDate = dates[0] + "-" + dates[1] + "-" + dates[2] + " " + hour + ":" + min + ":" + sec;
		return fullDate;
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
}
