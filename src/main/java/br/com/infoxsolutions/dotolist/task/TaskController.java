package br.com.infoxsolutions.dotolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	ITaskRepository taskRepository;
	
	@PostMapping("/")
	public TaskModel creator(@RequestBody TaskModel taskModel) {
		
		var task = this.taskRepository.save(taskModel);
		
		return task;
		
	}

}
