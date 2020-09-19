/**
 * 
 */
package com.church.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.church.data.repository.ApplicationUserRepository;
import com.church.data.repository.EventRepository;
import com.church.data.repository.TaskAndUserRepository;
import com.church.data.repository.TaskRepository;
import com.church.model.ApplicationUser;
import com.church.model.Task;
import com.church.model.TaskForEngineer;
import com.church.util.StatusEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
@Service
public class TaskServiceImpl {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ApplicationUserRepository userRepository;

	@Autowired
	private TaskAndUserRepository taskAndUserRepository;	

	public List<Task> getAllTasks() {
		List<Task> taskList = taskRepository.findTaskByState(getQueryParamForActiveTasks());
		log.info("Found :" + taskList.size() + " Records");
		return taskList;
	}

	private String getQueryParamForActiveTasks() {
		return StatusEnum.scheduled.label;
	}

	public List<Task> getTaskByEmailAddress(String emailAddress) {
		ApplicationUser user = userRepository.findByEmail(emailAddress);
		ArrayList<Task> taskList = new ArrayList<Task>();
		if (null != user) {
			log.info("Found the user: "+ user.getName());
			List<TaskForEngineer> taskForEngineerList = taskAndUserRepository.getTaskForEngineerByUserId(user.getId());			
			if (null != taskForEngineerList) {
				log.info("Found task for engineer:" + taskForEngineerList.size());
				taskForEngineerList.forEach(x -> {
					Optional<Task> taskObj = taskRepository.findById(x.getTaskId());
					if (taskObj.isPresent()) {
						taskList.add(taskObj.get());
					}
				});
			}
			else{
				log.warn("No task was assigned to user.");
			}
		}
		log.info("Printing the results:"+ taskList);
		return taskList;
	}

}
