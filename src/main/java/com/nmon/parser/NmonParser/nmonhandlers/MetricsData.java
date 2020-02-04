package com.nmon.parser.NmonParser.nmonhandlers;

import com.nmon.parser.NmonParser.utils.CommonUtils;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
public class MetricsData{

    public static HashMap<String, HashMap<String, Object> > metricNodes = new HashMap<>();
    /**
     * Object -> HashMap {String<Time>, HashMap{key, <values>} }
     * Metric ex:CPU..
     *    |-> time
     *      |-> metric ex: avg.
     *      |-> value
     */
    public static HashMap<String, Object> metricData = new HashMap<>();

    /**
     *
     * @param t
     */
    public MetricsData(TransactionTimingsData t) throws IOException {
        initializeMetricAttribute();
        initializeMetricsData(t);
    }

    /**
     * Initialize metric attributes from MetricFile into Metrics Data
     */
    public void initializeMetricAttribute() throws IOException {
        File resource = new ClassPathResource("static/data/nmon/MetricFile").getFile();
        Path path = FileSystems.getDefault().getPath(resource.getPath());

        try{
            List<String> list = Files.readAllLines(path);

            list.forEach(line -> {
                StringTokenizer token = new StringTokenizer(line, ",");
                String name = token.nextToken();

                HashMap<String, Object> hash = new HashMap<>();

                hash.put("description", token.nextToken());
                hash.put("attributes", getAttributesList(token));

                metricNodes.put(name, hash);

            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initializeMetricsData(TransactionTimingsData t) throws IOException {
        File resource = new ClassPathResource("static/data/nmon/NmonFile.nmon").getFile();
        Path path = FileSystems.getDefault().getPath(resource.getPath());

        try{

            List<String> lines = Files.readAllLines(path);

            for(Map.Entry<String, HashMap<String, Object>> entry: metricNodes.entrySet()){
                String key = entry.getKey();
                HashMap<String, Object> node = entry.getValue();

                List<String> filteredLines = lines.stream().filter(ls -> ls.startsWith(key)).collect(Collectors.toList());

                boolean firstLine = true;

                HashMap<String, HashMap<String, Float>> map = new HashMap<>();

                for(String  line : filteredLines){

                    if(!firstLine){

                        String[] tokens = line.split(",", -1);
                        AtomicInteger count = new AtomicInteger(0);

                        String Tname = tokens[count.getAndIncrement()];
                        String TZTime = tokens[count.getAndIncrement()];

                        System.out.println(Tname + " "+TZTime);
                        HashMap<String, String> tTime = t.transactionTimes.get(TZTime);

                        if(tTime != null){


                            List<String> list = (List<String>) node.get("attributes");

                            HashMap<String, Float> values = new HashMap<>();
                            list.forEach(item -> {
                                int i = count.getAndIncrement();
                                if(!tokens[i].equals("")){
                                    values.put(item, Float.parseFloat(tokens[i]));
                                }else{
                                    values.put(item, (float) 0.0);
                                }
                             });

                            String keyTime = tTime.get("time")+","+tTime.get("date");
                            //11:38:26,02-FEB-2020
                          /*  SimpleDateFormat format = new SimpleDateFormat();
                            Calendar cal = Calendar.getInstance();


                            cal.setTime(format.parse(keyTime));
                            *//*Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
                            */
                            Long timeStamp = CommonUtils.getTimeStampFromFormattedTime(keyTime, "hh:mm:ss,dd-MMM-yyyy");

                            map.put(TZTime, values);

                            metricData.put(Tname, map);
                        }else{
                            System.out.println(TZTime + "Missing..");
                        }

                    }else{
                        firstLine = false;
                    }

                }

            }

        }catch (Exception ex){
            System.out.println("An exception was thrown" + ex.getMessage());
        }
    }



    private List<String> getAttributesList(StringTokenizer token) {
        List<String> list = new ArrayList<>();

        while(token.hasMoreTokens()){
            list.add(token.nextToken());
        }

        return list;
    }





}



































