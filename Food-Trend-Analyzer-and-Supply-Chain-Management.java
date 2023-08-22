import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.*;

class FoodAnalyzer extends JFrame implements ActionListener {

    // Define the UI components
    private JTabbedPane tabbedPane;
    private JPanel foodAnalyzerPanel;
    private JPanel supplyChainPanel;
    private JComboBox<String> foodComboBox;
    private JTextField[] profitFields;
    private JButton analyzeButton;
    private JTextArea resultArea;
    private JSpinner[] supplySpinners;

    // Define the food items and their supply levels
    private static final String[] FOOD_ITEMS = {"MOMOS", "BURGER", "PIZZA", "TACO", "SANDWIZ"};
    private static final int[] INITIAL_SUPPLY = {100, 50, 20, 75, 30};

    public FoodAnalyzer() {

        // Initialize the UI components
        tabbedPane = new JTabbedPane();
        foodAnalyzerPanel = new JPanel();
        supplyChainPanel = new JPanel();
        foodComboBox = new JComboBox<>(FOOD_ITEMS);
        profitFields = new JTextField[5];
        analyzeButton = new JButton("PREDICT");
        resultArea = new JTextArea(2, 20);
        supplySpinners = new JSpinner[FOOD_ITEMS.length];

        // Create the UI for the food analyzer panel
        foodAnalyzerPanel.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.add(new JLabel("Select a food item: "));
        northPanel.add(foodComboBox);
        foodAnalyzerPanel.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 3));
        centerPanel.add(new JLabel("Profit of 1st year: "));
        centerPanel.add(profitFields[0] = new JTextField());
        centerPanel.add(new JLabel("Profit of 2nd year: "));
        centerPanel.add(profitFields[1] = new JTextField());
        centerPanel.add(new JLabel("Profit of 3rd year: "));
        centerPanel.add(profitFields[2] = new JTextField());
        centerPanel.add(new JLabel("Profit of 4th year: "));
        centerPanel.add(profitFields[3] = new JTextField());
        centerPanel.add(new JLabel("Profit of 5th year: "));
        centerPanel.add(profitFields[4] = new JTextField());

        foodAnalyzerPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.add(analyzeButton);
        foodAnalyzerPanel.add(southPanel, BorderLayout.SOUTH);

        JPanel eastPanel = new JPanel();
        eastPanel.add(new JLabel("Profit prediction for next year: "));
        eastPanel.add(resultArea);
        foodAnalyzerPanel.add(eastPanel, BorderLayout.EAST);

        // Create the UI for the supply chain panel
        supplyChainPanel.setLayout(new GridLayout(FOOD_ITEMS.length, 2));
        for (int i = 0; i < FOOD_ITEMS.length; i++) {
            supplyChainPanel.add(new JLabel(FOOD_ITEMS[i] + " supply: "));
            supplySpinners[i] = new JSpinner(new SpinnerNumberModel(INITIAL_SUPPLY[i], 0, 9999, 1));
            supplyChainPanel.add(supplySpinners[i], BorderLayout.WEST);
        }
        // Add the tabs to the tabbed pane
        tabbedPane.addTab("Food Ordering Trend Analyzer", foodAnalyzerPanel);
        // Supply Chain Management Panel
        tabbedPane.addTab("Supply Chain Management", supplyChainPanel);

        // Add action listeners to the UI components
        analyzeButton.addActionListener(this);
        for (int i = 0; i < FOOD_ITEMS.length; i++) {
            final int index = i;
            supplySpinners[i].addChangeListener(e -> {
                JSpinner source = (JSpinner) e.getSource();
                int value = (Integer) source.getValue();
                System.out.println(FOOD_ITEMS[index] + " supply changed to " + value);
            });
        }


        // Add the tabbed pane to the frame
        add(tabbedPane);

        // Set frame properties
        setTitle("Food Analyzer");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // Event handler for the "Analyze" button
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == analyzeButton) {
//            // Get the selected food item
//            String foodItem = (String) foodComboBox.getSelectedItem();
//            // Get the profit values
//            double[] profits = new double[5];
//            for (int i = 0; i < 5; i++) {
//                profits[i] = Double.parseDouble(profitFields[i].getText());
//            }
//            // Calculate the predicted profit for the next year
//            double nextYearProfit = predictProfit(profits);
//            // Display the result
//            resultArea.append(String.format("%.2f", nextYearProfit));
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");


            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lpu", "root", "Aryan@123");

            String query = "INSERT INTO prediction (1st_year_profit,2nd_year_profit,3rd_year_profit,4th_year_profit,5th_year_profit) VALUES (?, ?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setFloat(1,  (float) Double.parseDouble(profitFields[0].getText()));
            statement.setFloat(2,  (float) Double.parseDouble(profitFields[1].getText()));
             statement.setFloat(3,  (float) Double.parseDouble(profitFields[2].getText()));
             statement.setFloat(4,  (float) Double.parseDouble(profitFields[3].getText()));
             statement.setFloat(5, (float) Double.parseDouble(profitFields[4].getText()));
           int i= statement.executeUpdate();
           if(i>0){
               JOptionPane.showMessageDialog(null,"Inserted !");

            statement.close();
            connection.close();

           }}


         catch(Exception exception){
             System.out.println(exception.getMessage());
         }
            fetchData();
        }

    }

public void fetchData(){
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lpu", "root", "Aryan@123");

        Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String str = "select * from prediction ";
        ResultSet rs = st.executeQuery(str);
        //rs.next();
        rs.last();
        float finalresult;
        float c1 = 0;
        float c2 = 0;
        float c3 = 0;
        float c4 = 0;
        float c5 = 0;

        c1 = rs.getFloat(1);
        c2 = rs.getFloat(2);
        c3 = rs.getFloat(3);
        c4 = rs.getFloat(4);
        c5 = rs.getFloat(5);

        finalresult = (c1 + c2 + c3 + c4 + c5 )/ 5;

        System.out.println("final Result"+finalresult);
        resultArea.setText(String.valueOf(finalresult));


    } catch (Exception ex) {
       System.out.println("Error:"+ex.getMessage());
    }


}
    // A simple function to predict the profit for the next year
    private double predictProfit(double[] profits) {


        double sum = 0;
        for (double profit : profits) {
            sum += profit;
        }
        double averageProfit = sum / profits.length;
        return averageProfit * 1.1; // Assume a 10% increase in profit
    }

    public static void main(String[] args) {
        FoodAnalyzer analyzer = new FoodAnalyzer();
        analyzer.setVisible(true);
    }
}
