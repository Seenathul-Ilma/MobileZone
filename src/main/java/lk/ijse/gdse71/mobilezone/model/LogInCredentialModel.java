package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.dto.LogInCredentialsDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogInCredentialModel {

    public ArrayList<LogInCredentialsDTO> getAllCredentials() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from logInCredentials");

        ArrayList<LogInCredentialsDTO> logInCredentialsDTOS = new ArrayList<>();

        while (rst.next()) {
            LogInCredentialsDTO logInCredentialsDTO = new LogInCredentialsDTO(
                    rst.getString(1),  // User Id
                    rst.getString(2),  // Role
                    rst.getString(3),  // Username
                    rst.getString(4)   // Password
            );
            logInCredentialsDTOS.add(logInCredentialsDTO);
        }
        return logInCredentialsDTOS;
    }

    public boolean saveCredentials(LogInCredentialsDTO credentialDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into logInCredentials values (?,?,?,?)",
                credentialDTO.getUserId(),
                credentialDTO.getRole(),
                credentialDTO.getUserName(),
                credentialDTO.getPassword()
        );
    }

    public String getNextUserId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select userId from logInCredentials order by userId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last User ID
            String substring = lastId.substring(2); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("US%03d", newIdIndex); // Return the new User ID in format Unnn
        }
        return "US001";
    }

    public boolean deleteCredentials(String userId) throws SQLException {
        return CrudUtil.execute("delete from logInCredentials where userId=?", userId);
    }

    public boolean updateCredentials(LogInCredentialsDTO credentialDTO) throws SQLException {
        return CrudUtil.execute(
                "update logInCredentials set role=?, userName=?, password=? where userId=?",
                credentialDTO.getRole(),
                credentialDTO.getUserName(),
                credentialDTO.getPassword(),
                credentialDTO.getUserId()
        );
    }

    public ArrayList<String> getAllUserIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select userId from logInCredentials");

        ArrayList<String> userIds = new ArrayList<>();

        while (rst.next()) {
            userIds.add(rst.getString(1));
        }
        return userIds;
    }

    public String getAdminPassword() throws SQLException {
        String role = "Admin";
        ResultSet rst = CrudUtil.execute("select password from logInCredentials where role=?", role);

        if (rst.next()) {
            return rst.getString("password");
        }
        return "";
    }

    public String isExistUsernameAndPassword(String username, String password) throws SQLException {
        ResultSet rst = CrudUtil.execute("select userId from logInCredentials where userName=? AND password=?", username,password);

        if (rst.next()) {
            return rst.getString("userId");
        }
        return "";
    }

    /*public boolean isExist(String userId) throws SQLException {
        return CrudUtil.execute("select userId from logInCredentials where userId=?", userId);
    }*/
}
