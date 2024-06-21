package si.feri.itk.projectmanager.repository;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.lang.NonNull;

import java.util.List;

public abstract class QuerydslParent extends QuerydslRepositorySupport {
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public QuerydslParent(Class<?> domainClass) {
        super(domainClass);
    }

    protected <T> Page<T> getPage(JPQLQuery<T> query, Pageable pageable) {
        List<T> resultList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = query.fetchCount();
        if (totalCount == 0) {
            return new PageImpl<>(resultList, pageable.first(), totalCount);
        } else if (resultList.isEmpty()) {//there should be results but are not in current page, go to first one
            pageable = pageable.first();
            resultList = query
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
        return new PageImpl<>(resultList, pageable, totalCount);
    }

    @Override
    @NonNull
    protected Querydsl getQuerydsl() {
        Querydsl querydsl = super.getQuerydsl();
        if (querydsl == null) {
            throw new IllegalStateException("Querydsl at this point should not be null!");
        }
        return querydsl;
    }
}