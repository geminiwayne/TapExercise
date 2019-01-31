package com.exercise.generator;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.exercise.tap.Model;
import com.opencsv.CSVWriter;

public class CsvGenerator {
	List<Model> tapList = new ArrayList<Model>();

	// generate completed tap
	private void RandomCompletedTap(int id) {
		Model model = new Model();
		model.setID(id);
		Random d = new Random();
		int randomMin = d.nextInt(40 + 1) + 10;
		model.setDateTimeUTC(createRandomTime(randomMin));
		model.setTapType("ON");
		Random r = new Random();
		model.setStopId("Stop" + String.valueOf(r.nextInt(2 + 1) + 1));
		String company = "Company" + String.valueOf(randomMin);
		model.setCompanyId(company);
		Random b = new Random();
		int randomBus = b.nextInt(30 + 1) + 1;
		String bus = "Bus" + String.valueOf(randomBus);
		model.setBusID(bus);
		String pan = RandomPan();
		model.setPAN(pan);

		Model model1 = new Model();
		Random r1 = new Random();
		model1.setID(id + 1);
		model1.setDateTimeUTC(createRandomTime(randomMin + r1.nextInt(2 + 1) + 1));
		model1.setTapType("OFF");
		model1.setStopId("Stop" + String.valueOf(r1.nextInt(2 + 1) + 1));
		model1.setCompanyId(company);
		model1.setBusID(bus);
		model1.setPAN(pan);
		tapList.add(model);
		tapList.add(model1);
	}

	// generate uncompleted tap
	private void RandomUncomTap(int id) {
		Model model = new Model();
		model.setID(id);
		Random d = new Random();
		int randomMin = d.nextInt(40 + 1) + 10;
		model.setDateTimeUTC(createRandomTime(randomMin));
		model.setTapType("ON");
		Random r = new Random();
		model.setStopId("Stop" + String.valueOf(r.nextInt(2 + 1) + 1));
		model.setCompanyId("Company1");
		model.setBusID("Bus37");
		String pan = RandomPan();
		model.setPAN(pan);
		tapList.add(model);
	}

	// generate cancelled tap
	private void RandomCaTap(int id) {
		Model model = new Model();
		model.setID(id);
		Random d = new Random();
		int randomMin = d.nextInt(40 + 1) + 10;
		model.setDateTimeUTC(createRandomTime(randomMin));
		model.setTapType("ON");
		Random r = new Random();
		String stop = "Stop" + String.valueOf(r.nextInt(2 + 1) + 1);
		model.setStopId(stop);
		String company = "Company" + String.valueOf(randomMin);
		model.setCompanyId(company);
		Random b = new Random();
		int randomBus = b.nextInt(30 + 1) + 1;
		String bus = "Bus" + String.valueOf(randomBus);
		model.setBusID(bus);
		String pan = RandomPan();
		model.setPAN(pan);

		Model model1 = new Model();
		Random r1 = new Random();
		model1.setID(id + 1);
		model1.setDateTimeUTC(createRandomTime(randomMin + r1.nextInt(2 + 1) + 1));
		model1.setTapType("OFF");
		model1.setStopId(stop);
		model1.setCompanyId(company);
		model1.setBusID(bus);
		model1.setPAN(pan);
		tapList.add(model);
		tapList.add(model1);
	}

	// create random PAN
	private String RandomPan() {
		String pan = "";
		for (int i = 0; i < 16; i++) {
			Random r = new Random();
			int val = r.nextInt(10);
			pan = pan + String.valueOf(val);
		}
		return pan;
	}

	// create random date
	private int createRandomIntBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	private String createRandomTime(int val) {
		int second = createRandomIntBetween(10, 60);
		String date = "10/10/2018 " + "09:" + String.valueOf(val) + ":" + String.valueOf(second);
		return date;
	}

	// write taps CSV for testing
	private void WriteCsv(List<Model> output) {

		try {
			Writer writer = Files.newBufferedWriter(Paths.get("taps.csv"));
			CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			String[] headerRecord = { "ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN" };
			csvWriter.writeNext(headerRecord);
			for (int i = 0; i < tapList.size(); i++) {
				csvWriter.writeNext(new String[] { String.valueOf(output.get(i).getID()),
						String.valueOf(output.get(i).getDateTimeUTC()), String.valueOf(output.get(i).getTapType()),
						String.valueOf(output.get(i).getStopId()), String.valueOf(output.get(i).getCompanyId()),
						String.valueOf(output.get(i).getBusID()), String.valueOf(output.get(i).getPAN()) });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// generate random fake data to test
	public static void main(String[] args) {

		CsvGenerator csv = new CsvGenerator();
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter a random number: ");
		int n = reader.nextInt();
		reader.close();
		for (int i = 0; i < n; i++) {
			Random r = new Random();
			int randomVal = r.nextInt(10);
			if (randomVal < 7) {
				csv.RandomCompletedTap(i + 1);
				i++;
			}
			if (randomVal == 8) {
				csv.RandomUncomTap(i + 1);
			}
			if (randomVal == 9) {
				csv.RandomCaTap(i + 1);
				i++;
			}
		}
		csv.WriteCsv(csv.tapList);
		System.out.println("taps.csv generated successfully.");
	}
}
