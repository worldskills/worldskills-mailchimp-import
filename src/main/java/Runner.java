import org.apache.http.HttpStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Runner {
	Workbook workbook = null;
	String listId = null;
	String apiURI = null;
	String authToken = null;
	int success = 0;
	int failed = 0;

	public int getSuccess() {
		return success;
	}

	public int getFailed() {
		return failed;
	}

	public Runner(String filename, String listId, String apiURI, String authToken) {

		this.listId = listId;
		this.apiURI = apiURI;
		this.authToken = authToken;

		try {
			URL resource = Runner.class.getClassLoader().getResource(filename);
			FileInputStream file = new FileInputStream(new File(resource.toURI()));
			this.workbook = new HSSFWorkbook(file);
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("Could not read file");
			e.printStackTrace();
		}
		catch (URISyntaxException e) {
			System.out.println("URI Syntax error");
			e.printStackTrace();
		}
	}

	private void run(){
		if(workbook == null) {
			System.out.println("No workbook");
			System.exit(0);
		}


		Sheet sheet = workbook.getSheetAt(0);

		for (Row r : sheet) {

			//skip if email is null or empty
			if(r.getCell(4) == null || r.getCell(4).getStringCellValue().isEmpty()) continue;

			//handle rows
			Record rec = new Record();


			//Quick and dirty mapping of fields, could use a better sanity check
			if (r.getCell(4) != null)
				rec.setEmail(r.getCell(4).getStringCellValue());

			if (r.getCell(0) != null)
				rec.getFields().put("TYPE", r.getCell(0).getStringCellValue());

			if (r.getCell(1) != null)
				rec.getFields().put("TITLE", r.getCell(1).getStringCellValue());

			if (r.getCell(2) != null)
				rec.getFields().put("FNAME", (r.getCell(2).getStringCellValue()));

			if (r.getCell(3) != null)
				rec.getFields().put("LNAME", (r.getCell(3).getStringCellValue()));

			if (r.getCell(5) != null)
				rec.getFields().put("COUNTRY", r.getCell(5).getStringCellValue());

			if (r.getCell(6) != null)
				rec.getFields().put("JOB", r.getCell(6).getStringCellValue());

			if (r.getCell(7) != null)
				rec.getFields().put("ORG", r.getCell(7).getStringCellValue());

			//set status as pending, will force "double opt-in" and send an automated opt-in email
			rec.setStatus("pending");
			this.pushRecord(rec);
		}

	}

	private void pushRecord(Record rec) {

		Response res = RESTUtil.sendPOST(this.apiURI + "lists/" + this.listId + "/members/", this.authToken, rec);

		if(res.getStatus() != HttpStatus.SC_OK){
			System.out.println("Import failed: " + rec.getEmail());
			System.out.println(res.readEntity(RESTUtil.ErrorView.class));
			this.failed++;
		}
		else{
			System.out.println("Imported: " + rec.getEmail());
			this.success++;
		}

	}

	public static void main(String[] args){
		final String filename = "mailchimp_test.xls";
		final String listId = "mailchimp-list-id";
		final String apiURI = "https://<DC>.api.mailchimp.com/3.0/";
		final String authToken = "mailchimp-basic-token";

		Runner r = new Runner(filename, listId, apiURI, authToken);
		r.run();
		System.out.println();
		System.out.println("=================================");
		System.out.println("Success: " + r.getSuccess());
		System.out.println("Failed: " + r.getFailed());
		System.out.println("=================================");
	}

	private boolean isNullOrEmpty(String test){
		return !(test != null && !test.isEmpty());
	}

}

