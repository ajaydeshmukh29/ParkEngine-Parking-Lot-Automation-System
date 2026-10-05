import java.awt.*;                 // AWT : layouts, Color, Font, Dimension
import java.awt.event.*;           // AWT : ActionListener, ActionEvent
import java.io.PrintStream;
import javax.swing.*;              // Swing : JFrame, JButton, JTextArea ...

/*
    ParkEngineFrame
    ---------------
    Main window of the GUI.

    Layout (BorderLayout):
        NORTH  -> title label
        WEST   -> menu buttons   (same 5 options as your console menu)
        CENTER -> output log     (everything your classes print)
*/
class ParkEngineFrame extends JFrame
{
    private final ParkingLot parkingLot;
    private final EntryGate entryGate;
    private final ExitGate exitGate;

    private final JTextArea logArea = new JTextArea();

    ParkEngineFrame()
    {
        super("ParkEngine");

        // 1 : Redirect System.out into the text area (before anything prints)
        System.setOut(new PrintStream(new TextAreaStream(logArea), true));

        // 2 : Build the parking lot using your existing classes
        ParkingLotSetup setup = new ParkingLotSetup();
        parkingLot = setup.parkingLot;
        entryGate = setup.entryGate;
        exitGate = setup.exitGate;

        // 3 : Build the window
        buildUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 520);
        setLocationRelativeTo(null);     // centre on screen
    }

    private void buildUI()
    {
        setLayout(new BorderLayout(10, 10));

        // ---------- NORTH : title ----------
        JLabel title = new JLabel("ParkEngine - A Smart Parking Management System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(30, 60, 120));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        // ---------- WEST : buttons ----------
        JPanel menu = new JPanel(new GridLayout(0, 1, 8, 8));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        menu.add(makeButton("Park Vehicle",    e -> parkVehicle()));
        menu.add(makeButton("Exit Vehicle",    e -> exitVehicle()));
        menu.add(makeButton("Search Vehicle",  e -> searchVehicle()));
        menu.add(makeButton("Display Parking Lot", e -> parkingLot.displayParkingLot()));
        menu.add(makeButton("Clear Log",       e -> logArea.setText("")));
        menu.add(makeButton("Exit",            e -> System.exit(0)));

        // Keep the buttons at the top instead of stretching to full height
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.add(menu, BorderLayout.NORTH);
        add(menuWrapper, BorderLayout.WEST);

        // ---------- CENTER : log ----------
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    // Small helper: creates a button and attaches its click handler
    private JButton makeButton(String text, ActionListener listener)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(190, 42));
        button.addActionListener(listener);     // AWT event handling
        return button;
    }

    private void showError(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ---------------- Menu option 1 : Park Vehicle ----------------
    private void parkVehicle()
    {
        JComboBox<VehicleType> typeBox = new JComboBox<>(VehicleType.values());
        JTextField numberField = new JTextField(12);

        JPanel form = new JPanel(new GridLayout(2, 2, 6, 6));
        form.add(new JLabel("Vehicle type :"));
        form.add(typeBox);
        form.add(new JLabel("Vehicle number :"));
        form.add(numberField);

        int result = JOptionPane.showConfirmDialog(this, form, "Park Vehicle",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result != JOptionPane.OK_OPTION)
        {
            return;
        }

        String number = numberField.getText().trim();

        if(number.isEmpty() || number.contains(" "))
        {
            showError("Enter a vehicle number without spaces");
            return;
        }

        try
        {
            // Same Factory + ParkingLot calls as your console version
            Vehicle vehicle = VehicleFactory.creatVehicle((VehicleType) typeBox.getSelectedItem(), number);

            ParkingTicket ticket = parkingLot.parkVehicle(vehicle, entryGate);

            ticket.displayTicket();
        }
        catch(Exception eobj)
        {
            showError(eobj.getMessage());
        }
    }

    // ---------------- Menu option 2 : Exit Vehicle ----------------
    private void exitVehicle()
    {
        JTextField ticketField = new JTextField(10);
        JComboBox<String> paymentBox = new JComboBox<>(new String[] {"Cash", "UPI", "Card"});

        JPanel form = new JPanel(new GridLayout(2, 2, 6, 6));
        form.add(new JLabel("Ticket number :"));
        form.add(ticketField);
        form.add(new JLabel("Payment option :"));
        form.add(paymentBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Exit Vehicle",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result != JOptionPane.OK_OPTION)
        {
            return;
        }

        try
        {
            int ticketNumber = Integer.parseInt(ticketField.getText().trim());

            PaymentStrategy paymentStrategy;

            switch(paymentBox.getSelectedIndex())
            {
                case 0 :  paymentStrategy = new CashPayment();  break;
                case 1 :  paymentStrategy = new UPIPayment();   break;
                default : paymentStrategy = new CardPayment();  break;
            }

            parkingLot.removeVehicle(ticketNumber, exitGate, paymentStrategy);
        }
        catch(NumberFormatException nobj)
        {
            showError("Ticket number must be a number");
        }
        catch(Exception eobj)
        {
            showError(eobj.getMessage());
        }
    }

    // ---------------- Menu option 3 : Search Vehicle ----------------
    private void searchVehicle()
    {
        String number = JOptionPane.showInputDialog(this, "Enter vehicle number :", "Search Vehicle",
                            JOptionPane.PLAIN_MESSAGE);

        if(number == null || number.trim().isEmpty())
        {
            return;
        }

        ParkingTicket ticket = parkingLot.searchVehicle(number.trim());

        if(ticket == null)
        {
            System.out.println("This vechile is not parked");
        }
        else
        {
            ticket.displayTicket();
        }
    }
}
