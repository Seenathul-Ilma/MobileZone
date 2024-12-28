package lk.ijse.gdse71.mobilezone.dto.tm;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryTM {
    private String categoryId;
    private String categoryName;
    private String subCategory;
    private String description;
}
