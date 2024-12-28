package lk.ijse.gdse71.mobilezone.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.mobilezone.dto.OrderDTO;
import lk.ijse.gdse71.mobilezone.dto.OrderDetailDTO;
import lk.ijse.gdse71.mobilezone.dto.SupplierDTO;
import lk.ijse.gdse71.mobilezone.model.ChartDataModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private AnchorPane employeeStackBarchartAnchorpane;

    @FXML
    private CategoryAxis employeeStackXAxis;

    @FXML
    private NumberAxis employeeStackYAxis;

    @FXML
    private StackedBarChart<String, Number> employeeStackedBarChart;

    @FXML
    private AnchorPane SalePieChartAnchorpane;

    @FXML
    private AnchorPane SalesLineChartAnchorpane;

    @FXML
    private CategoryAxis stackXAxis;

    @FXML
    private NumberAxis stackYAxis;
    @FXML
    private StackedBarChart<String, Number> stackedBarChart;

    @FXML
    private AnchorPane anchorpane1;

    @FXML
    private AnchorPane expensesStackedBarChartAnchorpane;

    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private PieChart salesByCategoryPieChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private ChartDataModel chartDataModel; // Your DAO or service class

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chartDataModel = new ChartDataModel(); // Initialize your model class
        setupLineChart();
        setupPieChart();
        setupStackedBarChart();
        setUpEmployeeStackBarChart();
    }
    private void setUpEmployeeStackBarChart() {
        try {
            employeeStackXAxis.setLabel("Employee");
            employeeStackYAxis.setLabel("Total Sales");
            employeeStackedBarChart.setTitle("Employee performance per month");

            Map<String, Map<String, Integer>> employeeData = chartDataModel.getEmployeePerformanceByMonth();

            for (String name : employeeData.keySet()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(name);

                Map<String, Integer> totalSales = employeeData.get(name);
                for (String month : totalSales.keySet()) {
                    int sales = totalSales.get(month);

                    // Debugging: Print data being added to the chart
                    System.out.println("Adding to chart - Employee: " + name + ", Month: " + month + ", Sales: " + sales);

                    series.getData().add(new XYChart.Data<>(month, sales));
                }

                employeeStackedBarChart.getData().add(series);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setupStackedBarChart() {
        try{
            stackXAxis.setLabel("Months");
            stackYAxis.setLabel("Expenses");
            stackedBarChart.setTitle("Monthly expenses by category");

            Map<String, Map<String, Double>> expenseData = chartDataModel.getMonthlyExpensesByCategory();

            for (String category : expenseData.keySet()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(category);

                Map<String, Double> monthlyExpenses = expenseData.get(category);
                for (String month : monthlyExpenses.keySet()) {
                    double expense = monthlyExpenses.get(month);
                    series.getData().add(new XYChart.Data<>(month, expense));
                }

                stackedBarChart.getData().add(series);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private void setupPieChart() {
        try{
            salesByCategoryPieChart.setTitle("Sales Item By Category");
            salesByCategoryPieChart.setClockwise(true);
            chartDataModel.fetchSalesData(salesByCategoryPieChart);

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, display an error message to the user
        }
    }

    private void setupLineChart() {
        try {
            Map<Integer, Integer> data1 = chartDataModel.getMonthlyOrderData();

            // Set axis labels
            xAxis.setLabel("Number Of Month");
            yAxis.setLabel("Total Orders");

            // Define the series
            // 1st Line for orders per month
            XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
            series1.setName("Orders");

            // Populate the series with data
            for (Map.Entry<Integer, Integer> entry : data1.entrySet()) {
                series1.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // 2nd Line for order returns per month
            Map<Integer, Integer> data2 = chartDataModel.getMonthlyOrderReturnsData();

            XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
            series2.setName("Order Returns");

            for (Map.Entry<Integer, Integer> entry : data2.entrySet()) {
                series2.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // 3rd Line for purchases per month
            Map<Integer, Integer> data3 = chartDataModel.getMonthlyPurchasesData();

            XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
            series3.setName("Purchases");

            for (Map.Entry<Integer, Integer> entry : data3.entrySet()) {
                series3.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            Map<Integer, Integer> data4 = chartDataModel.getMonthlyPurchaseReturnData();

            XYChart.Series<Number, Number> series4 = new XYChart.Series<>();
            series4.setName("Purchase Returns");

            for (Map.Entry<Integer, Integer> entry : data4.entrySet()) {
                series4.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // Add the series to the chart
            //lineChart.getData().clear(); // Clear existing data
            lineChart.getData().addAll(series1, series2, series3, series4);

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, display an error message to the user
        }
    }


    @FXML
    void lineChartOnClicked(MouseEvent event) {

    }

    @FXML
    void pieChartOnClicked(MouseEvent event) {

    }

    @FXML
    void stackBarChartOnClicked(MouseEvent event) {

    }

}
