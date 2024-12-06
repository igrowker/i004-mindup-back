package com.mindup.core.controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindup.core.dtos.Appointment.RequestAppointmentsByDayDto;
import com.mindup.core.dtos.Appointment.RequestCreateAppointmentDto;
import com.mindup.core.dtos.Appointment.RequestUpdateAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseAppointmentDateDto;
import com.mindup.core.dtos.Appointment.ResponseAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseCreateAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseDeleteAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponsePatientsDto;
import com.mindup.core.dtos.Appointment.ResponseReactivateAppointmentDto;
import com.mindup.core.services.IAppointmentService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/core/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final IAppointmentService iAppointmentService;

    // Buscar por tipo de usuario con reservas aceptadas
    @GetMapping("patient-reserved/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPatientReservedAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getPatientReservedAppointments(id));
    }

    @GetMapping("/psychologist-reserved/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPsychologistReservedAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getPshychologistReservedAppointment(id));
    }

    @GetMapping("/psychologist-pending/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPsychologistPendientAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getPshychologistPendientAppointment(id));
    }

    // Buscar por usuario todas las reservas
    @GetMapping("/patient/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPatientAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getAppointmentsByPatient(id));
    }

    @GetMapping("/psychologist/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPsychologistAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getAppointmentsByPsychologist(id));
    }

    // 
    @PostMapping("/psychologist/appointment-date")
    public ResponseEntity<Set<ResponseAppointmentDateDto>> getAppointmentsByDay(
            @RequestBody RequestAppointmentsByDayDto requestAppointmentsByDayDto) {
                Set<ResponseAppointmentDateDto> responseAppointmentDto = iAppointmentService.getAppointmentsByDay(requestAppointmentsByDayDto);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    // psychologist puede ver sus pacientes
    @GetMapping("/psychologist-patients/{id}")
    public ResponseEntity<Set<ResponsePatientsDto>> getPsychologistPatients(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getPsychologistPatients(id));
    }

    // buscar por estados todos los appointmets (CANCELED, PENDING & ACCEPTED)
    @GetMapping("/pending")
    public ResponseEntity<Set<ResponseAppointmentDto>> getAppointmentPending() {
        return ResponseEntity.ok(iAppointmentService.getAppointmentsPending());
    }

    @GetMapping("/acepted")
    public ResponseEntity<Set<ResponseAppointmentDto>> getAppointmentAcepted() {
        return ResponseEntity.ok(iAppointmentService.getAppointmentsAccepted());
    }

    @GetMapping("/canceled")
    public ResponseEntity<Set<ResponseAppointmentDto>> getAppointmentCanceled() {
        return ResponseEntity.ok(iAppointmentService.getAppointmetsCanceled());
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ResponseAppointmentDto> aceptAppointment(@PathVariable String id) {
        ResponseAppointmentDto responseAppointmentDto = iAppointmentService.aceptAppointment(id);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ResponseAppointmentDto> cancelAppointment(@PathVariable String id) {
        ResponseAppointmentDto responseAppointmentDto = iAppointmentService.cancelAppointment(id);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    // metodos para interactuar con la entidad
    @PostMapping("/create")
    public ResponseEntity<ResponseCreateAppointmentDto> createAppointment(
            @RequestBody RequestCreateAppointmentDto requestCreateAppointmentDto) {
        ResponseCreateAppointmentDto responseAppointmentDto = iAppointmentService.add(requestCreateAppointmentDto);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    @PostMapping("/reschedule")
    public ResponseEntity<ResponseAppointmentDto> updateAppointment(
            @RequestBody RequestUpdateAppointmentDto requestUpdateAppointmentDto) {
        ResponseAppointmentDto responseAppointmentDto = iAppointmentService.update(requestUpdateAppointmentDto);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    // this is an soft delete that can be reactivated
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ResponseDeleteAppointmentDto> deactivateAppointment(
            @PathVariable String id) {
        ResponseDeleteAppointmentDto response = iAppointmentService.delete(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reactivate")
    public ResponseEntity<ResponseReactivateAppointmentDto> reactivateAppointment(
            @PathVariable String id) {
        ResponseReactivateAppointmentDto response = iAppointmentService.reactivateAppointment(id);
        return ResponseEntity.ok(response);
    }

}
