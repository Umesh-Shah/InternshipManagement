package ca.uwindsor.ims.dto;

import java.util.List;

public record ReportFiltersResponse(
    List<Integer> years,
    List<String> countries,
    List<String> universities,
    List<String> universityLocations,
    List<String> internshipTypes
) {}
