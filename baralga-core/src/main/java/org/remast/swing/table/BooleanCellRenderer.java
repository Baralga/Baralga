package org.remast.swing.table;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Implementation of TableCellRenderer that returns a renderer for boolean values
 * or the default table cell renderer for other types.
 */
@SuppressWarnings("serial")
public class BooleanCellRenderer extends DefaultTableCellRenderer {

	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	/**
	 * Returns the table cell renderer. If <tt>value</tt> is a non-null instance of
	 * Boolean class, returns a renderer for boolean. Else returns default table
	 * cell renderer.
	 *
	 * @param table the <code>JTable</code>
	 * @param value the value to assign to the cell at [row, column]
	 * @param isSelected true if cell is selected
	 * @param hasFocus true if cell has focus
	 * @param row the row of the cell to render
	 * @param column the column of the cell to render
	 * @return the default table cell renderer or a checkbox renderer for boolean values
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
												   boolean isSelected, boolean hasFocus, int row, int column) {
		// If value is a Boolean, use a JCheckBox as renderer
		if (value instanceof Boolean) {
			JCheckBox checkBox = new JCheckBox();
			checkBox.setHorizontalAlignment(JCheckBox.CENTER);
			checkBox.setBorderPainted(true);

			if (isSelected) {
				checkBox.setForeground(table.getSelectionForeground());
				checkBox.setBackground(table.getSelectionBackground());
			} else {
				checkBox.setForeground(table.getForeground());
				checkBox.setBackground(table.getBackground());
			}

			checkBox.setSelected((Boolean) value);

			if (hasFocus) {
				checkBox.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			} else {
				checkBox.setBorder(noFocusBorder);
			}

			return checkBox;
		}

		// For non-Boolean values, use default rendering
		return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
	}
}
