package com.company.service.impl;

import com.company.model.dao.entities.Status;
import com.company.model.dao.entities.Task;
import com.company.model.dto.request.StatusRequest;
import com.company.model.dao.repository.StatusRepository;
import com.company.service.inter.StatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    @Transactional
    public Status addStatus(Long taskId, StatusRequest statusRequest) {
        Task task = new Task();
        task.setId(taskId);
        statusRepository.deleteByTaskId(taskId);

        Status status = new Status();
        status.setIsDeleted(statusRequest.getIsDeleted());
        status.setIsArchive(statusRequest.getIsArchive());
        status.setIsImportant(statusRequest.getIsImportant());
        status.setIsComplete(statusRequest.getIsComplete());
        status.setIsTasks(statusRequest.getIsTasks());

        status.setTask(task);
        return statusRepository.save(status);
    }

    @Override
    public List<Status> getDeletedStatusesForTask(List<Long> taskId) {
        return statusRepository.findByTaskIdInAndIsDeleted(taskId, true);
    }

    @Override
    public List<Status> getArchiveStatusesForTask(List<Long> taskId) {
        return statusRepository.findByTaskIdInAndIsArchive(taskId, true);
    }

    @Override
    public List<Status> getCompleteStatusesForTask(List<Long> taskId) {
        return statusRepository.findByTaskIdInAndIsComplete(taskId, true);
    }

    @Override
    public List<Status> getImportantStatusesForTask(List<Long> taskId) {
        return statusRepository.findByTaskIdInAndIsImportant(taskId, true);
    }

    @Override
    public List<Status> getTasksStatusesForTask(List<Long> taskId) {
        return statusRepository.findByTaskIdInAndIsTasks(taskId, true);
    }
}
