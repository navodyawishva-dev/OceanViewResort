package com.oceanview.controller;

import com.oceanview.model.*;
import com.oceanview.service.*;
import com.oceanview.util.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StaffController extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService();
    private final RoomService        roomService        =
            new RoomService();
    private final CustomerService    customerService    =
            new CustomerService();
    private final BillingService     billingService     =
            new BillingService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {


            case "manageReservations":
                showManageReservations(request, response);
                break;
            case "addReservation":
                showAddReservationForm(request, response);
                break;
            case "editReservation":
                showEditReservationForm(request, response);
                break;
            case "searchReservations":
                handleSearchReservations(request, response);
                break;


            case "manageRooms":
                showManageRooms(request, response);
                break;
            case "addRoom":
                showAddRoomForm(request, response);
                break;
            case "editRoom":
                showEditRoomForm(request, response);
                break;


            case "manageRoomTypes":
                showManageRoomTypes(request, response);
                break;
            case "addRoomType":
                showAddRoomTypeForm(request, response);
                break;
            case "editRoomType":
                showEditRoomTypeForm(request, response);
                break;


            case "manageCustomers":
                showManageCustomers(request, response);
                break;
            case "addCustomer":
                showAddCustomerForm(request, response);
                break;
            case "editCustomer":
                showEditCustomerForm(request, response);
                break;


            case "generateBill":
                showGenerateBill(request, response);
                break;
            case "printBill":
                showPrintBill(request, response);
                break;
            case "processCheckout":
                showProcessCheckout(request, response);
                break;


            case "help":
                request.getRequestDispatcher("/help.jsp")
                        .forward(request, response);
                break;

            default:
                response.sendRedirect(
                        request.getContextPath() + "/dashboard?role=staff");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {


        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {


            case "saveReservation":
                handleSaveReservation(request, response);
                break;
            case "deleteReservation":
                handleDeleteReservation(request, response);
                break;
            case "cancelReservation":
                handleCancelReservation(request, response);
                break;
            case "checkIn":
                handleCheckIn(request, response);
                break;
            case "checkOut":
                handleCheckOut(request, response);
                break;


            case "saveRoom":
                handleSaveRoom(request, response);
                break;
            case "deleteRoom":
                handleDeleteRoom(request, response);
                break;


            case "saveRoomType":
                handleSaveRoomType(request, response);
                break;
            case "deleteRoomType":
                handleDeleteRoomType(request, response);
                break;


            case "saveCustomer":
                handleSaveCustomer(request, response);
                break;
            case "deleteCustomer":
                handleDeleteCustomer(request, response);
                break;


            case "processPayment":
                handleProcessPayment(request, response);
                break;
            case "confirmCheckout":
                handleConfirmCheckout(request, response);
                break;

            default:
                response.sendRedirect(
                        request.getContextPath() + "/dashboard?role=staff");
        }
    }


    private void showManageReservations(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("reservationList",
                    reservationService.getAllReservations());
            request.getRequestDispatcher("/manage-reservations.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/manage-reservations.jsp")
                    .forward(request, response);
        }
    }

    private void showAddReservationForm(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("customerList",
                    customerService.getAllCustomers());
            request.setAttribute("roomList",
                    roomService.getRoomsByStatus(Room.STATUS_AVAILABLE));
            request.setAttribute("formAction", "add");
            request.getRequestDispatcher("/add-edit-reservation.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/manage-reservations.jsp")
                    .forward(request, response);
        }
    }

    private void showEditReservationForm(HttpServletRequest request,
                                         HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        try {
            Reservation reservation =
                    reservationService.getReservationById(reservationId);
            request.setAttribute("reservation", reservation);
            request.setAttribute("customerList",
                    customerService.getAllCustomers());
            request.setAttribute("roomList",
                    roomService.getAllRooms());
            request.setAttribute("formAction", "edit");
            request.getRequestDispatcher("/add-edit-reservation.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageReservations(request, response);
        }
    }

    private void handleSaveReservation(HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException, IOException {

        String reservationId = request.getParameter("reservationId");
        String customerId    = request.getParameter("customerId");
        String roomId        = request.getParameter("roomId");
        String checkInDate   = request.getParameter("checkInDate");
        String checkOutDate  = request.getParameter("checkOutDate");
        String numGuestsStr  = request.getParameter("numGuests");
        String specialReqs   = request.getParameter("specialRequests");
        String staffId       = SessionManager.getLoggedInUserId(request);

        try {
            int numGuests = Integer.parseInt(numGuestsStr);

            if (reservationId == null || reservationId.trim().isEmpty()) {

                reservationService.createReservation(
                        customerId, roomId, checkInDate, checkOutDate,
                        numGuests, specialReqs, staffId);
                request.setAttribute("successMessage",
                        "Reservation created successfully!");
            } else {

                reservationService.updateReservation(
                        reservationId, roomId, checkInDate, checkOutDate,
                        numGuests, specialReqs);
                request.setAttribute("successMessage",
                        "Reservation updated successfully!");
            }
            request.setAttribute("reservationList",
                    reservationService.getAllReservations());
            request.getRequestDispatcher("/manage-reservations.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("customerList",
                    customerService.getAllCustomers());
            request.setAttribute("roomList",
                    roomService.getAllRooms());
            request.setAttribute("formAction",
                    (reservationId == null || reservationId.isEmpty())
                            ? "add" : "edit");
            request.getRequestDispatcher("/add-edit-reservation.jsp")
                    .forward(request, response);
        }
    }

    private void handleDeleteReservation(HttpServletRequest request,
                                         HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        try {
            reservationService.deleteReservation(reservationId);
            request.setAttribute("successMessage",
                    "Reservation deleted successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("reservationList",
                reservationService.getAllReservations());
        request.getRequestDispatcher("/manage-reservations.jsp")
                .forward(request, response);
    }

    private void handleCancelReservation(HttpServletRequest request,
                                         HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        try {
            reservationService.cancelReservation(reservationId);
            request.setAttribute("successMessage",
                    "Reservation cancelled successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("reservationList",
                reservationService.getAllReservations());
        request.getRequestDispatcher("/manage-reservations.jsp")
                .forward(request, response);
    }

    private void handleCheckIn(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        try {
            reservationService.checkIn(reservationId);
            request.setAttribute("successMessage",
                    "Guest checked in successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("reservationList",
                reservationService.getAllReservations());
        request.getRequestDispatcher("/manage-reservations.jsp")
                .forward(request, response);
    }

    private void handleCheckOut(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        try {
            reservationService.checkOut(reservationId);
            request.setAttribute("successMessage",
                    "Guest checked out successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("reservationList",
                reservationService.getAllReservations());
        request.getRequestDispatcher("/manage-reservations.jsp")
                .forward(request, response);
    }

    private void handleSearchReservations(HttpServletRequest request,
                                          HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String status  = request.getParameter("status");
        try {
            if (status != null && !status.trim().isEmpty()) {
                request.setAttribute("reservationList",
                        reservationService.getReservationsByStatus(status));
            } else {
                request.setAttribute("reservationList",
                        reservationService.searchReservations(keyword));
            }
            request.setAttribute("keyword", keyword);
            request.setAttribute("selectedStatus", status);
            request.getRequestDispatcher("/manage-reservations.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageReservations(request, response);
        }
    }


    private void showManageRooms(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("roomList", roomService.getAllRooms());
            request.getRequestDispatcher("/manage-rooms.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/manage-rooms.jsp")
                    .forward(request, response);
        }
    }

    private void showAddRoomForm(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("roomTypeList",
                    roomService.getAllRoomTypes());
            request.setAttribute("formAction", "add");
            request.getRequestDispatcher("/add-edit-room.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageRooms(request, response);
        }
    }

    private void showEditRoomForm(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        String roomId = request.getParameter("roomId");
        try {
            request.setAttribute("room", roomService.getRoomById(roomId));
            request.setAttribute("roomTypeList",
                    roomService.getAllRoomTypes());
            request.setAttribute("formAction", "edit");
            request.getRequestDispatcher("/add-edit-room.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageRooms(request, response);
        }
    }

    private void handleSaveRoom(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {

        String roomId      = request.getParameter("roomId");
        String roomNumber  = request.getParameter("roomNumber");
        String typeId      = request.getParameter("typeId");
        String floorStr    = request.getParameter("floor");
        String status      = request.getParameter("status");
        String description = request.getParameter("description");

        try {
            int floor = Integer.parseInt(floorStr);

            if (roomId == null || roomId.trim().isEmpty()) {
                roomService.addRoom(roomNumber, typeId, floor, description);
                request.setAttribute("successMessage",
                        "Room added successfully!");
            } else {
                roomService.updateRoom(
                        roomId, roomNumber, typeId, floor, status, description);
                request.setAttribute("successMessage",
                        "Room updated successfully!");
            }
            request.setAttribute("roomList", roomService.getAllRooms());
            request.getRequestDispatcher("/manage-rooms.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("roomTypeList",
                    roomService.getAllRoomTypes());
            request.setAttribute("formAction",
                    (roomId == null || roomId.isEmpty()) ? "add" : "edit");
            request.getRequestDispatcher("/add-edit-room.jsp")
                    .forward(request, response);
        }
    }

    private void handleDeleteRoom(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        String roomId = request.getParameter("roomId");
        try {
            roomService.deleteRoom(roomId);
            request.setAttribute("successMessage",
                    "Room deleted successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("roomList", roomService.getAllRooms());
        request.getRequestDispatcher("/manage-rooms.jsp")
                .forward(request, response);
    }


    private void showManageRoomTypes(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("roomTypeList",
                    roomService.getAllRoomTypes());
            request.getRequestDispatcher("/manage-room-types.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/manage-room-types.jsp")
                    .forward(request, response);
        }
    }

    private void showAddRoomTypeForm(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("formAction", "add");
        request.getRequestDispatcher("/add-edit-room-type.jsp")
                .forward(request, response);
    }

    private void showEditRoomTypeForm(HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException, IOException {
        String typeId = request.getParameter("typeId");
        try {
            request.setAttribute("roomType",
                    roomService.getRoomTypeById(typeId));
            request.setAttribute("formAction", "edit");
            request.getRequestDispatcher("/add-edit-room-type.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageRoomTypes(request, response);
        }
    }

    private void handleSaveRoomType(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {

        String typeId        = request.getParameter("typeId");
        String typeName      = request.getParameter("typeName");
        String priceStr      = request.getParameter("pricePerNight");
        String description   = request.getParameter("description");
        String occupancyStr  = request.getParameter("maxOccupancy");

        try {
            double price      = Double.parseDouble(priceStr);
            int    occupancy  = Integer.parseInt(occupancyStr);

            if (typeId == null || typeId.trim().isEmpty()) {
                roomService.addRoomType(
                        typeName, price, description, occupancy);
                request.setAttribute("successMessage",
                        "Room type added successfully!");
            } else {
                roomService.updateRoomType(
                        typeId, typeName, price, description, occupancy);
                request.setAttribute("successMessage",
                        "Room type updated successfully!");
            }
            request.setAttribute("roomTypeList",
                    roomService.getAllRoomTypes());
            request.getRequestDispatcher("/manage-room-types.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("formAction",
                    (typeId == null || typeId.isEmpty()) ? "add" : "edit");
            request.getRequestDispatcher("/add-edit-room-type.jsp")
                    .forward(request, response);
        }
    }

    private void handleDeleteRoomType(HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException, IOException {
        String typeId = request.getParameter("typeId");
        try {
            roomService.deleteRoomType(typeId);
            request.setAttribute("successMessage",
                    "Room type deleted successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("roomTypeList", roomService.getAllRoomTypes());
        request.getRequestDispatcher("/manage-room-types.jsp")
                .forward(request, response);
    }


    private void showManageCustomers(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("customerList",
                    customerService.getAllCustomers());
            request.getRequestDispatcher("/manage-customers.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/manage-customers.jsp")
                    .forward(request, response);
        }
    }

    private void showAddCustomerForm(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("formAction", "add");
        request.getRequestDispatcher("/add-edit-customer.jsp")
                .forward(request, response);
    }

    private void showEditCustomerForm(HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        try {
            request.setAttribute("customer",
                    customerService.getCustomerById(customerId));
            request.setAttribute("formAction", "edit");
            request.getRequestDispatcher("/add-edit-customer.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageCustomers(request, response);
        }
    }

    private void handleSaveCustomer(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {

        String customerId = request.getParameter("customerId");
        String fullName   = request.getParameter("fullName");
        String email      = request.getParameter("email");
        String phone      = request.getParameter("phone");
        String nationalId = request.getParameter("nationalId");
        String address    = request.getParameter("address");

        try {
            if (customerId == null || customerId.trim().isEmpty()) {
                customerService.registerCustomer(
                        fullName, email, phone, nationalId, address);
                request.setAttribute("successMessage",
                        "Customer registered successfully!");
            } else {
                customerService.updateCustomer(
                        customerId, fullName, email, phone, address);
                request.setAttribute("successMessage",
                        "Customer updated successfully!");
            }
            request.setAttribute("customerList",
                    customerService.getAllCustomers());
            request.getRequestDispatcher("/manage-customers.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("formAction",
                    (customerId == null || customerId.isEmpty()) ? "add" : "edit");
            request.getRequestDispatcher("/add-edit-customer.jsp")
                    .forward(request, response);
        }
    }

    private void handleDeleteCustomer(HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        try {
            customerService.deleteCustomer(customerId);
            request.setAttribute("successMessage",
                    "Customer deleted successfully!");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.setAttribute("customerList",
                customerService.getAllCustomers());
        request.getRequestDispatcher("/manage-customers.jsp")
                .forward(request, response);
    }



    private void showGenerateBill(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        String staffId       = SessionManager.getLoggedInUserId(request);
        try {
            Bill bill = billingService.generateBill(reservationId, staffId);
            request.setAttribute("bill", bill);
            request.setAttribute("payments",
                    billingService.getPaymentsForBill(bill.getBillId()));
            request.getRequestDispatcher("/generate-bill.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageReservations(request, response);
        }
    }

    private void showPrintBill(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {
        String billId = request.getParameter("billId");
        try {
            request.setAttribute("bill",
                    billingService.getBillById(billId));
            request.setAttribute("payments",
                    billingService.getPaymentsForBill(billId));
            request.getRequestDispatcher("/print-bill.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageReservations(request, response);
        }
    }

    private void handleProcessPayment(HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException, IOException {

        String billId        = request.getParameter("billId");
        String amountStr     = request.getParameter("amountPaid");
        String paymentMethod = request.getParameter("paymentMethod");
        String staffId       = SessionManager.getLoggedInUserId(request);

        try {
            double amount = Double.parseDouble(amountStr);
            billingService.processPayment(
                    billId, amount, paymentMethod, staffId);
            request.setAttribute("successMessage",
                    "Payment processed successfully!");


            Bill bill = billingService.getBillById(billId);
            request.setAttribute("bill", bill);
            request.setAttribute("payments",
                    billingService.getPaymentsForBill(billId));
            request.getRequestDispatcher("/generate-bill.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            Bill bill = billingService.getBillById(billId);
            request.setAttribute("bill", bill);
            request.setAttribute("payments",
                    billingService.getPaymentsForBill(billId));
            request.getRequestDispatcher("/generate-bill.jsp")
                    .forward(request, response);
        }
    }

    private void showProcessCheckout(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {
        String reservationId = request.getParameter("reservationId");
        try {
            Reservation reservation =
                    reservationService.getReservationById(reservationId);
            Bill bill =
                    billingService.getBillByReservationId(reservationId);
            request.setAttribute("reservation", reservation);
            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/process-checkout.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageReservations(request, response);
        }
    }

    private void handleConfirmCheckout(HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException, IOException {

        String reservationId = request.getParameter("reservationId");
        String paymentMethod = request.getParameter("paymentMethod");
        String staffId       = SessionManager.getLoggedInUserId(request);

        try {
            Bill bill = billingService.processCheckout(
                    reservationId, paymentMethod, staffId);
            reservationService.checkOut(reservationId);
            request.setAttribute("bill", bill);
            request.setAttribute("payments",
                    billingService.getPaymentsForBill(bill.getBillId()));
            request.setAttribute("successMessage",
                    "Checkout completed! Receipt generated.");
            request.getRequestDispatcher("/print-bill.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showManageReservations(request, response);
        }
    }
}
