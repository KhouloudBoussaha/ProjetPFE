package tn.sopra.continuix.controllers;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.dtos.UserNodeDTO;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.NotificationRepository;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.request.NotificationRequest;
import tn.sopra.continuix.services.EmailService;
import tn.sopra.continuix.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
@RestController
@RequestMapping("/notification")
public class OrgChartController {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public OrgChartController(UserServiceImpl userService, UserRepository userRepository, NotificationRepository notificationRepository, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    @GetMapping("/orgchart")
    public List<UserNodeDTO> getOrgChart() {
        return userService.getOrgChart();
    }}




