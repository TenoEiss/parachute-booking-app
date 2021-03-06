package com.parachute.booking.flight;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parachute.booking.pilot.Pilot;
import com.parachute.booking.pilot.PilotRepository;
import com.parachute.booking.plane.Plane;
import com.parachute.booking.plane.PlaneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class FlightServiceSearchIntegrationTest {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private PilotRepository pilotRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    LocalDateTime localDateTime;

    private final String requestMappingUrl = "/flights";

    private Flight createFlightForTestA() {
        Plane plane = new Plane();
        plane.setPlaneNumber(11L);

        Pilot pilot = new Pilot();
        pilot.setPilotLicenseNumber(111L);

        Flight flight = Flight.builder()
                .planeNumber(plane)
                .pilotLicenseNumber(pilot)
                .localDateTime(localDateTime)
                .build();

        pilotRepository.save(pilot);
        planeRepository.save(plane);
        flightRepository.save(flight);

        return flight;
    }

    private void createFlightForTestB() {
        Plane plane = new Plane();
        plane.setPlaneNumber(22L);

        Pilot pilot = new Pilot();
        pilot.setPilotLicenseNumber(222L);

        Flight flight = Flight.builder()
                .planeNumber(plane)
                .pilotLicenseNumber(pilot)
                .localDateTime(localDateTime)
                .build();

        pilotRepository.save(pilot);
        planeRepository.save(plane);
        flightRepository.save(flight);

    }

    private void createFlightForTestC() {
        Plane plane = new Plane();
        plane.setPlaneNumber(33L);

        Pilot pilot = new Pilot();
        pilot.setPilotLicenseNumber(333L);

        Flight flight = Flight.builder()
                .planeNumber(plane)
                .pilotLicenseNumber(pilot)
                .localDateTime(localDateTime)
                .build();

        pilotRepository.save(pilot);
        planeRepository.save(plane);
        flightRepository.save(flight);

    }

    @BeforeEach
    void setup() {
        flightRepository.deleteAll();
        planeRepository.deleteAll();
        pilotRepository.deleteAll();
    }

    @Test
    void getAllFlights_andReturnsStatusCode200() throws Exception {
        //given
        createFlightForTestA();
        createFlightForTestB();
        createFlightForTestC();

        MockHttpServletRequestBuilder request = get(requestMappingUrl);

        //when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        FlightDtoListed responseBody = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });
        assertThat(responseBody.getFlights()).hasSize(3);
    }

    @Test
    void getAllFlights_andReturnsStatusCode200_noAdmins() throws Exception {
        //given
        MockHttpServletRequestBuilder request = get(requestMappingUrl);

        //when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        FlightDtoListed responseBody = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });
        assertThat(responseBody.getFlights()).hasSize(0);
    }

    @Test
    void findById_andReturnsStatusCode200() throws Exception {
        //given
        Flight flight = createFlightForTestA();
        createFlightForTestB();
        createFlightForTestC();
        Long id = flight.getId();
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/{id}", id);

        //when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        FlightDto respondeBody = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), FlightDto.class);
        assertThat(respondeBody.getId()).isEqualTo(id);
        assertThat(respondeBody.getPlaneNumber()).isEqualTo(11L);
        assertThat(respondeBody.getPilotLicenseNumber()).isEqualTo(111L);
        assertThat(respondeBody.getLocalDateTime()).isEqualTo(localDateTime);
    }

    @Test
    void findById_andReturnsStatusCode400_flightDoesntExists() throws Exception {
        // given
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/{id}", 100);

        // when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findById_andReturnsStatusCode400_FlightIdIsNegative() throws Exception {
        // given
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/{id}", -1);

        // when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findAllByPlaneNumber_andReturnsStatusCode200() throws Exception {
        //given
        Flight flight = createFlightForTestA();
        createFlightForTestB();
        createFlightForTestC();
        Long plane = flight.getPlaneNumber().getPlaneNumber();
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/plane/{plane}", plane);

        //when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        FlightDtoListed respondeBody = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), FlightDtoListed.class);
        assertThat(respondeBody.getFlights()).allSatisfy(flightDto -> assertThat(flightDto.getPlaneNumber().equals(plane)));
    }

    @Test
    void findAllByPlanenumber_andReturnsStatusCode400() throws Exception {
        // given
        createFlightForTestA();
        createFlightForTestB();
        createFlightForTestC();
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/plane/{plane}", 100L);

        // when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findAllByPilotLicense_andReturnsStatusCode200() throws Exception {
        //given
        Flight flight = createFlightForTestA();
        createFlightForTestB();
        createFlightForTestC();
        Long pilot = flight.getPilotLicenseNumber().getPilotLicenseNumber();
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/pilot/{pilot}", pilot);

        //when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        FlightDtoListed respondeBody = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), FlightDtoListed.class);
        assertThat(respondeBody.getFlights()).allSatisfy(flightDto -> assertThat(flightDto.getPlaneNumber().equals(pilot)));
    }

    @Test
    void findAllByPilotLicense_andReturnsStatusCode400() throws Exception {
        // given
        createFlightForTestA();
        createFlightForTestB();
        createFlightForTestC();
        MockHttpServletRequestBuilder request = get(requestMappingUrl + "/pilot/{pilot}", 999L);

        // when
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    //TODO date time

//    @Test
//    void findByLocalDateTime_andReturnsStatusCode200() {
//
//    }
//
//    @Test
//    void findByLocalDateTime_andReturnsStatusCode400() {
//
//    }

}
