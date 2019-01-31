package com.exercise.tap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Tap {

	List<OutputModel> outputList = new ArrayList<OutputModel>();

	// read CSV file
	private List<Model> ReadCsV(String path) {
		List<Model> modelList = new ArrayList<Model>();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(path));
			BufferedReader bufferedReader = new BufferedReader(reader);
			// skip the head
			bufferedReader.readLine();
			CSVReader csvReader = new CSVReader(bufferedReader);
			{
				// read record one by one
				String[] nextRecord;
				while ((nextRecord = csvReader.readNext()) != null) {
					if (nextRecord.length != 7) {
						System.out.println("Invalid CSV, Please check data!");
					} else {
						// add each element to list
						Model ml = new Model();
						ml.setID(Integer.parseInt(nextRecord[0]));
						ml.setDateTimeUTC(nextRecord[1]);
						ml.setTapType(nextRecord[2]);
						ml.setStopId(nextRecord[3]);
						ml.setCompanyId(nextRecord[4]);
						ml.setBusID(nextRecord[5]);
						ml.setPAN(nextRecord[6]);
						modelList.add(ml);
					}
				}
			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelList;
	}

	// identify the type of tap include: uncompleted, completed, cancel( cancel type
	// is no charge)
	private void IdtTapType(List<Model> tempModel) {
		int i = 0, j = i + 1;
		while (i < tempModel.size() && j < tempModel.size()) {
			if (tempModel.get(i).getTapType().contains("ON")) {
				List<Model> payList = new ArrayList<Model>();
				if (tempModel.get(i).getPAN().equals(tempModel.get(j).getPAN())) {
					if (tempModel.get(j).getTapType().contains("OFF")) {
						if (tempModel.get(i).getStopId().equals(tempModel.get(j).getStopId())) {
							payList.add(tempModel.get(i));
							payList.add(tempModel.get(j));
							CancelledTap(payList);
							i = i + 2;
							j = i + 1;
						} else {
							payList.add(tempModel.get(i));
							payList.add(tempModel.get(j));
							CompletedTap(payList);
							i = i + 2;
							j = i + 1;
						}
					}
				} else {
					payList.add(tempModel.get(i));
					UnCompletedTap(payList);
					i = j;
					j++;
				}
				if (j == tempModel.size()) {
					payList.add(tempModel.get(i));
					UnCompletedTap(payList);
					i = j;
					j++;
				}
			} else {
				System.out.println(String.valueOf(tempModel.get(i).getID()) + ": Invalid data.");
				i = i + 2;
				j = i + 1;
			}
		}
	}

	// cancelled tap
	private void CancelledTap(List<Model> model) {
		OutputModel output = new OutputModel();
		output.setStarted(model.get(0).getDateTimeUTC());
		output.setFinished(model.get(0).getDateTimeUTC());
		output.setFromStopId(model.get(0).getStopId());
		output.setToStopId(model.get(0).getStopId());
		output.setCompanyId(model.get(0).getCompanyId());
		output.setBusID(model.get(0).getBusID());
		output.setPan(model.get(0).getPAN());
		output.setStatus("CANCELLED");
		output.setDurationSecs(0);
		output.setChargeAmount(0);
		outputList.add(output);
	}

	// completed tap charge for customer
	private void CompletedTap(List<Model> model) {
		OutputModel output = new OutputModel();
		output.setStarted(model.get(0).getDateTimeUTC());
		output.setFinished(model.get(1).getDateTimeUTC());
		output.setFromStopId(model.get(0).getStopId());
		output.setToStopId(model.get(1).getStopId());
		output.setCompanyId(model.get(0).getCompanyId());
		output.setBusID(model.get(0).getBusID());
		output.setPan(model.get(0).getPAN());
		output.setStatus("COMPLETED");
		try {
			Date date1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(model.get(0).getDateTimeUTC());
			Date date2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(model.get(1).getDateTimeUTC());
			output.setDurationSecs((date2.getTime() - date1.getTime()) / 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = model.get(0).getStopId() + model.get(1).getStopId();
		output.setChargeAmount(GetPrice(str));
		outputList.add(output);
	}

	// return price
	public double GetPrice(String str) {
		double maxPrice = 0;
		if ("Stop1Stop2".contains(str) || "Stop2Stop1".contains(str)) {
			if (maxPrice < 3.25) {
				maxPrice = 3.25;
			}
		}
		if ("Stop2Stop3".contains(str) || "Stop3Stop2".contains(str)) {
			if (maxPrice < 5.50) {
				maxPrice = 5.50;
			}
		}
		if ("Stop1Stop3".contains(str) || "Stop3Stop1".contains(str)) {
			if (maxPrice < 7.30) {
				maxPrice = 7.30;
			}
		}
		return maxPrice;
	}

	// uncompleted tap charge for customer
	private void UnCompletedTap(List<Model> model) {
		OutputModel output = new OutputModel();
		output.setStarted(model.get(0).getDateTimeUTC());
		output.setFinished(model.get(0).getDateTimeUTC());
		output.setFromStopId(model.get(0).getStopId());
		output.setToStopId(model.get(0).getStopId());
		output.setCompanyId(model.get(0).getCompanyId());
		output.setBusID(model.get(0).getBusID());
		output.setPan(model.get(0).getPAN());
		output.setStatus("UNCOMPLETED");
		output.setDurationSecs(0);
		String str = model.get(0).getStopId();
		output.setChargeAmount(GetPrice(str));
		outputList.add(output);
	}

	// write output CSV
	private void WriteCsv(List<OutputModel> output) {

		try {
			Writer writer = Files.newBufferedWriter(Paths.get("trips.csv"));
			CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
			String[] headerRecord = { "Started", "Finished", "DurationSecs", "FromStopId", "ToStopId", "ChargeAmount",
					"CompanyId", "BusID", "PAN", "Status" };
			csvWriter.writeNext(headerRecord);
			for (int i = 0; i < outputList.size(); i++) {
				csvWriter.writeNext(new String[] { outputList.get(i).getStarted(), outputList.get(i).getFinished(),
						String.valueOf(outputList.get(i).getDurationSecs()), outputList.get(i).getFromStopId(),
						outputList.get(i).getToStopId(), String.valueOf(outputList.get(i).getChargeAmount()),
						outputList.get(i).getCompanyId(), outputList.get(i).getBusID(), outputList.get(i).getPan(),
						outputList.get(i).getStatus() });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		Tap t = new Tap();
		List<Model> modelList = t.ReadCsV("taps.csv");
		t.IdtTapType(modelList);
		t.WriteCsv(t.outputList);
		System.out.println("trips.csv generated successfully.");
	}
}
