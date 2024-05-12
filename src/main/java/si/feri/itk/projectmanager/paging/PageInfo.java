package si.feri.itk.projectmanager.paging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PageInfo {
    private int elementsPerPage = 100;
    private long totalElements = 0;
    private int pageNumber = 0;
    public PageInfo(Integer elementsPerPage, Long totalElements, Integer pageNumber) {
        if (elementsPerPage!=null) {
            this.elementsPerPage = elementsPerPage;
        }
        if (totalElements!=null) {
            this.totalElements = totalElements;
        }
        if (pageNumber!=null) {
            this.pageNumber = pageNumber;
        }
    }

    public static PageRequest toPageRequest(PageInfo pageInfo, SortInfo<?> sortInfo) {
        if (pageInfo == null) {
            pageInfo = PageInfo.firstPage();
        }
        return pageInfo.toPageRequest(sortInfo);
    }

    public static PageRequest toPageRequest(PageInfoRequest pageInfoRequest, SortInfo<?> sortInfo) {
        PageInfo pageInfo = fromPageInfoRequest(pageInfoRequest);
        return toPageRequest(pageInfo, sortInfo);
    }

    public static PageInfo fromPageInfoRequest(PageInfoRequest pageInfoRequest) {
        if (pageInfoRequest == null) {
            return null;
        }
        return new PageInfo(pageInfoRequest.getElementsPerPage(), null, pageInfoRequest.getPageNumber());
    }

    public PageRequest toPageRequest(Sort sort) {
        if (sort == null) {
            return toPageRequest();
        }
        return PageRequest.of(this.getPageNumber() - 1, this.getElementsPerPage(), sort);
    }

    public PageRequest toPageRequest() {
        return toPageRequest(Sort.unsorted());
    }

    public PageRequest toPageRequest(SortInfo<?> sortInfo) {
        if (sortInfo == null) {
            return toPageRequest();
        }
        return PageRequest.of(this.getPageNumber() - 1, this.getElementsPerPage(), sortInfo.getSort());
    }
    public static PageInfo from(Page<?> page) {
        return builder()
                .elementsPerPage(page.getSize())
                .totalElements(page.getTotalElements())
                .pageNumber(page.getNumber() + 1)
                .build();
    }

    /**
     * Construct PageInfo with 100 results expected
     * @return
     */
    public static PageInfo firstPage() {
        return firstPage(100);
    }

    public static PageInfo firstPage(int elementsPerPage) {
        return builder()
                .elementsPerPage(elementsPerPage)
                .totalElements(0)
                .pageNumber(1)
                .build();
    }

    public static PageInfo firstPageUnlimitedIfZero(Integer elementsPerPage) {
        if (elementsPerPage == null || elementsPerPage == 0) {
            return firstPage(Integer.MAX_VALUE);
        }
        return firstPage(elementsPerPage);
    }


    public static PageInfo firstPage(Integer elementsPerPage) {
        if (elementsPerPage == null) {
            return firstPage(Integer.MAX_VALUE);
        }

        return firstPage(elementsPerPage.intValue());
    }

    public boolean isLastPage() {
        return (long)pageNumber*(long)elementsPerPage>=totalElements;
    }
    public PageInfo nextPage() {
        return builder()
                .elementsPerPage(elementsPerPage)
                .totalElements(totalElements)
                .pageNumber(pageNumber+1)
                .build();
    }
}
