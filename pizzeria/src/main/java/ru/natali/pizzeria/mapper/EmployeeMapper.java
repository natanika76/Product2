package ru.natali.pizzeria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.natali.pizzeria.model.Employee;
import ru.natali.pizzeria.dto.EmployeeDTO;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EmployeeMapper {
    Employee toEntity(EmployeeDTO dto);
    EmployeeDTO toDto(Employee entity);
    void updateEntity(EmployeeDTO dto, @MappingTarget Employee entity);
}
