package com.cjinks.initialapp.database;

import java.time.LocalDate;
import java.util.LinkedHashMap;

public class Goal {

    public String goalName;
    public String goalDesc;

    public int completionRangeStart;
    public int completionRangeEnd;

    public LinkedHashMap<LocalDate, Integer> goalProgressData;

    //public String colorRange;

    public Goal(String goalName, String goalDesc, int completionRangeStart, int completionRangeEnd)
    {
        this.goalName = goalName;
        this.goalDesc = goalDesc;
        this.completionRangeStart = completionRangeStart;
        this.completionRangeEnd = completionRangeEnd;

        goalProgressData = new LinkedHashMap<LocalDate, Integer>();
    }

    public void addProgressDataPoint(LocalDate date, int value)
    {
        if(value >= completionRangeStart && value <= completionRangeEnd)
        {
            goalProgressData.put(date, value);
        }
    }
}
