package lk.ijse.gdse71.mobilezone.model;
import lk.ijse.gdse71.mobilezone.dto.EmployeeDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from employee");

        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();

        while (rst.next()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    rst.getString(1),  // Customer ID
                    rst.getString(2),  // Name
                    rst.getString(3),  // NIC
                    rst.getString(4),  // Email
                    rst.getString(5),  // Phone
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8),
                    rst.getString(9),
                    rst.getString(10)
            );
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;
    }

    public String getNextEmployeeId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select employeeId from employee order by employeeId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(2);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("EM%02d", newIdIndex);
        }
        return "EM01";
    }

    public boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into employee values (?,?,?,?,?,?,?,?,?,?)",
                employeeDTO.getEmployeeId(),
                employeeDTO.getUserId(),
                employeeDTO.getName(),
                employeeDTO.getNic(),
                employeeDTO.getDesignation(),
                employeeDTO.getEmail(),
                employeeDTO.getContact(),
                employeeDTO.getSalary(),
                employeeDTO.getCompensation(),
                employeeDTO.getTotalSalary()
        );
    }

    public boolean deleteEmployee(String employeeId) throws SQLException {
        return CrudUtil.execute("delete from employee where employeeId=?", employeeId);
    }

    public boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return CrudUtil.execute(
                "update employee set userId=?, name=?, nic=?, designation=?, email=?, contact=?, salary=?, compensation=?, totalSalary=? where employeeId=?",
                employeeDTO.getUserId(),
                employeeDTO.getName(),
                employeeDTO.getNic(),
                employeeDTO.getDesignation(),
                employeeDTO.getEmail(),
                employeeDTO.getContact(),
                employeeDTO.getSalary(),
                employeeDTO.getCompensation(),
                employeeDTO.getTotalSalary(),
                employeeDTO.getEmployeeId()
        );
    }

    public boolean checkUserIds(String selectedUserId) throws SQLException {
        int count = 0;
        ResultSet resultSet = CrudUtil.execute("select employeeId, count(employeeId) from employee where userId=? group by employeeId;", selectedUserId);
        if(resultSet.next()){
            return true;
        }
        return false;
    }

    public boolean checkUserIds(String selectedUserId, int count) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select employeeId, count(employeeId) from employee where userId=? group by employeeId;", selectedUserId);
        while(resultSet.next()){
            count++;
        }

        if(count == 1 ){
            return false;
        }
        return true;
    }

    public ArrayList<String> getAllEmployeeIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select employeeId from employee");

        ArrayList<String> employeeIds = new ArrayList<>();

        while (rst.next()) {
            employeeIds.add(rst.getString(1));
        }
        return employeeIds;
    }

    /*public boolean checkUserIdExist(String userId) throws SQLException {
        //LogInCredentialModel logInCredentialModel = new LogInCredentialModel();
        //return logInCredentialModel.isExist(userId);
        //return CrudUtil.execute("select userId from logInCredentials where userId=?", userId);
    }*/
}
