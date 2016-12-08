package at.jku.dke.databse;

import java.util.ArrayList;
import java.util.List;

import com.frequentis.semnotam.pr.SegmentType;

public class SegmentDB {

	private static List<SegmentType> segmentsDB;

	public static List<SegmentType> get() {
		if (segmentsDB == null) {
			return new ArrayList<>();
		}
		return segmentsDB;
	}

	public static void saveDB(List<SegmentType> segments) {
		segmentsDB = segments;
	}
}
