package tw.com.owllistener.network;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.com.owllistener.ProvidesDate;

public class SavesReadings {
	private static final Logger logger = LoggerFactory.getLogger(SavesReadings.class);
	private String filename;
	private ProvidesDate provider;

	public SavesReadings(String filename, ProvidesDate provider) {
		this.filename = filename;
		this.provider = provider;
	}

	public void save(EnergyMessage message) throws IOException {
		File file = new File(createFilename());

		logger.info("Attempt to save received message to " + file.getAbsolutePath());
		FileWriter appender = new FileWriter(file, true);
		CSVPrinter printer = new CSVPrinter(appender , CSVFormat.DEFAULT);
		
		String datetime = getCurrentDateTime();
		String id = message.getUnitMac();
		Integer batteryLevel = message.batteryLevel();	
		EnergyMessageChannel firstChannel = message.getChannel(0);
		Double current = firstChannel.getCurrent();
		Double total = firstChannel.getDayTotal();
		
		logger.info(String.format("saving data id:%s batteryLevel:%s current:%s total:%s", 
				id, batteryLevel , current, total));
		printer.printRecord(datetime, id, batteryLevel , current, total);
		printer.flush();
		printer.close();
		appender.close();
	}

	private String createFilename() {
		String toAppend = new SimpleDateFormat("dd-MM-yyyy").format(provider.getDate());
		return filename +"_"+toAppend+".csv";
	}

	private String getCurrentDateTime() {
		Calendar cal = Calendar.getInstance();
    	
    	DateFormat format = DateFormat.getDateTimeInstance();
    	return format.format(cal.getTime());  	
	}

}
