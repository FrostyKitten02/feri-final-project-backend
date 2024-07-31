package si.feri.itk.projectmanager.repository.personlist;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import si.feri.itk.projectmanager.dto.request.person.PersonListSearchParams;
import si.feri.itk.projectmanager.model.person.PersonList;
import si.feri.itk.projectmanager.model.person.QPersonList;
import si.feri.itk.projectmanager.repository.QuerydslParent;
import si.feri.itk.projectmanager.util.StringUtil;

public class CustomPersonListRepoImpl extends QuerydslParent implements CustomPersonListRepo {
    public CustomPersonListRepoImpl() {
        super(PersonList.class);
    }

    @Override
    public Page<PersonList> searchPeople(PersonListSearchParams searchParams, Pageable pageable) {
        QPersonList qPersonList = QPersonList.personList;
        BooleanBuilder restrictions = new BooleanBuilder();

        if (searchParams != null && !StringUtil.isNullOrEmpty(searchParams.getSearchStr())) {
            BooleanBuilder searchStringRestrictions = createSearchStringRestrictions(searchParams.getSearchStr(), qPersonList);
            restrictions.and(searchStringRestrictions);
        }

        Querydsl querydsl = getQuerydsl();
        JPAQuery<PersonList> query = querydsl.<PersonList>createQuery()
                .from(qPersonList)
                .where(restrictions);
        querydsl.applySorting(pageable.getSort(), query);
        return getPage(query, pageable);
    }

    private BooleanBuilder createSearchStringRestrictions(String searchString, QPersonList qPersonList) {
        BooleanBuilder searchStringRestrictions = new BooleanBuilder();
        for (String ss : searchString.split(" ")) {
            searchStringRestrictions.or(qPersonList.name.containsIgnoreCase(ss));
            searchStringRestrictions.or(qPersonList.lastname.containsIgnoreCase(ss));
            searchStringRestrictions.or(qPersonList.email.containsIgnoreCase(ss));
        }
        return searchStringRestrictions;
    }


}
