package com.company.controller;

import com.company.dao.entities.Status;
import com.company.dto.response.TaskRespons;
import com.company.dto.response.UserRespons;
import com.company.service.impl.StatusService;
import com.company.service.impl.UserServiceImpl;
import com.company.service.inter.JwtService;
import com.company.service.inter.TaskService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TaskDeleteController {
    private final JwtService service;
    private final TaskService taskService;
    private final UserServiceImpl userService;
    private final StatusService statusService;
    private static final Logger log = LoggerFactory.getLogger(TaskDeleteController.class);


    @GetMapping("/tasksdelete")
    public String getTasks(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        String email = service.extractUserName(token);
        Long userId = userService.findByUserId(email);

        List<TaskRespons> userTasks = taskService.getAllTasksById(userId);
        List<Long> userTaskIds = userTasks.stream().map(TaskRespons::getId).collect(Collectors.toList());

        List<Status> deletedStatuses = statusService.getDeletedStatusesForTask(userTaskIds);

        model.addAttribute("deletedStatuses", deletedStatuses);

        UserRespons userResp = (UserRespons) session.getAttribute("userRespons");
        model.addAttribute("userRespons", userResp);
        return "tasks-deleted";
    }
}
