package com.nisum.api.cl;

import com.nisum.api.cl.infrastructure.adapters.jpa.role.RoleDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.role.data.RoleData;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserPhoneData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class UserGeniusApiRestfulApplication implements CommandLineRunner {

	private final UserDataDAO userDataDAO;
	private final RoleDataDAO roleDataDAO;
	private final UserPhoneDataDAO userPhoneDataDAO;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(UserGeniusApiRestfulApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		RoleData role1 = new RoleData();
		role1.setName("ROLE_ADMIN");

		RoleData role2 = new RoleData();
		role2.setName("ROLE_USER");

		roleDataDAO.saveAll(List.of(role1, role2));

		UserData user1 = new UserData();
		user1.setUsername("Doe");
		user1.setName("John Doe");
		user1.setEmail("john.doe@example.com");
		user1.setPassword(passwordEncoder.encode("Password1!"));
		user1.setCreatedDate(new Date());
		user1.setUpdatedDate(new Date());
		user1.setIsActive(true);

		UserData user2 = new UserData();
		user2.setUsername("Smith");
		user2.setName("Jane Smith");
		user2.setEmail("jane.smith@example.com");
		user2.setPassword(passwordEncoder.encode("Password2!"));
		user2.setCreatedDate(new Date());
		user2.setUpdatedDate(new Date());
		user2.setIsActive(true);

		userDataDAO.saveAll(List.of(user1, user2));

		UserPhoneData phone1 = new UserPhoneData();
		phone1.setNumber("1234567890");
		phone1.setCityCode("01");
		phone1.setCountryCode("US");
		phone1.setUser(user1);

		UserPhoneData phone2 = new UserPhoneData();
		phone2.setNumber("0987654321");
		phone2.setCityCode("02");
		phone2.setCountryCode("US");
		phone2.setUser(user2);

		userPhoneDataDAO.saveAll(List.of(phone1, phone2));
	}
}
