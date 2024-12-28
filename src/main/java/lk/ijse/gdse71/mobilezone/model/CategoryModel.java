package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.dto.CategoryDTO;
import lk.ijse.gdse71.mobilezone.dto.ItemDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryModel {
    public ArrayList<CategoryDTO> getAllCategories() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from category");

        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();

        while (rst.next()) {
            CategoryDTO categoryDTO = new CategoryDTO(
                    rst.getString(1),  // Category Id
                    rst.getString(2),  // Category Name
                    rst.getString(3),  // Sub Category
                    rst.getString(4)   // Description
            );
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    public String getNextCategoryId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select categoryId from category order by categoryId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last Category ID
            String substring = lastId.substring(2); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("IC%03d", newIdIndex); // Return the new Category ID in format ICnnn
        }
        return "IC001";
    }

    public boolean deleteCategory(String categoryId) throws SQLException {
        return CrudUtil.execute("delete from category where categoryId=?", categoryId);
    }

    public boolean saveCategory(CategoryDTO categoryDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into category values (?,?,?,?)",
                categoryDTO.getCategoryId(),
                categoryDTO.getCategoryName(),
                categoryDTO.getSubCategory(),
                categoryDTO.getDescription()
        );
    }

    public boolean updateCategory(CategoryDTO categoryDTO) throws SQLException {
        return CrudUtil.execute(
                "update category set categoryName=?, subCategory=?, description=? where categoryId=?",
                categoryDTO.getCategoryName(),
                categoryDTO.getSubCategory(),
                categoryDTO.getDescription(),
                categoryDTO.getCategoryId()
        );
    }

    public ArrayList<String> getAllCategoryIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select categoryId from category");

        ArrayList<String> categoryIds = new ArrayList<>();

        while (rst.next()) {
            categoryIds.add(rst.getString(1));
        }

        return categoryIds;
    }
}
