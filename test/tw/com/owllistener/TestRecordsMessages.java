package tw.com.owllistener;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.SavesReadings;

public class TestRecordsMessages {

	private String filename;
	private Path path;

	@Before
	public void beforeEachTestRuns() throws IOException {
		filename = "testFilename.csv";
		path = Paths.get(filename);
		removeFile();
	}
	
	@After
	public void afterEachTestRuns() throws IOException {
		removeFile();
	}

	private void removeFile() throws IOException {
		Files.deleteIfExists(path);
	}
	
	@Test
	public void shouldSaveMessageintoCsvFile() throws IOException, ParseException {
		SavesReadings recorder = new SavesReadings(filename);
		EnergyMessage message = new EnergyMessage("macid");
		EnergyMessageChannel channel = new EnergyMessageChannel(10.999, 10009.1);
		message.addChannel(channel);
		message.setBatteryLevel(90);
		recorder.save(message);
		
		Reader in = new FileReader(filename);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		int count = 0;
		CSVRecord lastRecord = null;
		for(CSVRecord record : records) {
			lastRecord = record;
			count++;
		}
		in.close();
		assertEquals(1, count);
		assertNotNull(lastRecord);
		String datetime = lastRecord.get(0);
		
		DateFormat format = DateFormat.getDateInstance();
		format.parse(datetime); // just check we can parse date
	
		assertEquals("macid", lastRecord.get(1));
		assertEquals("90", lastRecord.get(2));
		assertEquals("10.999", lastRecord.get(3));
		assertEquals("10009.1", lastRecord.get(4));
			
	}
	
	@Test
	public void shouldAppendintoCsvFile() throws IOException, ParseException {
		SavesReadings recorder = new SavesReadings(filename);
		EnergyMessage message = new EnergyMessage("macid");
		EnergyMessageChannel channel = new EnergyMessageChannel(99.999, 22009.1);
		message.addChannel(channel);
		message.setBatteryLevel(70);
		recorder.save(message);
		recorder.save(message);
		recorder.save(message);
		
		Reader in = new FileReader(filename);
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		int count = 0;
		for(@SuppressWarnings("unused") CSVRecord record : records) {
			count++;
		}
		in.close();
		assertEquals(3, count);
			
	}

}
