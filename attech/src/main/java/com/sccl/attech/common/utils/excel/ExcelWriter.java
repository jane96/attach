package com.sccl.attech.common.utils.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * 生成导出Excel文件对象
 * 
 * @author luoyang
 * 
 */
public class ExcelWriter {
	// 设置cell编码解决中文高位字节截断
	private static short XLS_ENCODING = HSSFCell.ENCODING_UTF_16;

	// 定制浮点数格式
	private static String NUMBER_FORMAT = "#,##0.00";

	// 定制日期格式
	private static String DATE_FORMAT = "m/d/yy"; // "m/d/yy h:mm"

	private OutputStream out = null;

	private HSSFWorkbook workbook = null;

	public static HSSFSheet sheet = null;

	private HSSFRow row = null;
	
	private String ids = null; // 存储合并了的行
	
	private HSSFFont tableFont = null;
	private HSSFFont titleFont = null;
	private HSSFCellStyle titleStyle = null;
	private HSSFCellStyle cellStyle = null;
	private HSSFCellStyle tableStyle =null;
	private HSSFCellStyle dateCellStyle = null;

	public ExcelWriter() {
		
	}

	/**
	 * 初始化Excel
	 * 
	 */
	public ExcelWriter(OutputStream out) {
		this.out = out;
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet();
		tableFont = workbook.createFont();
		titleFont = workbook.createFont();
		titleStyle = workbook.createCellStyle();
		cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		tableStyle = workbook.createCellStyle();
		dateCellStyle = workbook.createCellStyle();
	}

	/**
	 * 导出Excel文件
	 * 
	 * @throws IOException
	 */
	public void export() throws FileNotFoundException, IOException {
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			throw new IOException(" 生成导出Excel文件出错! ", e);
		} catch (IOException e) {
			throw new IOException(" 写入Excel文件出错! ", e);
		}

	}

	/**
	 * 增加一行
	 * 
	 * @param index
	 *            行号
	 */
	public void createRow(int index) {
		this.row = this.sheet.createRow(index);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 5000);
	
		//sheet.setColumnWidth(10, 5000);
//		if(index ==0){
//			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
//		}
	}
	public void createRowRegions(int index,String value) {
		this.row = this.sheet.createRow(index);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 5000);
//		String[]  values;
//		if(value != null && !"".equals(value)){
//			values = value.split(",");
//		}else{
//			values = new String[] {"无数据"};
//		}
//		CellRangeAddressList regions = new CellRangeAddressList(index, index, 2, 2);  
//	    // 生成下拉框内容  
//	    DVConstraint constraint = DVConstraint.createExplicitListConstraint(values);  
//	    // 绑定下拉框和作用区域  
//	    HSSFDataValidation data_validation = new HSSFDataValidation(regions,constraint);  
//	    // 对sheet页生效  
//	    sheet.addValidationData(data_validation);  
		
		//sheet.setColumnWidth(10, 5000);
//		if(index ==0){
//			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
//		}
	}
	/**
	 * 增加一行
	 * @param index 
	 * @param size  列个数
	 * @param width 列宽
	 *            行号
	 */
	public void createRow11(int index,int size,int width) {
		this.row = this.sheet.createRow(index);
		for(int i = 0; i <=size; i ++){
			sheet.setColumnWidth(i, width);
		}
		
		
//		//sheet.setColumnWidth(10, 5000);
//		if(index ==0){
//			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, size));
//		}
	}
	
		/**
		 * 增加合并行
		 * 用于县域经济报表
		 * 
		 * @param index
		 *            行号
		 */
		public void createMergerRow(int index) {
			this.row = this.sheet.createRow(index);
			
			for(int i =0; i < 34; i++){
				sheet.setColumnWidth(i, 6000);
			}
			if(index ==0){
				for(int i =0; i <= 2; i++){
					sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
				}
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 34));
			}
		
		
		
	}
		/**
		 * 增加合并行
		 * 
		 * @param index
		 *     行号
		 */
	public void createMergerRow1(int index) {
			this.row = this.sheet.createRow(index);
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
			sheet.setColumnWidth(12, 6000);
			sheet.setColumnWidth(13, 6000);
			sheet.setColumnWidth(14, 6000);
			//sheet.setColumnWidth(10, 5000);
			if(index ==0){
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 13));
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 14, 14));
			}
			if(index == 1){
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 11));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
				sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
			}
			
		
		
		
	}

	/**
	 * 获取行
	 * @param index
	 * @return
	 */
	public HSSFRow getRow(int index){
		HSSFRow row = this.sheet.getRow(index);
		return row;
	}
	/**
	 * 特殊处理合并单元格
	 */
	public void createAddMergedRegion(){
		sheet.addMergedRegion(new CellRangeAddress(1, 23, 6, 11));
	}
	/**
	 * 获取合并的行数
	 * @return
	 */
	public String getMergeRow(){
		
		return ids;
	}
	/**
	 * 对于一些合并行的话，不能创建行了，只能获取已经创建的行，在进行表格操作
	 * @param row 行
	 * @param cells 列
	 * @param value 值
	 */
	public void setRowCell(int row,int cells,Object value){
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 4000);
		if(cells == 8){
			CellRangeAddressList regions = new CellRangeAddressList(25, 25, 8, 8);  
		    // 生成下拉框内容  
		    DVConstraint constraint = DVConstraint.createExplicitListConstraint(new String[] { "2", "3","默认图层"});  
		    // 绑定下拉框和作用区域  
		    HSSFDataValidation data_validation = new HSSFDataValidation(regions,constraint);  
		    // 对sheet页生效  
		    data_validation.createErrorBox("Error", "Error");  
		    data_validation.createPromptBox("", null);  
		    sheet.addValidationData(data_validation); 
		}
		HSSFCell cell = this.getRow(row).createCell((short) cells);
		if(value instanceof Integer){
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Integer) value).intValue());
			cell.setCellStyle(tableStyle());
			
		}else if(value instanceof Double){
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Double) value).doubleValue());
			HSSFDataFormat format = workbook.createDataFormat();
			//cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);
			cellStyle.setBorderTop((short) 1);
			// 设置表格字体
			tableFont.setFontName("宋体");
			cellStyle.setFont(tableFont);

			cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
		}else if(value instanceof String){
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);
			cellStyle.setBorderTop((short) 1);
			cellStyle.setWrapText(true);
			// 设置表格字体
			tableFont.setFontName("宋体");
			cellStyle.setFont(tableFont);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(value.toString());
			cell.setCellStyle(cellStyle);
		}else if(value instanceof Long){
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Long) value).longValue());
			cell.setCellStyle(tableStyle());
		}
		
		
		
	}
	
	/**
	 * 获取单元格的值
	 * 
	 * @param index
	 *            列号
	 */
	public String getCell(int index) {
		HSSFCell cell = this.row.getCell((short) index);
		String strExcelCell = "";
		if (cell != null) { // add this condition
			// judge
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				strExcelCell = "FORMULA ";
				break;
			case HSSFCell.CELL_TYPE_NUMERIC: {
				strExcelCell = String.valueOf(cell.getNumericCellValue());
			}
				break;
			case HSSFCell.CELL_TYPE_STRING:
				strExcelCell = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				strExcelCell = "";
				break;
			default:
				strExcelCell = "";
				break;
			}
		}
		return strExcelCell;
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, Integer value) {
		if(value == null){
			setCell(index,"");
		}else{
			HSSFCell cell = this.row.createCell((short) index);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(value);
			cell.setCellStyle(tableStyle());
		}
		
	}
	public void setCell(int index, Long value) {
		if(value == null){
			setCell(index,"");
		}else{
			HSSFCell cell = this.row.createCell((short) index);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(value);
			cell.setCellStyle(tableStyle());
		}
		
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	@SuppressWarnings("deprecation")
	public void setCell(int index, Double value) {
		if(value == null){
			setCell(index,"");
		}else{
			HSSFCell cell = this.row.createCell((short) index);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(value);
			HSSFDataFormat format = workbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);
			cellStyle.setBorderTop((short) 1);
			// 设置表格字体
			tableFont.setFontName("宋体");
			cellStyle.setFont(tableFont);

			cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
		}
		
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, String value) {
		HSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		if(value != null && !"".equals(value)){
			cell.setCellValue(value);
		}else{
			cell.setCellValue(" ");
		}
		
		cell.setCellStyle(tableStyle());

	}
	public void setCell(int index, Object value) {
		Double v = Double.parseDouble(String.valueOf(value));
		HSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(v);
		cell.setCellStyle(tableStyle());

	}

	/**
	 * 设置头部
	 * 
	 * @param index
	 * @param value
	 */
	@SuppressWarnings("deprecation")
	public void setTitleCell(int index, String value) {
		HSSFCell cell = this.row.createCell((short) index);

		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		cell.setCellStyle(setTitleStyle());
		
		
	}

	/**
	 * 设置表头样式
	 * 
	 * @return
	 */
	public HSSFCellStyle setTitleStyle() {
		// 设置标题样式
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // /水平居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
		
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		//自动换行
		titleStyle.setWrapText(true);

		// 设置字体
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setFontName("宋体");
		titleStyle.setFont(titleFont);

		return titleStyle;
	}

	/**
	 * 设置表格样式
	 * 
	 * @return
	 */
	public HSSFCellStyle tableStyle() {
		// 设置表格样式
		
		tableStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		tableStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		tableStyle.setBorderBottom((short) 1);
		tableStyle.setBorderLeft((short) 1);
		tableStyle.setBorderRight((short) 1);
		tableStyle.setBorderTop((short) 1);

		// 设置表格字体
		//HSSFFont tableFont = workbook.createFont();
		tableFont.setFontName("宋体");
		tableStyle.setFont(tableFont);
		return tableStyle;
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, Date value) {
		if(value==null){
			setCell(index,"");
		}
		HSSFCell cell = this.row.createCell((short) index);
		dateCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		dateCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		dateCellStyle.setBorderBottom((short) 1);
		dateCellStyle.setBorderLeft((short) 1);
		dateCellStyle.setBorderRight((short) 1);
		dateCellStyle.setBorderTop((short) 1);
		Calendar v =Calendar.getInstance();
		v.setTime(value);
		cell.setCellValue(v.getTime());
		dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT)); // 设置cell样式为定制的日期格式
		
		cell.setCellStyle(dateCellStyle); // 设置该cell日期的显示格式
		
	}

	

	public static void main(String[] args) {
		System.out.println(" 开始导出Excel文件 ");

		File f = new File("D:\\qt24.xls");
		ExcelWriter e = new ExcelWriter();

		try {
			//TODO 
			e = new ExcelWriter(new FileOutputStream(f));
			e.createRow(0);
			e.setTitleCell(0, "外勤人员");
			e.setTitleCell(1, "所属部门");
			e.setTitleCell(2, "出差天数");
			e.setTitleCell(3, "日期(0代表上午，1代表下午，2代表全天)");
			e.createRow(1);
			e.setTitleCell(0,"0");
			e.setTitleCell(1,"1");
			e.setTitleCell(2,"2");
			e.setTitleCell(3, "1");	
			e.createRow(2);
			e.setTitleCell(0,"0");
			e.setTitleCell(1,"1");
			e.setTitleCell(2,"2");
			e.setTitleCell(3,"2");
			e.createRow(3);
			e.setTitleCell(0,"0");
			e.setTitleCell(1,"1");
			e.setTitleCell(2,"2");
			e.setTitleCell(3,"3");
			sheet.addMergedRegion(new Region(1,(short)0,3,(short)0)); 			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			e.export();
			System.out.println(" 导出Excel文件[成功] ");
		} catch (IOException ex) {
			System.out.println(" 导出Excel文件[失败] ");
			ex.printStackTrace();
		}
	}

}
