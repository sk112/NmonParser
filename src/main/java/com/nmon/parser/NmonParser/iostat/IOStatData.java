package com.nmon.parser.NmonParser.iostat;

import com.nmon.parser.NmonParser.common.CommonUtils;
import com.opencsv.CSVWriter;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class IOStatData {

    LinkedHashMap<String , Object> ioStatMap = new LinkedHashMap<>();

    /**
     *  - time
     *      \ --metric
     *      \ --value
     */
    public LinkedHashMap<String, HashMap<String, String>> iostatData = new LinkedHashMap<String, HashMap<String, String>>();

    Integer timeInterval;

    public IOStatData() throws IOException, ParseException {

        loadAttributes();

        //Set TimeInterval Value to the field.
        this.timeInterval = (Integer) ioStatMap.get("Timeinterval");

        loadIOStatData();

        convertIOStatToCSV();

    }

    private void convertIOStatToCSV() throws IOException {

//        SortedMap<String, HashMap<String, String>> iostatData = new TreeMap<String, HashMap<String, String>>(this.iostatData);


        File file = new File("iostat.csv");

        FileWriter fileWriter = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(fileWriter);

        List<String> list = ((List<String>)ioStatMap.get("Metrics"));

        list.add(0, "timestamp");

//        String [] headers = list.toArray(new String[0]);

        List<String[]> data;

        AtomicBoolean notSet = new AtomicBoolean(true);
        AtomicReference<List<String>> listMetricKeys = new AtomicReference<>();

        iostatData.keySet().forEach(entry -> {
            HashMap<String, String> map = iostatData.get(entry);

            if(notSet.get()){
                Set<String> metricKeys = map.keySet();

                listMetricKeys.set(new ArrayList<>(metricKeys));

                listMetricKeys.get().add(0, "timestamp");
                String [] headers = listMetricKeys.get().toArray(new String[0]);
                csvWriter.writeNext(headers);

                notSet.set(false);
            }

            List<String> listValues = new ArrayList<>(map.values());

            listValues.add(0, entry);


            String [] values = listValues.toArray(new String[0]);

            csvWriter.writeNext(values);
        });

        fileWriter.close();
    }

    private void loadIOStatData() throws IOException, ParseException {
        File resource = new ClassPathResource("static/data/iostat/IOStatData.iostat").getFile();
        Path  path = FileSystems.getDefault().getPath(resource.getPath());

        List<String> lines = Files.readAllLines(path);

        List<String> times = lines.stream().filter(line -> line.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2} AM|PM")).collect(Collectors.toList());

        List<String> filter = lines.stream().filter(line -> line.startsWith(String.valueOf(ioStatMap.get("Device")))).collect(Collectors.toList());

        Iterator<String> metric = filter.iterator();
        Iterator<String> time = times.iterator();
        while(metric.hasNext() && time.hasNext()){

            String metricName = metric.next();
            String timeString = time.next();

            StringTokenizer tokens = new StringTokenizer(metricName, " ");

            tokens.nextToken(); //ignoring 'sdb'
            List<HashMap<String,String>> values = new ArrayList<>();

            int count = 0;
            HashMap<String,String> map =  new HashMap<>();
            while(tokens.hasMoreTokens()){

                map.put(((List<String>)ioStatMap.get("Metrics")).get(count), tokens.nextToken());
                count++;
            }

            iostatData.put(String.valueOf(CommonUtils.getTimeStampFromFormattedTime(timeString, "MM/dd/yyyy hh:mm:ss a")), map);
        }
    }

    private void loadAttributes() throws IOException {

        File resource = new ClassPathResource("static/data/iostat/IOstatHeadersFile").getFile();
        Path path = FileSystems.getDefault().getPath(resource.getPath());

        Yaml yml = new Yaml();

        InputStream input = this.getClass().getClassLoader().getResourceAsStream("static/data/iostat/IOstatHeadersFile");

        this.ioStatMap = yml.load(input);

    }
}
