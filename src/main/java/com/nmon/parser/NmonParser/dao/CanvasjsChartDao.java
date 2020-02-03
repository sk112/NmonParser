package com.nmon.parser.NmonParser.dao;

import java.util.List;
import java.util.Map;

public interface CanvasjsChartDao {

    List<List<Map<Object, Object>>> getCanvasjsChartData();

}