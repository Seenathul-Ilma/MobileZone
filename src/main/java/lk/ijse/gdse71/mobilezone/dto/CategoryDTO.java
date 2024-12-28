package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {
    private String categoryId;
    private String categoryName;
    private String subCategory;
    private String description;
}
