package com.nmon.parser.NmonParser;

import com.nmon.parser.NmonParser.iostat.IOStatData;
import com.nmon.parser.NmonParser.nmonhandlers.MetricsData;
import com.nmon.parser.NmonParser.nmonhandlers.TransactionTimingsData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;

@SpringBootApplication
public class NmonParserApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NmonParserApplication.class);
	}

	public static void main(String[] args) throws IOException {
		IOStatData io = new IOStatData();
		TransactionTimingsData t = new TransactionTimingsData();
		MetricsData m = new MetricsData(t);

		SpringApplication.run(NmonParserApplication.class, args);
	}

}
