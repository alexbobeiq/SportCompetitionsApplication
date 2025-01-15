package com.example.SportCompetitionsApplication;

import com.example.SportCompetitionsApplication.repository.ArbitriiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SportCompetitionsApplication implements CommandLineRunner {

	private final ArbitriiRepository staffRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SportCompetitionsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//log.info("astea vin : {}", staffRepository.aduTotiArbitrii());
	}
}
