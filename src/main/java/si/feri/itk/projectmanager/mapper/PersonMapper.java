package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.model.Person;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto toDto(Person person);
}
