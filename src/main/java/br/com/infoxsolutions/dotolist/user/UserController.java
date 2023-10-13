package br.com.infoxsolutions.dotolist.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	
	@PostMapping("/usuario")
	public void create(@RequestBody UserModel userModel) {
		
		System.out.println(userModel.getName());
		
	}
	

}
