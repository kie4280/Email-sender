import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;

public class Excelreader {

    Sheet st;
    public int row = 0;
    public String subject = "";
    private boolean error = false;

    public Excelreader(String address) {
        try {
            FileInputStream reader = new FileInputStream(address);
            Workbook wb = WorkbookFactory.create(reader);
            st = wb.getSheetAt(0);

        } catch (FileNotFoundException e) {
            Core.gui.setTextField2("file not found !");
            Core.gui.setEnterButtonEnable(true);
            error = true;
            return;
        }  catch (InvalidFormatException | IOException e) {
            Core.gui.setTextField2("invalid file format !");
            Core.gui.setEnterButtonEnable(true);
            error = true;
            return;
        }
    }

    public String[][] read() {

        String array[][] = new String[1000][1000];
        if(error) {return null;}
        for (int rownum = st.getFirstRowNum(); rownum <= st.getLastRowNum(); rownum++) {
            Row rows = st.getRow(rownum);
            if (rows != null) {

                for (int cellnum = 0; cellnum < rows.getLastCellNum(); cellnum++) {
                    Cell cells = rows.getCell(cellnum, Row.RETURN_BLANK_AS_NULL);
                    if (cells != null) {
                        switch (cells.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                array[rownum][cellnum + 1] = cells.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                array[rownum][cellnum + 1] = Integer.toString((int) cells.getNumericCellValue());
                                break;
                        }
                    }
                }
                array[rownum][0] = Integer.toString(rows.getLastCellNum());
            }
            else {
                array[rownum][0] = Integer.toString(0);
            }
        }
        for (int i = 0; i < st.getNumMergedRegions(); i++) {
            CellRangeAddress cellRange = st.getMergedRegion(i);
            for (int a = cellRange.getFirstRow(); a <= cellRange.getLastRow(); a++) {
                for (int b = cellRange.getFirstColumn() + 1; b <= cellRange.getLastColumn() + 1; b++) {
                    array[a][b] = array[cellRange.getFirstRow()][cellRange.getFirstColumn() + 1];
                }
            }
        }
        for(int i=1;i<=Integer.parseInt(array[0][0]);i++) {
            if(array[0][i] != null) {
                subject = array[0][i];
                break;
            }
        }
        row = st.getLastRowNum();
        return array;
    }

}
