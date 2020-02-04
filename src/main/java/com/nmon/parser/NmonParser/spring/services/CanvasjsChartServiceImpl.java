package com.nmon.parser.NmonParser.spring.services;

import java.util.List;
import java.util.Map;

import com.nmon.parser.NmonParser.spring.dao.CanvasjsChartDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CanvasjsChartServiceImpl implements CanvasjsChartService {

    @Autowired
    private CanvasjsChartDaoImpl canvasjsChartDaoImpl;

    public void setCanvasjsChartDaoImpl(CanvasjsChartDaoImpl canvasjsChartDaoImpl) {
        this.canvasjsChartDaoImpl = canvasjsChartDaoImpl;
    }

    @Override
    public List<List<Map<Object, Object>>> getCanvasjsChartData() {
        return canvasjsChartDaoImpl.getCanvasjsChartData();
    }

}                        