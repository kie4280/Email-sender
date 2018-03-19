import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Excelreader2 {


    Sheet st;
    public int row = 0;

    public Excelreader2(String address) {
        try {
            FileInputStream reader = new FileInputStream(address);
            Workbook wb = WorkbookFactory.create(reader);
            st = wb.getSheetAt(0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }

    public String[][] read() {

        int rowcount = 0;
        int cellcount;
        String array[][] = new String[1000][1000];

        for (int rownum = st.getFirstRowNum(); rownum <= st.getLastRowNum(); rownum++) {
            Row rows = st.getRow(rownum);
            if (rows != null) {

                for (int cellnum = 0; cellnum < rows.getLastCellNum(); cellnum++) {
                    Cell cells = rows.getCell(cellnum, Row.RETURN_BLANK_AS_NULL);
                    if (cells != null) {
                        switch (cells.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                array[rowcount][cellnum + 1] = cells.getRichStringCellValue().getString();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                array[rowcount][cellnum + 1] = Integer.toString((int) cells.getNumericCellValue());
                                break;
                        }
                    }
                }
                array[rowcount][0] = Integer.toString(rows.getLastCellNum() + 1);
                rowcount++;
            }
        }

        row = rowcount;
        int maccell = 0;
        for (int a = 0; a < row; a++) {

            for (int c = Integer.parseInt(array[a][0]); c > 0 && array[a][c] == null; c--) {
                array[a][0] = Integer.toString(c - 1);
            }
            maccell = Math.max(maccell, Integer.parseInt(array[a][0]));
        }
        for (int a = 0; a < row; a++) {
            array[a][0] = Integer.toString(maccell);
        }
        return array;
    }

}


