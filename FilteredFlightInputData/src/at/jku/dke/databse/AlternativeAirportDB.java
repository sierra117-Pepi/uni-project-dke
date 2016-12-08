package at.jku.dke.databse;

import java.util.ArrayList;
import java.util.List;

import com.frequentis.semnotam.pr.AlternateAerodromePropertyType;

public class AlternativeAirportDB {
	private static List<AlternateAerodromePropertyType> alternativeAirportDB;

	/**
	 * @return
	 */
	public static List<AlternateAerodromePropertyType> get() {
		if (alternativeAirportDB == null) {
			return new ArrayList<>();
		}
		return alternativeAirportDB;
	}

	public static void saveDB(List<AlternateAerodromePropertyType> alternativeAirports) {
		alternativeAirportDB = alternativeAirports;
	}
}
