package br.com.infoxsolutions.dotolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.infoxsolutions.dotolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
//public class FilterTaskAuth implements Filter {
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		
//		//Executar Alguma ação
//		
//		chain.doFilter(request, response);
//		
//	}

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

	@Autowired
	IUserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var servletPath = request.getServletPath();

		if (servletPath.equals("/tasks/")) {

			// Pegar a autenticação (Usuario e Senha)

			var authorization = request.getHeader("Authorization");

			var authEncoded = authorization.substring("Basic".length()).trim();

			byte[] authDecode = Base64.getDecoder().decode(authEncoded);

			var authString = new String(authDecode);

			String[] credentials = authString.split(":");

			String username = credentials[0];
			String password = credentials[1];

			System.out.println("Authorization");
			System.out.println(username);
			System.out.println(password);

			// Validar usuario

			var user = this.userRepository.findByUsername(username);

			if (user == null) {

				response.sendError(401);

			} else {
				// Validar senha
				var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

				if (passwordVerify.verified) {

					// Segue viagem
					filterChain.doFilter(request, response);
				} else {
					response.sendError(401);
				}

			}
		}else {
			filterChain.doFilter(request, response);
		}

	}

}
