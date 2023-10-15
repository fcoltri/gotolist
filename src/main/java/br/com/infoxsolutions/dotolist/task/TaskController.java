package br.com.infoxsolutions.dotolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.infoxsolutions.dotolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	ITaskRepository taskRepository;
	
	@PostMapping("/")
	public ResponseEntity creator(@RequestBody TaskModel taskModel, HttpServletRequest request) {
		
		System.out.println("Chegou aqui" + request.getAttribute("userId"));
		
		var idUser = request.getAttribute("userId");
		
		taskModel.setUserId((UUID)idUser);
			
		var currentDate = LocalDateTime.now();
		
		if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio / data do término deve ser maior que a data atual!");
		}
		
		if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data do término!");
		}
		
		
		var task = this.taskRepository.save(taskModel);
		
		return ResponseEntity.ok().body(task);
		
	}
	
	@GetMapping("/")
	public List<TaskModel> list(HttpServletRequest request) {
		
		var idUser = request.getAttribute("userId");
		
		var tasks = this.taskRepository.findByUserId((UUID) idUser);
		
		return tasks;
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
	
		var task = this.taskRepository.findById(id).orElse(null);
		
		//Verificando se a tarefa existe
		if(task == null) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa nao existe.");
		}
		
		var idUser = request.getAttribute("userId");
		
		//Validação para verificar se o usuario é o dono da tarefa
		if(!task.getUserId().equals(idUser)) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário na tem permissoa para alteração essa tarefa.");
			
		}
		
		
		Utils.copyNonNullProperties(taskModel, task);

		var taskUpdated = this.taskRepository.save(task);
		
		return ResponseEntity.ok().body(taskUpdated);
	}

}
