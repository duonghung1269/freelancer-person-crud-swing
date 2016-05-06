/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import javax.swing.table.AbstractTableModel;
import model.Person;
import model.PersonCollection;

/**
 *
 * @author duonghung1269
 */
public class PersonTableModel extends AbstractTableModel {

    private String[] columnNames = { "Name", "Age", "Gender"};
    private static boolean DEBUG = true;
    private Object[][] data;
    
    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
    
    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }
    
    @Override
    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
      if (DEBUG) {
        System.out.println("Setting value at " + row + "," + col
            + " to " + value + " (an instance of "
            + value.getClass() + ")");
      }

      data[row][col] = value;
      fireTableCellUpdated(row, col);

      if (DEBUG) {
        System.out.println("New value of data:");        
      }
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(PersonCollection personCollection) {               
        this.data = new Object[personCollection.getPersonList().size()][3];
        for (int i = 0; i < personCollection.getPersonList().size(); i++) {
            Person person = personCollection.getPersonList().get(i);
            data[i] = new Object[]{person.getName(), person.getAge(), person.getGender()};
        }
    }


}
