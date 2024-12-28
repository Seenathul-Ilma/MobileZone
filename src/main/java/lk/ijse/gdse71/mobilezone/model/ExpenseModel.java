package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.dto.ExpenseDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseModel {
    public ArrayList<ExpenseDTO> getAllExpenses() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from expenses");

        ArrayList<ExpenseDTO> expenseDTOS = new ArrayList<>();

        while (rst.next()) {
            ExpenseDTO expenseDTO = new ExpenseDTO(
                    rst.getString(1),  // Expense Id
                    rst.getString(2),  // desc
                    rst.getDouble(3),  // amount
                    rst.getDate(4),   // date
                    rst.getString(5)
            );
            expenseDTOS.add(expenseDTO);
        }
        return expenseDTOS;
    }

    public String getNextExpenseId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select exp_Id from expenses order by exp_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(2);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("EX%03d", newIdIndex);
        }
        return "EX001";
    }

    public boolean deleteExpense(String expId) throws SQLException {
        return CrudUtil.execute("delete from expenses where exp_Id=?", expId);
    }

    public boolean saveExpense(ExpenseDTO expenseDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into expenses values (?,?,?,?,?)",
                expenseDTO.getExp_Id(),
                expenseDTO.getDescription(),
                expenseDTO.getAmount(),
                expenseDTO.getDate(),
                expenseDTO.getExpCategory()
        );
    }

    public boolean updateExpense(ExpenseDTO expenseDTO) throws SQLException {
        return CrudUtil.execute(
                "update expenses set description=?, amount=?, date=?, expCategory=? where exp_Id=?",
                expenseDTO.getDescription(),
                expenseDTO.getAmount(),
                expenseDTO.getDate(),
                expenseDTO.getExpCategory(),
                expenseDTO.getExp_Id()
        );
    }
}
