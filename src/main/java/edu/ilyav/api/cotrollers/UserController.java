package edu.ilyav.api.cotrollers;

import edu.ilyav.api.models.Role;
import edu.ilyav.api.models.UserInfo;
import edu.ilyav.api.service.UserService;
import edu.ilyav.api.service.exceptions.ResourceNotFoundException;
import edu.ilyav.api.util.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@Value("${guest.mode.pass}")
	private String GUEST_PASSWORD;

	private final String GUEST_USERNAME = "guest";

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public UserInfo login(@RequestBody UserInfo userInfo) throws ServletException, ResourceNotFoundException {

		if(GUEST_USERNAME.equals(userInfo.getUserName())) {
			userInfo.setPassword(GUEST_PASSWORD);
		}

		if (userInfo.getUserName() == null || userInfo.getPassword() == null) {
			throw new ServletException("Please fill in username and password");
		}

		UserInfo user = userService.findByUserName(userInfo.getUserName());

		if (user == null) {
			throw new ServletException("User name not found.");
		}

		if (!BCrypt.checkpw(userInfo.getPassword(), user.getPassword())) {
			throw new ServletException("Invalid login. Please check your username and password");
		}

		DateTime currentTime = new DateTime();

		String role = Constants.GUEST;
		if(user.getLogin() != null && !user.getLogin().getRoles().isEmpty()) {
			for (Role r: user.getLogin().getRoles()) {
				role = r.getRoleName();
			}
		}
        try {
            if (Constants.GUEST.equals(role)) {

                user.setToken(Jwts.builder().setSubject(userInfo.getUserName()).setIssuedAt(new Date())
                        .setExpiration(currentTime.plusMinutes(30).toDate())
                        .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes("UTF-8")).compact());

            } else {
                user.setToken(Jwts.builder().setSubject(userInfo.getUserName()).setIssuedAt(new Date())
                        .setExpiration(currentTime.plusMinutes(30).toDate())
                        .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes("UTF-8")).compact());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		userService.saveOrUpdate(user);

		user.setPassword(Constants.EMPTY);

		return user;
	}

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public UserInfo registerUser(@RequestBody UserInfo user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userService.createNewOne(user);
	}

	@RequestMapping(value="/private/all", method = RequestMethod.GET)
	public List<UserInfo> getAllUsers(HttpServletRequest req) throws ResourceNotFoundException {
		if(isGuestMode(req))
			throw new ResourceNotFoundException("You do not have this privilege in Guest mode.");
		else
			return userService.findAllUsers();
	}
}
