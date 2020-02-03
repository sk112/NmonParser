package com.nmon.parser.NmonParser.iostat;

import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class IOStatData {

    HashMap<String , Object> ioStatMap = new HashMap<>();

    /**
     *  - time
     *      \ --metric
     *      \ --value
     */
    public HashMap<Long ,HashMap<String, String>> iostatData = new HashMap<>();
    Integer timeInterval;

    public IOStatData() throws IOException {

        loadAttributes();

        //Set TimeInterval Value to the field.
        this.timeInterval = (Integer) ioStatMap.get("Timeinterval");

        loadIOStatData();

    }

    private void loadIOStatData() throws IOException {
        File resource = new ClassPathResource("static/data/iostat/IOStatData.iostat").getFile();
        Path  path = FileSystems.getDefault().getPath(resource.getPath());

        List<String> lines = Files.readAllLines(path);

        List<String> filter = lines.stream().filter(line -> line.startsWith(String.valueOf(ioStatMap.get("Device")))).collect(Collectors.toList());

        long startTime = 0;

        for(String str : filter){

            StringTokenizer tokens = new StringTokenizer(str, " ");

            tokens.nextToken(); //ignoring 'sdb'
            List<HashMap<String,String>> values = new ArrayList<>();

            int count = 0;
            HashMap<String,String> map =  new HashMap<>();
            while(tokens.hasMoreTokens()){

                map.put(((List<String>)ioStatMap.get("Metrics")).get(count), tokens.nextToken());
                count++;
            }

            iostatData.put(startTime, map);

            startTime = startTime + timeInterval;
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
