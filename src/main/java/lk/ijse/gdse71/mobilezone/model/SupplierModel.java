package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.dto.CustomerDTO;
import lk.ijse.gdse71.mobilezone.dto.SupplierDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier");

        ArrayList<SupplierDTO> supplierDTOS = new ArrayList<>();

        while (rst.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    rst.getString(1),  // Supplier ID
                    rst.getString(2),  // Company Name
                    rst.getString(3),  // Contact Person
                    rst.getString(4),  // NIC
                    rst.getString(5),  // Address
                    rst.getString(6),  // Email
                    rst.getString(7)   // Phone
            );
            supplierDTOS.add(supplierDTO);
        }
        return supplierDTOS;
    }

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into supplier values (?,?,?,?,?,?,?)",
                supplierDTO.getSupplierId(),
                supplierDTO.getCompanyName(),
                supplierDTO.getContactPerson(),
                supplierDTO.getNic(),
                supplierDTO.getAddress(),
                supplierDTO.getEmail(),
                supplierDTO.getPhone()
        );
    }

    public boolean deleteSupplier(String supplierId) throws SQLException {
        return CrudUtil.execute("delete from supplier where supplierId=?", supplierId);
    }

    public String getNextSupplierId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select supplierId from supplier order by supplierId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("S%03d", newIdIndex);
        }
        return "S001";
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "update supplier set companyName=?, contactPerson=?, nic=?, address=?, email=?, phone=? where supplierId=?",
                supplierDTO.getCompanyName(),
                supplierDTO.getContactPerson(),
                supplierDTO.getNic(),
                supplierDTO.getAddress(),
                supplierDTO.getEmail(),
                supplierDTO.getPhone(),
                supplierDTO.getSupplierId()
        );
    }

    public SupplierDTO findById(String selectedSupplierId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier where supplierId=?", selectedSupplierId);

        if (rst.next()) {
            return new SupplierDTO(
                    rst.getString(1),  // Customer ID
                    rst.getString(2),  // Name
                    rst.getString(3),  // NIC
                    rst.getString(4),  // Email
                    rst.getString(5),   // Phone
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }

    public ArrayList<String> getAllSupplierIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select supplierId from supplier");

        ArrayList<String> supplierIds = new ArrayList<>();

        while (rst.next()) {
            supplierIds.add(rst.getString(1));
        }

        return supplierIds;
    }
}
