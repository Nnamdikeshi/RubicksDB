import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Nnamdi on 11/18/2016.
 */
public class RubicksGUI extends JFrame implements WindowListener{
    private JTable rubicksDataTable;
    private JPanel rootPanel;
    private JTextField solverName;
    private JTextField solvedTimeField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JLabel solverTextField;
    private JLabel solvedTime;

    RubicksGUI(final RubickDataModel rubickDataTableModel) {
        setContentPane(rootPanel);
        pack();
        setTitle("Rubicks Cube Database Application");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Set up JTable
        rubicksDataTable.setGridColor( Color.BLUE);
        rubicksDataTable.setModel(rubickDataTableModel);

        //Event handlers for add, delete and quit buttons
        addButton.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Get Movie title, make sure it's not blank
                String solveName = solverTextField.getText();
                // If a name hasnt been entered then display message
                if (solveName == null || solveName.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Enter the name for the next Rubicks solver.");
                    return;
                }

                int timeTaken;

                // Any time above 100 isn't acceptable in this solve table
                try {
                    timeTaken = Integer.parseInt(solvedTimeField.getText());
                    if (timeTaken > 100){
                        throw new NumberFormatException("Time needs to be less than 100.");
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(rootPane,
                            "The time needs to be less than 100.");
                    return;
                }

                System.out.println("Adding " + solveName + " " + timeTaken + " ");
                boolean insertedRow = rubickDataTableModel.insertRow(solveName, timeTaken);

                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new movie");
                }
            }

        });

        // Exits application
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RubicksDB.shutdown();
                System.exit(0);
            }
        });
        // Deletes row in data table
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = rubicksDataTable.getSelectedRow();

                if (currentRow == -1) {
                    JOptionPane.showMessageDialog(rootPane, "Please choose a movie to delete");
                }
                boolean deleted = rubickDataTableModel.deleteRow(currentRow);
                if (deleted) {
                    RubicksDB.loadSolvers ();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting movie");
                }
            }
        });
    }


    @Override
    public void windowOpened ( WindowEvent e ) {

    }

    public void windowClosing ( WindowEvent e ) {

    }

    public void windowClosed ( WindowEvent e ) {

    }

    public void windowIconified ( WindowEvent e ) {

    }

    public void windowDeiconified ( WindowEvent e ) {

    }

    public void windowActivated ( WindowEvent e ) {

    }

    public void windowDeactivated ( WindowEvent e ) {

    }
}
