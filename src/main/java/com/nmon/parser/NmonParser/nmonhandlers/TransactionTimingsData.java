package com.nmon.parser.NmonParser.nmonhandlers;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.sql.Timestamp;

public class TransactionTimingsData {

    public HashMap<String, HashMap<String, String>> transactionTimes = new HashMap<>();

    public TransactionTimingsData() throws IOException {
        File resource = new ClassPathResource("static/data/NmonFile.nmon").getFile();
        Path path = FileSystems.getDefault().getPath(resource.getPath());

        try{
            List<String> lines = Files.readAllLines(path);
            List<String> timings =
                    lines.stream()
                    .filter(line -> line.startsWith("ZZZZ"))
                    .collect(Collectors.toList());

            timings.forEach(line -> {

                StringTokenizer token = new StringTokenizer(line, ",");

                token.nextToken(); // vomiting 'ZZZZ'

                String name = token.nextToken();

                HashMap<String, String> hash = new HashMap<>();

                String time = token.nextToken();
                hash.put("time", time);

                String date = token.nextToken();
                hash.put("date", date);

                // format : 11:39:08,02-FEB-2020
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss,dd-MMM-yyyy");
                Calendar cal = Calendar.getInstance();

                try {
                    cal.setTime(format.parse(time+","+date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                hash.put("timestamp", String.valueOf(cal.getTimeInMillis()));

                transactionTimes.put(name, hash);

            });

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
