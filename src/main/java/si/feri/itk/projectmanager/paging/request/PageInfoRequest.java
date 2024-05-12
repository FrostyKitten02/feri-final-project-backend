package si.feri.itk.projectmanager.paging.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.paging.PageInfo;

@Getter
@Setter
public class PageInfoRequest {
    @Min(1)
    private Integer elementsPerPage;
    @Min(1)
    private Integer pageNumber;
}
