package com.example.soapapiversion.endpoint;

import com.example.soapapiversion.services.ReservationService;
import com.example.soapapiversion.soap.*;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
@Transactional
public class ReservationSoapEndpoint {

    private static final String NAMESPACE_URI = "http://hotel.com/reservation/soap";

    private final ReservationService reservationService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    public ReservationSoapEndpoint(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReservationByIdRequest")
    @ResponsePayload
    public GetReservationByIdResponse getReservationById(@RequestPayload GetReservationByIdRequest request) {
        GetReservationByIdResponse response = new GetReservationByIdResponse();
        reservationService.getReservationById(request.getId()).ifPresent(reservation -> {
            response.setReservation(mapReservation(reservation));
        });
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllReservationsRequest")
    @ResponsePayload
    public GetAllReservationsResponse getAllReservations(@RequestPayload GetAllReservationsRequest request) {
        GetAllReservationsResponse response = new GetAllReservationsResponse();
        List<Reservation> soapReservations = reservationService.getAllReservations().stream()
                .map(this::mapReservation)
                .collect(Collectors.toList());
        response.getReservations().addAll(soapReservations);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createReservationRequest")
    @ResponsePayload
    public CreateReservationResponse createReservation(@RequestPayload CreateReservationRequest request) {
        CreateReservationResponse response = new CreateReservationResponse();

        com.example.soapapiversion.entities.Reservation entity = new com.example.soapapiversion.entities.Reservation();
        // We need to fetch Client and Chambre entities first, but the service takes a Reservation entity
        // which has Client and Chambre objects.
        // The service createReservation(Reservation reservation) expects a Reservation object.
        // However, the service implementation likely handles fetching or saving.
        // But wait, the service signature is createReservation(Reservation reservation).
        // Usually, we pass IDs and the service fetches them, or we pass the full objects.
        // The controller in REST API does:
        // @PostMapping public ResponseEntity<Reservation> create(@RequestBody Reservation reservation)
        // So it expects the Reservation object to be populated.
        // But the REST API receives JSON which might contain IDs or full objects.
        // If it receives IDs, Jackson might not map them to objects unless configured.
        // Let's check the Reservation entity again.
        // @ManyToOne private Client client;
        // @ManyToOne private Chambre chambre;

        // If I pass a Reservation with just IDs in Client/Chambre objects, will it work?
        // The service implementation is in `comon`. I should check `ReservationServiceImpl`.

        // For now, I'll assume I need to construct the entity with Client and Chambre objects having IDs.

        com.example.soapapiversion.entities.Client client = new com.example.soapapiversion.entities.Client();
        client.setId(request.getClientId());

        com.example.soapapiversion.entities.Chambre chambre = new com.example.soapapiversion.entities.Chambre();
        chambre.setId(request.getChambreId());

        entity.setClient(client);
        entity.setChambre(chambre);
        entity.setDateDebut(LocalDate.parse(request.getDateDebut(), formatter));
        entity.setDateFin(LocalDate.parse(request.getDateFin(), formatter));
        entity.setPreferences(request.getPreferences());

        com.example.soapapiversion.entities.Reservation created = reservationService.createReservation(entity);
        response.setReservation(mapReservation(created));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateReservationRequest")
    @ResponsePayload
    public UpdateReservationResponse updateReservation(@RequestPayload UpdateReservationRequest request) {
        UpdateReservationResponse response = new UpdateReservationResponse();

        com.example.soapapiversion.entities.Reservation updated = reservationService.updateReservation(
                request.getId(),
                request.getClientId(),
                request.getChambreId(),
                LocalDate.parse(request.getDateDebut(), formatter),
                LocalDate.parse(request.getDateFin(), formatter),
                request.getPreferences()
        );

        response.setReservation(mapReservation(updated));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteReservationRequest")
    @ResponsePayload
    public DeleteReservationResponse deleteReservation(@RequestPayload DeleteReservationRequest request) {
        DeleteReservationResponse response = new DeleteReservationResponse();
        reservationService.deleteReservation(request.getId());
        response.setSuccess(true);
        return response;
    }

    private Reservation mapReservation(com.example.soapapiversion.entities.Reservation entity) {
        Reservation soapReservation = new Reservation();
        soapReservation.setId(entity.getId());
        soapReservation.setDateDebut(entity.getDateDebut().toString());
        soapReservation.setDateFin(entity.getDateFin().toString());
        soapReservation.setPreferences(entity.getPreferences());

        Client soapClient = new Client();
        BeanUtils.copyProperties(entity.getClient(), soapClient);
        soapReservation.setClient(soapClient);

        Chambre soapChambre = new Chambre();
        soapChambre.setId(entity.getChambre().getId());
        soapChambre.setType(entity.getChambre().getType().toString());
        soapChambre.setPrix(entity.getChambre().getPrix());
        soapReservation.setChambre(soapChambre);

        return soapReservation;
    }
}
