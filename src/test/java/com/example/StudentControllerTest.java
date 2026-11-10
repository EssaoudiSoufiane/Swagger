package com.example;

import com.example.demo.controllers.StudentController;
import com.example.demo.entities.Student;
import com.example.demo.services.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @Mock
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    @Test
    void saveReturnsCreatedAndSavedStudent() {
        Student input = new Student();
        input.setNom("LACHGAR");
        Student saved = new Student();
        saved.setId(3);
        saved.setNom("LACHGAR");
        when(service.save(input)).thenReturn(saved);

        var response = controller.save(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(saved, response.getBody());
    }

    @Test
    void listReturnsStudents() {
        var students = List.of(new Student(), new Student());
        when(service.findAll()).thenReturn(students);

        var response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());
    }

    @Test
    void countReturnsTotal() {
        when(service.countStudents()).thenReturn(2L);

        var response = controller.countStudent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Long.valueOf(2), response.getBody());
    }

    @Test
    void byYearReturnsYearAndCount() {
        var groups = List.<Object[]>of(new Object[]{1985, 2L});
        doReturn(groups).when(service).findNbrStudentByYear();

        var response = controller.findByYear();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertArrayEquals(groups.get(0), (Object[]) response.getBody().iterator().next());
    }

    @Test
    void deleteReturnsNoContentForExistingStudent() {
        when(service.delete(3)).thenReturn(true);

        var response = controller.delete(3);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteReturnsNotFoundForMissingStudent() {
        when(service.delete(3)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, controller.delete(3).getStatusCode());
    }
}

