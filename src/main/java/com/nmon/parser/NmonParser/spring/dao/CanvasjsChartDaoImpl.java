package com.nmon.parser.NmonParser.spring.dao;


import com.nmon.parser.NmonParser.nmonhandlers.MetricsData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CanvasjsChartDaoImpl implements CanvasjsChartDao {

    @Override
    public  List<List<Map<Object, Object>>> getCanvasjsChartData() {
       return generateChartData("CPU001");
    }

    List<List<Map<Object, Object>>> generateChartData(String metric) {
        HashMap<String, HashMap<String, Float>> map1 = new HashMap<>()/*(HashMap<String, HashMap<String, Float>>) MetricsData.metricData.get(metric)*/;
        Map<Object,Object> map = null;
        List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
        List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();
//
//        for(Map.Entry<String, HashMap<String, Float>> entry : map1.entrySet()) {
//            map = new HashMap<Object, Object>();
//            map.put("x", entry.getKey());
//            map.put("y", entry.getValue().get("Sys%"));
//            dataPoints1.add(map);
//        }

        list.add(dataPoints1);

        return list;
    }
}