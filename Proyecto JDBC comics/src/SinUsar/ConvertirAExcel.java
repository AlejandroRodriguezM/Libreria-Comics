package SinUsar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;


public class ConvertirAExcel {
	
    private String title;
    private String author;
    private float price;
 
    public ConvertirAExcel() {
    	this.title = "";
        this.author = "";
        this.price = 0;
    }
 
    public ConvertirAExcel(String title, String author, float price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public float getPrice() {
		return price;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
//	public void writeExcel(List<ConvertirAExcel> listBook, String excelFilePath) throws IOException {
//	    try (Workbook workbook = new HSSFWorkbook()) {
//			Sheet sheet = workbook.createSheet();
// 
//			int rowCount = 0;
// 
//			for (ConvertirAExcel aBook : listBook) {
//			    Row row = sheet.createRow(++rowCount);
//			    writeBook(aBook, row);
//			}
// 
//			try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
//			    workbook.write(outputStream);
//			}
//		}
//	}
	
//	public void writeBook(ConvertirAExcel aBook, Row row) {
//	    Cell cell = row.createCell(1);
//	    cell.setCellValue(aBook.getTitle());
//	 
//	    cell = row.createCell(2);
//	    cell.setCellValue(aBook.getAuthor());
//	 
//	    cell = row.createCell(3);
//	    cell.setCellValue(aBook.getPrice());
//	}
	
	public List<ConvertirAExcel> getListBook() {
		ConvertirAExcel book1 = new ConvertirAExcel("Head First Java", "Kathy Serria", 79);
		ConvertirAExcel book2 = new ConvertirAExcel("Effective Java", "Joshua Bloch", 36);
		ConvertirAExcel book3 = new ConvertirAExcel("Clean Code", "Robert Martin", 42);
		ConvertirAExcel book4 = new ConvertirAExcel("Thinking in Java", "Bruce Eckel", 35);
	 
	    List<ConvertirAExcel> listBook = Arrays.asList(book1, book2, book3, book4);
	 
	    return listBook;
	}
	

	
}


























	//	private static Connection conn = DBManager.conexion();

	//	public static void main(String[] args) {
	//
	//		// Blank workbook
	//		XSSFWorkbook workbook = new XSSFWorkbook();
	//
	//		// Create a blank sheet
	//		XSSFSheet sheet = workbook.createSheet("Employee Data");
	//
	//		// This data needs to be written (Object[])
	//		Map<String, Object[]> data = new TreeMap<String, Object[]>();
	//		data.put("1", new Object[] { "ID", "NAME", "LASTNAME" });
	//		data.put("2", new Object[] { 1, "Amit", "Shukla" });
	//		data.put("3", new Object[] { 2, "Lokesh", "Gupta" });
	//		data.put("4", new Object[] { 3, "John", "Adwards" });
	//		data.put("5", new Object[] { 4, "Brian", "Schultz" });
	//
	//		// Iterate over data and write to sheet
	//		Set<String> keyset = data.keySet();
	//		int rownum = 0;
	//		for (String key : keyset) {
	//			Row row = sheet.createRow(rownum++);
	//			Object[] objArr = data.get(key);
	//			int cellnum = 0;
	//			for (Object obj : objArr) {
	//				Cell cell = row.createCell(cellnum++);
	//				if (obj instanceof String)
	//					cell.setCellValue((String) obj);
	//				else if (obj instanceof Integer)
	//					cell.setCellValue((Integer) obj);
	//			}
	//		}
	//		try {
	//			// Write the workbook in file system
	//			FileOutputStream out = new FileOutputStream(new File("howtodoinjava_demo.xlsx"));
	//			workbook.write(out);
	//			workbook.close();
	//			out.close();
	//			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
	//		} catch (Exception e) {
	//			System.out.println(e);
	//		}
	//	}
