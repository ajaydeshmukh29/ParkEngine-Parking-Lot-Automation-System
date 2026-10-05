import javax.swing.SwingUtilities;

/*
    ParkEngineApp
    -------------
    Entry point of the GUI version.
    Console version  : java program1017
    GUI version      : java ParkEngineApp
*/
class ParkEngineApp
{
    public static void main(String[] args)
    {
        // Start every Swing UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ParkEngineFrame().setVisible(true));
    }
}
