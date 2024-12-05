package com.mindup.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.Set;

import com.mindup.core.dtos.Appointment.RequestAppointmentsByDayDto;
import com.mindup.core.dtos.Appointment.RequestCreateAppointmentDto;
import com.mindup.core.dtos.Appointment.RequestUpdateAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseCreateAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseDeleteAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponsePatientsDto;
import com.mindup.core.dtos.Appointment.ResponseReactivateAppointmentDto;
import com.mindup.core.entities.AppointmentEntity;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    
    ResponseAppointmentDto toResponseDto(AppointmentEntity appointment);
    Set<ResponseAppointmentDto> toResponseDtoSet(Set<AppointmentEntity> appointments);
    AppointmentEntity toEntity(RequestCreateAppointmentDto requestDto);

    AppointmentEntity requestAppointmentsByDayDtoToAppointmentEntity (RequestAppointmentsByDayDto requestAppointmentsByDayDto);

    ResponseCreateAppointmentDto appointmentToResponseCreateAppointmentDto(AppointmentEntity appointmentEntity);

    ResponseDeleteAppointmentDto appointmentToResponseDeleteAppointmentDto (AppointmentEntity appointmentEntity);
    ResponseReactivateAppointmentDto appointmentToResponseReactivateAppointmentDto (AppointmentEntity appointmentEntity);

    void updateAppointmentFromDto(RequestUpdateAppointmentDto requestDto, @MappingTarget AppointmentEntity appointment);

    ResponsePatientsDto appointmentToResponsePatientsDto (AppointmentEntity appointmentEntity);
}