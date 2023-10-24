import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame
{
    private JTextArea receiptTextArea;
    private JRadioButton thinCrustRadioButton, regularCrustRadioButton, deepDishCrustRadioButton;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppingsCheckBoxes;
    private JButton orderButton, clearButton, quitButton;

    public PizzaGUIFrame()
    {
        // Initializations
        thinCrustRadioButton = new JRadioButton("Thin");
        regularCrustRadioButton = new JRadioButton("Regular");
        deepDishCrustRadioButton = new JRadioButton("Deep-dish");
        sizeComboBox = new JComboBox<>(new String[]{"Small", "Medium", "Large", "Super"});
        toppingsCheckBoxes = new JCheckBox[]{
                new JCheckBox("Pepperoni"),
                new JCheckBox("Mushrooms"),
                new JCheckBox("Olives"),
                new JCheckBox("Onions"),
                new JCheckBox("Bacon"),
                new JCheckBox("Pineapple")
        };
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        // radio buttons
        ButtonGroup crustButtonGroup = new ButtonGroup();
        crustButtonGroup.add(thinCrustRadioButton);
        crustButtonGroup.add(regularCrustRadioButton);
        crustButtonGroup.add(deepDishCrustRadioButton);

        // Set up panels with titled borders
        JPanel crustPanel = createTitledPanel("Crust Type", thinCrustRadioButton, regularCrustRadioButton, deepDishCrustRadioButton);
        JPanel sizePanel = createTitledPanel("Pizza Size", sizeComboBox);
        JPanel toppingsPanel = createTitledPanel("Toppings", toppingsCheckBoxes);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // layout
        receiptTextArea = new JTextArea(15, 40);
        receiptTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.add(crustPanel);
        optionsPanel.add(sizePanel);
        optionsPanel.add(toppingsPanel);
        optionsPanel.add(buttonPanel);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        orderButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                compileOrder();
            }
        });

        clearButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // Clear all selections
                receiptTextArea.setText("");
                crustButtonGroup.clearSelection();
                sizeComboBox.setSelectedIndex(0);
                for (JCheckBox checkBox : toppingsCheckBoxes)
                {
                    checkBox.setSelected(false);
                }
            }
        });

        quitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });

        // Set up the frame
        setTitle("Pizza Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private JPanel createTitledPanel(String title, Component... components)
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        for (Component component : components)
        {
            panel.add(component);
        }
        return panel;
    }

    private void compileOrder() {
        StringBuilder orderDetails = new StringBuilder();
        // Get crust
        String crustType = "";
        if (thinCrustRadioButton.isSelected())
        {
            crustType = "Thin";
        } else if (regularCrustRadioButton.isSelected())
        {
            crustType = "Regular";
        } else if (deepDishCrustRadioButton.isSelected())
        {
            crustType = "Deep-dish";
        }
        orderDetails.append("Type of Crust: ").append(crustType).append("\n");

        // Get size
        String size = sizeComboBox.getSelectedItem().toString();
        orderDetails.append("Size: ").append(size).append("\n");

        // Get toppings
        orderDetails.append("Toppings: ");
        boolean isFirstTopping = true;
        double toppingPrice = 0; // Initialize topping price
        for (JCheckBox checkBox : toppingsCheckBoxes)
        {
            if (checkBox.isSelected())
            {
                if (!isFirstTopping)
                {
                    orderDetails.append(", ");
                }
                orderDetails.append(checkBox.getText());
                isFirstTopping = false;
                toppingPrice += 1.00; // Add $1.00 for each selected topping
            }
        }
        orderDetails.append("\n");

        // Calculate subtotal, tax, and total
        double basePrice = size.equals("Small") ? 8.00 : size.equals("Medium") ? 12.00 : size.equals("Large") ? 16.00 : 20.00;
        double subtotal = basePrice + toppingPrice;
        double tax = 0.07 * subtotal;
        double total = subtotal + tax;

        // Format
        orderDetails.append("Sub-total: $").append(String.format("%.2f", subtotal)).append("\n");
        orderDetails.append("Tax (7%): $").append(String.format("%.2f", tax)).append("\n");
        orderDetails.append("----------------------------------------\n");
        orderDetails.append("Total: $").append(String.format("%.2f", total)).append("\n");
        orderDetails.append("----------------------------------------\n");

        // Display the order
        receiptTextArea.setText(orderDetails.toString());
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            PizzaGUIFrame frame = new PizzaGUIFrame();
            frame.setVisible(true);
        });
    }
}
