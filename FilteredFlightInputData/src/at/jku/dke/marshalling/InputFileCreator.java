package at.jku.dke.marshalling;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.exist.xmldb.XmldbURI;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import com.frequentis.semnotam.pr.AerodromeType;
import com.frequentis.semnotam.pr.AircraftPropertyType;
import com.frequentis.semnotam.pr.AircraftType;
import com.frequentis.semnotam.pr.AircraftTypeType;
import com.frequentis.semnotam.pr.AlternateAerodromePropertyType;
import com.frequentis.semnotam.pr.DepartureAerodromePropertyType;
import com.frequentis.semnotam.pr.DestinationAerodromePropertyType;
import com.frequentis.semnotam.pr.FilterInputPropertyType;
import com.frequentis.semnotam.pr.FilterInputType;
import com.frequentis.semnotam.pr.FlightPathPropertyType;
import com.frequentis.semnotam.pr.FlightPathType;
import com.frequentis.semnotam.pr.SegmentPropertyType;
import com.frequentis.semnotam.pr.SegmentType;
import com.frequentis.semnotam.pr.TimePeriodPropertyType;
import com.frequentis.semnotam.pr.TimePeriodType;

import aero.aixm.CodeFlightRuleType;

public class InputFileCreator {

	public static void createInputFile(String type, String designator, String wingspam, String maxWeight,
			String minWeight, String departTime, String arriveTime, String route, String departAirport, String rulesDep,
			String conditionsDep, String arriveAirport, String rulesDes, String conditionsDes,
			List<SegmentType> segmentDB, List<AlternateAerodromePropertyType> alternativeAirportDB) {
		/* Create the root element and fill it with data */
		FilterInputPropertyType filterInput = new FilterInputPropertyType();
		FilterInputType filterInputType = new FilterInputType();
		/* Create the AirCraft and fill it with data */
		AircraftPropertyType aircraftPropertyType = setAircraftData(type, designator, wingspam, maxWeight, minWeight);
		try {
			/* Create the Timeperiod and fill it with data */
			TimePeriodPropertyType timePeriodPropertyType = setTimePeriod(convertDate(departTime),
					convertDate(arriveTime));
			/* Create the Flightpath and fill it with data */
			DepartureAerodromePropertyType departureAerodromePropertyType = setDepartureAerodromePropertyType(
					departAirport, rulesDep, conditionsDep);
			DestinationAerodromePropertyType destinationAerodromePropertyType = setDestinationAerodromePropertyType(
					arriveAirport, rulesDes, conditionsDes);
			FlightPathPropertyType flightPathPropertyType = setFlightPathData(route, departureAerodromePropertyType,
					destinationAerodromePropertyType, segmentDB, /* Area */ alternativeAirportDB);
			/* Insert elements */
			filterInputType.setHasAircraft(aircraftPropertyType);
			filterInputType.setHasTimePeriod(timePeriodPropertyType);
			filterInputType.setHasFlightPath(flightPathPropertyType);
			filterInput.setFilterInput(filterInputType);

			/* Create the XML-File */
			File file = createXMLFile(filterInput);

			/* Store the XML-File in xmldb */
			storeXMLFile(file);

		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private static void storeXMLFile(File file)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {

		final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
		String collection = "/db/sample";

		String driver = "org.exist.xmldb.DatabaseImpl";
		Class<?> cl = Class.forName(driver);

		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");
		DatabaseManager.registerDatabase(database);

		Collection col = DatabaseManager.getCollection(URI + collection);
		if (col == null) {
			Collection root = DatabaseManager.getCollection(URI + XmldbURI.ROOT_COLLECTION_URI);
			CollectionManagementService mgtService = (CollectionManagementService) root
					.getService("CollectionManagementService", "1.0");
			col = mgtService.createCollection(collection.substring((XmldbURI.ROOT_COLLECTION_URI + "/").length()));
		}
		XMLResource document = (XMLResource) col.createResource(file.getName(), "XMLResource");
		document.setContent(file);
		col.storeResource(document);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static File createXMLFile(FilterInputPropertyType filterInput) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(FilterInputPropertyType.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		JAXBElement element = new JAXBElement(new QName("FilterInputType"), FilterInputPropertyType.class, filterInput);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File file = null;
		marshaller.marshal(element, file = new File("inputFlightData.xml"));

		return file;
	}

	private static FlightPathPropertyType setFlightPathData(String route,
			DepartureAerodromePropertyType departureAerodromePropertyType,
			DestinationAerodromePropertyType destinationAerodromePropertyType, /* Area */List<SegmentType> segmentDB,
			List<AlternateAerodromePropertyType> alternativeAirportDB) {
		FlightPathPropertyType flightPathPropertyType = new FlightPathPropertyType();
		FlightPathType flightPathType = new FlightPathType();
		flightPathType.setId("fp1");
		flightPathType.setRouteName(route);
		flightPathType.setHasDepartureAerodrome(departureAerodromePropertyType);
		flightPathType.setHasDestinationAerodrome(destinationAerodromePropertyType);
		if (alternativeAirportDB != null && alternativeAirportDB.size() > 0) {
			flightPathType = setAlternativeAirport(flightPathType, alternativeAirportDB);
		}
		// TODO implement Area setting only if POJO not null
		flightPathType = setSegments(flightPathType, segmentDB);
		flightPathPropertyType.setFlightPath(flightPathType);
		return flightPathPropertyType;
	}

	private static FlightPathType setAlternativeAirport(FlightPathType flightPathType,
			List<AlternateAerodromePropertyType> alternativeAirportDB) {
		for (int i = 0; i < alternativeAirportDB.size(); i++) {
			AlternateAerodromePropertyType aerodromePropertyType = alternativeAirportDB.get(i);
			flightPathType.getHasAlternateAerodrome().add(aerodromePropertyType);
		}

		return flightPathType;
	}

	private static FlightPathType setSegments(FlightPathType flightPathType, List<SegmentType> segmentDB) {
		if (segmentDB != null) {
			for (int i = 0; i < segmentDB.size(); i++) {
				SegmentPropertyType segmentPropertyType = new SegmentPropertyType();
				SegmentType segment = segmentDB.get(i);
				segmentPropertyType.setSegment(segment);
				flightPathType.getHasSegment().add(segmentPropertyType);
			}
		}
		return flightPathType;
	}

	private static DepartureAerodromePropertyType setDepartureAerodromePropertyType(String designator, String rulesDep,
			String conditionsDep) {
		DepartureAerodromePropertyType aerodromePropertyType = new DepartureAerodromePropertyType();
		AerodromeType aerodromeType = new AerodromeType();
		aerodromeType.setId("ad1");
		aerodromeType.setDesignator(designator);
		if (rulesDep != null && (rulesDep.equals("IFR") || rulesDep.equals("VFR"))) {
			aerodromeType.setFlightRule(setFlightRules(rulesDep));
		}
		if (conditionsDep != null && (conditionsDep.equals("IMC") || conditionsDep.equals("VMC"))) {
			aerodromeType.setWeather(conditionsDep);
		}
		aerodromePropertyType.setDepartureAerodrome(aerodromeType);
		return aerodromePropertyType;
	}

	private static DestinationAerodromePropertyType setDestinationAerodromePropertyType(String designator,
			String rulesDes, String conditionsDes) {
		DestinationAerodromePropertyType aerodromePropertyType = new DestinationAerodromePropertyType();
		AerodromeType aerodromeType = new AerodromeType();
		aerodromeType.setId("ad2");
		aerodromeType.setDesignator(designator);
		if (rulesDes != null && (rulesDes.equals("IFR") || rulesDes.equals("VFR"))) {
			aerodromeType.setFlightRule(setFlightRules(rulesDes));
		}
		if (conditionsDes != null && (conditionsDes.equals("IMC") || conditionsDes.equals("VMC"))) {
			aerodromeType.setWeather(conditionsDes);
		}
		aerodromePropertyType.setDestinationAerodrome(aerodromeType);
		return aerodromePropertyType;
	}

	private static TimePeriodPropertyType setTimePeriod(Date depart, Date arrival)
			throws DatatypeConfigurationException {
		TimePeriodPropertyType timePeriodPropertyType = new TimePeriodPropertyType();
		TimePeriodType timePeriodType = new TimePeriodType();

		XMLGregorianCalendar xmlGregorianCalendarStart = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(createCalender(depart));
		XMLGregorianCalendar xmlGregorianCalendarEnd = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(createCalender(arrival));
		timePeriodType.setBeginPosition(xmlGregorianCalendarStart);
		timePeriodType.setEndPosition(xmlGregorianCalendarEnd);
		timePeriodPropertyType.setTimePeriod(timePeriodType);
		return timePeriodPropertyType;
	}

	private static AircraftPropertyType setAircraftData(String type, String designator, String wingspam,
			String maxWeight, String minWeight) {
		AircraftPropertyType aircraftPropertyType = new AircraftPropertyType();
		AircraftType aircraftType = new AircraftType();
		if (type.equals("landplane")) {
			aircraftType.setId("plane1");
			aircraftType.setType(AircraftTypeType.LANDPLANE);
			aircraftType.setDesignator(designator);
			aircraftType.setWingspanFt(new BigDecimal(wingspam));
			aircraftType.setMaxWeightLb(new BigDecimal(maxWeight));
			aircraftType.setMinWeightLb(new BigDecimal(minWeight));
		} else if (type.equals("heliport")) {
			aircraftType.setId("heli1");
			aircraftType.setType(AircraftTypeType.HELICOPTER);
			aircraftType.setDesignator(designator);
			aircraftType.setWingspanFt(new BigDecimal(wingspam));
			aircraftType.setMaxWeightLb(new BigDecimal(maxWeight));
			aircraftType.setMinWeightLb(new BigDecimal(minWeight));
		}
		aircraftPropertyType.setAircraft(aircraftType);
		return aircraftPropertyType;
	}

	private static Date convertDate(String date) {
		try {
			SimpleDateFormat formatterLocalTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			TimeZone timeZoneLocalTime = Calendar.getInstance().getTimeZone();
			formatterLocalTime.setTimeZone(timeZoneLocalTime);
			Date parsedDateLocalTime = formatterLocalTime.parse(date);
			return parsedDateLocalTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static GregorianCalendar createCalender(Date date) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		gregorianCalendar.setTime(date);
		return gregorianCalendar;
	}

	private static CodeFlightRuleType setFlightRules(String flightRule) {
		CodeFlightRuleType codeFlightRuleType = new CodeFlightRuleType();
		codeFlightRuleType.setValue(flightRule);
		return codeFlightRuleType;
	}

}
