package org.remast.baralga.gui.dialogs;

import info.clearthought.layout.TableLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.jdesktop.swingx.JXHeader;
import org.remast.baralga.gui.model.PresentationModel;
import org.remast.baralga.gui.settings.UserSettings;
import org.remast.swing.dialog.EscapeDialog;
import org.remast.swing.util.LabeledItem;
import org.remast.util.TextResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The settings dialog for editing both application and user settings.
 */
public class SettingsDialog extends EscapeDialog {

	private JCheckBox rememberWindowSizeLocation;
	private JCheckBox showStopwatch;
	private JComboBox<LabeledItem<String>> durationFormatSelector;
	private final PresentationModel model;

	private static final LabeledItem<String>[] DURATION_FORMAT_OPTIONS = new LabeledItem[]{
			new LabeledItem<>(UserSettings.DURATION_FORMAT_DECIMAL, "Decimal"),
			new LabeledItem<>(UserSettings.DURATION_FORMAT_HOURS_AND_MINUTES, "Hours and Minutes")
	};

	public SettingsDialog(final Frame owner, final PresentationModel model) {
		super(owner);
		this.model = model;
		initialize();
	}

	private void initialize() {
		setLocationRelativeTo(getOwner());
		setSize(400, 180);
		setTitle("Settings");

		final double border = 5;
		final double[][] size = {
				{border, TableLayout.FILL, border}, // Columns
				{border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border} // Rows
		};

		setLayout(new TableLayout(size));

		// Remember window size and location checkbox
		rememberWindowSizeLocation = new JCheckBox("Remember window size and location");
		add(rememberWindowSizeLocation, "1, 1");

		// Show stopwatch checkbox
		showStopwatch = new JCheckBox("Show stopwatch");
		add(showStopwatch, "1, 3");

		// Duration format selector
		JLabel durationFormatLabel = new JLabel("Duration Format:");
		add(durationFormatLabel, "1, 5");
		durationFormatSelector = new JComboBox<>(DURATION_FORMAT_OPTIONS);
		add(durationFormatSelector, "1, 7");

		JButton resetButton = new JButton("Reset to defaults");
		resetButton.addActionListener(e -> resetSettings());
		add(resetButton, "1, 9");

		readFromModel();

		// Action listeners
		rememberWindowSizeLocation.addActionListener(e -> writeToModel());
		showStopwatch.addActionListener(e -> {
			writeToModel();
			model.changeStopWatchVisibility();
		});
		durationFormatSelector.addActionListener(e -> writeToModel());
	}

	private void readFromModel() {
		rememberWindowSizeLocation.setSelected(UserSettings.instance().isRememberWindowSizeLocation());
		showStopwatch.setSelected(UserSettings.instance().isShowStopwatch());

		String durationFormat = UserSettings.instance().getDurationFormat();
		for (LabeledItem<String> item : DURATION_FORMAT_OPTIONS) {
			if (item.getItem().equals(durationFormat)) {
				durationFormatSelector.setSelectedItem(item);
				break;
			}
		}
	}

	private void writeToModel() {
		UserSettings.instance().setRememberWindowSizeLocation(rememberWindowSizeLocation.isSelected());
		UserSettings.instance().setShowStopwatch(showStopwatch.isSelected());
		LabeledItem<String> selectedFormat = (LabeledItem<String>) durationFormatSelector.getSelectedItem();
		if (selectedFormat != null) {
			UserSettings.instance().setDurationFormat(selectedFormat.getItem());
		}
	}

	private void resetSettings() {
		UserSettings.instance().reset();
		readFromModel();
	}
}
