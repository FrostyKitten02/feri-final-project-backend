package si.feri.itk.projectmanager.repository.projectlist;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import si.feri.itk.projectmanager.dto.request.project.ProjectListSearchParams;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.model.project.ProjectList;
import si.feri.itk.projectmanager.model.person.QPerson;
import si.feri.itk.projectmanager.model.person.QPersonOnProject;
import si.feri.itk.projectmanager.model.project.QProjectList;
import si.feri.itk.projectmanager.repository.QuerydslParent;
import si.feri.itk.projectmanager.util.StringUtil;

import java.time.LocalDate;
import java.util.UUID;


public class CustomProjectListRepoImpl extends QuerydslParent implements CustomProjectListRepo {
    public CustomProjectListRepoImpl() {
        super(ProjectList.class);
    }

    @Override
    public Page<ProjectList> searchUsersProjects(ProjectListSearchParams searchParams, String userId, Pageable pageable) {
        if (userId == null) {
            throw new InternalServerException("User id missing!");
        }

        QProjectList qProjectList = QProjectList.projectList;
        BooleanBuilder restrictions = new BooleanBuilder();

        BooleanBuilder projectIdRestrictions = createProjectIdRestrictions(searchParams.getSearchOnlyOwnedProjects(), qProjectList, userId);
        restrictions.and(projectIdRestrictions);

        BooleanBuilder projectDatesRestrictions = createProjectDatesRestrictions(searchParams, qProjectList);
        restrictions.and(projectDatesRestrictions);

        final String searchString = searchParams.getSearchStr();
        if (!StringUtil.isNullOrEmpty(searchString)) {
           BooleanBuilder searchStringRestrictions = createSearchStringRestrictions(searchString, qProjectList);
           restrictions.and(searchStringRestrictions);
        }

        Querydsl querydsl = getQuerydsl();
        JPAQuery<ProjectList> query = querydsl.<ProjectList>createQuery()
                .from(qProjectList)
                .where(restrictions);
        querydsl.applySorting(pageable.getSort(), query);
        return getPage(query, pageable);
    }

    private BooleanBuilder createSearchStringRestrictions(String searchString, QProjectList qProjectList) {
        BooleanBuilder searchStringRestrictions = new BooleanBuilder();
        for (String ss : searchString.split(" ")) {
            searchStringRestrictions.or(qProjectList.title.containsIgnoreCase(ss));
            searchStringRestrictions.or(qProjectList.title.in(ss));
        }
        return searchStringRestrictions;
    }

    private BooleanBuilder createProjectIdRestrictions(Boolean searchOnlyOwnedProjects, QProjectList qProjectList, String userId) {
        BooleanBuilder projectIdRestrictions = new BooleanBuilder();
        //searching owned only when parameter is explicitly set to true
        if (searchOnlyOwnedProjects == null || !searchOnlyOwnedProjects) {
            //getting person id by userId
            QPerson qperson = QPerson.person;
            JPQLQuery<UUID> personIdSubquery = JPAExpressions.select(qperson.id)
                    .from(qperson)
                    .where(qperson.clerkId.eq(userId))
                    .limit(1);

            //getting all project ids where person is on project
            QPersonOnProject qPersonOnProject = QPersonOnProject.personOnProject;
            JPQLQuery<UUID> projectIdsSubquery = JPAExpressions.select(qPersonOnProject.projectId)
                    .from(qPersonOnProject)
                    .where(qPersonOnProject.person.id.eq(personIdSubquery));

            projectIdRestrictions.or(qProjectList.id.in(projectIdsSubquery));
        }
        projectIdRestrictions.or(qProjectList.ownerId.eq(userId));
        return projectIdRestrictions;
    }

    private BooleanBuilder createProjectDatesRestrictions(ProjectListSearchParams searchParams, QProjectList qProjectList) {
        BooleanBuilder projectDatesRestrictions = new BooleanBuilder();

        LocalDate startFrom = searchParams.getStartDateFrom();
        if (startFrom != null) {
            projectDatesRestrictions.and(qProjectList.startDate.goe(startFrom));
        }

        LocalDate startTo = searchParams.getStartDateTo();
        if (startTo != null) {
            projectDatesRestrictions.and(qProjectList.startDate.loe(startTo));
        }

        LocalDate endFrom = searchParams.getEndDateFrom();
        if (endFrom != null) {
            projectDatesRestrictions.and(qProjectList.endDate.goe(endFrom));
        }

        LocalDate endTo = searchParams.getEndDateTo();
        if (endTo != null) {
            projectDatesRestrictions.and(qProjectList.endDate.loe(endTo));
        }

        return projectDatesRestrictions;
    }

}
