/**
 * 
 */
package com.church.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.church.data.repository.ApplicationUserRepository;
import com.church.data.repository.TaskAndUserRepository;
import com.church.data.repository.TaskRepository;
import com.church.model.ApplicationUser;
import com.church.model.Task;
import com.church.model.TaskForEngineer;
import com.church.util.StatusEnum;
import com.church.util.UserTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Slf4j
public class TaskAllotmentManager {

	@Autowired
	private TaskAndUserRepository taskAndUserRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	private HashMap<Task, List<ApplicationUser>> taskUserMap = new HashMap<Task, List<ApplicationUser>>();

	private HashMap<String, Integer> taskAssignedToUserMap = new HashMap<String, Integer>();

	/**
	 * Initilize the cache on the tasks alloted to users in the system.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		// loadTasksForAssignment();
		TaskAndUserLoader loader = new TaskAndUserLoader(this);
		threadPoolTaskExecutor.execute(loader);
	}

	/**
	 * This method loads the tasks assigned to the users. This is called when
	 * server startup. This maintains the state of task assigned to users.
	 */
	protected void loadTaskAllotmentToUser() {
		log.info("Initializing the task allotment - this may take some time");
		List<TaskForEngineer> taskForEngineerList = taskAndUserRepository.findAll();
		Task task = null;
		Iterator<TaskForEngineer> taskForEngIter = taskForEngineerList.iterator();
		while (taskForEngIter.hasNext()) {
			TaskForEngineer taskForEngineerObj = taskForEngIter.next();
			Optional<Task> taskObj = taskRepository.findById(taskForEngineerObj.getTaskId());
			if (taskObj.isPresent()) {
				task = taskObj.get();
				if (task.getPresentState().equals(StatusEnum.completed.label)
						|| task.getPresentState().equals(StatusEnum.onhold.label)
						|| task.getPresentState().equals(StatusEnum.cancelled.label)) {
					continue;
				}
			}
			List<String> userIdList = taskForEngineerObj.getUserIdList();
			Iterator<String> iter = userIdList.iterator();
			ArrayList<ApplicationUser> applicationUserList = new ArrayList<ApplicationUser>();
			while (iter.hasNext()) {
				String userId = iter.next();
				Optional<ApplicationUser> applicationUserObj = applicationUserRepository.findById(userId);
				if (applicationUserObj.isPresent()) {
					ApplicationUser userObj = applicationUserObj.get();
					applicationUserList.add(userObj);
					incrementTaskCountForUser(userObj);
				}
			}
			taskUserMap.put(task, applicationUserList);
		}
	}

	/**
	 * This method is to set the number of tasks assigned to user. This helps to
	 * find the total number of task assigned to a user at any point of time.
	 * 
	 * @param applicationUser
	 */
	private void incrementTaskCountForUser(ApplicationUser applicationUser) {
		if (taskAssignedToUserMap.containsKey(applicationUser.getId())) {
			Integer count = taskAssignedToUserMap.get(applicationUser.getId());
			count++;
			taskAssignedToUserMap.put(applicationUser.getId(), count);
		} else {
			taskAssignedToUserMap.put(applicationUser.getId(), 1);
		}
	}

	/**
	 * If in case any task is completed then the user is offloaded on task. This
	 * helps in making a decision that the user can be assigned another task.
	 * 
	 * @param applicationUser
	 */
	private void decrementTaskCountForUser(ApplicationUser applicationUser) {
		if (taskAssignedToUserMap.containsKey(applicationUser.getId())) {
			Integer count = taskAssignedToUserMap.get(applicationUser.getId());
			count--;
			taskAssignedToUserMap.put(applicationUser.getId(), count);
		}
	}

	/**
	 * Returns the max task assigned to a user. Helps in assigning the task to
	 * any user.
	 * 
	 * @param userId
	 * @return
	 */
	protected Integer getMaxTaskAssignedToUser(String userId) {
		if (taskAssignedToUserMap.containsKey(userId)) {
			return taskAssignedToUserMap.get(userId);
		}
		log.info("No task assigned to user.");
		return 0;
	}

	public void loadTasksForAssignment() {
		log.info("Loading all tasks and assignments.");
		List<Task> scheduledTasks = taskRepository.findTaskByState(StatusEnum.scheduled.name());
		scheduledTasks.forEach(x -> autoAssignTask(x));
	}

	/**
	 * Based on the task name find the best user to whom that can be assigned.
	 * 
	 * @param taskName
	 * @return
	 */
	private ApplicationUser getBestUserForTask(String taskName) {
		log.info("Trying to find the best user for the task. :" + taskName);
		List<Task> taskListOfType = taskUserMap.keySet().stream().filter(x -> x.getTaskName().equals(taskName))
				.collect(Collectors.toList());
		List<ApplicationUser> masterList = new ArrayList<ApplicationUser>();
		taskListOfType.forEach(x -> {
			masterList.addAll(taskUserMap.get(x));
		});
		String userTypeVal = getUserTypeByTaskName(taskName);
		if (masterList.isEmpty()) {
			// If no user is already assigned , pick one user available.
			return getUserForTaskName(taskName);
		}
		List<ApplicationUser> userList = filterApplicationUserByTaskName(masterList, taskName);
		if (userList.isEmpty()) {
			log.error("No " + userTypeVal + " available for :" + taskName);
			return null;
		}
		Collections.sort(userList, new MinTaskAssignedComparator());
		return userList.get(0);
	}

	/**
	 * Get task user type based on task name.
	 * 
	 * @param taskName
	 * @return
	 */
	public String getUserTypeByTaskName(String taskName) {
		String userTypeVal = "";
		if (taskName.toLowerCase().contains("review")) {
			userTypeVal = UserTypeEnum.reviewer.label;
		} else if (taskName.toLowerCase().contains("create")) {
			userTypeVal = UserTypeEnum.engineer.label;
		}
		return userTypeVal;
	}

	private List filterApplicationUserByTaskName(List<ApplicationUser> masterList, String userTypeVal) {
		List<ApplicationUser> userList = masterList.stream()
				.filter(x -> x.getUserTypeList().stream().anyMatch(y -> y.label.equals(userTypeVal)))
				.collect(Collectors.toList());
		return userList;
	}

	/**
	 * Auto assigns the task to the user based on the availability.
	 * 
	 * @param taskObj
	 */
	public void autoAssignTask(Task taskObj) {
		if (null == taskObj.getTaskName()) {
			log.error("Invalid task name cannot be null");
			return;
		}
		Optional<TaskForEngineer> taskAssignedAlready = taskAndUserRepository.findById(taskObj.getId());
		if (taskAssignedAlready.isPresent()) {
			log.info("Task is already assigned to :" + taskAssignedAlready.get().getUserIdList());
			return;
		}
		ApplicationUser user = getBestUserForTask(taskObj.getTaskName());
		if (null != user) {
			TaskForEngineer taskForEngineer = new TaskForEngineer();
			ArrayList<String> userIdList = new ArrayList<String>();
			taskForEngineer.setUserIdList(userIdList);
			taskForEngineer.setTaskId(taskObj.getId());
			taskForEngineer.getUserIdList().add(user.getId());
			taskAndUserRepository.save(taskForEngineer);
			incrementTaskCountForUser(user);
			updateReminder(taskObj, user);
			log.info(taskObj.getTaskName() + " has been assigned to " + user.getName());
		} else {
			log.error("Auto assignment failed - no user available");
		}

	}

	/**
	 * Add the user to whom the task was assigned. This will be used in
	 * reminding the users.
	 */
	private void updateReminder(Task taskObj, ApplicationUser userObj) {
		taskObj.getReminders().forEach(x -> x.addTaskUser(userObj));
		taskRepository.save(taskObj);
		log.info("Updated the application user for the task.");
	}

	public ApplicationUser getUserForTaskName(String taskName) {
		List<ApplicationUser> masterList = applicationUserRepository.findAll();
		List<ApplicationUser> filterList = filterApplicationUserByTaskName(masterList, getUserTypeByTaskName(taskName));
		if (null != filterList && !filterList.isEmpty()) {
			return filterList.get(0);
		}
		return null;
	}

	/**
	 * This runnable loads the tasks assigned to user on application startup.
	 * 
	 * This helps to identify the task load and user availability.
	 * 
	 * @author pradheep
	 *
	 */
	class TaskAndUserLoader implements Runnable {

		private TaskAllotmentManager taskAllotmentManager;

		public TaskAndUserLoader(TaskAllotmentManager taskAllotmentManager) {
			this.taskAllotmentManager = taskAllotmentManager;
		}

		@Override
		public void run() {
			log.info("Loading all tasks at: " + new Date());
			this.taskAllotmentManager.loadTaskAllotmentToUser();
			log.info("Loading all tasks complete at : " + new Date());
		}
	}

	/**
	 * Sort users based on the tasks assigned to them.
	 * 
	 * @author pradheep
	 *
	 */
	class MinTaskAssignedComparator implements Comparator<ApplicationUser> {

		@Override
		public int compare(ApplicationUser o1, ApplicationUser o2) {
			return getMaxTaskAssignedToUser(o1.getId()).compareTo(getMaxTaskAssignedToUser(o2.getId()));
		}
	}

	public static void main(String args[]) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("test", new Integer(1));
		Integer cnt = map.get("test");
		cnt++;
		map.put("test", cnt);
		System.out.println(map.get("test"));
	}

}
