package si.feri.itk.projectmanager.repository.salarylist;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import si.feri.itk.projectmanager.dto.request.salary.SalaryListSearchParams;
import si.feri.itk.projectmanager.model.person.QSalaryList;
import si.feri.itk.projectmanager.model.person.SalaryList;
import si.feri.itk.projectmanager.repository.QuerydslParent;

import java.time.LocalDate;
import java.util.UUID;

public class CustomSalaryListRepoImpl extends QuerydslParent implements CustomSalaryListRepo {

    public CustomSalaryListRepoImpl() {
        super(SalaryList.class);
    }

    @Override
    public Page<SalaryList> searchSalaries(SalaryListSearchParams searchParams, Pageable pageable) {
        QSalaryList qSalaryList = QSalaryList.salaryList;
        BooleanBuilder restrictions = new BooleanBuilder();

        if (searchParams != null) {
            BooleanBuilder createUserRestrictions = createUserIdRestrictions(searchParams.getForUser(), qSalaryList);
            restrictions.and(createUserRestrictions);

            BooleanBuilder datesRestrictions = createSalariesDatesRestriction(searchParams, qSalaryList);
            restrictions.and(datesRestrictions);
        }

        Querydsl querydsl = getQuerydsl();
        JPAQuery<SalaryList> query = querydsl.<SalaryList>createQuery()
                .from(qSalaryList)
                .where(restrictions);
        querydsl.applySorting(pageable.getSort(), query);
        return getPage(query, pageable);
    }

    public static BooleanBuilder createUserIdRestrictions(UUID userId, QSalaryList qSalaryList) {
        if (userId == null) {
            return null;
        }

        return new BooleanBuilder().and(qSalaryList.personId.eq(userId));
    }


    public BooleanBuilder createSalariesDatesRestriction(SalaryListSearchParams searchParams, QSalaryList qSalaryList) {
        BooleanBuilder salaryDatesRestriction = new BooleanBuilder();

        LocalDate startFrom = searchParams.getStartDateFrom();
        if (startFrom != null) {
            salaryDatesRestriction.and(qSalaryList.startDate.goe(startFrom));
        }

        LocalDate startTo = searchParams.getStartDateTo();
        if (startTo != null) {
            salaryDatesRestriction.and(qSalaryList.startDate.loe(startTo));
        }

        LocalDate endFrom = searchParams.getEndDateFrom();
        if (endFrom != null) {
            salaryDatesRestriction.and(qSalaryList.endDate.goe(endFrom));
        }

        LocalDate endTo = searchParams.getEndDateTo();
        if (endTo != null) {
            salaryDatesRestriction.and(qSalaryList.endDate.loe(endTo));
        }

        return salaryDatesRestriction;
    }
}
