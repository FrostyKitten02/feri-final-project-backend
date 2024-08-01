package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.PersonTypeListDto;
import si.feri.itk.projectmanager.model.person.PersonTypeList;

@Mapper
public interface PersonTypeMapper {
    PersonTypeMapper INSTANCE = Mappers.getMapper(PersonTypeMapper.class);

    PersonTypeListDto toDto(PersonTypeList personTypeList);
}
