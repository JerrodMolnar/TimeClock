package com.example.timeclock;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDifference
{
    private String strDuration;
    private long differenceInTime;
    private long differenceInSeconds;
    private long differenceInMinutes;
    private long differenceInHours;
    private long differenceInDays;
    private long differenceInYears;

    public void FindDifferenceInTimes(String start_date, String end_date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("E MM/dd/yyyy hh:mm:ss a");
        strDuration = "";
        try
        {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            differenceInTime = d2.getTime() - d1.getTime();

            differenceInSeconds = (differenceInTime / 1000) % 60;

            differenceInMinutes = (differenceInTime / (1000 * 60)) % 60;

            differenceInHours = (differenceInTime / (1000 * 60 * 60)) % 24;

            differenceInYears = (differenceInTime / (1000L * 60 * 60 * 24 * 365));

            differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getStrDuration()
    {
        String mm = differenceInMinutes < 10 ? "0" + differenceInMinutes : differenceInMinutes + "";
        String ss = differenceInSeconds < 10 ? "0" + differenceInSeconds : differenceInSeconds + "";

        if (differenceInYears > 0)
        {
            strDuration += differenceInYears + " yrs, ";
        } else if (differenceInDays > 0)
        {
            strDuration += differenceInDays + " days, ";
        }

        strDuration += differenceInHours + ":" + mm + ":" + ss;
        if (differenceInTime < 0)
        {
            strDuration = "Negative value. Start time must be before quitting time.";
        }
        return strDuration;
    }

    public long getDifferenceInTime()
    {
        return differenceInTime;
    }


}
