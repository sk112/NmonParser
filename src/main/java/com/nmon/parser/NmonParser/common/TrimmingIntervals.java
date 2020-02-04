package com.nmon.parser.NmonParser.common;

import com.nmon.parser.NmonParser.iostat.IOStatData;
import com.nmon.parser.NmonParser.nmonhandlers.MetricsData;
import com.nmon.parser.NmonParser.nmonhandlers.TransactionTimingsData;
import org.codehaus.plexus.component.annotations.Component;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrimmingIntervals {

    List<String> ioTimes;
    List<String> nmonTimes;

    Integer iostatInterval = 0;
    Integer nmonInterval = 0;

    int startTime;
    int endTime;

    public TrimmingIntervals(TransactionTimingsData m, IOStatData io, String start, String end) throws ParseException {

        Long startTime = CommonUtils.getTimeStampFromFormattedTime(start, "MM/dd/yyyy hh:mm:ss a");
        Long endTime = CommonUtils.getTimeStampFromFormattedTime(end,"MM/dd/yyyy hh:mm:ss a");

        List<String> nmonTimeData = new ArrayList<>(m.transactionTimes.keySet());
        List<String> iostatTimeData = new ArrayList<>(io.iostatData.keySet());

        Iterator<String> it1 =  nmonTimeData.iterator();
        Iterator<String> it2 = iostatTimeData.iterator();

        while(it1.hasNext() && it2.hasNext()){
            System.out.println(m.transactionTimes.get(it1.next()).get("timestamp")+ " " +it2.next());
        }

//        int nmonTimeInt = calculateInterval(m.metricData);
    }
}
