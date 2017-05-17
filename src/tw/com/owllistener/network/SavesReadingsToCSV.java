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

public class SavesReadingsToCSV implements RecordsReadings {
	private static final Logger logger = LoggerFactory.getLogger(SavesReadingsToCSV.class);
	private String filename;
	private ProvidesDate provider;
	private File file;

	public SavesReadingsToCSV(String filename, ProvidesDate provider) {
		this.filename = filename;
		this.provider = provider;
	}

	@Override
	public void init() {
		file = new File(createFilename());
	}

	@Override
	public boolean record(EnergyMessage message) {

		try {
			logger.info("Attempt to record received message to " + file.getAbsolutePath());
			FileWriter appender = new FileWriter(file, true);
			CSVPrinter printer = new CSVPrinter(appender, CSVFormat.DEFAULT);

			String datetime = getCurrentDateTime();
			String id = message.getUnitMac();
			Integer batteryLevel = message.batteryLevel();
			EnergyMessageChannel firstChannel = message.getChannel(0);
			Double current = firstChannel.getCurrent();
			Double total = firstChannel.getDayTotal();

			logger.info(String.format("saving data id:%s batteryLevel:%s current:%s total:%s",
					id, batteryLevel, current, total));
			printer.printRecord(datetime, id, batteryLevel, current, total);
			printer.flush();
			printer.close();
			appender.close();
			return true;
		}
		catch (IOException exception) {
			logger.error("Unable to process due to an exception ", exception);
			return false;
		}
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
