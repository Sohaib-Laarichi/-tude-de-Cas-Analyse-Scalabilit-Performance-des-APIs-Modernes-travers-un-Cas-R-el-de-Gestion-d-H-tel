package com.example.grpcversion.service;

import com.example.grpcversion.entities.Reservation;
import com.example.grpcversion.entities.Client;
import com.example.grpcversion.entities.Chambre;
import com.example.grpcversion.services.ReservationService;

import com.example.grpcversion.grpc.ReservationServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ReservationGrpcService
        extends ReservationServiceGrpc.ReservationServiceImplBase {

    private final ReservationService reservationService;

    public ReservationGrpcService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ---------------- GET BY ID ----------------
    @Override
    public void getReservationById(com.example.grpcversion.grpc.GetReservationByIdRequest request,
                                   StreamObserver<com.example.grpcversion.grpc.GetReservationByIdResponse> responseObserver) {

        reservationService.getReservationById(request.getId()).ifPresentOrElse(
                reservation -> {
                    com.example.grpcversion.grpc.GetReservationByIdResponse response =
                            com.example.grpcversion.grpc.GetReservationByIdResponse.newBuilder()
                                    .setReservation(mapReservation(reservation))
                                    .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                () -> responseObserver.onError(
                        io.grpc.Status.NOT_FOUND
                                .withDescription("Reservation not found")
                                .asRuntimeException()
                )
        );
    }

    // ---------------- GET ALL ----------------
    @Override
    public void getAllReservations(com.example.grpcversion.grpc.GetAllReservationsRequest request,
                                   StreamObserver<com.example.grpcversion.grpc.GetAllReservationsResponse> responseObserver) {

        List<com.example.grpcversion.grpc.Reservation> grpcReservations =
                reservationService.getAllReservations()
                        .stream()
                        .map(this::mapReservation)
                        .collect(Collectors.toList());

        com.example.grpcversion.grpc.GetAllReservationsResponse response =
                com.example.grpcversion.grpc.GetAllReservationsResponse.newBuilder()
                        .addAllReservations(grpcReservations)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // ---------------- CREATE ----------------
    @Override
    public void createReservation(com.example.grpcversion.grpc.CreateReservationRequest request,
                                  StreamObserver<com.example.grpcversion.grpc.CreateReservationResponse> responseObserver) {

        Reservation entity = new Reservation();

        Client client = new Client();
        client.setId(request.getClientId());

        Chambre chambre = new Chambre();
        chambre.setId(request.getChambreId());

        entity.setClient(client);
        entity.setChambre(chambre);
        entity.setDateDebut(LocalDate.parse(request.getDateDebut()));
        entity.setDateFin(LocalDate.parse(request.getDateFin()));
        entity.setPreferences(request.getPreferences());

        Reservation created = reservationService.createReservation(entity);

        com.example.grpcversion.grpc.CreateReservationResponse response =
                com.example.grpcversion.grpc.CreateReservationResponse.newBuilder()
                        .setReservation(mapReservation(created))
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // ---------------- UPDATE ----------------
    @Override
    public void updateReservation(com.example.grpcversion.grpc.UpdateReservationRequest request,
                                  StreamObserver<com.example.grpcversion.grpc.UpdateReservationResponse> responseObserver) {

        Reservation updated = reservationService.updateReservation(
                request.getId(),
                request.getClientId(),
                request.getChambreId(),
                LocalDate.parse(request.getDateDebut()),
                LocalDate.parse(request.getDateFin()),
                request.getPreferences()
        );

        com.example.grpcversion.grpc.UpdateReservationResponse response =
                com.example.grpcversion.grpc.UpdateReservationResponse.newBuilder()
                        .setReservation(mapReservation(updated))
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // ---------------- DELETE ----------------
    @Override
    public void deleteReservation(com.example.grpcversion.grpc.DeleteReservationRequest request,
                                  StreamObserver<com.example.grpcversion.grpc.DeleteReservationResponse> responseObserver) {

        reservationService.deleteReservation(request.getId());

        responseObserver.onNext(
                com.example.grpcversion.grpc.DeleteReservationResponse.newBuilder()
                        .setSuccess(true)
                        .build()
        );
        responseObserver.onCompleted();
    }

    // ---------------- MAPPER ----------------
    private com.example.grpcversion.grpc.Reservation mapReservation(Reservation entity) {

        com.example.grpcversion.grpc.Client grpcClient =
                com.example.grpcversion.grpc.Client.newBuilder()
                        .setId(entity.getClient().getId())
                        .setNom(entity.getClient().getNom())
                        .setPrenom(entity.getClient().getPrenom())
                        .setEmail(entity.getClient().getEmail())
                        .setTelephone(entity.getClient().getTelephone())
                        .build();

        com.example.grpcversion.grpc.Chambre grpcChambre =
                com.example.grpcversion.grpc.Chambre.newBuilder()
                        .setId(entity.getChambre().getId())
                        .setType(entity.getChambre().getType().toString())
                        .setPrix(entity.getChambre().getPrix().doubleValue())
                        .build();

        return com.example.grpcversion.grpc.Reservation.newBuilder()
                .setId(entity.getId())
                .setClient(grpcClient)
                .setChambre(grpcChambre)
                .setDateDebut(entity.getDateDebut().toString())
                .setDateFin(entity.getDateFin().toString())
                .setPreferences(entity.getPreferences() == null ? "" : entity.getPreferences())
                .build();
    }
}
