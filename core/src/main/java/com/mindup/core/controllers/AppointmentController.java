package com.mindup.core.controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindup.core.dtos.Appointment.RequestCreateAppointmentDto;
import com.mindup.core.dtos.Appointment.RequestUpdateAppointmentDto;
import com.mindup.core.dtos.Appointment.ResponseAppointmentDto;

import com.mindup.core.services.IAppointmentService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentController {

    private IAppointmentService iAppointmentService;
    

    // Buscar por tipo de usuario con reservas aceptadas
    @GetMapping("/patient-reserved/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPatientReservedAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getPatientReservedAppointments(id));
    }

    @GetMapping("/psychologist-reserved/{id}")
    public ResponseEntity<Set<ResponseAppointmentDto>> getPsychologistReservedAppointments(@PathVariable String id) {
        return ResponseEntity.ok(iAppointmentService.getPshychologistReservedAppointment(id));
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

    @PostMapping("/create")
    public ResponseEntity<ResponseAppointmentDto> createAppointment(@RequestBody RequestCreateAppointmentDto requestCreateAppointmentDto ) {
        ResponseAppointmentDto responseAppointmentDto = iAppointmentService.add(requestCreateAppointmentDto);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseAppointmentDto> updateAppointment(@RequestBody RequestUpdateAppointmentDto requestUpdateAppointmentDto ) {
        ResponseAppointmentDto responseAppointmentDto = iAppointmentService.update(requestUpdateAppointmentDto);
        return ResponseEntity.ok(responseAppointmentDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable String id) {
        iAppointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
