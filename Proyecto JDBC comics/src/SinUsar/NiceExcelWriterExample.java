package SinUsar;

import java.io.IOException;
import java.util.List;

public class NiceExcelWriterExample {
	
	public static void main(String[] args) throws IOException {
		
		ConvertirAExcel excelWriter = new ConvertirAExcel();
		 
		@SuppressWarnings("unused")
		List<ConvertirAExcel> listBook = excelWriter.getListBook();
		@SuppressWarnings("unused")
		String excelFilePath = "NiceJavaBooks.xls";
		 
//		excelWriter.writeExcel(listBook, excelFilePath);
	}

}
