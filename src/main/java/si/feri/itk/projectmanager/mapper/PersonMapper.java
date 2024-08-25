package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;
import si.feri.itk.projectmanager.dto.model.person.PersonListDto;
import si.feri.itk.projectmanager.dto.model.person.PersonOnProjectDto;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonList;
import si.feri.itk.projectmanager.model.person.PersonOnProject;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    PersonDto toDto(Person person);
    PersonListDto toDto(PersonList personList);
    @Mapping(target = "id", source = "personOnProject.id", ignore = true)
    PersonOnProjectDto toDto(Person person, PersonOnProject personOnProject);
}
