package si.feri.itk.projectmanager.repository.persontypelist;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import si.feri.itk.projectmanager.dto.request.persontype.PersonTypeListSearchParams;
import si.feri.itk.projectmanager.model.person.PersonTypeList;
import si.feri.itk.projectmanager.model.person.QPersonTypeList;
import si.feri.itk.projectmanager.repository.QuerydslParent;

import java.time.LocalDate;
import java.util.UUID;

public class CustomPersonTypeListRepoImpl extends QuerydslParent implements CustomPersonTypeListRepo {
    public CustomPersonTypeListRepoImpl() {
        super(PersonTypeList.class);
    }

    @Override
    public Page<PersonTypeList> searchPersonTypes(PersonTypeListSearchParams searchParams, Pageable pageable) {
        QPersonTypeList qPersonList = QPersonTypeList.personTypeList;
        BooleanBuilder restrictions = new BooleanBuilder();

        if (searchParams != null) {
            BooleanBuilder createUserRestrictions = createUserIdRestrictions(searchParams.getForUser(), qPersonList);
            restrictions.and(createUserRestrictions);

            BooleanBuilder datesRestrictions = createPersonTypeListDateRestrictions(searchParams, qPersonList);
            restrictions.and(datesRestrictions);
        }

        Querydsl querydsl = getQuerydsl();
        JPAQuery<PersonTypeList> query = querydsl.<PersonTypeList>createQuery()
                .from(qPersonList)
                .where(restrictions);
        querydsl.applySorting(pageable.getSort(), query);
        return getPage(query, pageable);
    }

    public BooleanBuilder createUserIdRestrictions(UUID userId, QPersonTypeList qPersonTypeList) {
        if (userId == null) {
            return null;
        }

        return new BooleanBuilder().and(qPersonTypeList.personId.eq(userId));
    }


    public BooleanBuilder createPersonTypeListDateRestrictions(PersonTypeListSearchParams searchParams, QPersonTypeList qPersonTypeList) {
        BooleanBuilder personTypeListDateRestrictions = new BooleanBuilder();

        LocalDate startFrom = searchParams.getStartDateFrom();
        if (startFrom != null) {
            personTypeListDateRestrictions.and(qPersonTypeList.startDate.goe(startFrom));
        }

        LocalDate startTo = searchParams.getStartDateTo();
        if (startTo != null) {
            personTypeListDateRestrictions.and(qPersonTypeList.startDate.loe(startTo));
        }

        LocalDate endFrom = searchParams.getEndDateFrom();
        if (endFrom != null) {
            personTypeListDateRestrictions.and(qPersonTypeList.endDate.goe(endFrom));
        }

        LocalDate endTo = searchParams.getEndDateTo();
        if (endTo != null) {
            personTypeListDateRestrictions.and(qPersonTypeList.endDate.loe(endTo));
        }

        return personTypeListDateRestrictions;
    }
}
