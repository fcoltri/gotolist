package br.com.infoxsolutions.dotolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private IUserRepository userRepository;
	
	
	@PostMapping("/usuario")
	public ResponseEntity create(@RequestBody UserModel userModel) {
		
		var user = this.userRepository.findByUsername(userModel.getUsername());
		
		if(user != null) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já Existe!!");
			
		}
		
		var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
		
		userModel.setPassword(passwordHashred);
		
		var userCreated = userRepository.save(userModel);
		
		return ResponseEntity.ok().body(userCreated);
		
	}
	

}
