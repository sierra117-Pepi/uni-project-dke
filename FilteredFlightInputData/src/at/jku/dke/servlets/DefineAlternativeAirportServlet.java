package at.jku.dke.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frequentis.semnotam.pr.AerodromeType;
import com.frequentis.semnotam.pr.AlternateAerodromePropertyType;

import aero.aixm.CodeFlightRuleType;
import at.jku.dke.databse.AlternativeAirportDB;

@SuppressWarnings("serial")
@WebServlet("/defineAirportServlet")
public class DefineAlternativeAirportServlet extends HttpServlet {

	private static List<AlternateAerodromePropertyType> alternativeAirportDB;

	public static List<AlternateAerodromePropertyType> getAeroports() {
		return alternativeAirportDB;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		alternativeAirportDB = AlternativeAirportDB.get();
		Integer id = alternativeAirportDB.size() + 1;
		if (request.getParameter("airport") != null && !request.getParameter("airport").equals("")) {
			alternativeAirportDB = createAirport(request, alternativeAirportDB, id);
			response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/correct_altapdata.jsp");
		} else {
			response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/flight_altap.jsp");
		}
	}

	private static List<AlternateAerodromePropertyType> createAirport(HttpServletRequest request,
			List<AlternateAerodromePropertyType> alternativeAirportDB, Integer id) {
		String airport = request.getParameter("airport");
		String rules = request.getParameter("rules");
		String conditions = request.getParameter("conditions");
		AlternateAerodromePropertyType alternateAerodromePropertyType = new AlternateAerodromePropertyType();
		AerodromeType aerodromeType = new AerodromeType();
		aerodromeType.setDesignator(airport);
		aerodromeType.setId("altAP" + id.toString());
		if (rules != null && (rules.equals("IFR") || rules.equals("VFR"))) {
			aerodromeType.setFlightRule(setFlightRules(rules));
		}
		if (conditions != null && (conditions.equals("IMC") || conditions.equals("VMC"))) {
			aerodromeType.setWeather(conditions);
		}

		alternateAerodromePropertyType.setAlternateAerodrome(aerodromeType);
		alternativeAirportDB.add(alternateAerodromePropertyType);
		return alternativeAirportDB;
	}

	private static CodeFlightRuleType setFlightRules(String flightRule) {
		CodeFlightRuleType codeFlightRuleType = new CodeFlightRuleType();
		codeFlightRuleType.setValue(flightRule);
		return codeFlightRuleType;
	}
}
