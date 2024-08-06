package com.nisum.api.cl;

import com.nisum.api.cl.infrastructure.adapters.jpa.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserData;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserPhoneData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class UserGeniusApiRestfulApplication implements CommandLineRunner {

	private final UserDataDAO userDataDAO;
	private final UserPhoneDataDAO userPhoneDataDAO;

	public static void main(String[] args) {
		SpringApplication.run(UserGeniusApiRestfulApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		UserData user1 = new UserData();
		user1.setName("John Doe");
		user1.setEmail("john.doe@example.com");
		user1.setPassword("Password1!");
		user1.setCreatedDate(new Date());
		user1.setUpdatedDate(new Date());
		user1.setIsActive(true);

		UserData user2 = new UserData();
		user2.setName("Jane Smith");
		user2.setEmail("jane.smith@example.com");
		user2.setPassword("Password2!");
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
